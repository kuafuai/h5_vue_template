<template>
  <!-- 文件选择区域 -->
  <!--  <view class="example-body">-->

  <uni-file-picker
      :limit="limit"
      :return-type="'array'"
      file-extname="jpg,jpeg,png"
      v-model="resources"
      @select="handleFileChange"
      @delete="handelDelete"
      sourceType="['album','camera']"
  >
    <view class="content"
          style="display:flex;flex-direction:column;justify-content:center;align-items:center;border:none">
      <image src="../static/picture.png" style="width:80rpx;height:80rpx" alt=""></image>
      <view style="line-height: 17px;color: rgba(154,154,154,1);font-size: 0.75rem;margin-top:10rpx">
        {{ $t('image_upload.upload_text') }}
      </view>
    </view>
  </uni-file-picker>
  <!--  </view>-->
</template>

<script>
export default {
  options: {
    styleIsolation: 'shared', // 解除样式隔离
  }
};
</script>

<script setup>
import {getCurrentInstance, ref, watch} from 'vue';
import {onLoad, onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance()
// 通过 defineModel() 传递资源数组，支持外部传入
const num = ref([])
const resources = ref([])
// const pre_url = import.meta.env.VITE_APP_SERVICE_API;
const pre_url = import.meta.env.VITE_APP_BASE_API;
// console.log("pre", pre_url);
let image_model = defineModel()
// console.log("初始值", image_model.value)
let selectedFiles = ref([]);

onShow(()=>{
  // 失焦
  selectedFiles.value=image_model.value

})
onLoad(() => {
  // console.log("1234321234321234321234321234", image_model.value)
  // selectedFiles.value = image_model.value
})

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
  size: {
    type: Number,
    default: 5,
  }
});

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
  console.log("image_model111", newValue)
  resources.value=newValue
  selectedFiles.value = newValue;
  num.value=newValue
  console.log(image_model.value,resources.value,selectedFiles.value)
}, {deep: true, once: true});

// // 监听外部传入的资源数组，如果发生变化则更新 selectedFiles
// watch(resources.value, (newValue) => {
//   if (newValue && newValue.length > 0) {
//     console.log("resources.value变化", newValue)
//     selectedFiles.value = newValue.map(resource => ({
//       name: resource.name,
//       extname: extractFileNameAndExtension(resource.url),
//       url: resource.url,
//     }));
//     if (image_model.value == undefined || image_model.value == null) {
//       image_model.value = []
//       // console.log(image_model.value)
//
//     }
//     image_model.value.splice(0, image_model.value.length);
//
//
//     image_model.value.push(...resources.value)
//     // console.log("resources.value变化后的元素", resources.value, selectedFiles.value, image_model.value)
//   }
//
// }, {deep: true});

// 监听外部传入的资源数组，如果发生变化则更新 selectedFiles
// watch(image_model.value, (newValue) => {
//   console.log("监听", image_model.value)
//   if (newValue && newValue.length > 0) {
//
//     // selectedFiles.value = newValue.map(resource => ({
//     //   name: resource.fileName,
//     //   extname: extractFileNameAndExtension(resource.url),
//     //   url: resource.url,
//     // }));
//     selectedFiles.value = newValue
//   }
// },{ deep: true });


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
      resources.value=[]
      setTimeout(() => {
        resources.value=num.value
      }, 0);
      return;
    }
  }
  selectedFiles.value = files.tempFiles;
  console.log(selectedFiles.value,"selectedFiles");

  // 自动上传文件
  await uploadFiles();
};

const handelDelete = (e) => {
  console.log(e,resources.value);

  resources.value.splice(e.index, 1)
  image_model.value.splice(e.index, 1)
  // image_model.value = resources.value.splice(e.index, 1)
  // // resources.value=resources.value.splice(e.index, 1)
  // // image_model.value = resources.value
  // console.log("删除后的元素", resources.value, image_model.value)
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
      console.log(file,"file1111");

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
        "BackendAddress": import.meta.env.VITE_APP_SERVICE_API,
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
            resources.value.push({
              name: fileInfo.fileName,
              extension: fileInfo.extension,
              url: response.data.url,
            });

            if (image_model.value == undefined) {
              image_model.value = []
            }
            image_model.value=resources.value
            selectedFiles.value=resources.value
            // image_model.value = resources.value
            console.log("select_image", resources.value,image_model.value, selectedFiles.value)

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
</script>

<style scoped>
::v-deep .is-add {
  flex-direction: column !important;
  border: none !important;
}
</style>