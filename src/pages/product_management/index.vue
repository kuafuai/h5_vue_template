<template>
  <base-wrapper>
    <base-wrapper
>
<base-list
ref="product_list_ref"
:isPage="true"
api="product_info.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.productName?item.productName:item.productName"
title="商品名称"
>

</uni-list-item>
<uni-list-item
:rightText="item.productDescription?item.productDescription:item.productDescription"
title="商品描述"
>

</uni-list-item>
<uni-list-item
:rightText="item.productPrice?item.productPrice:item.productPrice"
title="商品价格"
>

</uni-list-item>
<uni-list-item
:rightText="item.stockQuantity?item.stockQuantity:item.stockQuantity"
title="库存数量"
>

</uni-list-item>
</template>
</base-list>
<base-button
        ref="add_product_button_ref"
    @click="click_7798"
    model="base"
    type="primary"
    title="新增商品"
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






const click_7798=(
)=>{
  proxy.$navigate( "/pages/add_product/index",false)

}

</script>

<style lang="scss" scoped></style>