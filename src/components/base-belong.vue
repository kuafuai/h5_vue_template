<template>
  <button @click="handleClick">{{show_value}}
    <image src="../static/right.png"
      style="width:32rpx;position:absolute;right:24rpx;display: flex; justify-content:center; " mode="widthFix">
    </image>
  </button>
</template>

<script setup>
import {getCurrentInstance} from "vue"
import {onShow} from "@dcloudio/uni-app";

let data = defineModel()
let form = defineModel("form")
const {proxy} = getCurrentInstance();
let show_value = ref("")
const props = defineProps({
  title: {type: String, default: ""},
  show_name: {type: String, default: ""},
  value_name: {type: String, default: ""},
  map: {type: Object, default: {}},
  page_name: {type: String, default: ""}
})

show_value.value = "请选择" + props.title
onShow(() => {
  let res = uni.getStorageSync(props.page_name)
  if (res != null && Object.keys(res).length !== 0) {
    for (var item in props.map) {
      // 如果form有值，不进行赋值
      if (form.value[item]==undefined ||  form.value[item]==null || form.value[item]==""){
        form.value[item] = res[props.map[item]]  // 修改表单中其他的值
      }

    }
    data.value = res[props.value_name] // 赋值给变量
    show_value.value = res[props.show_name]
    uni.removeStorageSync(props.page_name)
  }
})

// 点击跳转页面
const handleClick = () => {
  proxy.$navigate("/pages/" + props.page_name + "/index", false)
}
</script>

<style scoped>

</style>