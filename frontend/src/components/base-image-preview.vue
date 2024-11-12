<template>
  <el-image
      :src="realSrc"
      fit="cover"
      :style="`width:${realWidth};height:${realHeight};`"
      :preview-src-list="realSrcList"
      :preview-teleported="true"
  >
    <template #error>
      <div class="image-slot">
        <el-icon>
          <picture-filled/>
        </el-icon>
      </div>
    </template>
  </el-image>
</template>

<script setup>

import axios from "axios";

const props = defineProps({
  src: {
    type: String,
    default: ""
  },
  width: {
    type: [Number, String],
    default: ""
  },
  height: {
    type: [Number, String],
    default: ""
  }
});

const realSrc = ref(null);
const realSrcList = ref([])

watch(() => props.src, async (val, v) => {
  if (val) {
    let real_src_list = props.src.split(";");
    let srcList = [];
    for (const item of real_src_list) {
      if (item.length > 0) {
        let url = import.meta.env.VITE_APP_BASE_API + item;
        let dd = await getImage(url);
        srcList.push(dd);
      }
    }
    if (real_src_list.length > 0) {
      realSrc.value = srcList[0];
      realSrcList.value = srcList;
    }
  }
});


const realWidth = computed(() =>
    typeof props.width == "string" ? props.width : `${props.width}px`
);

const realHeight = computed(() =>
    typeof props.height == "string" ? props.height : `${props.height}px`
);

async function getImage(url) {
  const res = await axios({
    method: 'get',
    url: url,
    responseType: 'blob',
    headers: {'Authorization': 'Bearer ' + localStorage.getItem("h5_token")}
  })
  return await blobToBase64(res.data)
}

function blobToBase64(blob) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onloadend = () => resolve(reader.result);
    reader.onerror = reject;
    reader.readAsDataURL(blob); // 将 blob 读取为 Data URL (Base64)
  });
}

</script>

<style lang="scss" scoped>
.el-image {
  border-radius: 5px;
  background-color: #ebeef5;
  box-shadow: 0 0 5px 1px #ccc;

  :deep(.el-image__inner) {
    transition: all 0.3s;
    cursor: pointer;

    &:hover {
      transform: scale(1.2);
    }
  }

  :deep(.image-slot) {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    color: #909399;
    font-size: 30px;
  }
}
</style>