<template>
  <view class="video-container">
    <!-- 使用 v-for 遍历并展示所有视频 -->
    <view v-for="(videoUrl, index) in videoUrls" :key="index" class="video-item">
      <video
          :src="videoUrl"
          controls
          @ended="onVideoEnded(index)"
      ></video>
    </view>

  </view>
</template>

<script setup>
import { ref, watch } from 'vue'

// 接收传入的参数
const props = defineProps({
  videoUrls: {
    type: String,
    default: ''
  },
  posterUrl: {
    type: String,
    default: ''
  }
})

// 将视频 URL 字符串按逗号分割为数组
const videoUrls = ref(props.videoUrls.split(',').map(url => url.trim()))

// 视频播放结束事件处理函数
const onVideoEnded = (index) => {
  console.log(`视频 ${index + 1} 播放结束`)
  // 可以根据需求处理结束事件
}

// 监听传入的 videoUrls 变化
watch(() => props.videoUrls, (newUrls) => {
  videoUrls.value = newUrls.split(',').map(url => url.trim())
})
</script>

<style scoped>
.video-container {
  /*position: relative;*/
  /*width: 100%;*/
  /*height: 100%;*/
}

.video-item {
  /*margin-bottom: 20px;*/
}



.no-video {
  text-align: center;
  color: #999;
}
</style>
