<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="隐患信息管理"
description="隐患详情"
>

</base-list-header>
<base-description
ref="hazard_info_detail_ref"
:params="{'hazardInfoId':allParams.hazardInfoId,
}"
api="hazard_info_all.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.hazardInfoId"
type="base"
title="编号"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.hazardLocation"
type="base"
title="隐患区域"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.hazardDescription"
type="base"
title="隐患描述"
class="mb-10"
>
</uni-section>



<uni-section
        title="隐患图片"
        class="mb-10"
>
    <base-file-preview :url="description.hazardImage"></base-file-preview>
</uni-section>



<uni-section
:sub-title="description.hazardBasicDescription"
type="base"
title="隐患依据"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.rectificationSuggestions"
type="base"
title="整改建议"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.startTime"
type="base"
title="开始时间"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.endTime"
type="base"
title="结束时间"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.hazardAcceptanceStatusDescription"
type="base"
title="验收状态"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.correlationBasisStatusDescription"
type="base"
title="隐患是否关联依据"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.taskName"
type="base"
title="所属任务"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.enterpriseName"
type="base"
title="被检查企业"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.tenantId"
type="base"
title="租户ID"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.createTime"
type="base"
title="创建时间"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.updateTime"
type="base"
title="截止时间"
class="mb-10"
>
</uni-section>


</template>
</base-description>
</base-wrapper>
<base-wrapper
>
<base-form
@success="success_3144"
:id="allParams.null"
table_module="acceptance_report"
random_int="7849"
model="add"
:rules="{acceptanceReportId:{
rules:[
    {required: true,
    errorMessage: '请输入编号'}],

},rectificationProgress:{
rules:[
    {required: false,
    errorMessage: '请输入验收结果'}],

},acceptanceImage:{
rules:[
    {required: true,
    errorMessage: '请输入验收照片'}],

},acceptanceInstructions:{
rules:[
    {required: true,
    errorMessage: '请输入验收说明'}],

},hazardInfoId:{
rules:[
    {required: false,
    errorMessage: '请输入隐患信息ID'}],

},tenantId:{
rules:[
    {required: false,
    errorMessage: '请输入租户ID'}],

},createTime:{
rules:[
    {required: false,
    errorMessage: '请输入创建时间'}],

},updateTime:{
rules:[
    {required: false,
    errorMessage: '请输入更新时间'}],

},}"
@fail="fail_3144"
primary_key="acceptanceReportId"
:form="form_7849"
query_module="acceptance_report_all"
>
<template #form-items>
<uni-forms-item v-show="!allParams.rectificationProgress" label="验收结果" name="rectificationProgress" >
    <base-select  v-model="form_7849.rectificationProgress" api="acceptance_report_all.get_rectification_progress_status_list" title="验收结果"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.acceptanceImage"   label="验收照片" name="acceptanceImage" required>
    <base-image-upload v-model="form_7849.acceptanceImage"  limit="3"/>
</uni-forms-item>


<uni-forms-item v-show="!allParams.acceptanceInstructions" label="验收说明" name="acceptanceInstructions" required>
    <uni-easyinput   placeholder="请输入验收说明" v-model="form_7849.acceptanceInstructions" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.hazardInfoId" label="隐患信息ID" name="hazardInfoId" >
    <base-select  v-model="form_7849.hazardInfoId" api="acceptance_report_all.get_hazard_info_list" title="隐患信息ID"></base-select>
</uni-forms-item>


<uni-forms-item v-show="!allParams.tenantId" label="租户ID" name="tenantId" >
    <uni-easyinput   placeholder="请输入租户ID" v-model="form_7849.tenantId" />
</uni-forms-item>


</template>
</base-form>
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



















const form_7849 = ref({...allParams.value});


const success_3144=(
)=>{
  proxy.$refs.acceptance_report_detail_ref.refresh(pageVar.hazard_info_id)
  uni.showToast({
  title: '添加成功',
  icon: 'success',
  duration: 2000
});

}
const fail_3144=(
)=>{
  uni.showToast({
  title: '提交失败,请重试',
  icon: 'error',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>