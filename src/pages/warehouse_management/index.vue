<template>
  <base-wrapper>
    <base-wrapper
>
<base-list
ref="warehouse_list_ref"
:isPage="true"
api="warehouse_info.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.warehouseName?item.warehouseName:item.warehouseName"
title="仓库名称"
>

</uni-list-item>
<uni-list-item
:rightText="item.warehouseAddress?item.warehouseAddress:item.warehouseAddress"
title="仓库地址"
>

</uni-list-item>
<uni-list-item
:rightText="item.warehouseManager?item.warehouseManager:item.warehouseManager"
title="仓库经理"
>

</uni-list-item>
<uni-list-item
:rightText="item.warehouseContact?item.warehouseContact:item.warehouseContact"
title="仓库联系电话"
>

</uni-list-item>
</template>
</base-list>
<base-button
        ref="add_warehouse_button_ref"
    @click="click_6555"
    model="base"
    type="primary"
    title="新增仓库"
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






const click_6555=(
)=>{
  proxy.$navigate( "/pages/add_warehouse/index",false)

}

</script>

<style lang="scss" scoped></style>