<template>
  <view class="chat-page">
    <scroll-view
      class="chat-history"
      scroll-y
      :scroll-into-view="lastMessageId"
      scroll-with-animation
    >
      <div class="chat">
        <view
          v-for="(msg, index) in messages"
          :key="index"
          :id="`msg-${index}`"
          class="message-item"
        >
          <!-- AI 聊天内容：头像在左，内容在右 -->
          <view
            v-if="msg.role === 'ai'"
            class="message-ai"
            :key="index"
            :id="`msg-${index}`"
          >
            <view
              class="message-content ai-content"
              v-html="msg.content"
            ></view>
            <!-- AI 聊天内容 -->
          </view>

          <!-- 用户聊天内容：头像在右，内容在左 -->
          <view v-else class="message-user">
            <view class="message-content user-content">{{ msg.content }}</view>
            <!-- 用户聊天内容 -->
          </view>
        </view>
      </div>
    </scroll-view>
    <view :class="showOptions?'fixed-bottom-container on':'fixed-bottom-container'">
      <!-- 输入框区域 -->
      <view class="input-area" :style="{ bottom: inputAreaBottom + 'px' }">
        <!-- <base-upload v-model="fileInput"/> -->

        <view class="input-container">
          <!-- 输入框 -->
          <input
            v-model="inputText"
            :placeholder="inputPlaceholder"
            @focus="setPlaceholder"
            @confirm="sendMessage"
            :disabled="isSending"
            @blur="onInputBlur"
          />

          <!-- 发送按钮 -->
          <!-- <view class="send-icon" @click="sendMessage"> -->
          <view
            class="send-icon"
            @click="currentClickHandler"
            :class="isFocused ? 'send-iconFocused' : 'send-icon'"

          >
            <img
              :src="iconSrc"
              class="icon-image"
              :class="{
                'icon-image': !isFocused /* 默认样式 */,
                'icon-imageFocused': isFocused /* 获取焦点时样式 */,
                rotate:
                  !isFocused && showOptions /* 点击时旋转，但仅在默认状态下 */,
              }"
            />
          </view>
        </view>
      </view>
      <!-- 功能区域 -->
      <view v-if="showOptions" class="options-container">
        <view class="option">
          <view class="option-icon">
            <img src="../static/wenjian.png" class="icon-image" />
          </view>
          <view class="option-text">文件</view>
        </view>
        <view class="option" @click="toggleModal">
          <view class="option-icon">
            <img
              src="../static/qiehuanzhinengti.png"
              style="width: 25px; height: 25px"
              class="icon-image"
            />
          </view>
          <view class="option-text">切换智能体</view>
        </view>
      </view>
    </view>
    <!-- 弹窗遮罩层 -->
    <view v-if="showModal" class="modal-mask" @click="closeModal"></view>
    <view v-if="showModal" class="modal-content">
      <scroll-view scroll-y class="modal-body">
        <view
          v-for="(item, index) in aiOptions"
          :key="index"
          class="modal-item"
          :class="{ selected: selectedIndex === index }"
          @click="selectOption(index)"
        >
          {{ item }}
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script setup>
import { nextTick, ref } from "vue";
import { fetchEventSource } from "@microsoft/fetch-event-source";
import BaseUpload from "@/components/base-upload.vue";
import BaseSelect from "@/components/base-select.vue";
const BASE_API = import.meta.env.VITE_APP_BASE_API;
const { proxy } = getCurrentInstance();
const ctrl = new AbortController();
const conversationId = ref();
const token = uni.getStorageSync("h5_token");
const messages = ref([]);
const inputText = ref("");
const showOptions = ref(false);
const isSending = ref(false);
const isTyping = ref(false); // 是否正在输入文字
const showModal = ref(false);
const chatbotRequest = ref({
  query: "",
  conversationId: null,
  userId: "1",
  fileUrl: "",
});
const aiOptions = ref([
  "码上飞",
  "泰罗智能体",
  "迪迦智能体",
  "盖亚智能体",
  "赛文智能体",
  "杰克智能体",
  "奥特曼智能体",
]);
const toggleModal = () => {
  showModal.value = !showModal.value;
};

// 关闭弹窗
const closeModal = () => {
  showModal.value = false;
};

