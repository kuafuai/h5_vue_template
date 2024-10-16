<template>
  <base-wrapper>
    <base-wrapper
>
<base-description
ref="asset_detail_ref"
:params="{'assetId':allParams.assetId,
}"
api="asset_info.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.assetId"
type="base"
title="资产ID"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.assetName"
type="base"
title="资产名称"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.assetType"
type="base"
title="资产类型"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.specification"
type="base"
title="规格"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.purchaseDate"
type="base"
title="购买日期"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.status"
type="base"
title="状态"
class="mb-10"
>
</uni-section>


</template>
</base-description>
<base-button
        ref="allocate_asset_button_ref"
    @click="click_336"
    model="float"
    type="primary"
    title="分配资产"
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






const click_336=(
)=>{
  proxy.$navigate( "/pages/allocate_asset/index?assetId=" + assetId,false)

}

</script>

<style lang="scss" scoped></style>