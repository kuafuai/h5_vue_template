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
        :scroll-into-view="`msg-${messages.length - 1}`">
      <div class="chat">
        <view v-for="(msg, index) in messages" :key="index" :id="`msg-${index}`" class="message-item">
          <!-- AI 聊天内容：头像在左，内容在右 -->
          <view v-if="msg.role === 'ai'" class="message-ai">
            <view class="avatar ai-avatar"></view> <!-- AI 头像 -->
            <view class="message-content ai-content" v-html="msg.content"></view> <!-- AI 聊天内容 -->
          </view>

          <!-- 用户聊天内容：头像在右，内容在左 -->
          <view v-else class="message-user">
            <view class="message-content user-content">{{ msg.content }}</view> <!-- 用户聊天内容 -->
            <view class="avatar user-avatar"></view> <!-- 用户头像 -->
          </view>
        </view>
      </div>
    </scroll-view>

    <!-- 输入框区域 -->
    <view class="input-area">
      <!-- 输入框 -->
      <input
          v-model="inputText"
          placeholder="请输入您的问题"
          class="input-box"
          @keyup.enter="sendMessage"
          :disabled="isSending"
      />

      <!-- 文件选择器 -->
      <uni-file-picker
          v-model="fileInput"
          fileMediatype="all"
          mode="grid"
          @select="select"
          @progress="progress"
          @success="success"
          @fail="fail"
      />
      <!--      <base-upload/>-->

      <!-- 发送按钮 -->
      <button class="send-icon" @click="sendMessage"></button>
    </view>

  </view>
</template>

<script setup>
import {nextTick, ref} from 'vue'
import {fetchEventSource} from "@microsoft/fetch-event-source";

const BASE_API = import.meta.env.VITE_APP_BASE_API;

const ctrl = new AbortController()
const conversationId = ref()
let times = 0
const token = uni.getStorageSync("h5_token");
const messages = ref([])
const inputText = ref('')
const isSending = ref(false)
const {proxy} = getCurrentInstance()
const chatbotRequest = ref({
  query: '',
  conversationId: null,
  userId: '1'
})
const fileInput = ref()
// 用于实时接收数据的 SseEmitter
let eventSource = null

// 返回上一页
const goBack = () => {
  uni.navigateBack()
}

// 触发文件上传
const triggerFileUpload = () => {
  document.querySelector('input[type="file"]').click()
}

// 处理文件上传
const handleFileUpload = (event) => {
  const file = event.target.files[0]
  if (file) {
    // 处理文件上传逻辑
    console.log('上传的文件:', file)
  }
}

// 发送消息
const sendMessage = () => {
  if (!inputText.value.trim() || isSending.value) return
  isSending.value = true  // 设置为发送中状态

  console.log(inputText.value)
  let text_query=JSON.parse(JSON.stringify(inputText.value))+""
  messages.value.push({role: 'user', content: text_query})

  // 开始通过 SSE 调用后端接口
  startSseConnection(inputText.value)
  inputText.value = ''
}

// 使用 SSE 连接后端并处理数据
const startSseConnection = (query) => {
  if (eventSource) {
    eventSource.close()  // 如果已有连接，则关闭之前的连接
  }

  chatbotRequest.value.query = inputText.value
  console.log("chatbotRequest", chatbotRequest.value)
  if(conversationId.value) {
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
        if (newMessage.event === 'workflow_started'){
          messages.value.push({role: 'ai', content: ""})
          if(newMessage.conversation_id != null) {
            conversationId.value = newMessage.conversation_id
          }
        }
        if (newMessage.event === 'message') {

          messages.value[messages.value.length - 1].content += newMessage.answer
        }
        if(newMessage.event === "message_end") {
          isSending.value = false
        }
        /*else {
          ctrl?.abort()
        }*/
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


/*  eventSource.onmessage = (event) => {
    const data = event.data
    console.log('收到AI回复:', data)

    // 模拟打字机效果
    typeWriterEffect(data)
  }*/
/*
  eventSource.onerror = (error) => {
    console.error('SSE 错误:', error)
    eventSource.close()  // 关闭连接
  }*/
}

// 获取上传状态
function select(selectedFile) {
  fileInput.value = selectedFile
  // 构建请求 URL
  console.log("selectedFile = ", selectedFile)
  console.log("selectedFile.tempFiles[0] = ", selectedFile.tempFiles[0])
  formData.append('file', selectedFile.tempFiles[0]);
  console.log('选择文件：', selectedFile)
}

// 获取上传进度
function progress(e) {
  console.log('上传进度：', e)
}

// 上传成功
function success(e) {
  console.log('上传成功')
}

// 上传失败
function fail(e) {
  console.log('上传失败：', e)
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
  height: 100vh;
  background-color: #f7f8fa;
}

.navbar {
  display: flex;
  align-items: center;
  height: 54px;
  background-color: #4072ee;
  color: white;
  padding: 0 10px;
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


.input-area {
  display: flex;
  align-items: center;
  padding: 10px;
  background-color: #ffffff;
  border-top: 1px solid #e5e5e5;
}

.input-box {
  flex: 1;
  height: 40px;
  padding: 5px 15px;
  border: 1px solid #e5e5e5;
  border-radius: 20px;
  background-color: #f9f9f9;
  font-size: 14px;
  outline: none;
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
  width: 40px;
  height: 40px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

.send-icon:active {
  transform: scale(0.95);
  background-color: #3058ba;
}


/*
.send-icon {

  margin-left: 10px;
  background: url('../static/send.png') no-repeat center;
  background-size: contain;
}

*/

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

</style>