// 选择选项
const selectOption = (index) => {
  selectedIndex.value = index;
  console.log("当前选中：", aiOptions.value[index]);
  closeModal(); // 选择后关闭弹窗
};
const selectedIndex = ref(0);
const data = { text: "aaa", value: "1" };
const inputAreaBottom = ref(12);
// 切换功能区域的显示与隐藏
const toggleOptions = () => {
  if (!isFocused.value) {
    // 仅在未获取焦点时允许旋转
    showOptions.value = !showOptions.value;
  }
  inputAreaBottom.value = showOptions.value ? 120 : 20;
  inputPlaceholder.value = "有什么问题尽管问";
};
const inputPlaceholder = ref("有什么问题尽管问");
const defaultIcon = "../static/tianjia.png"; // 默认图片
const focusedIcon = "../static/send.png"; // 获取焦点时的图片
const iconSrc = ref(defaultIcon);
const isFocused = ref(false);
const setPlaceholder = () => {
  inputPlaceholder.value = "有什么问题尽管问";
  iconSrc.value = focusedIcon;
  isFocused.value = true;
};
const onInputBlur = () => {
  if (!inputText.value.trim()) {
    inputPlaceholder.value = "有什么问题尽管问"; // 恢复默认占位符
    isFocused.value = false;
    iconSrc.value = "../static/tianjia.png"; // 恢复默认图标
  }
};
const currentClickHandler = computed(() => {
  return isFocused.value || isTyping.value ? sendMessage : toggleOptions;
});
onMounted(() => {
  messages.value.push({
    role: "ai",
    content:
      "halo，我是你的新朋友小飞飞，初次见面很开心，我拥有自动生成端到端应用程序的能力，小白三分钟也能开发出一款属于自己的APP哦，还在等什么，快来体验！",
  });
});
const fileInput = ref();
// 用于实时接收数据的 SseEmitter
let eventSource = null;
const emits = defineEmits(["send_message", "send_message"]);

const downLoadFileRequest = ref({
  content: "",
  conversationId: conversationId,
  difyId: null,
});

