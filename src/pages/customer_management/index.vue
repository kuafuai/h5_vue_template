<template>
  <base-wrapper>
    <base-wrapper
>
<base-list
ref="customer_list_ref"
:isPage="true"
api="customer_info.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.customerName?item.customerName:item.customerName"
title="客户名称"
>

</uni-list-item>
<uni-list-item
:rightText="item.contactPerson?item.contactPerson:item.contactPerson"
title="联系人"
>

</uni-list-item>
<uni-list-item
:rightText="item.contactPhone?item.contactPhone:item.contactPhone"
title="联系电话"
>

</uni-list-item>
<uni-list-item
:rightText="item.address?item.address:item.address"
title="地址"
>

</uni-list-item>
</template>
</base-list>
<base-button
        ref="add_customer_button_ref"
    @click="click_9557"
    model="base"
    type="primary"
    title="新增客户"
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






const click_9557=(
)=>{
  proxy.$navigate( "/pages/add_customer/index",false)

}

</script>

<style lang="scss" scoped></style>