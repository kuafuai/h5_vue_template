<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="项目信息"
description="展示项目信息"
>

</base-list-header>
<base-list
ref="contact_info_show_list"
:isPage="true"
@click="click_704"
:params="{'reviewStatus':'2',
}"
is_click="'true'"
api="project_info_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.userName?item.userName:item.userInfoId"
title="负责人用户ID:"
>

</uni-list-item>
<uni-list-item
:rightText="item.projectName?item.projectName:item.projectName"
title="项目名称:"
>

</uni-list-item>
<uni-list-item
:rightText="item.projectOneWord?item.projectOneWord:item.projectOneWord"
title="项目一句话:"
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








const click_704=(
item
)=>{
  proxy.$navigate( "/pages/project_all_detail/index?userInfoId=" + item.userInfoId+"&"+"projectInfoId=" + item.projectInfoId,false)

}

</script>

<style lang="scss" scoped></style>