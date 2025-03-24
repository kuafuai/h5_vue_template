<template>
  <view class="image-container">
    <image :src="get_resource_url('static/fa5-spinner.png')" alt="Rotating Image" class="rotating-image" />
  </view>
</template>

<script setup>
import get_resource_url from '../config/static_config';
</script>

<style scoped>

.image-container {
  /*position: absolute;*/
  /*top: 27rpx;*/
  /*left: 50%;  !* 让左侧对齐到 .title 的中点 *!*/
  /*transform: translateX(-50%); !* 向左移动 50% 使其居中 *!*/
  width: 30rpx;
  height: 30rpx;
//display: flex;
//justify-content: center;
//align-items: center;
}

.rotating-image {
  width: 20rpx; /* 调整图片大小 */
  height: 20rpx;
  animation: rotate 2s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>