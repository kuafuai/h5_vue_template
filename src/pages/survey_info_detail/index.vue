<template>
  <base-wrapper>
    <base-list-header
nickname="调查问卷"
description="展示调查问卷的信息"
>

</base-list-header>
<base-button
        ref="dati_button_ref"
    @click="click_308"
    model="base"
    type="primary"
    title="开始作答"
>

</base-button>
<base-tab
:items="['我的答题情况', '问卷答题情况']"
>
<template #content-0>
<base-wrapper
>
<base-list
ref="company_select_base_form"
:isPage="true"
@click="click_3502"
:params="{'surveyInfoId':allParams.id,
'userId':allParams.currentUser.userId,
}"
api="answer_info_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.typeName?item.typeName:item.questionType"
title="问题类型:"
>

</uni-list-item>
<uni-list-item
:rightText="item.name?item.name:item.userId"
title="用户:"
>

</uni-list-item>
<uni-list-item
:rightText="item.surveyName?item.surveyName:item.surveyInfoId"
title="问卷:"
>

</uni-list-item>
<uni-list-item
:rightText="item.questionName?item.questionName:item.questionInfoId"
title="问题:"
>

</uni-list-item>
<uni-list-item
:rightText="item.optionName?item.optionName:item.userAnswer"
title="答案内容:"
>

</uni-list-item>
</template>
</base-list>
</base-wrapper>
</template>
<template #content-1>
<base-statistics
group_result_show_display="chart"
:is_group="true"
ref="answer_info_statistics_1"
:params="{'surveyInfoId':allParams.id,
'userAnswer':'',
}"
model="answer_info_statistics_1_count"
api="answer_info_statistic"
title="选择题的统计"
>

</base-statistics>
<base-statistics
group_result_show_display="text"
:is_group="true"
ref="answer_info_statistics_2"
:params="{'surveyInfoId':allParams.id,
'questionType':'3',
}"
model="answer_info_statistics_2_"
api="answer_info_statistic"
title="填空题的统计"
>

</base-statistics>
</template>
</base-tab>
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
















const click_308=(
)=>{
  proxy.$navigate( "/pages/answer/index?surveyInfoId=" + allParams.value.id,false)

}

</script>

<style lang="scss" scoped></style>