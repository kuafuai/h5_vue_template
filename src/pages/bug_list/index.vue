<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="BUG信息"
description="展示BUG信息"
>

</base-list-header>
<base-list
ref="bug_info_show_list"
:isPage="true"
@click="click_1739"
is_click="'true'"
api="bug_info_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.title?item.title:item.title"
title="标题:"
>

</uni-list-item>
<uni-list-item
:rightText="item.status?item.status:item.status"
title="状态:"
>

</uni-list-item>
<uni-list-item
:rightText="item.priority?item.priority:item.priority"
title="优先级:"
>

</uni-list-item>
<uni-list-item
:rightText="item.username?item.username:item.createdBy"
title="创建者ID:"
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








const click_1739=(
item
)=>{
  proxy.$navigate( "/pages/bug_detail/index?bugInfoId=" + item.bugInfoId,false)

}

</script>

<style lang="scss" scoped></style>