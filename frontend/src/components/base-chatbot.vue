
<template>
  <view class="chat-page"
        :style="{ backgroundColor: isInputFocused ? '#ffffff' : '#f3f2f5', overflow: isInputFocused ? 'hidden' : 'auto' }">
    <scroll-view ref=" scrollView" :scroll-into-view="lastMessageId" class="chat-history" scroll-y
                 :scroll-with-animation="true" @scroll="onScroll" v-if="isInputFocused">
      <view v-for="(msg, index) in messages" :key="index" :id="`msg-${index}`" class="message-item">
        <!-- AI 聊天内容：头像在左，内容在右 -->
        <view v-if="msg.role === 'ai'" class="message-ai">
          <view class="ai-container" :class="{ 'no-backgroundcolor': sentFiles && isSent }">
            <view class="message-content ai-content" :class="{ 'no-shadow': sentFiles && isSent }">
              <markdown-render :markdown="msg.content" />
            </view>
            <view v-if="index === messages.length - 1">
              <view v-if="sentFiles && isSent" class="divider"></view>
              <view class="button-container" v-if="sentFiles && isSent">
                <view v-if="sentFiles && isSent" @click="exportToWord(msg.content)" class="button-container-word">
                  <image src="../static/littleword.png" mode="scaleToFill" />
                  <view style="color: #3273f3; font-size: 14px">导出word</view>
                </view>

                <!-- <view class="button-container-fresh">
                  <image src="../static/shuaxin.png" mode="scaleToFill" />
                </view> -->
              </view>
            </view>
          </view>
          <!-- AI 聊天内容 -->
          <!-- 添加气泡按钮部分 -->

          <!-- <view class="additional-buttons" v-if="showAdditionalButtons">
            <view class="additional-button" v-for="(reply, replyIndex) in questions" :key="replyIndex"
              @click="sendMessage(reply, 1)">
              {{ reply }}
            </view>
          </view> -->
        </view>

        <!-- 用户聊天内容：头像在右，内容在左 -->
        <view v-else class="message-user">
          <view v-if="msg.file" class="message-file">
            <view class="message-file-icon">
              <image src="../static/wenjianleixing.png" alt="" />
            </view>
            <view class="message-file-name">{{ msg.file.name }}</view>
          </view>

          <view class="message-content user-content">{{ msg.content }}</view>
          <!-- 用户聊天内容 -->
        </view>
      </view>
    </scroll-view>
    <!-- 一键到底部按钮 -->
    <!-- <view class="scroll-to-bottom-btn" v-if="!isAtBottom" @click="scrollToBottom">
      <image src="../static/xiangxia.png" alt="到达底部" />
    </view> -->
    <view>
      <!-- 智能体装修 -->

      <view :class="isAnMation ? 'decoration-boxs decoration-box-empty' : 'decoration-box'">
        <!-- <view v-if="!isInputFocused" @click="shareAdd">
          <image src="../static/fenxianglianjie.png" style="width: 1.56rem;height: 1.56rem;position: fixed;
    right: 0.31rem;
    top: 3.63rem;
    z-index: 9999;" mode="scaleToFill" />
        </view> -->

        <view class="decoration-head" v-if="!isInputFocused">
          <view class="difyTitle">
            <text class="difyCont">{{ difyTitle }}</text>
            <view v-if="!isInputFocused" @click="shareAdd" class="difyTitle-container">
              <image src="../static/fenxianglianjie.png"
                     style="width: 1rem;height: 1rem; color: #3273f3; " mode="scaleToFill" />
              <text style="font-size: 0.88rem;">分享</text>
            </view>
          </view>
          <view class="initialSentenceContent"> {{ initialSentenceContent }}</view>
        </view>
        <view :class="showOptions ? 'fixed-bottom-container on' : 'fixed-bottom-container'
          " :style="uploadedFile ? { height: '38vh' } : {}">
          <!-- 输入框区域 -->
          <view class="input-area" :style="{ bottom: inputAreaBottom + 'px' }">
            <!-- <base-upload v-model="fileInput"/> -->
            <view class="outer-container" :class="{ 'with-file': uploadedFile }">
              <!-- 文件显示区域 -->
              <view v-if="uploadedFile" class="file-display">
                <image class="file-icon" src="../static/wenjianleixing.png"></image>
                <text class="file-name">{{ uploadedFile.name }}</text>
                <view class="delete-icon" @click="deleteFile">
                  <image src="../static/shanchu.png" alt="" />
                </view>
              </view>
              <view v-if="uploadedFile" class="divider"></view>
              <view class="input-container" :class="{ 'no-border': uploadedFile }">
                <!-- 输入框 -->
                <uni-easyinput :focus="isFocus" v-model="inputText" :placeholder="inputPlaceholder"
                               @focus="setPlaceholder" @confirm="sendMessageFn('1')" :confirmType="done" :disabled="isSending"
                               @blur="onInputBlur" />

                <!-- 发送按钮 -->
                <view class="send-icon">
                  <view class="send-icon" @click="currentClickHandler" :class="[
                    isFocused || uploadedFile ? 'send-iconFocused' : 'send-icon',
                    inputText.trim() === '' && (isFocused || uploadedFile || isTrue) ? 'send-iconDisabled' : ''
                  ]">
                    <image :src="finalIconSrc" class="icon-image" :class="{
                      'icon-image': !isFocused && !isTrue && !uploadedFile /* 默认样式 */,
                      'icon-imageFocused': isFocused || isTrue || uploadedFile /* 聚焦时样式或有文件时样式 */,
                      rotate: !isFocused && !uploadedFile && showOptions /* 点击时旋转，但仅在没有文件上传的情况下 */,
                    }" />
                  </view>
                </view>
              </view>
            </view>
            <!-- 功能区域 -->
            <view v-if="showOptions" class="options-container">
              <view class="option" @click="chooseFile">
                <view class="option-icon">
                  <image src="../static/wenjian.png" mode="widthFix" />
                </view>
                <view class="option-text">文件</view>
              </view>

              <!-- <view class="option" @click="toggleModal">
                <view class="option-icons">
                  <image src="../static/qiehuanzhinengti.png" mode="widthFix" />
                </view>
                <view class="option-text">切换智能体</view>
              </view> -->
            </view>
          </view>
          <!-- 弹窗遮罩层 -->
          <view v-if="showModal" class="modal-mask" @click="closeModal"></view>
          <view v-if="showModal" class="modal-content">
            <scroll-view scroll-y class="modal-body">
              <view v-for="(item, index) in aiOptions" :key="index" class="modal-item"
                    :class="{ selected: selectedIndex === index }" @click="selectOption(index)">
                {{ item }}
              </view>
            </scroll-view>
          </view>
        </view>

        <view class="click-box" v-if="!isInputFocused">
          <!-- 标题 -->
          <view class="click-box-header">不知道问点啥？看看这</view>
          <view class="click-box-content">
            <view class="card special-card-1" v-if="questions[0]">
              <view class="card-text">
                <text class="vertical"
                      style="height: 2.625rem;display:flex;align-items: center;justify-content: center;">
                  {{ questions[0] }}
                </text>
              </view>
              <view class="card-button " style="margin: auto;" @click="sendMessage(questions[0], 1)">试一试</view>
            </view>
            <view class="card-group" style="margin: 12px 0;">
              <view class="card special-card-2" v-if="questions[1]">
                <view class="card-text">
                  <text class="vertical"
                        style="height:2.625rem;display:flex;align-items: center;justify-content: center;">
                    {{ questions[1] }}
                  </text>
                </view>
                <view class="card-button" style="margin: auto;" @click="sendMessage(questions[1], 1)">试一试</view>
              </view>
              <view class="card special-card-3" v-if="questions[2]">
                <view class="card-text">
                  <text class="vertical"
                        style="height: 2.625rem;display:flex;align-items: center;justify-content: center;">
                    {{ questions[2] }}
                  </text>
                </view>
                <view class="card-button" style="margin: auto;" @click="sendMessage(questions[2], 1)">试一试</view>
              </view>
            </view>
            <view class="bottom-default-card" v-if="questions.length > 3">
              <view class=" default-card" v-for="(reply, replyIndex) in questions.slice(3)" :key="replyIndex">
                <view class="card-cont">{{ reply }}</view>
                <view class="card-button" @click="sendMessage(reply, 1)" style="margin-right: 8px;">试一试</view>
              </view>
            </view>
          </view>
        </view>
      </view>

    </view>

  </view>
