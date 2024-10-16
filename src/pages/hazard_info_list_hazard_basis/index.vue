<template>
  <base-wrapper>
    <base-one-show
ref="base_one_show_2526b1"
firstSearchDataComment="依据来源"
@show_search="base_one_show_2526b1_search"
list_ref="base_list_2526b1"
select_text_key="hazardBasicDescription"
select_value_key="hazardBasicId"
firstSearchData="hazardBasicDescription"
>
<template #list>
<base-list
ref="base_list_2526b1"
:isPage="true"
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
        ref="base_button_2526b1"
    @click="click_1705(item)"
    model="base"
    type="primary"
    title="选择"
    op_param="item"
>

</base-button>
</template>
</base-list>
</template>
</base-one-show>
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


const base_one_show_2526b1_search = (item) => {
    proxy.$refs.base_list_2526b1.refresh(item)
}

let more_result_list = ref([])

const click_1705=(
item
)=>{
  proxy.$refs.base_one_show_2526b1.refresh(item)

}

</script>

<style lang="scss" scoped></style>