<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="（基本信息）隐患依据管理"
description="隐患依据"
>

</base-list-header>
<base-tab
:items="['全部', '收藏']"
>
<template #content-0>
<base-search
@search="search_7081"
ref="hazard_basic_search_ref"
firstSearchDataComment="关键词"
:searchData="{firstMenu:null,secondaryMenu:null,}"
class="m-b-10"
firstSearchData="keyword"
>
<template #collapse="{item}">
<uni-forms-item  label="一级菜单" name="firstMenu" >
    <base-select  v-model="item.firstMenu" api="hazard_basic_all.get_category_menu_list" title="一级菜单"></base-select>
</uni-forms-item>

<uni-forms-item  label="二级菜单" name="secondaryMenu" >
    <base-select  v-model="item.secondaryMenu" api="hazard_basic_all.get_category_menu_list" title="二级菜单"></base-select>
</uni-forms-item>

</template>
</base-search>
<base-list
ref="show_hazard_basic_list"
:isPage="true"
@click="click_8003"
api="hazard_basic_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.keyword!=null && item.keyword!=undefined ?item.keyword+'':item.keyword"
title="关键词:"
>

</uni-list-item>
<uni-list-item
:rightText="item.hazardDescription!=null && item.hazardDescription!=undefined ?item.hazardDescription+'':item.hazardDescription"
title="隐患描述:"
>

</uni-list-item>
<uni-list-item
:rightText="item.rectificationSuggestions!=null && item.rectificationSuggestions!=undefined ?item.rectificationSuggestions+'':item.rectificationSuggestions"
title="整改建议:"
>

</uni-list-item>
<uni-list-item
:rightText="item.hazardTypeStatusDescription!=null && item.hazardTypeStatusDescription!=undefined ?item.hazardTypeStatusDescription+'':item.hazardClassification"
title="检查分类:"
>

</uni-list-item>
</template>
<template #op="{item}">
<base-button
        ref="hazard_basic_select_ref"
    @click="click_4971(item)"
    model="base"
    type="primary"
    title="选择"
    op_param="item"
>

</base-button>
</template>
</base-list>
</template>
<template #content-1>
<base-list
ref="show_hazard_basic_list"
:isPage="true"
@click="click_185"
api="hazard_basic_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.keyword!=null && item.keyword!=undefined ?item.keyword+'':item.keyword"
title="关键词:"
>

</uni-list-item>
<uni-list-item
:rightText="item.hazardDescription!=null && item.hazardDescription!=undefined ?item.hazardDescription+'':item.hazardDescription"
title="隐患描述:"
>

</uni-list-item>
<uni-list-item
:rightText="item.rectificationSuggestions!=null && item.rectificationSuggestions!=undefined ?item.rectificationSuggestions+'':item.rectificationSuggestions"
title="整改建议:"
>

</uni-list-item>
<uni-list-item
:rightText="item.hazardTypeStatusDescription!=null && item.hazardTypeStatusDescription!=undefined ?item.hazardTypeStatusDescription+'':item.hazardClassification"
title="检查分类:"
>

</uni-list-item>
</template>
<template #op="{item}">
<base-button
        ref="hazard_basic_cancel_collect_ref"
    model="base"
    type="primary"
    title="取消收藏"
    op_param="item"
>

</base-button>
<base-button
        ref="hazard_basic_collect_ref"
    model="base"
    type="primary"
    title="收藏"
    op_param="item"
>

</base-button>
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

























const click_4971=(
item
)=>{
  proxy.$back(item)

}
const 取消收藏=(
)=>{

}
const 收藏=(
)=>{

}
const search_7081=(
item
)=>{
  proxy.$refs.show_hazard_basic_list.refresh(item)

}

</script>

<style lang="scss" scoped></style>