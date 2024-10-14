<template>
  <view v-if="model=='one'">
    <view>数据的值id：{{ show_value_one ? show_value_one : data_value }}</view>
    <button @click="handleClick">前去选择{{ title }}</button>
  </view>
  <view v-else-if="model='more'">
    <view>数据的值id：{{ show_value_one ? show_value_one : data_value }}</view>
    <button @click="handleClick">前去选择{{ title }}</button>
  </view>
</template>

<script setup>
import {onLoad, onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance()
const data_value = defineModel()
const props = defineProps({
  model: {type: String, default: "one"},
  show_value: {type: String, default: ""},
  origin_page_path: {type: String, default: ""},
  // api: {type: String, default: ''},
  page_path: {type: String, default: ""},
  title: {type: String, default: ""},
  ref_id: {type: String, default: ""}
});
let param;
let show_value_one = ref()
onLoad((options) => {
  console.log('URL参数:', options)
  param = options;
})

onShow(() => {
  // for (let i = 0; i < 3; i++) {
  // 防止网络延迟无法成功赋值
  setTimeout(() => {
    // show_value_one.value = props.show_value;

    let res = uni.getStorageSync("select_storage_" + props.ref_id)
    res = {...res}
    // let value = param["select_result_" + props.ref_id];
    // let show = param["select_show_" + props.ref_id];

    if (res != null && Object.keys(res).length != 0) {
      // console.log("one,"+props.ref_id,res)
      data_value.value = res["select_result_" + props.ref_id];
      show_value_one.value = res["select_show_" + props.ref_id];
      uni.removeStorageSync("select_storage_" + props.ref_id)
      // console.log("data_value",props.ref_id,data_value.value,show_value_one.value)
    }

  }, 200); // 每次延迟 i*100 毫秒，依次执行
  // }
})
onMounted(() => {


})


const handleClick = () => {
  let path = props.page_path;
  let flag = false;
  if (param) {
    path += "?"
    for (let item in param) {
      path = path + item + "=" + param[item] + "&"
    }
    flag = true;
  }

  var select_result = null
  if (data_value.value) {
    if (!flag) {
      path += "?"
    }
    select_result = data_value.value
  }

  path = path + "select_result=" + select_result
      + "&" + "select_path=" + props.origin_page_path + "&" + "select_id=" + props.ref_id

  // 删除最后一个 "&"
  if (path.endsWith("&")) {
    path = path.slice(0, -1); // 移除最后一个字符（即 "&"）
  }
  console.log("one", path)
  proxy.$navigate(path, false)
}
</script>

<style scoped>

</style>