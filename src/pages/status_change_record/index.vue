<template>
  <base-wrapper>
    <base-wrapper
>
<base-list
ref="status_change_record_list_ref"
:isPage="true"
api="status_change_record_info.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.assetId?item.assetId:item.assetId"
title="资产ID:"
>

</uni-list-item>
<uni-list-item
:rightText="item.oldStatus?item.oldStatus:item.oldStatus"
title="旧状态:"
>

</uni-list-item>
<uni-list-item
:rightText="item.newStatus?item.newStatus:item.newStatus"
title="新状态:"
>

</uni-list-item>
<uni-list-item
:rightText="item.changeDate?item.changeDate:item.changeDate"
title="变更日期:"
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







</script>

<style lang="scss" scoped></style>