<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="联系记录"
description="创业者联系人信息"
>

</base-list-header>
<base-list
ref="contact_info_show_list"
:isPage="true"
@click="click_6121"
:params="{'userInfoId':allParams.currentUser.userId,
}"
is_click="'true'"
api="contact_info_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.projectName?item.projectName:item.projectInfoId"
title="项目id:"
>

</uni-list-item>
<uni-list-item
:rightText="item.userName?item.userName:item.userInfoId"
title="项目负责人id:"
>

</uni-list-item>
<uni-list-item
:rightText="item.buinessPersonName?item.buinessPersonName:item.buinessPersonId"
title="投资人id:"
>

</uni-list-item>
<uni-list-item
:rightText="item.statusDescription?item.statusDescription:item.applicationStatus"
title="申请状态:"
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








const click_6121=(
item
)=>{
  proxy.$navigate( "/pages/user_info_follow_communicate_detail/index?contactInfoId=" + item.contactInfoId,false)

}

</script>

<style lang="scss" scoped></style>