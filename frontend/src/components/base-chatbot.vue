<template>
  <view class="chat-page">
    <!-- 顶部导航栏 -->
    <view class="navbar">
      <text class="title">智能体</text>
    </view>

    <!-- 聊天记录 -->
    <scroll-view
        class="chat-history"
        scroll-y
        :scroll-into-view="`msg-${messages.length - 1}`"
        scroll-with-animation>
      <div class="chat">
        <view v-for="(msg, index) in messages" :key="index" :id="`msg-${index}`" class="message-item">
          <!-- AI 聊天内容：头像在左，内容在右 -->
          <view v-if="msg.role === 'ai'" class="message-ai"
                :key="index"
                :id="`msg-${index}`">
            <view class="avatar ai-avatar"></view> <!-- AI 头像 -->
            <view class="message-content ai-content" v-html="msg.content"></view> <!-- AI 聊天内容 -->
            <button class="export-btn" @click="exportToWord(msg.content, index)">导出 Word</button>

          </view>

          <!-- 用户聊天内容：头像在右，内容在左 -->
          <view v-else class="message-user">
            <view class="message-content user-content">{{ msg.content }}</view> <!-- 用户聊天内容 -->
            <view class="avatar user-avatar"></view> <!-- 用户头像 -->
          </view>
        </view>
      </div>
    </scroll-view>

    <slot name="default" :item="fileInput">
<!--      <base-select data="data" api="dify_config.page" title="智能体名称"/>-->
    </slot>
    <!-- 输入框区域 -->
    <view class="input-area">
      <base-upload v-model="fileInput"/>


    <view class="input-container">

      <!-- 输入框 -->
      <input
          v-model="inputText"
          placeholder="请输入您的问题"
          class="input-box"
          @keyup.enter="sendMessage"
          :disabled="isSending"
      />

      <!-- 文件选择器 -->
<!--      <uni-file-picker
          v-model="fileInput"
          fileMediatype="all"
          mode="grid"
          @select="select"
          @progress="progress"
          @success="success"
          @fail="fail"
      />-->

      <!-- 发送按钮 -->
      <button class="send-icon" @click="sendMessage"></button>
    </view>
    </view>

  </view>
</template>

<script setup>
import {nextTick, ref} from 'vue'
import {fetchEventSource} from "@microsoft/fetch-event-source";
import BaseUpload from "@/components/base-upload.vue";
import BaseSelect from "@/components/base-select.vue";

const BASE_API = import.meta.env.VITE_APP_BASE_API;
const {proxy} = getCurrentInstance()

const ctrl = new AbortController()
const conversationId = ref()
const token = uni.getStorageSync("h5_token");
const messages = ref([])
const inputText = ref('')
const isSending = ref(false)
const chatbotRequest = ref({
  query: '',
  conversationId: null,
  userId: '1',
  fileUrl: ''
})
const data = {text: 'aaa', value: '1'}
const fileInput = ref()
// 用于实时接收数据的 SseEmitter
let eventSource = null
const emits = defineEmits(["send_message", "send_message"]);


const downLoadFileRequest = ref({
  content: '',
  conversationId: conversationId,
  difyId: null
})