const exportToWord = async (text, index) => {
  if (text != null) {
    downLoadFileRequest.value.content = text;
    const res = await proxy.$api.chatbot.downLoadWord(
      JSON.stringify(downLoadFileRequest.value)
    );

    const byteCharacters = atob(res.data);
    const byteNumbers = new Array(byteCharacters.length)
      .fill(0)
      .map((_, i) => byteCharacters.charCodeAt(i));
    const byteArray = new Uint8Array(byteNumbers);

    const blob = new Blob([byteArray], {
      type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url; // 创建下载链接
    link.download = "chat_message.docx"; // 设置文件名
    link.click();
    // 释放内存
    window.URL.revokeObjectURL(url);
  }

  console.log("text and index", text, index);
};

// 发送消息
const sendMessage = (text) => {
  console.log(fileInput.value);
  if (!inputText.value.trim() || isSending.value) return;
  isSending.value = true; // 设置为发送中状态

  let text_query = JSON.parse(JSON.stringify(inputText.value));
  messages.value.push({ role: "user", content: text_query });

  if (fileInput.value) {
    chatbotRequest.value.fileUrl = fileInput.value;
  }
  const lastMessageId = computed(() => {
    if (messages.value.length === 0) return "";
    return `msg-${messages.value.length - 1}`;
  });
  // 更新 message 内容并自动滚动到最新的消息
  const scrollToBottom = () => {
    nextTick(() => {
      const chatHistory = document.querySelector(".chat-history");
      chatHistory.scrollTop = chatHistory.scrollHeight;
    });
  };
  watch(messages, (newMessages) => {
    if (newMessages.length) {
      scrollToBottom();
    }
  });
  // 开始通过 SSE 调用后端接口
  startSseConnection(inputText.value);
  inputText.value = "";
  fileInput.value = "";
};

// 使用 SSE 连接后端并处理数据
const startSseConnection = (query) => {
  if (eventSource) {
    eventSource.close(); // 如果已有连接，则关闭之前的连接
  }

  chatbotRequest.value.query = inputText.value;
  console.log("chatbotRequest", chatbotRequest.value);
  if (conversationId.value) {
    chatbotRequest.value.conversationId = conversationId.value;
  }

  const jsonData = JSON.stringify(chatbotRequest.value);
  fetchEventSource(BASE_API + "/chatbot", {
    method: "POST",
    openWhenHidden: true,
    headers: {
      "Content-Type": "application/json",
      "Cache-Control": "no-cache",
      Connection: "keep-alive",
      Authorization: `Bearer ${token}`,
    },
    signal: ctrl.signal,
    body: jsonData,
    onmessage: (event) => {
      try {
        const newMessage = JSON.parse(event.data);

        console.log(newMessage);
        if (newMessage.event === "workflow_started") {
          messages.value.push({ role: "ai", content: "" });
          if (newMessage.conversation_id != null) {
            conversationId.value = newMessage.conversation_id;
          }
        }
        if (newMessage.event === "message") {
          messages.value[messages.value.length - 1].content += newMessage.answer;
        }
        if (newMessage.event === "message_end") {
          isSending.value = false;
        }

        // 更新 message 内容并自动滚动到最新的消息
        nextTick(() => {
          const chatHistory = document.querySelector(".chat-history");
          chatHistory.scrollTop = chatHistory.scrollHeight;
        });
      } catch (e) {
        console.log("eeeee", e);
        ctrl?.abort();
        isSending.value = false;
      }
    },
    onerror: (err) => {
      console.error("SSE 错误:", err);
      isSending.value = false;
    },
    onclose: () => {
      isSending.value = false;
    },
  });
};

// 获取上传状态
function select(selectedFile) {
  fileInput.value = selectedFile;
  // 构建请求 URL
  console.log("selectedFile = ", selectedFile);
  console.log("selectedFile.tempFiles[0] = ", selectedFile.tempFiles[0]);
  console.log("选择文件：", selectedFile);
}

// 打字机效果
const typeWriterEffect = (content) => {
  const aiMessage = { role: "ai", content: "" };
  messages.value.push(aiMessage);

  let i = 0;
  aiMessage.content = "";

  const interval = setInterval(() => {
    aiMessage.content += content[i];
    i++;

    // 更新 message 内容并自动滚动到最新的消息
    nextTick(() => {
      const chatHistory = document.querySelector(".chat-history");
      chatHistory.scrollTop = chatHistory.scrollHeight;
    });

    if (i === content.length) {
      clearInterval(interval);
      isSending.value = false; // 回复完成后恢复发送按钮状态
    }
  }, 100); // 每100毫秒显示一个字符，模拟打字机效果
};
</script>

<style scoped>
.chat-page {
  display: flex;
  flex-direction: column;
  height: 100%;
  flex: 1;
  background-color: #fff;
  overflow: hidden;
  /* 防止溢出 */
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
  z-index: 10;
  /* 确保位于其他内容之上 */
}

.title {
  flex: 1;
  text-align: center;
  font-size: 18px;
  font-weight: bold;
}

.chat-history {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 10px;
  /* 避开输入框 */
}

.fixed-bottom-container {
  width: 100%;
  background-color: #fff;
  z-index: 1000;
  height: 60px;
  /* 确保层级最高 */
}
.on{
  width: 100%;
  background-color: #fff;
  z-index: 1000;
  height: 25vh;
}

.message-item {
  display: flex;
  margin-bottom: 10px;
}

/* AI 消息样式：头像在左，聊天内容在右 */
.message-ai {
  display: flex;
  justify-content: start;
  width: 100%;
  margin: 8px 0 8px 8px;
}

/* 用户消息样式：头像在右，聊天内容在左 */
.message-user {
  display: flex;
  justify-content: flex-end;
  width: 100%;
  margin-right: 8px;
}

.message-content {
  padding: 10px 15px;
  border-radius: 10px;
  /* 圆角效果 */
  font-size: 15px;
  line-height: 20px;
  max-width: 80%;
  /* 限制消息最大宽度 */
  text-align: center;
  /* 居中文本 */
  word-wrap: break-word;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  /* 添加阴影 */
}

/* AI 的聊天内容样式 */
.ai-content {
  background-color: #f5f5f5;
  color: #333;
  text-align: left;
  /* 左对齐文本 */
}

/* 用户的聊天内容样式 */
.user-content {
  background-color: #4072ee;
  color: white;
  text-align: left;
  /* 左对齐文本 */
  border: 1px solid #3058ba;
}

/* 底部聊天框 */
.input-area {
  display: flex;
  justify-content: center;
  /* 居中显示输入框 */
  align-items: center;
  left: 0;
  bottom: 12px;
  width: 95%;
  transition: bottom 0.3s ease;
  padding: 0 9px;
  margin-bottom: 10px;
}

/* 输入框容器 */
.input-container {
  position: relative;
  display: flex;
  align-items: center;
  width: 95%;
  /* 适配屏幕宽度 */
  max-width: 500px;
  height: 40px;
  border-radius: 15px;
  /* 圆角效果 */
  background-color: #ffffff;
  /* 背景色 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  /* 添加阴影 */
  overflow: hidden;
  padding: 0 10px;
}

/* 输入框 */
.input-box {
  flex: 10;
  height: 100%;
  border: none;
  outline: none;
  font-size: 14px;
  padding: 0 10px;
  border-radius: 15px;
  /* 圆角效果 */
  background-color: transparent;
  /* 背景透明 */
}

/deep/ .uni-input-wrapper {
  width: 90% !important;
}

/* 发送按钮图标 */
.send-icon {
  flex: 1;
  width: 40px;
  height: 40px;
  background-color: #ffffff;
  /* 按钮背景色 */
  border-radius: 50%;
  /* 圆形按钮 */
  cursor: pointer;
  transition: transform 0.2s ease;
  /* 缩放动画效果 */
  position: absolute;
  right: 4px;
  /* 靠右侧显示 */
  display: flex;
  /* 让内容居中对齐 */
  justify-content: center;
  align-items: center;
}

.send-icon:active {
  transform: scale(0.95);
  /* 点击时缩小 */
}

.send-iconFocused {
  flex: 1;
  width: 28px;
  height: 28px;
  background-color: #3273f3;
  /* 按钮背景色 */
  border-radius: 50%;
  /* 圆形按钮 */
  cursor: pointer;
  transition: transform 0.2s ease;
  /* 缩放动画效果 */
  position: absolute;
  right: 9px;
  /* 靠右侧显示 */
  display: flex;
  /* 让内容居中对齐 */
  justify-content: center;
  align-items: center;
}

.icon-imageFocused {
  width: 20px !important;
  height: 20px !important;
  padding: 0 10px;
}

.icon-image {
  width: 60%;
  /* 图片占按钮的60%大小 */
  height: 60%;
  /* 图片占按钮的60%大小 */
  object-fit: contain;
  /* 确保图片内容完整显示 */
}

.rotate {
  transform: rotate(45deg);
  /* 旋转90度 */
}

/* word 导出按钮*/
.export-btn {
  height: 30px;
  width: 100%;
  margin-top: 10px;
  /* 留出空间 */
  padding: 5px 10px;
  background-color: #4072ee;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 12px;
  align-self: flex-start;
  /* 对齐到左侧 */
}

.export-btn:hover {
  background-color: #3058ba;
}

.options-container {
  display: flex;
  justify-content: flex-start;
  /* 内容居中对齐 */
  gap: 16px;
  /* 功能块之间的间距 */
  width: 100%;
  max-width: 600px;
  padding: 0 10px;
  margin-bottom: 14px;
}

.option {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.option-icon {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 75px;
  height: 75px;
  border-radius: 10px;
  background-color: #f5f5f5;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.icon-image {
  width: 28px;
  /* 图标宽度 */
  height: 28px;
  /* 图标高度 */
  object-fit: contain;
}

.option-text {
  font-size: 14px;
  /* 文字大小 */
  color: #666;
  /* 浅灰色文字 */
  text-align: center;
  margin-top: 4px;
}

.modal-mask {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  /* 背景变暗 */
  z-index: 999;
  /* 层级最高 */
}

/* 弹窗内容 */
.modal-content {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: #ffffff;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
  z-index: 1000;
  /* 高于遮罩层 */
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  animation: slide-up 0.3s ease-in-out;
  /* 从下往上弹出 */
}

.modal-body {
  max-height: 300px;
  /* 限制高度 */
  overflow-y: auto;
  padding: 30px 0;
  /* 调整顶部和底部的间距 */
}

/* 弹窗选项样式 */
.modal-item {
  padding: 10px 0;
  /* 调整上下内边距，使每个选项更紧凑 */
  text-align: center;
  font-size: 14px;
  /* 字体调小 */
  color: #333;
  cursor: pointer;
  transition: background-color 0.2s ease, border-color 0.2s ease;
  position: relative;
  /* 便于边框控制 */
}

/* 默认无边框 */
.modal-item::before,
.modal-item::after {
  content: "";
  display: block;
  position: absolute;
  left: 0;
  right: 0;
  height: 1px;
  background-color: transparent;
  /* 初始无边框 */
  transition: background-color 0.2s ease;
}

/* 被选中状态：上下边框 */
.modal-item.selected::before {
  top: 0;
  background-color: #ccc;
}

.modal-item.selected::after {
  bottom: 0;
  background-color: #ccc;
}

/* 选中状态：背景色 */
.modal-item.selected {
}

/* 弹窗从下往上动画 */
@keyframes slide-up {
  from {
    transform: translateY(100%);
  }

  to {
    transform: translateY(0);
  }
}
</style>
