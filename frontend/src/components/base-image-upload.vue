<template>
  <!-- 文件选择区域 -->
  <!--  <view class="example-body">-->

  <uni-file-picker :limit="limit" :return-type="'array'" file-extname="jpg,jpeg,png" v-model="resources"
    @select="handleFileChange" @delete="handelDelete" sourceType="['album','camera']">
    <view class="content"
      style="display:flex;flex-direction:column;justify-content:center;align-items:center;border:none">
      <image :src="get_resource_url('static/picture.png')" style="width:80rpx;height:80rpx" alt=""></image>
      <view style="line-height: 17px;color: rgba(154,154,154,1);font-size: 0.75rem;margin-top:10rpx">
        {{ $t('image_upload.upload_text') }}
      </view>
    </view>
  </uni-file-picker>
  <!--  </view>-->
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
import { getCurrentInstance, ref, watch } from 'vue';
import { onLoad, onShow } from "@dcloudio/uni-app";

const { proxy } = getCurrentInstance()
// 通过 defineModel() 传递资源数组，支持外部传入
const resources = ref([])
let image_model = defineModel()
let selectedFiles = ref([]);

onShow(() => {
})
onLoad(() => {
})

// #ifdef MP-WEIXIN
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
    default: () => import.meta.env.VITE_APP_SERVICE_API === '/' ? "/common/upload" : import.meta.env.VITE_APP_SERVICE_API + "/common/upload"
  },
  size: {
    type: Number,
    default: 5,
  },
  mode: {
    type: String,
    default: 'sig',
  }
});
// #endif
// #ifdef H5
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
    default: () => import.meta.env.VITE_APP_BASE_API === '/' ? "/common/upload" : import.meta.env.VITE_APP_BASE_API + "/common/upload"
  },
  size: {
    type: Number,
    default: 5,
  },
  mode: {
    type: String,
    default: 'sig',
  }
});
// #endif





// 提取文件名和扩展名的函数
function extractFileNameAndExtension(url) {
  const parts = url.split('/');
  const fileNameWithExtension = parts.pop();
  const [fileName, extension] = fileNameWithExtension.split('.');
  return {
    fileName: fileName + "." + extension,
    extension: extension,
  };
}

// 监听 image_model 数组变化，只触发一次
watch(image_model, (newValue) => {
  if (newValue) {
    const list = Array.isArray(newValue) ? newValue : image_model.value.split(",");
    resources.value = list.map(item => {
      if (typeof item === "string") {
        item = { name: item, url: item };
      }
      return item;
    });
  } else {
    resources.value = [];
    selectedFiles.value = [];
    return [];
  }

}, { deep: true, once: true });

const handleFileChange = async (files) => {
  // 文件大小验证
  for (var i = 0; i < files.tempFiles.length; i++) {
    var file_item = files.tempFiles[i];
    console.log(`File ${i} Path: `, file_item.path)
    if (file_item.size > props.size * 1024 * 1024) {
      uni.showToast({
        title: proxy.$tt('image_upload.file_size_prefix') + `${props.size}` + proxy.$tt('image_upload.file_size_suffix'),
        icon: "none",
      });
      files.tempFiles = [];
      selectedFiles.value = [];
      resources.value = []
      return;
    }
  }
  selectedFiles.value = files.tempFiles;
  console.log(selectedFiles.value, "selectedFiles");

  // 自动上传文件
  await uploadFiles();
};

const handelDelete = (e) => {
  console.log(e, resources.value);

  resources.value.splice(e.index, 1)
  if (props.mode === 'sig') {
    image_model.value = listToString(resources.value, ",");
  } else {
    image_model.value.splice(e.index, 1)
  }
  console.log('=====', image_model.value)
}

const uploadFiles = async () => {
  if (selectedFiles.value.length === 0) {
    uni.showToast({
      title: proxy.$tt('image_upload.please_select_file'),
      icon: 'none',
    });
    return;
  }

  try {
    for (const file of selectedFiles.value) {
      console.log(file, "file1111");

      await uploadFile(file);
    }
    uni.showToast({
      title: proxy.$tt('image_upload.file_upload_success'),
      icon: 'success',
    });
  } catch (error) {
    console.log(error);
    uni.showToast({
      title: proxy.$tt('image_upload.file_upload_fail'),
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
      header: {

        "BackendAddress": import.meta.env.VITE_APP_BASE_API,
        "Authorization": "Bearer " + uni.getStorageSync("h5_token")
      },
      success: (res) => {
        console.log("base-image", res)
        if (res.statusCode === 200) {
          const response = JSON.parse(res.data);
          if (response.code === 0) {
            const fileInfo = extractFileNameAndExtension(response.data.url);
            console.log("fileInfo", fileInfo)
            // 将文件上传结果存储到传入的资源数组中
            // #ifdef MP-WEIXIN
            resources.value.push({
              name: fileInfo.fileName,
              extension: fileInfo.extension,
              url: import.meta.env.VITE_APP_SERVICE_API + response.data.url,
            });
            // #endif
            // #ifdef H5
            resources.value.push({
              name: fileInfo.fileName,
              extension: fileInfo.extension,
              url: response.data.url,
            });
            // #endif


            if (image_model.value == undefined) {
              image_model.value = []
            }
            if (props.mode === 'sig') {
              image_model.value = listToString(resources.value, ",");
            } else {
              image_model.value = resources.value
            }
            selectedFiles.value = resources.value

            console.log('=====', image_model.value)

            resolve(res);
          } else {
            reject(new Error(response.message));
          }
        } else {
          reject(new Error(proxy.$tt('common.upload_fail_text')));
        }
      },
      fail: (err) => {
        reject(err);
      },
    });
  });
};


function listToString(list, separator) {
  let strs = "";
  separator = separator || ",";
  for (let i in list) {
    if (undefined !== list[i].url && list[i].url.indexOf("blob:") !== 0) {
      strs += list[i].url + separator;
    }
  }
  return strs != "" ? strs.substr(0, strs.length - 1) : "";
}

</script>

<style scoped>
::v-deep .is-add {
  flex-direction: column !important;
  border: none !important;
}
</style>