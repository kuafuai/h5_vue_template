<template>
  <base-wrapper>
    <base-wrapper
>
<base-list
ref="notification_list_ref"
:isPage="true"
@click="click_6377"
is_click="'true'"
api="notification_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.caseTitle?item.caseTitle:item.caseId"
title="关联案件ID"
>

</uni-list-item>
</template>
</base-list>
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






const click_6377=(
item
)=>{
  uni.showToast({
  title: '展示通知详细信息并标记为已读',
  icon: 'none',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>