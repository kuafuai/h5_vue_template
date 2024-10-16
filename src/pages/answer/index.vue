<template>
  <base-wrapper>
    <base-survey
@success="success_8544"
:userId="allParams.currentUser.userId"
@expired="expired_8544"
:id="allParams.surveyInfoId"
op_model="survey_write_a93faf2"
>

</base-survey>
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





const success_8544=(
)=>{
  proxy.$navigate( "/pages/survey_info_detail/index?id=" + allParams.value.surveyInfoId,true)

}
const expired_8544=(
)=>{
  proxy.$navigate( "/pages/survey_info_detail/index?id=" + allParams.value.surveyInfoId,true)

}

</script>

<style lang="scss" scoped></style>