<template>
  <base-wrapper
>
<base-description
ref="product_detail_description_ref"
:params="{'id':allParams.productId,
}"
api="product_all.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.id"
type="base"
title="主键"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.username"
type="base"
title="用户ID"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.title"
type="base"
title="标题"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.description"
type="base"
title="描述"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.price"
type="base"
title="价格"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.imageUrl"
type="base"
title="图片链接"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.createdAt"
type="base"
title="创建时间"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.updatedAt"
type="base"
title="更新时间"
class="mb-10"
>
</uni-section>


</template>
</base-description>
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







</script>

<style lang="scss" scoped></style>