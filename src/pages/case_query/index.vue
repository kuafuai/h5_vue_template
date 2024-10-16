<template>
  <base-wrapper>
    <base-wrapper
>
<base-list
ref="case_list_ref"
:isPage="true"
@click="click_1956"
:params="{'caseTitle':'LIKE '%${search_query}%'',
'caseNumber':'LIKE '%${search_query}%'',
}"
is_click="'true'"
api="case_info_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.caseNumber?item.caseNumber:item.caseNumber"
title="案件编号"
>

</uni-list-item>
<uni-list-item
:rightText="item.caseTitle?item.caseTitle:item.caseTitle"
title="案件标题"
>

</uni-list-item>
<uni-list-item
:rightText="item.caseStatus?item.caseStatus:item.caseStatus"
title="案件状态"
>

</uni-list-item>
<uni-list-item
:rightText="item.username?item.username:item.userId"
title="关联用户ID"
>

</uni-list-item>
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






const click_1956=(
item
)=>{
  proxy.$navigate( "/pages/case_detail/${item.case_id}/index",false)

}

</script>

<style lang="scss" scoped></style>