<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="任务信息管理"
description="任务详情"
>

</base-list-header>
<base-form
@success="success_6268"
:params="{'taskInfoId':allParams.taskInfoId,
}"
:id="allParams.null"
table_module="task_info"
random_int="5196"
model="update"
:rules="{taskInfoId:{
rules:[
    {required: true,
    errorMessage: '请输入编号'}],

},taskName:{
rules:[
    {required: true,
    errorMessage: '请输入任务名称'}],

},taskStatus:{
rules:[
    {required: false,
    errorMessage: '请输入任务状态'}],

},enterpriseInformationId:{
rules:[
    {required: false,
    errorMessage: '请输入被检查企业'}],

},userInfoId:{
rules:[
    {required: false,
    errorMessage: '请输入发起人'}],

},checker:{
rules:[
    {required: false,
    errorMessage: '请输入检查人'}],

},startTime:{
rules:[
    {required: true,
    errorMessage: '请输入开始日期'}],

},endTime:{
rules:[
    {required: true,
    errorMessage: '请输入截止时间'}],

},checkRequire:{
rules:[
    {required: false,
    errorMessage: '请输入检查要求'}],

},tenantId:{
rules:[
    {required: false,
    errorMessage: '请输入租户ID'}],

},hazardInfoCount:{
rules:[
    {required: false,
    errorMessage: '请输入隐患数量'}],

},hazardBasicInfoCount:{
rules:[
    {required: false,
    errorMessage: '请输入未绑定依据的隐患数量'}],

},hazardNotBasicInfoCount:{
rules:[
    {required: false,
    errorMessage: '请输入绑定依据的隐患数量'}],

},hazardAcceptanceCount:{
rules:[
    {required: false,
    errorMessage: '请输入验收的隐患'}],

},hazardNotAcceptanceCount:{
rules:[
    {required: false,
    errorMessage: '请输入未验收的隐患'}],

},createTime:{
rules:[
    {required: false,
    errorMessage: '请输入创建时间'}],

},updateTime:{
rules:[
    {required: false,
    errorMessage: '请输入更新时间'}],

},}"
@fail="fail_6268"
primary_key="taskInfoId"
:form="form_5196"
query_module="task_info"
>
<template #form-items>

<uni-forms-item v-show="!allParams.taskName" label="任务名称" name="taskName" required>
    <uni-easyinput   placeholder="请输入任务名称" v-model="form_5196.taskName" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.enterpriseInformationId" label="被检查企业" name="enterpriseInformationId" >
    <base-one  v-model="form_5196.enterpriseInformationId" ref_id="c53d87b7e" :show_value="form_5196.enterpriseName" origin_page_path="/pages/update_task_info/index" page_path="/pages/update_task_info_enterprise_information_id/index"   title="被检查企业"></base-one>
</uni-forms-item>


<uni-forms-item v-show="!allParams.userInfoId" label="发起人" name="userInfoId" >
    <base-select  v-model="form_5196.userInfoId" api="task_info_all.get_user_info_list" title="发起人"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.checker" label="检查人" name="checker" >
    <base-one  v-model="form_5196.checker" model="more" ref_id="5c14bb6b8" :show_value="form_5196.userName" origin_page_path="/pages/update_task_info/index" page_path="/pages/update_task_info_checker/index"   title="检查人"></base-one>
</uni-forms-item>


<uni-forms-item  v-show="!allParams.startTime" label="开始日期" name="startTime" required>
    <uni-datetime-picker    placeholder="请输入开始日期" v-model="form_5196.startTime" type="date"
                         :format="'yyyy-MM-dd'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>

<uni-forms-item  v-show="!allParams.endTime" label="截止时间" name="endTime" required>
    <uni-datetime-picker    placeholder="请输入截止时间" v-model="form_5196.endTime" type="date"
                         :format="'yyyy-MM-dd'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.checkRequire" label="检查要求" name="checkRequire" >
    <uni-easyinput   placeholder="请输入检查要求" v-model="form_5196.checkRequire" />
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




















const form_5196 = ref({...allParams.value});


const success_6268=(
)=>{
  proxy.$navigate( "/pages/task_info_detail/index?taskInfoId=" + allParams.value.taskInfoId,false)
  uni.showToast({
  title: '修改成功',
  icon: 'success',
  duration: 2000
});
  proxy.$refs.task_info_detail.refresh()
  proxy.$refs.show_task_info_list.refresh()

}
const 修改失败=(
)=>{
  uni.showToast({
  title: '提交失败，请重试',
  icon: 'error',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>