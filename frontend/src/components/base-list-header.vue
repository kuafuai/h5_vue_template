<template>
  <view class="container">
    <view class="left">
      <view class="toolbar">工具栏</view>
      <view class="nickname">{{ nickname ? nickname : "啊啥的就看哈说" }}</view>
      <view class="description">{{
          description
              ? description
              : "啊谁来打卡是克林顿将阿拉山口家的辣椒大连锁酒店"
        }}</view>
    </view>
    <view class="right">
      <view class="background-image" :style="{background: url(get_resource_url('static/hua.png'))+' '+ 'no-repeat'}"></view>
    </view>
  </view>
</template>
<script>
import get_resource_url from '../config/static_config';
export default {
  options: {
    styleIsolation: 'shared', // 解除样式隔离
  }
};
</script>
<script setup>
import {defineProps, ref} from "vue";

const props = defineProps({
  nickname: {
    type: String,
    default: '',
  },
  description: {
    type: String,
    default: '',
  },
});
// const nickname = ref("张三"); // 替换为实际昵称
// const description = ref("这是描述信息"); // 替换为实际描述
</script>

<style scoped>
.container {
  display: flex;
  width: 100%;
  height: 20vh; /* 调整为适合的高度 */
  background-color: rgb(93, 95, 239); /* 整体背景色 */
  position: sticky;
  top: 0px;
  z-index: 9;
  /* overflow: hidden; */
}

.left {
  width: 50%;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  padding: 16px;
  box-sizing: border-box;
  color: #fff;
}

.toolbar {
}

.info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: calc(100% - 32px); /* 计算剩余高度 */
  gap: 27px;
}

.nickname {
  font-size: 1.125rem;
  font-weight: bold;
  margin: 1rem 0 0 0;
}

.description {
  font-size: 0.75rem;
  margin: 0.4rem 0;
}

.right {
  width: 50%;
  position: relative;
  overflow: hidden;
  height: 100%; /* 确保 right 盒子高度为 100% */
}

.background-image {
   /* 确保路径正确 */
  background-size: contain; /* 或 contain，根据需要选择 */
  background-position: bottom right;
  position: absolute;
  bottom: -10%;
  right: 0;
  width: 100%;
  height: 100%; /* 背景图高度占 70% */
}
</style>