<template>
  <uni-section class="custom-section" :title="title" :sub-title="number"></uni-section>
</template>

<script setup>
import {defineProps, ref} from "vue";
import {onLoad} from "@dcloudio/uni-app";
import {getCurrentInstance} from "vue"

const {proxy} = getCurrentInstance();
const props = defineProps({
  title: {
    type: String,
    default: '',
  },
  api: {
    type: String,
    default: '',
  },
  model: {
    type: String,
    default: '',
  },
  params: {
    type: Object, default: () => {}
  },
});
const number = ref("0")
onLoad(async () => {
  let data = {}
  if(props.params){
    Object.assign(data, props.params);
  }
  
  var res = await proxy.$api[props.api][props.model](data)
  if (res.data != null) {
    number.value = String(res.data);
  }
})
</script>

<style scoped>
.custom-section {
  /* width: 48%; */
    /* margin-bottom: 10px; */
    /* padding: 15px; */
    /* border-radius: 8px; */
    background-color: white;
    /* box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1); */
    text-align: center;
    /* background: linear-gradient(to bottom, rgb(93, 95, 239), white); */
}

.custom-section::after {
  content: ''; /* 清除浮动 */
  display: table;
  clear: both;
}

.uni-section__title {
  font-size: 18px;
  font-weight: bold;
  color: #5D5FEF; /* 使用首选颜色 */
  margin-bottom: 8px;
}
::v-deep .uni-section-header__decoration{
  width: 0px;
  margin-right: 0px; 
    background-color:white;
}
.uni-section__sub-title {
  font-size: 24px;
  color: #333;
}
</style>
