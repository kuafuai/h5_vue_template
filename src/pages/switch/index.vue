<template>
  <base-wrapper>
    <base-wrapper
>
<base-button
        ref="user_button_ref"
    @click="click_7805"
    model="base"
    type="primary"
    title="我是创业者"
>

</base-button>
<base-button
        ref="business_button_ref"
    @click="click_2382"
    model="base"
    type="primary"
    title="我是投资人"
>

</base-button>
</base-wrapper>
  </base-wrapper>

</template>

<script setup>
import {getCurrentInstance, ref} from "vue"
const {proxy} = getCurrentInstance();
import {onLoad} from "@dcloudio/uni-app";
const allParams = ref({})
// onLoad(() => {
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  const urlParams = currentPage.options; // 获取页面传递的参数

  const localStorageKey = "/" + currentPage.route; // 当前页面的路径作为 key
  const localStorageParams = uni.getStorageSync(localStorageKey) || null;
  let currentUser = uni.getStorageSync("currentUser") || null;
  if (localStorageParams != null && urlParams != null) {
    allParams.value = {
      ...urlParams,
      ...JSON.parse(localStorageParams)
    };
  } else if (localStorageParams != null && urlParams == null) {
    allParams.value = {

      ...JSON.parse(localStorageParams)
    };
  } else if (localStorageParams == null && urlParams != null) {
    allParams.value = {
      ...urlParams, // 展开 URL 参数

    };
  }
// })
if (currentUser != null) {
  var parse = JSON.parse(currentUser);
  allParams.value["currentUser"] = parse;
  allParams.value[parse["relevanceTable"] + "Id"] = parse.userId
}




const click_2382=(
)=>{
  proxy.$navigate( "/pages/business_login/index",false)

}
const click_7805=(
)=>{
  proxy.$navigate( "/pages/user_login/index",false)

}

</script>

<style lang="scss" scoped></style>