const exportToWord = async (text, index) => {

  if(text != null) {
    downLoadFileRequest.value.content = text
    const res = await proxy.$api.chatbot.downLoadWord(JSON.stringify(downLoadFileRequest.value))

    const byteCharacters = atob(res.data);
    const byteNumbers = new Array(byteCharacters.length).fill(0).map((_, i) => byteCharacters.charCodeAt(i));
    const byteArray = new Uint8Array(byteNumbers);

    const blob = new Blob([byteArray], { type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document" });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url // 创建下载链接
    link.download = 'chat_message.docx';    // 设置文件名
    link.click()
    // 释放内存
    window.URL.revokeObjectURL(url);
  }

  console.log("text and index", text, index)
}

// 发送消息
const sendMessage = (text) => {

  console.log(fileInput.value)
  if (!inputText.value.trim() || isSending.value) return
  isSending.value = true  // 设置为发送中状态

  let text_query = JSON.parse(JSON.stringify(inputText.value))
  messages.value.push({role: 'user', content: text_query})

  if(fileInput.value) {
    chatbotRequest.value.fileUrl = fileInput.value
  }

  // 更新 message 内容并自动滚动到最新的消息
  nextTick(() => {
    const chatHistory = document.querySelector('.chat-history')
    chatHistory.scrollTop = chatHistory.scrollHeight
  })

  // 开始通过 SSE 调用后端接口
  startSseConnection(inputText.value)
  inputText.value = ''
  fileInput.value = ''
}

// 使用 SSE 连接后端并处理数据
const startSseConnection = (query) => {
  if (eventSource) {
    eventSource.close()  // 如果已有连接，则关闭之前的连接
  }

  chatbotRequest.value.query = inputText.value
  console.log("chatbotRequest", chatbotRequest.value)
  if (conversationId.value) {
    chatbotRequest.value.conversationId = conversationId.value
  }

  const jsonData = JSON.stringify(chatbotRequest.value)
  fetchEventSource(BASE_API + '/chatbot', {
    method: "POST",
    openWhenHidden: true,
    headers: {
      'Content-Type': 'application/json',
      'Cache-Control': 'no-cache',
      'Connection': 'keep-alive',
      'Authorization': `Bearer ${token}`,
    },
    signal: ctrl.signal,
    body: jsonData,
    onmessage: event => {
      try {

        const newMessage = JSON.parse(event.data)

        console.log(newMessage)
        if (newMessage.event === 'workflow_started') {
          messages.value.push({role: 'ai', content: ""})
          if (newMessage.conversation_id != null) {
            conversationId.value = newMessage.conversation_id
          }
        }
        if (newMessage.event === 'message') {

          messages.value[messages.value.length - 1].content += newMessage.answer
        }
        if (newMessage.event === "message_end") {
          isSending.value = false
        }

        // 更新 message 内容并自动滚动到最新的消息
        nextTick(() => {
          const chatHistory = document.querySelector('.chat-history')
          chatHistory.scrollTop = chatHistory.scrollHeight
        })

      } catch (e) {
        console.log("eeeee", e)
        ctrl?.abort()
        isSending.value = false
      }
    },
    onerror: err => {
      console.error('SSE 错误:', err)
      isSending.value = false
    },
    onclose: () => {
      isSending.value = false
    },
  })

}

// 获取上传状态
function select(selectedFile) {
  fileInput.value = selectedFile
  // 构建请求 URL
  console.log("selectedFile = ", selectedFile)
  console.log("selectedFile.tempFiles[0] = ", selectedFile.tempFiles[0])
  console.log('选择文件：', selectedFile)
}

// 打字机效果
const typeWriterEffect = (content) => {
  const aiMessage = {role: 'ai', content: ''}
  messages.value.push(aiMessage)

  let i = 0
  aiMessage.content = ''

  const interval = setInterval(() => {
    aiMessage.content += content[i]
    i++

    // 更新 message 内容并自动滚动到最新的消息
    nextTick(() => {
      const chatHistory = document.querySelector('.chat-history')
      chatHistory.scrollTop = chatHistory.scrollHeight
    })

    if (i === content.length) {
      clearInterval(interval)
      isSending.value = false  // 回复完成后恢复发送按钮状态
    }
  }, 100) // 每100毫秒显示一个字符，模拟打字机效果
}
</script>

<style scoped>
.chat-page {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #f7f8fa;
  overflow: hidden; /* 防止溢出 */
}

.navbar {
  position: fixed;
  left: 0;
  width: 100%;
  height: 54px;
  display: flex;
  align-items: center;
  background-color: #4072ee;
  color: white;
  padding: 0 10px;
  z-index: 10; /* 确保位于其他内容之上 */
}

.title {
  flex: 1;
  text-align: center;
  font-size: 18px;
  font-weight: bold;
}

.chat-history {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  margin-top: 54px; /* 避开导航栏 */
  margin-bottom: 60px; /* 避开输入框 */
}

.message-item {
  display: flex;
  margin-bottom: 10px;
  justify-content: flex-end;
}

/* AI 消息样式：头像在左，聊天内容在右 */
.message-ai {
  display: flex;
  flex-direction: row;
  justify-content: start;
  align-items: center;
}

/* 用户消息样式：头像在右，聊天内容在左 */
.message-user {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  justify-content: flex-end;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin: 0 10px;
}

.ai-avatar {
  background: url('../static/ai.jpeg') no-repeat center;
  background-size: cover;
}

.user-avatar {
  background: url('../static/me.jpeg') no-repeat center;
  background-size: cover;
  order: 2; /* 将头像放在右侧 */
  margin-left: 0;
}

.message-content {
  max-width: 70%;
  padding: 10px;
  border-radius: 10px;
  font-size: 14px;
  line-height: 20px;
  order: 1; /* 将聊天内容放在左侧 */
}

/* AI 的聊天内容样式 */
.ai-content {
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  white-space: pre-wrap;
  word-wrap: break-word;
}

/* 用户的聊天内容样式 */
.user-content {
  background-color: #4072ee;
  color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-right: 10px;
  display: inline-block;
  padding: 10px;
  word-wrap: anywhere;
  max-width: 600px;
  border-radius: 10px;
}


/* 底部聊天框 */
.input-area {
  display: flex;
  align-items: center;
  padding: 10px;
  background-color: #ffffff;
  border-top: 1px solid #e5e5e5;
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 50px;
  z-index: 10; /* 确保位于其他内容之上 */
}

/* 输入框容器 */
.input-container {
  position: relative;
  width: 100%;
  max-width: 500px;
}

.input-box {
  flex: 1;
  height: 40px;

/*  padding: 5px 15px;
  border: 1px solid #e5e5e5;*/
  border-radius: 20px;
  background-color: #f9f9f9;
  font-size: 14px;
  outline: none;

  width: 90%;
  padding: 5px 15px; /* 为按钮留出空间 */
  border: 1px solid #ccc;
}

.icons {
  display: flex;
  align-items: center;
  margin-left: 10px;
}

.send-icon {
  display: flex;
  justify-content: center;
  align-items: center;
  background-size: 20%;
  border: none;
  border-radius: 50%;
  background: #3058ba url('../static/send.png') no-repeat center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;

  position: absolute;
  right: 10px; /* 按钮距离输入框右侧的距离 */
  top: 50%;
  transform: translateY(-50%);
  width: 30px;
  height: 30px;
  color: #fff;
  font-size: 16px;
}

.send-icon:active {
  transform: scale(0.95);
  background-color: #3058ba;
}


/*
.chat {
  flex: 1;
  max-height: 580px;
  overflow-y: auto;
  padding: 10px;

  .leftMsg,
  .rightMsg {
    display: flex;
    flex-direction: row;
    justify-content: start;
    align-items: center;
    margin: 10px;

    img {
      width: 40px;
      height: 40px;
      border-radius: 20px;
      overflow: hidden;
      object-fit: cover;
      margin: 0 10px;
    }

    .msg {
      display: inline-block;
      padding: 10px;
      word-wrap: anywhere;
      max-width: 600px;
      background-color: #364d79;
      border-radius: 10px;
    }
  }

  .rightMsg {
    justify-content: end;

    .msg {
      color: black;
      background-color: #dfdfdf;
    }
  }
}
*/


/* word 导出按钮*/
.export-btn {
  height: 30px;
  width: 100%;
  margin-top: 10px; /* 留出空间 */
  padding: 5px 10px;
  background-color: #4072ee;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 12px;
  align-self: flex-start; /* 对齐到左侧 */
}

.export-btn:hover {
  background-color: #3058ba;
}

</style>
