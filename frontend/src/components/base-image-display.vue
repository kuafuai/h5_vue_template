<template>
  <view class="flex">
    <view class="file-preview" :style='{width: width+"px",height:height+"px",borderRadius:radius+"px"}'
          v-for="(item, index) in fileList">
      <view class="image_list">
        <image v-if="isImage(item.url)" :src="item.url" mode="widthFix" alt="预览图片" @click="previewImage(index)"/>
      </view>
    </view>
  </view>
</template>

<script setup>
const {proxy} = getCurrentInstance();
const fileList = ref([]);
const props = defineProps({
  url: {
    type: [Array, String]
  },
  width: {
    type: Number,
    default: 200
  },
  height: {
    type: Number,
    default: 200
  },
  radius: {
    type: Number,
    default: 5
  }
});

// 双向绑定变量
const model = defineModel({type: [Array, String]});

watch(model, (newValue, oldValue) => {
  if (newValue) {
    let temp = 1;
    const list = Array.isArray(newValue) ? newValue : newValue.split(',');

    fileList.value = list.map(item => {
      if (typeof item === "string") {
        item = {name: item, url: item};
      }
      item.uid = item.uid || new Date().getTime() + temp++;
      return item;
    });
  } else {
    if (props.url) {
      let temp = 1;
      const list = Array.isArray(props.url) ? props.url : props.url.split(',');

      fileList.value = list.map(item => {
        if (typeof item === "string") {
          item = {name: item, url: item};
        }
        item.uid = item.uid || new Date().getTime() + temp++;
        return item;
      });
    } else {
      fileList.value = [];
    }
  }
}, {immediate: true, deep: true})


const isImage = (newUrl) => {
  let fileExtension = extractFileExtension(newUrl);
  const imageTypes = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp'];
  return imageTypes.includes(fileExtension);
}

const previewImage = (index) => {
  uni.previewImage({
    current: index,
    urls: fileList.value.map(item => item.url)
  })
}


// 提取文件扩展名的函数
function extractFileExtension(url) {
  const parts = url.split('.');
  return parts.length > 1 ? parts.pop().toLowerCase() : '';
}

</script>

<style scoped>

.file-preview {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 5px;
  /*border: 2px solid #e0e0e0;*/
  margin: 10px 0 0 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05); /* 添加轻微阴影效果 */
  overflow: hidden;
}

.image_list {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  align-items: center;
  margin: 10rpx 0;
}

</style>