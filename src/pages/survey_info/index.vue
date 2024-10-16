<template>
  <base-wrapper>
    <base-wrapper
>
<base-list
ref="company_select_base_form"
:isPage="true"
@click="click_2313"
:params="{'creatorId':allParams.creatorId,
'status':'1',
}"
is_click="'true'"
api="survey_info_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.surveyName?item.surveyName:item.surveyName"
title="主题:"
>

</uni-list-item>
<uni-list-item
:rightText="item.surveyDescription?item.surveyDescription:item.surveyDescription"
title="描述:"
>

</uni-list-item>
<uni-list-item
:rightText="item.startTime?item.startTime:item.startTime"
title="开始时间:"
>

</uni-list-item>
<uni-list-item
:rightText="item.endTime?item.endTime:item.endTime"
title="结束时间:"
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






const click_2313=(
item
)=>{
  proxy.$navigate( "/pages/survey_info_detail/index?id=" + item.id,false)

}

</script>

<style lang="scss" scoped></style>