</template>

<script setup>
import { nextTick, ref, watch } from "vue";
import { fetchEventSource } from "@microsoft/fetch-event-source";
import MarkdownRender from "./MarkdownRender.vue";
// import BaseUpload from "@/components/base-upload.vue";
// import BaseSelect from "@/components/base-select.vue";
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
const showAdditionalButtons = ref(true);
const chatbotRequest = ref({
  query: "",
  conversationId: null,
  userId: "1",
  fileUrl: "",
});

const isTrue = ref(true)
// 输入框书否聚焦
const isFocus = ref(false);

const fileList = ref();
const isSent = ref(false);
const aiOptions = ref(["码上飞"]);
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
  inputPlaceholder.value = "有什么问题尽管问...";
};
const inputPlaceholder = ref("有什么问题尽管问...");
const defaultIcon = "../static/tianjia.png"; // 默认图片
const focusedIcon = "../static/send.png"; // 获取焦点时的图片
const iconSrc = ref(defaultIcon);
const isFocused = ref(false);
const lastMessageId = ref("");
const finalIconSrc = computed(() => {
  // 优先级：uploadedFile > isFocused > 默认 iconSrc
  if (uploadedFile.value) {
    return focusedIcon; // 文件上传成功时，显示发送图标
  } else if (isFocused.value || isTrue.value) {
    return focusedIcon; // 输入框聚焦时，使用 iconSrc 逻辑
  } else {
    return defaultIcon; // 默认图片
  }
});


