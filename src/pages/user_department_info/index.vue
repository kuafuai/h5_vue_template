<template>
  <base-wrapper>
    <base-wrapper
>
<base-list
ref="user_department_list_ref"
:isPage="true"
api="user_department_info.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.name?item.name:item.name"
title="名称:"
>

</uni-list-item>
<uni-list-item
:rightText="item.contactInfo?item.contactInfo:item.contactInfo"
title="联系方式:"
>

</uni-list-item>
<uni-list-item
:rightText="item.department?item.department:item.department"
title="所属部门:"
>

</uni-list-item>
</template>
</base-list>
<base-button
        ref="add_user_department_button_ref"
    @click="click_9509"
    model="float"
    type="primary"
    title="添加用户/部门"
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






const click_9509=(
)=>{
  proxy.$navigate( "/pages/add_user_department/index",false)

}

</script>

<style lang="scss" scoped></style>