<template>
  <base-wrapper>
    <base-information-perfection
table_model="buiness_person"
not_null_field="['bPhoneNumber', 'buinessPersonName', 'bProjectOrganization', 'bTitle', 'bWechatNumber']"
:id="allParams.userId"
@message_fail="message_fail_1951"
dataset="buiness_person"
@message_success="message_success_1951"
>

</base-information-perfection>
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




const message_success_1951=(
item
)=>{
  proxy.$navigate( "/pages/buiness_person/index",true)

}
const message_fail_1951=(
item
)=>{
  proxy.$navigate( "/pages/buiness_perfect/index",true)

}

</script>

<style lang="scss" scoped></style>