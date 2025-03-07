<template>
  <view>
    <!-- 自定义文件选择区域 -->
    <view class="content" @click="chooseVideo">
      <view class="VideoBox">
        <image src="../static/picture.png" style="width:80rpx;height:80rpx" alt=""></image>
        <view style="line-height: 17px;color: rgba(154,154,154,1);font-size: 0.75rem;margin-top:10rpx">
          点击录制/选择
        </view>
      </view>
    </view>

    <!-- 已选择的视频展示 -->
    <view v-if="resources.length > 0" class="video-list">
      <view v-for="(video, index) in resources" :key="index" class="video-item-container">
        <video :src="video" controls class="video-item"></video>
        <view @click="deleteVideo(index)" class="delete_but">X</view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from "vue";

// 定义响应式数据
const resources = ref([]); // 存储上传后的视频数据

const fromdatasValue = defineModel()
// 监听外部传入的 image_model 数组变化
watch(fromdatasValue, (newValue) => {
  console.log("111111121111111111",newValue)
  if (Array.isArray(newValue)){
    resources.value=newValue;
  }
  // 将逗号分隔的字符串转化为数组
  resources.value = newValue ? newValue.split(',').map(item => item.trim()) : [];
  console.log(resources.value)
}, { deep: true, immediate: true });

// 组件属性
const props = defineProps({
  size: {
    type: Number,
    default: 50, // 文件大小限制（单位：MB）
  },
  uploadUrl: {
    type: String,
    default: () => import.meta.env.VITE_APP_BASE_API + "/common/upload",
  },
  limit:{
    type: Number,
    default: 3, // 支持上传多个视频，默认限制为3个
  }
});

// 选择视频
const chooseVideo = () => {
  // 限制视频数量
  if (resources.value.length >= props.limit) {
    uni.showToast({
      title: `最多上传${props.limit}个视频`,
      icon: "none",
    });
    return;
  }

  uni.chooseVideo({
    sourceType: ["album"], // 支持相册和拍摄
    compressed: true, // 是否压缩视频
    maxDuration: 30, // 视频最大时长（单位：秒）
    success: async (res) => {
      console.log("选择的视频信息:", res);
      if (res.size > props.size * 1024 * 1024) {
        uni.showToast({
          title: `视频大小不能超过 ${props.size} MB`,
          icon: "none",
        });
        return;
      }

      // 上传视频文件
      try {
        const uploadResult = await uploadFile(res.tempFilePath);
        console.log("上传成功:", uploadResult);
      } catch (err) {
        console.error("上传失败:", err);
        uni.showToast({
          title: "上传失败，请重试",
          icon: "none",
        });
      }
    },
    fail: (err) => {
      console.error("视频选择失败:", err);
    },
  });
};

// 上传视频文件
const uploadFile = (filePath) => {
  console.log(filePath, "uploadFile接受的参数");

  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: props.uploadUrl, // 上传地址
      filePath: filePath, // 文件路径
      name: "file", // 后端接收的字段名
      header: {
        // "Content-Type": "multipart/form-data",
        "BackendAddress": import.meta.env.VITE_APP_BASE_API
      },
      success: (res) => {
        console.log("上传响应:", res);
        if (res.statusCode === 200) {
          const data = JSON.parse(res.data);
          if (data.code === 0) {
            // 将上传成功的视频添加到资源列表中
            resources.value.push( data.data.url );
            fromdatasValue.value = resources.value.map(item => item).join(',');
            resolve(data);
          } else {
            reject(data.message);
          }
        } else {
          reject("网络错误");
        }
      },
      fail: (err) => {
        reject(err);
      },
    });
  });
};

// 删除已上传视频
const deleteVideo = (index) => {
  uni.showModal({
    title: "确认删除",
    content: "确定要删除这个视频吗？",
    success: (res) => {
      if (res.confirm) {
        resources.value.splice(index, 1); // 从资源列表中删除视频
        console.log("删除视频:", index,resources.value)
        uni.showToast({
          title: "视频已删除",
          icon: "none",
        });
        // 更新外部传递的值
        fromdatasValue.value = resources.value.map(item => item).join(',');
      }
    },
  });
};

</script>

<style scoped>
.content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  /* 子元素靠左对齐 */
  border: none;
}

.VideoBox {
  width: 5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  border: none;
  margin-left: 0;
  /* 确保左边距为 0 */
}

.icon {
  width: 80rpx;
  height: 80rpx;
}

.tip {
  line-height: 17px;
  color: rgba(154, 154, 154, 1);
  font-size: 0.75rem;
  margin-top: 10rpx;
}

.video-list {
  margin-top: 20rpx;
  position: relative;
}

.video-item-container {
  position: relative;
  margin-bottom: 10rpx;
}

.delete_but {
  position: absolute;
  top: 0;
  background: rgba(255, 255, 255, .6);
  padding: 2px 10px;
  right: 0;
  z-index: 99;
}

.video-item {
  width: 100%;
  margin-bottom: 20rpx;
}
</style>
