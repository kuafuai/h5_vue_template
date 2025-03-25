<template>
  <!-- 文件选择区域 -->
  <view class="example-body">
    <uni-file-picker
        :limit="limit"
        :file-mediatype="accept"
        :return-type="array"
        :file-extname="file_extname"
        v-model="selectedFiles"
        @select="handleFileChange"
    ></uni-file-picker>
  </view>
</template>

<script setup>
import {ref, watch} from 'vue';

const fileurl = defineModel();
// #ifdef MP-WEIXIN
const pre_url = import.meta.env.VITE_APP_SERVICE_API;

const props = defineProps({
  limit: {
    type: Number,
    default: 1,
  },
  accept: {
    type: String,
    default: 'all',
  },
  title: {
    type: String,
    default: '选择文件',
  },
  uploadUrl: {
    type: String,
    default: () => import.meta.env.VITE_APP_SERVICE_API==='/'?"/common/upload":import.meta.env.VITE_APP_SERVICE_API+"/common/upload"
  },
  size: {
    type: Number,
    default: 5
  },
  mode:{
    type: String,
    default: "sig"
  }
});
// #endif
// #ifdef H5
const pre_url = import.meta.env.VITE_APP_BASE_API;

const props = defineProps({
  limit: {
    type: Number,
    default: 1,
  },
  accept: {
    type: String,
    default: 'all',
  },
  title: {
    type: String,
    default: '选择文件',
  },
  uploadUrl: {
    type: String,
    default: () => import.meta.env.VITE_APP_BASE_API==='/'?"/common/upload":import.meta.env.VITE_APP_BASE_API+"/common/upload"
  },
  size: {
    type: Number,
    default: 5
  },
  mode:{
    type: String,
    default: "sig"
  }
});
// #endif

console.log("pre", pre_url)
const file_extname = ref(["doc", "docx", "xls", "xlsx", "ppt", "txt", "pdf"])



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
  for (var i = 0; i < files.tempFiles.length; i++) {
    var file_item = files.tempFiles[i]
    console.log(file_item.size);
    if (file_item.size > props.size * 1024 * 1024) {
      uni.showToast({
        title: `文件大小在${props.size}MB以内,请压缩后上传`,
        icon: "none"
      })
      files.tempFiles = []
      selectedFiles.value = []
      return
    }
  }
  console.log(files)
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
    console.log(import.meta.env.VITE_APP_SERVICE_API)
    uni.uploadFile({
      url: props.uploadUrl,
      filePath: file.path,
      name: 'file',
      header: {
        // "BackendAddress": import.meta.env.VITE_APP_SERVICE_API
        "BackendAddress": import.meta.env.VITE_APP_BASE_API
      },
      success: (res) => {
        if (res.statusCode === 200) {
          const response = JSON.parse(res.data);
          if (response.code === 0) {
            // fileurl.value = [{
            //   "url":response.data.url
            // }]
            // #ifdef MP-WEIXIN
            if (props.mode==='sig'){
              fileurl.value=import.meta.env.VITE_APP_SERVICE_API+response.data.url
            }else {
              fileurl.value=[
                {
                  "url":import.meta.env.VITE_APP_SERVICE_API+response.data.url
                }
              ]
            }
            // #endif
            // #ifdef H5
            if (props.mode==='sig'){
              fileurl.value=response.data.url
            }else {
              fileurl.value=[
                {
                  "url":response.data.url
                }
              ]
            }
            // #endif


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
.example-body {
  padding: 4px !important;
  border-radius: 10px;
  width: 100% !important;
  /* margin-bottom: 20px; */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
}

.uni-file-picker {
  /*border: 2px dashed #007aff; !* 添加虚线边框并使用主题颜色 *!*/
  padding: 15px;
  border-radius: 8px;
  background-color: #fff; /* 确保背景为白色 */
  transition: border-color 0.3s ease; /* 鼠标悬停时边框颜色渐变 */
}

.uni-file-picker:hover {
  border-color: #005bb5; /* 鼠标悬停时边框颜色变为深蓝 */
}

::v-deep .uni-file-picker__files {
  align-items: center !important;
}
</style>