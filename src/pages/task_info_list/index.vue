<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="任务信息管理"
description="任务列表"
>

</base-list-header>
<base-search
@search="search_1420"
:other_search_condition="[{'name':'隐患是否都关联依据','value':[{'text':'是','value':'hazard_basic_info_count=0'},{'text':'否','value':'hazard_basic_info_count!=0'}]},{'name':'隐患是否都已验收','value':[{'text':'是','value':'hazard_not_acceptance_count=0'},{'text':'否','value':'hazard_not_acceptance_count!=hazard_acceptance_count'}]}]"
ref="base_search_ref"
firstSearchDataComment="任务名称"
:searchData="{taskStatus:null,companyName:null,}"
class="m-b-10"
firstSearchData="taskName"
>
<template #collapse="{item}">
<uni-forms-item  label="任务状态" name="taskStatus" >
    <base-select  v-model="item.taskStatus" api="task_info_all.get_task_status_enums_list" title="任务状态"></base-select>
</uni-forms-item>

</template>
</base-search>
<base-list
ref="show_task_info_list"
:isPage="true"
@click="click_6814"
:is_click="true"
api="task_info_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.taskName!=null && item.taskName!=undefined ?item.taskName+'':item.taskName"
title="任务名称:"
>

</uni-list-item>
<uni-list-item
:rightText="item.taskStatusDescription!=null && item.taskStatusDescription!=undefined ?item.taskStatusDescription+'':item.taskStatus"
title="任务状态:"
>

</uni-list-item>
<uni-list-item
:rightText="item.userName!=null && item.userName!=undefined ?item.userName+'':item.userInfoId"
title="发起人:"
>

</uni-list-item>
<base-list-item
:rightText="item.checker!=null && item.checker!=undefined ?item.checker+'':item.checker"
title="检查人:"
>

</base-list-item>
<uni-list-item
:rightText="item.startTime!=null && item.startTime!=undefined ?item.startTime+'':item.startTime"
title="开始日期:"
>

</uni-list-item>
<uni-list-item
:rightText="item.endTime!=null && item.endTime!=undefined ?item.endTime+'':item.endTime"
title="截止时间:"
>

</uni-list-item>
<uni-list-item
:rightText="item.hazardInfoCount!=null && item.hazardInfoCount!=undefined ?item.hazardInfoCount+'':item.hazardInfoCount"
title="隐患数量:"
>

</uni-list-item>
<uni-list-item
:rightText="item.hazardBasicInfoCount!=null && item.hazardBasicInfoCount!=undefined ?item.hazardBasicInfoCount+'':item.hazardBasicInfoCount"
title="未绑定依据的隐患数量:"
>

</uni-list-item>
<uni-list-item
:rightText="item.hazardNotBasicInfoCount!=null && item.hazardNotBasicInfoCount!=undefined ?item.hazardNotBasicInfoCount+'':item.hazardNotBasicInfoCount"
title="绑定依据的隐患数量:"
>

</uni-list-item>
<uni-list-item
:rightText="item.hazardAcceptanceCount!=null && item.hazardAcceptanceCount!=undefined ?item.hazardAcceptanceCount+'':item.hazardAcceptanceCount"
title="验收的隐患:"
>

</uni-list-item>
<uni-list-item
:rightText="item.hazardNotAcceptanceCount!=null && item.hazardNotAcceptanceCount!=undefined ?item.hazardNotAcceptanceCount+'':item.hazardNotAcceptanceCount"
title="未验收的隐患:"
>

</uni-list-item>
</template>
</base-list>
<base-button
        ref="add_task_ref"
    @click="click_9122"
    model="float"
    type="primary"
    title="添加任务"
>

</base-button>
<base-dialog
ref="show_task_dialog"
comp_type="base"
type="bottom"
content="您确定要提交吗？"
>
<template #dialog>
<base-form
@success="success_5282"
:id="allParams.null"
table_module="task_info"
random_int="3879"
model="add"
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
@fail="fail_5282"
primary_key="taskInfoId"
:form="form_3879"
query_module="task_info"
>
<template #form-items>

<uni-forms-item v-show="!allParams.taskName" label="任务名称" name="taskName" required>
    <uni-easyinput   placeholder="请输入任务名称" v-model="form_3879.taskName" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.taskStatus" label="任务状态" name="taskStatus" >
    <base-select 
    disabled
 v-model="form_3879.taskStatus" api="task_info_all.get_task_status_enums_list" title="任务状态"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.enterpriseInformationId" label="被检查企业" name="enterpriseInformationId" >
    <base-one  v-model="form_3879.enterpriseInformationId" ref_id="c1090b4e5" :show_value="form_3879.enterpriseName" origin_page_path="/pages/task_info_list/index" page_path="/pages/task_info_list_enterprise_information_id/index"   title="被检查企业"></base-one>
</uni-forms-item>


<uni-forms-item v-show="!allParams.userInfoId" label="发起人" name="userInfoId" >
    <base-select  v-model="form_3879.userInfoId" api="task_info_all.get_user_info_list" title="发起人"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.checker" label="检查人" name="checker" >
    <base-one  v-model="form_3879.checker" model="more" ref_id="e71401c1e" :show_value="form_3879.userName" origin_page_path="/pages/task_info_list/index" page_path="/pages/task_info_list_checker/index"   title="检查人"></base-one>
</uni-forms-item>


<uni-forms-item  v-show="!allParams.startTime" label="开始日期" name="startTime" required>
    <uni-datetime-picker    placeholder="请输入开始日期" v-model="form_3879.startTime" type="date"
                         :format="'yyyy-MM-dd'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>

<uni-forms-item  v-show="!allParams.endTime" label="截止时间" name="endTime" required>
    <uni-datetime-picker    placeholder="请输入截止时间" v-model="form_3879.endTime" type="date"
                         :format="'yyyy-MM-dd'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.checkRequire" label="检查要求" name="checkRequire" >
    <uni-easyinput   placeholder="请输入检查要求" v-model="form_3879.checkRequire" />
</uni-forms-item>



<uni-forms-item v-show="!allParams.tenantId" label="租户ID" name="tenantId" >
    <uni-easyinput 
    disabled
  placeholder="请输入租户ID" v-model="form_3879.tenantId" />
</uni-forms-item>


</template>
</base-form>
</template>
</base-dialog>
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
































const form_3879 = ref({...allParams.value});



const search_1420=(
item
)=>{
  proxy.$refs.show_task_info_list.refresh(item)

}
const click_6814=(
item
)=>{
  proxy.$navigate( "/pages/task_info_detail/index?taskInfoId=" + item.taskInfoId,false)

}
const click_9122=(
)=>{
  proxy.$refs.show_task_dialog.showDialog()

}
const success_5282=(
)=>{
  proxy.$refs.show_task_dialog.closeDialog()
  uni.showToast({
  title: '添加成功',
  icon: 'success',
  duration: 2000
});
  proxy.$refs.show_task_info_list.refresh()

}
const fail_5282=(
)=>{
  uni.showToast({
  title: '提交失败，请重试',
  icon: 'error',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>