<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="用户信息管理"
description="用户信息"
>

</base-list-header>
<base-description
:params="{'userInfoId':allParams.currentUser.userId,
}"
api="user_info_all.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.userInfoId"
type="base"
title="编号"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.userName"
type="base"
title="姓名"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.phoneNumber"
type="base"
title="手机号"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.technicalStatusDescription"
type="base"
title="技术方向"
class="mb-10"
>
</uni-section>


</template>
</base-description>
<base-button
        ref="form_button_ref"
    @click="click_9353"
    model="base"
    type="primary"
    title="BUG清单"
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








const click_9353=(
)=>{
  proxy.$navigate( "/pages/my_bug_list/index",false)

}

</script>

<style lang="scss" scoped></style>