const isInputFocused = ref(false);
const isAnMation = ref(false);
const setPlaceholder = () => {
  inputPlaceholder.value = "有什么问题尽管问...";
  iconSrc.value = focusedIcon;
  isFocused.value = true;
  // isInputFocused.value = true;
};
const onInputBlur = () => {
  if (!inputText.value.trim()) {
    inputPlaceholder.value = "有什么问题尽管问..."; // 恢复默认占位符
    isFocused.value = false;
    iconSrc.value = "../static/tianjia.png"; // 恢复默认图标

  }
};

// 有没有上传文件  uploadedFile.value
// 输入框是否获取焦点  isFocused.value
// 是否正在输入文字  isTyping.value

// 判断是否点击回车
const enterNum = ref("");

// const currentClickHandler = computed(() => {
// console.log(enterNum.value,"enterNum.value")
//   return uploadedFile.value || isFocused.value || isTyping.value
//     ? sendMessage(null, enterNum.value)
//     : toggleOptions;
// });

const currentClickHandler = () => {
  console.log(enterNum.value, "enterNum.value");
  if (uploadedFile.value || isFocused.value || isTyping.value || isTrue.value) {
    sendMessage(null, 1);
  } else {
    toggleOptions();
  }
};

// 点击回车把enterNum置为1

const sendMessageFn = (e) => {
  if (e == 1 && !isTyping.value) {
    enterNum.value = 1;
    sendMessage(null, enterNum.value);
  }
};

const fileInput = ref("");
const uploadedFile = ref(null);
let isAtBottom = ref(true);
const sentFiles = ref([]);
// 选择文件
const chooseFile = () => {
  uni.chooseFile({
    count: 1, // 限制只能选择一个文件
    extension: [".doc", ".docx"],
    success: (res) => {
      console.log("文件选择成功", res);
      const tempFilePath = res.tempFilePaths[0]; // 获取临时文件路径

      // 设置临时文件信息到 uploadedFile
      uploadedFile.value = {
        name: res.tempFiles[0].name || "未命名文件",
        path: tempFilePath,
      };

      // 调用上传文件方法
      uploadFile(tempFilePath);
    },
    fail: (err) => {
      console.error("文件选择失败", err);
    },
  });
};

// 上传文件
const uploadFile = (filePath) => {
  uni.uploadFile({
    url: import.meta.env.VITE_APP_BASE_API + "/common/upload", // 替换为你的实际上传接口
    filePath: filePath,
    name: "file", // 文件字段名
    success: (res) => {
      // 解析服务器返回的信息
      const response = JSON.parse(res.data);

      // 更新 uploadedFile 对象，保存服务器返回的文件信息
      uploadedFile.value = {
        ...uploadedFile.value,
        // url: response.data.url,
        // id: response.data.id,
      };
      if (response.data) {
        fileInput.value = response.data.url;
      }
      console.log("res", res);
      isFocus.value = true;
      console.log("上传成功", uploadedFile.value);
    },
    fail: (err) => {
      console.error("上传失败", err);
    },
  });
};

