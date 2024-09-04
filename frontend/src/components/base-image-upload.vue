<template>
  <!-- 文件选择区域 -->
  <!--  <view class="example-body">-->
  <uni-file-picker
      :limit="limit"
      :file-mediatype="accept"
      :return-type="array"
      v-model="selectedFiles"
      @select="handleFileChange"
  ></uni-file-picker>
  <!--  </view>-->
</template>

<script setup>
import {ref, watch} from 'vue';

const fileurl = defineModel();
// const pre_url = import.meta.env.VITE_APP_SERVICE_API;
const pre_url = import.meta.env.VITE_APP_BASE_API;
console.log("pre",pre_url)
const props = defineProps({
  limit: {
    type: Number,
    default: 1,
  },

  title: {
    type: String,
    default: '选择文件',
  },
  uploadUrl: {
    type: String,
    default: () => import.meta.env.VITE_APP_BASE_API + "/common/upload",
  },
});

function extractFileNameAndExtension(url) {
  const parts = url.split('/');
  const fileNameWithExtension = parts.pop();
  const [fileName, extension] = fileNameWithExtension.split('.');
  return {
    fileName: fileName + "." + extension,
    extension: extension,
  };
}

const selectedFiles = ref([]);
watch(fileurl, (newValue) => {
  if (newValue) {
    const e = extractFileNameAndExtension(newValue);
    selectedFiles.value = [{
      "name": e.fileName,
      "extname": e.extension,
      "url": newValue,
    }];
  }
});

const handleFileChange = async (files) => {
  selectedFiles.value = files.tempFiles;
  console.log(selectedFiles);

  // 自动上传文件
  await uploadFiles();
};

const uploadFiles = async () => {
  if (selectedFiles.value.length === 0) {
    uni.showToast({
      title: '请先选择文件',
      icon: 'none',
    });
    return;
  }

  try {
    for (const file of selectedFiles.value) {
      await uploadFile(file);
    }
    uni.showToast({
      title: '文件上传成功',
      icon: 'success',
    });
  } catch (error) {
    uni.showToast({
      title: '文件上传失败',
      icon: 'none',
    });
  }
};

const uploadFile = (file) => {
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: props.uploadUrl,
      filePath: file.path,
      name: 'file',
      success: (res) => {
        if (res.statusCode === 200) {
          const response = JSON.parse(res.data);
          if (response.code === 0) {
            fileurl.value = response.data.url;
            resolve(res);
          } else {
            reject(new Error(response.message));
          }
        } else {
          reject(new Error('上传失败'));
        }
      },
      fail: (err) => {
        reject(err);
      },
    });
  });
};
</script>

<style scoped>

</style>
