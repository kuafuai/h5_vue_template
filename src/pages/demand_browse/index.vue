<template>
  <base-wrapper
>
<base-list
ref="demand_list_ref"
:isPage="true"
api="demand_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.username?item.username:item.userId"
title="用户ID"
>

</uni-list-item>
<uni-list-item
:rightText="item.title?item.title:item.title"
title="标题"
>

</uni-list-item>
<uni-list-item
:rightText="item.description?item.description:item.description"
title="描述"
>

</uni-list-item>
</template>
</base-list>
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






const click_one_item=(
)=>{
  proxy.$navigate( "/pages/demand_detail/index?demandId=" + item.id,false)

}

</script>

<style lang="scss" scoped></style>