// 删除文件
const deleteFile = () => {
  // 删除已保存的文件信息
  uploadedFile.value = null;
};
// 用于实时接收数据的 SseEmitter
let eventSource = null;
const emits = defineEmits(["send_message", "send_message"]);
const difyTitle = ref("");
const initialSentenceContent = ref("");
const questions = ref([
  "帮我写一个小作文",
  "帮我写一个码上飞",
  "码上飞很不错，我也想做一个码上飞",
]);
const showContent = ref(true)
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
watch(
    messages,
    (newMessages) => {
      if (isAtBottom.value) {
        scrollToBottom(); // 如果当前在底部，则新增消息时滚动到底部
      }
    },
    { deep: true }
);

// 更新 message 内容并自动滚动到最新的消息
const scrollToBottom = () => {
  uni.pageScrollTo({
    scrollTop: document.body.scrollHeight, // 滚动到页面的最底部
    duration: 300, // 滚动持续时间
  });
};
const onScroll = () => { };

const initialSentence = async () => {
  const res = await proxy.$api.dify_config.get(1);


  if (res.data && res.data.initialSentence) {
    initialSentenceContent.value = res.data.initialSentence;
    // messages.value.push({
    //   role: "ai",
    //   content: res.data.initialSentence,
    // });
  } else {
    /* messages.value.push({
       role: "ai",
       content:
         "hello，我是你的新朋友小飞飞，初次见面很开心，我拥有自动生成端到端应用程序的能力，小白三分钟也能开发出一款属于自己的APP哦，还在等什么，快来体验！",
     });*/
  }
  if (res.data.difyTitle) {
    difyTitle.value = res.data.difyTitle; // 存储 difyTitle
  }
  if (res.data.questions) {
    let jsonQuestions = res.data.questions;
    if (jsonQuestions.includes("'")) {
      // 替换单引号为双引号
      jsonQuestions = jsonQuestions.replace(/'/g, '"');
    }

    // 将字符串转换为数组
    let arr = JSON.parse(jsonQuestions);
    questions.value = arr;
    console.log("questions", questions.value);
  }
};

onMounted(async () => {
  scrollToBottom();
  await initialSentence();
});
// 发送消息
const sendMessage = (text, isEnter) => {
  console.log(text, isEnter);
  if (isTyping.value && isEnter == "1" ) {
    console.log("正在输入，按下回车或空格时不会发送");
    return;
  }
  if (isEnter == "1" && (inputText.value.trim() || text)) {
    isAnMation.value = true;
    setTimeout(() => {
      isInputFocused.value = true;
    }, 390)
    isTrue.value = true
    if ((!inputText.value.trim() && !text) || isSending.value) return;
    isSending.value = true; // 设置为发送中状态
    const currentFile = uploadedFile.value ? { ...uploadedFile.value } : null;

    if (fileInput.value) {
      console.log("====", fileInput.value);
      chatbotRequest.value.fileUrl = fileInput.value;
    }

    messages.value.push({
      role: "user",
      content: inputText.value ? inputText.value : text,
      file: currentFile,
    });
    if (currentFile) {
      sentFiles.value.push(currentFile); // 保存已发送文件
    }

    // 开始通过 SSE 调用后端接口
    startSseConnection(inputText.value ? inputText.value : text);
    isSent.value = true;
    inputText.value = "";
    fileInput.value = "";
    uploadedFile.value = null; // 清空文件信息
    deleteFile();
    showAdditionalButtons.value = false;
    enterNum.value = 0;
  }
};
const getFileName = (fileUrl) => {
  return fileUrl.split("/").pop(); // 提取路径最后一部分作为文件名
};
// 使用 SSE 连接后端并处理数据
const startSseConnection = (query) => {
  if (eventSource) {
    eventSource.close(); // 如果已有连接，则关闭之前的连接
  }

  chatbotRequest.value.query = query;
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
        if (newMessage.event === "workflow_started") {
          messages.value.push({ role: "ai", content: "" });
          if (newMessage.conversation_id != null) {
            conversationId.value = newMessage.conversation_id;
          }
        }
        if (newMessage.event === "message") {
          messages.value[messages.value.length - 1].content +=
              newMessage.answer;
          // console.log('msg-' + (messages.value.length - 1), "'msg-'+messages.value.length - 1")
          // lastMessageId.value = 'msg-' + (messages.value.length - 1)
        }
        if (newMessage.event === "message_end") {
          isSending.value = false;

          lastMessageId.value = "msg-" + (messages.value.length - 1);
          isTrue.value = false
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

const shareAdd = async () => {
  uni.setClipboardData({
    data: location.href,
    success: () => {
      uni.showToast({
        title: '         复制成功，快去分享应用吧！          ',
        icon: 'success',
      });
    },
    fail: (err) => {
      uni.showToast({
        title: '         链接复制失败，请重试         ',
        icon: 'none',
      });
      console.error('复制失败:', err);
    },
  });
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
::v-deep .uni-easyinput__content {
  background: #fff !important;
}

.chat-page {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #fff;
  overflow: hidden;
  flex: 1;
  /* 防止溢出 */
}
.chat-history{
  padding-top: 1.3125rem;
  box-sizing: border-box;
}
.decoration-box{
  padding-top: 0.4375rem;
  box-sizing: border-box;
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
  z-index: 1000;
  height: 75px;
}

.on {
  width: 100%;
  background-color: #fff;
  z-index: 1000;
  height: 28vh;
}

.outer-container {
  display: flex;
  flex-direction: column;
  /* 文件和输入框上下排列 */
  width: 100%;
  max-width: 500px;
  margin: 0 auto;
  /* 居中 */
  border-radius: 15px;
  /* 圆角 */
  /* background-color: #ffffff; */
  /* 白色背景 */
  transition: all 0.3s ease;
  /* 平滑过渡 */
}

.outer-container.with-file {
  border: 1px solid #ddd;
  /* 有文件时显示边框 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  /* 添加阴影 */
}

.file-display {
  position: relative;
  display: flex;
  align-items: center;
  padding: 3px;
  margin: 5px;
  margin-bottom: 0px;
  margin-top: 7px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: rgba(255, 255, 255, 1) !important;
  width: 45%;
}

.file-icon {
  width: 30px;
  height: 35px;
  margin-right: 2px;
  flex: 2;
}

.file-name {
  flex: 6;
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 12px;
}

.delete-icon {
  flex: 1;
}

.delete-icon image {
  width: 12px;
  height: 12px;
  cursor: pointer;
  position: absolute;
  top: 4px;
  right: 4px;
}

.message-item {
  display: flex;
  margin-bottom: 10px;
}

/* AI 消息样式：头像在左，聊天内容在右 */
.message-ai {
  display: flex;
  justify-content: start;
  flex-direction: column;
  width: 100%;
  margin: 8px 0 8px 8px;
}

.ai-container {
  display: flex;
  flex-direction: column;
  border-radius: 10px;
  /* 圆角效果 */
  font-size: 15px;
  line-height: 20px;
  max-width: 98%;
  /* 限制消息最大宽度 */
  text-align: center;
  /* 居中文本 */
  word-wrap: break-word;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  /* 添加阴影 */
}

.no-backgroundcolor {
  background-color: #f5f5f5;
}

.button-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 7px 13px;
}

.button-container-word {
  height: 24px;
  display: flex;
  background-color: rgba(223, 229, 253, 1);
  border-radius: 5px;
  align-items: center;
  padding: 3px;
  cursor: pointer;
}

.button-container-word image {
  width: 21px;
  height: 21px;
}

.button-container-fresh {
  width: 24px;
  height: 24px;
  background-color: rgba(223, 229, 253, 1);
  border-radius: 5px;
  padding: 3px;
  cursor: pointer;
}

.button-container-fresh image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 用户消息样式：头像在右，聊天内容在左 */
.message-user {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-left: auto;
  /* 将盒子右对齐 */
  margin-right: 8px;
  /* 与屏幕右边留一点间距 */
  max-width: 80%;
}

.message-content {
  padding: 9px 13px;
  border-radius: 10px;
  /* 圆角效果 */
  font-size: 16px;
  line-height: 20px;
  max-width: 100%;
  /* 限制消息最大宽度 */
  text-align: center;
  /* 居中文本 */
  word-wrap: break-word;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  /* 添加阴影 */
}

.no-shadow {
  box-shadow: none;
}

.message-file {
  display: flex;
  align-items: center;
  margin-bottom: 7px;
  background-color: #f9f9f9;
  /* 文件展示背景色 */
  padding: 5px 5px 5px 6px;
  border-radius: 8px;
  max-width: 100%;
}

.message-file-icon image {
  margin-right: 8px;
  width: 30px;
  height: 30px;
}

/deep/ .is-input-border {
  border: none !important;
}

.additional-buttons {
  width: 90%;
  gap: 9px;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: flex-start;
  font-size: 14px;
}

.additional-button {
  padding: 9px;
  background-color: rgba(255, 255, 255, 1);
  border: 1px solid #dcdcdc;
  border-radius: 10px;
  text-align: center;
  cursor: pointer;
  transition: background-color 0.2s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.additional-button:hover {}

.message-file-name {
  font-size: 14px;
  color: #333;
  white-space: nowrap;
  /* 禁止换行 */
  overflow: hidden;
  /* 超出内容隐藏 */
  text-overflow: ellipsis;
  /* 超出部分显示省略号 */
  max-width: 200px;
}

/* AI 的聊天内容样式 */
.ai-content {
  background-color: #f5f5f5;
  color: #101010;
  text-align: left;
  /* 左对齐文本 */
  font-weight: 520;
  font-family: PingFangSC-regular;
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
  flex-direction: column;
  /* 居中显示输入框 */
  align-items: center;
  left: 0;
  bottom: 12px;
  width: 95%;
  transition: bottom 0.3s ease;
  padding: 0 9px;
  margin-bottom: 10px;
  align-items: flex-start;
  transition: padding-top 0.3s ease;
}

.uni-easyinput__placeholder-class {
  font-size: 14px !important;
}

/* 输入框容器 */
.input-container {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
  /* 适配屏幕宽度 */
  max-width: 500px;
  height: 45px;
  border-radius: 15px;
  /* 圆角效果 */
  background-color: #ffffff;
  /* 背景色 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  /* 添加阴影 */
  overflow: hidden;
  transition: height 0.3s ease;
}

.input-container.no-border {
  border: none;
  /* 当有文件时去掉边框 */
  box-shadow: none;
  /* 移除阴影 */
}

.divider {
  width: 100%;
  height: 1px;
  /* 分割线高度 */
  background-color: #ddd;
  /* 分割线颜色 */
  margin: 8px 0;
  /* 分割线上下间距 */
}

input {
  flex: 1;
  height: 100%;
  border: none;
  outline: none;
  font-size: 14px;
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

.send-iconDisabled {
  background-color: #efefefd0;
  width: 28px;
  height: 28px;
  right: 9px
}

.icon-imageFocused {
  background-image: url(../static/send.png) !important;
  background-size: 100% 100%;
  /* 背景图片占据整个盒子 */
  background-repeat: no-repeat;
  /* 防止背景图片重复 */
  width: 20px !important;
  height: 20px !important;
  padding: 0;
  /* 去除内边距，确保背景图片充满盒子 */
}

.disabled {
  background: #fff;
}

.icon-image {
  background-image: url(../static/tianjia.png);
  background-size: 100% 100%;
  /* 背景图片占据整个盒子 */
  background-repeat: no-repeat;
  /* 防止背景图片重复 */
  width: 100%;
  /* 盒子宽度占父容器大小 */
  height: 100%;
  /* 盒子高度占父容器大小 */
  object-fit: cover;
  /* 确保图片比例显示并填满盒子 */
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
  margin-top: 0.8rem;
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

.option-icon image {
  width: 40%;
  height: 25px;
}

.option-icons {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 75px;
  height: 75px;
  border-radius: 10px;
  background-color: #f5f5f5;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.option-icons image {
  width: 30%;
  height: 25px;
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
.modal-item.selected {}

/* 弹窗从下往上动画 */
@keyframes slide-up {
  from {
    transform: translateY(100%);
  }

  to {
    transform: translateY(0);
  }
}

.hidden-file-input {
  display: none;
}

.scroll-to-bottom-btn {
  position: fixed;
  bottom: 80px;
  left: 50%;
  transform: translateX(-50%);
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  cursor: pointer;
  transition: transform 0.3s ease, opacity 0.3s ease;
  background-color: #fff;
  z-index: 999;
}

.scroll-to-bottom-btn image {
  width: 18px;
  height: 18px;
  object-fit: contain;
}

.scroll-to-bottom-btn.hidden {
  opacity: 0;
  pointer-events: none;
  /* 隐藏按钮时无法点击 */
}

.decoration-boxs {
  background-color: #f3f2f5;
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  flex: 1;
  animation: slideAndFade 0.4s linear;
}

.decoration-box {
  background-color: #f3f2f5;
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  flex: 1;

}

@keyframes slideAndFade {
  0% {
    transform: translateY(-0px);
    opacity: 1;
  }

  10% {
    transform: translateY(10vh);
    opacity: 0.95;
  }

  20% {
    transform: translateY(20vh);
    opacity: 0.7;
  }

  50% {
    transform: translateY(50vh);
    opacity: 0.6;
  }

  60% {
    transform: translateY(60vh);
    opacity: 0.3;
  }

  90% {
    transform: translateY(90vh);
    opacity: 0.1;
  }

  100% {
    transform: translateY(100vh);
    opacity: 0;
  }
}

.decoration-box-empty {
  background-color: #fff;
}

.decoration-head {
  display: flex;
  justify-content: flex-start;
  flex-direction: column;
  width: 100%;
  padding: 14px 10px 14px 20px;
  box-sizing: border-box;
}

.difyTitle {
  font-size: 20px;
  color: #101010;
  margin-bottom: 6px;
  display: inline-block;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.difyCont {
  background: linear-gradient(to bottom,#F3F2F5 40% , #DFE5FD 50%, #FDCBF1 100%);
  line-height: 28px;
  font-weight: 600;
  font-size: 1.25rem;
}

.initialSentenceContent {
  font-size: 0.88rem;
  color: rgba(16, 16, 16, 1);
  font-family: PingFangSC-regular;
  line-height: 20px;
  margin-bottom: 6px;
  font-style: italic;
  font-weight: 500;
}

.click-box {
  width: 97%;
  /* padding: 0 9px; */
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  margin: 0 auto;
}

.click-box-content {
  font-size: 14px;
  font-weight: 500;
}

.click-box-header {
  height: 1.25rem;
  line-height: 20px;
  color: #03182b;
  font-size: 0.875rem;
  text-align: left;
  font-family: PingFangSC-medium;
  font-weight: 600;
  margin-bottom: 8px;
}

.card {
  width: 100%;
  border-radius: 10px;
  padding: 12px 14px;
  box-sizing: border-box;
}

.card-group {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  gap: 16px;
  width: 100%;
}

.special-card-1,
.special-card-2,
.special-card-3 {
  flex: 1;
  align-items: center;
  text-align: center;
}

.special-card-1 {
  background: linear-gradient(to right, #e7f3ff, #fde7f3);
}

.special-card-2 {
  background: linear-gradient(132.06deg, rgba(255, 255, 255, 1) 1.02%, rgba(253, 203, 241, 0.53) 99.77%);
}

.special-card-3 {
  background: linear-gradient(133.74deg, rgba(223, 229, 253, 1) 3.95%, rgba(229, 241, 240, 1) 101.85%);
}

.bottom-default-card {
  display: flex;
  flex-direction: column;
  width: 100%;
  border-radius: 10px;
  padding: 8px 6px;
  box-sizing: border-box;
  background: linear-gradient(132.06deg, rgba(252, 242, 249, 1) 1.02%, rgba(223, 229, 253, 1) 99.77%);
}

.default-card {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  padding: 7px 0;
  box-sizing: border-box;
}

.card-button {
  width: 4.5rem;
  height: 1.5rem;
  font-size: 0.75rem;
  color: #3273f3;
  background-color: #fff;
  padding: 3px 10px;
  border-radius: 6px;
  /* 按钮圆角 */
  cursor: pointer;
  /* margin-top: auto; */
  text-align: center;
  box-sizing: border-box;
}

.card-text {
  margin: 10px;
  text-overflow: ellipsis;
  /* display: -webkit-box; */
  /* -webkit-box-orient: vertical; */
  /* -webkit-line-clamp: 2; */
  /* 这里是超出几行省略 */
  /* overflow: hidden; */
  /* min-height: 39px; */
}


::v-deep .card-text text {
  word-wrap: break-word;
  word-break: break-all;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  /* 这里是超出几行省略 */
  overflow: hidden;
  color: rgba(3, 24, 43, 1);
  font-size: 0.88rem;
  text-align: left;
  font-family: SourceHanSansSC-regular;
}

::v-deep .card-text .vertical span {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  /* 这里是超出几行省略 */
  overflow: hidden;
}
.difyTitle-container{
  width: 4.06rem;
  height: 1.56rem;
  background: linear-gradient(131.82deg, rgba(255, 255, 255, 1) 5.19%, rgba(253, 203, 241, 1) 100.78%);
  border-radius: 0.31rem;
  display:flex;
  align-items:center;
  justify-content:center;
  gap:0.15rem;
  color:#1684fc;
  line-height: 28px;
  padding: 2px 0 2px 0;

}

</style>

    