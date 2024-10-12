<template>
  <div class="file-preview">
    <!-- 如果是图片类型，则显示图片 -->
    <view v-for="item in url">
      <img v-if="isImage(item.url)" :src="item.url" alt="预览图片" class="preview-image"/>
      <div v-else class="file-preview-button">
        <view v-if="item!=null && item!=''">
          <button @click="openFile(item.url)" class="custom-button">
            {{item.fileName}}
          </button>
        </view>
        <view v-else>
          暂无文件
        </view>

      </div>
    </view>


    <!-- 如果是文件类型，则显示打开文件按钮 -->

  </div>
</template>

<script setup>
import {ref, computed, watch} from 'vue';

const props = defineProps({
  url: {
    type: Array,
    required: true,
  },
});


const fileExtension = ref('');

// // 监听 props.url 的变化
// watch(
//     () => props.url,
//     (newUrl) => {
//       console.log('URL 发生变化:', newUrl);
//       fileExtension.value = extractFileExtension(newUrl);
//     },
//     {immediate: true} // 立即执行一次，以处理初始值
// );
//
// 计算属性：判断文件是否为图片类型
// const isImage = computed(() => {
//   const imageTypes = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp'];
//   return imageTypes.includes(fileExtension.value);
// });
const isImage =  (newUrl) => {
  var fileExtension = extractFileExtension(newUrl);
  const imageTypes = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp'];
  return imageTypes.includes(fileExtension);
}


// 提取文件扩展名的函数
function extractFileExtension(url) {
  const parts = url.split('.');
  return parts.length > 1 ? parts.pop().toLowerCase() : '';
}

// 方法：打开文件
const openFile = (url) => {
  if (url) {
    // 使用 uniapp 内置的 api 打开文件
    uni.openDocument({
      filePath: url,
      success: function () {
        console.log('文件打开成功');
      },
      fail: function (error) {
        console.error('文件打开失败', error);
        uni.showToast({
          title: '文件打开失败',
          icon: 'none',
        });
      },
    });
  } else {
    uni.showToast({
      title: '无法找到文件路径',
      icon: 'none',
    });
  }
};
</script>

<style scoped>
.file-preview {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 5px;
  /*border: 2px solid #e0e0e0;*/
  border-radius: 10px;
  margin: 10px 0 0 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05); /* 添加轻微阴影效果 */
}

.preview-image {
  max-width: 100%;
  max-height: 250px;
  border-radius: 10px;
  object-fit: cover; /* 保持图片的纵横比 */
  transition: transform 0.3s ease; /* 添加缩放过渡效果 */
}

/*.preview-image:hover {*/
/*  transform: scale(1.05); !* 鼠标悬停时图片轻微放大 *!*/
/*}*/

.file-preview-button {
  text-align: center;
}

.custom-button {
  background-color: #007bff;
  color: #fff;
  /*padding: 12px 12px;*/
  border-radius: 8px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

/*.custom-button:hover {*/
/*  background-color: #0056b3;*/
/*  transform: translateY(-2px); !* 鼠标悬停时按钮微微上移 *!*/
/*}*/

/*.custom-button:active {*/
/*  background-color: #004085;*/
/*  transform: translateY(0); !* 点击时按钮恢复原位 *!*/
/*}*/
</style>
