<template>
  <base-wrapper>
    <base-wrapper
>
<base-search
@search="search_2179"
ref="base_search_ref"
firstSearchDataComment="企业名称"
:searchData="{}"
class="m-b-10"
:is_show_advance="false"
firstSearchData="enterpriseName"
>
<template #collapse="{item}">

</template>
</base-search>
<base-list
ref="show_task_info_list"
:isPage="true"
@click="click_3375"
api="enterprise_information.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.enterpriseName!=null && item.enterpriseName!=undefined ?item.enterpriseName+'':item.enterpriseName"
title="企业名称:"
>

</uni-list-item>
<uni-list-item
:rightText="item.personInCharge!=null && item.personInCharge!=undefined ?item.personInCharge+'':item.personInCharge"
title="主要负责人:"
>

</uni-list-item>
<uni-list-item
:rightText="item.phoneNumber!=null && item.phoneNumber!=undefined ?item.phoneNumber+'':item.phoneNumber"
title="联系电话:"
>

</uni-list-item>
<uni-list-item
:rightText="item.enterpriseAddress!=null && item.enterpriseAddress!=undefined ?item.enterpriseAddress+'':item.enterpriseAddress"
title="企业地址:"
>

</uni-list-item>
<uni-list-item
:rightText="item.sector!=null && item.sector!=undefined ?item.sector+'':item.sector"
title="所属行业:"
>

</uni-list-item>
</template>
<template #op="{item}">
<base-button
        ref="click_123"
    @click="click_9651(item)"
    model="base"
    type="primary"
    title="选择"
    op_param="item"
>

</base-button>
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











const click_9651=(
item
)=>{

}
const search_2179=(
item
)=>{
  proxy.$refs.show_task_info_list.refresh(item)

}
const click_3375=(
item
)=>{

}

</script>

<style lang="scss" scoped></style>