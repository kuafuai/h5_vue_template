<template>
  <!-- 文件选择区域 -->
  <!--  <view class="example-body">-->

  <uni-file-picker
      :limit="limit"
      :return-type="'array'"
      file-extname="jpg,jpeg,png"
      v-model="selectedFiles"
      @select="handleFileChange"
      @delete="handelDelete"
  ></uni-file-picker>
  <!--  </view>-->
</template>

<script setup>
import {ref, watch} from 'vue';
import {onLoad, onShow} from "@dcloudio/uni-app";

// 通过 defineModel() 传递资源数组，支持外部传入

const resources = ref([])
// const pre_url = import.meta.env.VITE_APP_SERVICE_API;
const pre_url = import.meta.env.VITE_APP_BASE_API;
// console.log("pre", pre_url);
let image_model = defineModel()
// console.log("初始值", image_model.value)
let selectedFiles = ref([]);

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
  selectedFiles.value = newValue;
}, {deep: true, once: true});

// 监听外部传入的资源数组，如果发生变化则更新 selectedFiles
watch(resources.value, (newValue) => {
  if (newValue && newValue.length > 0) {
    console.log("", newValue)
    selectedFiles.value = newValue.map(resource => ({
      name: resource.fileName,
      extname: extractFileNameAndExtension(resource.url),
      url: resource.url,
    }));
    if (image_model.value == undefined || image_model.value == null) {
      image_model.value = []
      // console.log(image_model.value)

    }
    image_model.value.splice(0, image_model.value.length);


    image_model.value.push(...resources.value)
    // console.log("resources.value变化后的元素", resources.value, selectedFiles.value, image_model.value)
  }

}, {deep: true});

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
    if (file_item.size > props.size * 1024 * 1024) {
      uni.showToast({
        title: `文件大小在${props.size}MB以内,请压缩后上传`,
        icon: "none",
      });
      files.tempFiles = [];
      selectedFiles.value = [];
      return;
    }
  }

  selectedFiles.value = files.tempFiles;
  console.log(selectedFiles);

  // 自动上传文件
  await uploadFiles();
};

const handelDelete = (e) => {

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
    console.log(error);
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
      header: {
        "BackendAddress": import.meta.env.VITE_APP_SERVICE_API,
      },
      success: (res) => {
        console.log("base-image", res)
        if (res.statusCode === 200) {
          const response = JSON.parse(res.data);
          if (response.code === 0) {
            const fileInfo = extractFileNameAndExtension(response.data.url);
            // 将文件上传结果存储到传入的资源数组中
            resources.value.push({
              name: fileInfo.fileName,
              extension: fileInfo.extension,
              url: response.data.url,
            });

            if (image_model.value == undefined) {
              image_model.value = []
            }
            image_model.value.push(resources.value)
            // image_model.value = resources.value
            console.log("select_image", resources.value,image_model.value, selectedFiles.value)

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
