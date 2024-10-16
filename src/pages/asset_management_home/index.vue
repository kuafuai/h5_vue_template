<template>
  <base-wrapper>
    <base-wrapper
>
<base-list
ref="asset_list_ref"
:isPage="true"
@click="click_6112"
is_click="'true'"
api="asset_info.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.assetName?item.assetName:item.assetName"
title="资产名称:"
>

</uni-list-item>
<uni-list-item
:rightText="item.assetType?item.assetType:item.assetType"
title="资产类型:"
>

</uni-list-item>
<uni-list-item
:rightText="item.specification?item.specification:item.specification"
title="规格:"
>

</uni-list-item>
<uni-list-item
:rightText="item.purchaseDate?item.purchaseDate:item.purchaseDate"
title="购买日期:"
>

</uni-list-item>
<uni-list-item
:rightText="item.status?item.status:item.status"
title="状态:"
>

</uni-list-item>
</template>
</base-list>
<base-button
        ref="add_asset_button_ref"
    @click="click_2612"
    model="float"
    type="primary"
    title="添加资产"
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






const click_2612=(
)=>{
  proxy.$navigate( "/pages/add_asset/index",false)

}
const click_6112=(
item
)=>{
  proxy.$navigate( "/pages/asset_detail/index?assetId=" + item.assetId,false)

}

</script>

<style lang="scss" scoped></style>