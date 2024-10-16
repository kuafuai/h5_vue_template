<template>
  <base-wrapper>
    <base-wrapper
>
<base-tab
:items="['客户线索详情', '项目列表']"
>
<template #content-0>
<base-description
:params="{'salesLeadId':allParams.salesLeadId,
}"
api="sales_lead_all.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.salesLeadId"
type="base"
title="主键"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.salesName"
type="base"
title="客户线索名称"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.userName"
type="base"
title="跟进人"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.salesDescription"
type="base"
title="客户线索的描述"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.salesStatusName"
type="base"
title="客户线索状态"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.partnerName"
type="base"
title="合作伙伴"
class="mb-10"
>
</uni-section>


</template>
</base-description>
</template>
<template #content-1>
<base-list
:isPage="true"
:params="{'salesLeadId':allParams.salesLeadId,
}"
api="sales_lead_item_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.salesItemName?item.salesItemName:item.salesItemName"
title="客户项目名称"
>

</uni-list-item>
<uni-list-item
:rightText="item.salesItemDescription?item.salesItemDescription:item.salesItemDescription"
title="客户项目描述"
>

</uni-list-item>
<uni-list-item
:rightText="item.salesStatusName?item.salesStatusName:item.salesStatus"
title="客户项目状态"
>

</uni-list-item>
<uni-list-item
:rightText="item.userName?item.userName:item.userId"
title="跟进人"
>

</uni-list-item>
<uni-list-item
:rightText="item.amount?item.amount:item.amount"
title="项目佣金"
>

</uni-list-item>
</template>
</base-list>
</template>
</base-tab>
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














</script>

<style lang="scss" scoped></style>