<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="项目信息"
description="展示项目详情与记录"
>

</base-list-header>
<base-tab
:items="['项目详情', '沟通记录', '跟进记录']"
>
<template #content-0>
<base-description
ref="base_description_ref"
:params="{'projectInfoId':allParams.projectInfoId,
}"
api="project_info_all.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.projectInfoId"
type="base"
title="编号"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.userName"
type="base"
title="负责人用户ID"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.projectName"
type="base"
title="项目名称"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.projectOneWord"
type="base"
title="项目一句话"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.projectDescription"
type="base"
title="项目描述"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.companyEstablishedDate"
type="base"
title="公司成立时间"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.market"
type="base"
title="市场"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.product"
type="base"
title="产品"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.competition"
type="base"
title="竞争"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.coreCompetence"
type="base"
title="核心竞争力"
class="mb-10"
>
</uni-section>



<uni-section
        title="商业计划书"
        class="mb-10"
>
    <base-file-preview :url="description.businessPlanFile"></base-file-preview>
</uni-section>



<uni-section
:sub-title="description.statusDescription"
type="base"
title="审核状态"
class="mb-10"
>
</uni-section>


</template>
</base-description>
</template>
<template #content-1>
<base-button
        ref="contact_button_ref"
    @click="click_3643"
    model="base"
    type="primary"
    title="申请添加联系人"
>

</base-button>
<base-dialog
ref="contact_show_dialog_ref1212"
text="完善用户信息"
type="bottom"
>
<template #dialog>
<base-form
@success="success_9755"
:params="{'buinessPersonId':allParams.currentUser.userId,
'projectInfoId':allParams.projectInfoId,
}"
:id="allParams.null"
table_module="contact_info"
random_int="8388"
model="update_or_add"
:rules="{contactInfoId:{
rules:[
    {required: true,
    errorMessage: '请输入编号'}],

},projectInfoId:{
rules:[
    {required: false,
    errorMessage: '请输入项目id'}],

},userInfoId:{
rules:[
    {required: false,
    errorMessage: '请输入项目负责人id'}],

},prjectOwnerWechat:{
rules:[
    {required: false,
    errorMessage: '请输入项目负责人wechat'}],

},buinessPersonId:{
rules:[
    {required: false,
    errorMessage: '请输入投资人id'}],

},investorWechat:{
rules:[
    {required: false,
    errorMessage: '请输入投资人wechat'}],

},applicationStatus:{
rules:[
    {required: false,
    errorMessage: '请输入申请状态'}],

},}"
@fail="fail_9755"
primary_key="contactInfoId"
:form="form_8388"
>
<template #form-items>
<uni-forms-item v-show="!allParams.projectInfoId" label="项目id" name="projectInfoId" >
    <base-select  v-model="form_8388.projectInfoId" api="contact_info_all.get_project_info_list" title="项目id"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.userInfoId" label="项目负责人id" name="userInfoId" >
    <base-select  v-model="form_8388.userInfoId" api="contact_info_all.get_user_info_list" title="项目负责人id"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.prjectOwnerWechat" label="项目负责人wechat" name="prjectOwnerWechat" >
    <base-select 
    disabled
 v-model="form_8388.prjectOwnerWechat" api="contact_info_all.get_user_info_list" title="项目负责人wechat"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.buinessPersonId" label="投资人id" name="buinessPersonId" >
    <base-select  v-model="form_8388.buinessPersonId" api="contact_info_all.get_buiness_person_list" title="投资人id"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.investorWechat" label="投资人wechat" name="investorWechat" >
    <base-select 
    disabled
 v-model="form_8388.investorWechat" api="contact_info_all.get_buiness_person_list" title="投资人wechat"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.applicationStatus" label="申请状态" name="applicationStatus" >
    <base-select 
    disabled
 v-model="form_8388.applicationStatus" api="contact_info_all.get_application_status_list" title="申请状态"></base-select>
</uni-forms-item>

</template>
</base-form>
</template>
</base-dialog>
<base-button
        ref="goutong_button_ref"
    @click="click_4245"
    model="float"
    type="primary"
    title="更新信息"
>

</base-button>
<base-dialog
ref="show_dialog_ref1212"
text="完善用户信息"
type="bottom"
>
<template #dialog>
<base-form
@success="success_2600"
:params="{'buinessPersonId':allParams.currentUser.userId,
'projectInfoId':allParams.projectInfoId,
}"
:id="allParams.null"
table_module="project_follow_info"
random_int="6158"
model="update_or_add"
:rules="{projectFollowInfoId:{
rules:[
    {required: true,
    errorMessage: '请输入编号'}],

},projectInfoId:{
rules:[
    {required: false,
    errorMessage: '请输入项目Id'}],

},buinessPersonId:{
rules:[
    {required: false,
    errorMessage: '请输入投资人id'}],

},communicationStatus:{
rules:[
    {required: false,
    errorMessage: '请输入沟通状态'}],

},attentionRate:{
rules:[
    {required: false,
    errorMessage: '请输入打分'}],

},}"
@fail="fail_2600"
primary_key="projectFollowInfoId"
:form="form_6158"
>
<template #form-items>
<uni-forms-item v-show="!allParams.projectInfoId" label="项目Id" name="projectInfoId" >
    <base-select  v-model="form_6158.projectInfoId" api="project_follow_info_all.get_project_info_list" title="项目Id"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.buinessPersonId" label="投资人id" name="buinessPersonId" >
    <base-select  v-model="form_6158.buinessPersonId" api="project_follow_info_all.get_buiness_person_list" title="投资人id"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.communicationStatus" label="沟通状态" name="communicationStatus" >
    <base-select  v-model="form_6158.communicationStatus" api="project_follow_info_all.get_communication_status_list" title="沟通状态"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.attentionRate" label="打分" name="attentionRate" >
    <uni-easyinput   placeholder="请输入打分" v-model="form_6158.attentionRate" />
</uni-forms-item>


</template>
</base-form>
</template>
</base-dialog>
<base-description
ref="project_follow_info_description_ref_1212"
:params="{'projectInfoId':allParams.projectInfoId,
'buinessPersonId':allParams.currentUser.userId,
}"
api="project_follow_info_all.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.projectFollowInfoId"
type="base"
title="编号"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.projectName"
type="base"
title="项目Id"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.buinessPersonName"
type="base"
title="投资人id"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.statusDescription"
type="base"
title="沟通状态"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.attentionRate"
type="base"
title="打分"
class="mb-10"
>
</uni-section>


</template>
</base-description>
</template>
<template #content-2>
<base-button
        ref="gengjing_button_ref"
    @click="click_7373"
    model="float"
    type="primary"
    title="更新信息"
>

</base-button>
<base-dialog
ref="gengjin_show_dialog_ref1212"
text="完善用户信息"
type="bottom"
>
<template #dialog>
<base-form
@success="success_5722"
:id="allParams.null"
table_module="follow_communicate_info"
random_int="4320"
model="add"
:rules="{followCommunicateInfoId:{
rules:[
    {required: true,
    errorMessage: '请输入编号'}],

},projectInfoId:{
rules:[
    {required: false,
    errorMessage: '请输入项目Id'}],

},buinessPersonId:{
rules:[
    {required: false,
    errorMessage: '请输入投资人id'}],

},title:{
rules:[
    {required: false,
    errorMessage: '请输入沟通主题'}],

},description:{
rules:[
    {required: false,
    errorMessage: '请输入沟通详情'}],

},}"
@fail="fail_5722"
primary_key="followCommunicateInfoId"
:form="form_4320"
>
<template #form-items>
<uni-forms-item v-show="!allParams.projectInfoId" label="项目Id" name="projectInfoId" >
    <base-select  v-model="form_4320.projectInfoId" api="follow_communicate_info_all.get_project_info_list" title="项目Id"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.buinessPersonId" label="投资人id" name="buinessPersonId" >
    <base-select  v-model="form_4320.buinessPersonId" api="follow_communicate_info_all.get_buiness_person_list" title="投资人id"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.title" label="沟通主题" name="title" >
    <uni-easyinput   placeholder="请输入沟通主题" v-model="form_4320.title" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.description" label="沟通详情" name="description" >
    <uni-easyinput   placeholder="请输入沟通详情" v-model="form_4320.description" />
</uni-forms-item>


</template>
</base-form>
</template>
</base-dialog>
<base-list
ref="gengjing_flow_show_list"
:isPage="true"
@click="click_6557"
:params="{'projectInfoId':allParams.projectInfoId,
'buinessPersonId':allParams.currentUser.userId,
}"
api="follow_communicate_info_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.projectName?item.projectName:item.projectInfoId"
title="项目Id:"
>

</uni-list-item>
<uni-list-item
:rightText="item.buinessPersonName?item.buinessPersonName:item.buinessPersonId"
title="投资人id:"
>

</uni-list-item>
<uni-list-item
:rightText="item.title?item.title:item.title"
title="沟通主题:"
>

</uni-list-item>
<uni-list-item
:rightText="item.description?item.description:item.description"
title="沟通详情:"
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
























const form_8388 = ref({...allParams.value});













const form_6158 = ref({...allParams.value});

















const form_4320 = ref({...allParams.value});








const click_4245=(
)=>{
  proxy.$refs.show_dialog_ref1212.showDialog()

}
const success_2600=(
item
)=>{
  proxy.$refs.show_dialog_ref1212.closeDialog()
  proxy.$refs.project_follow_info_description_ref_1212.refresh()

}
const fail_2600=(
)=>{
  uni.showToast({
  title: '提交失败，请重试',
  icon: 'error',
  duration: 2000
});

}
const click_7373=(
)=>{
  proxy.$refs.gengjin_show_dialog_ref1212.showDialog()

}
const success_5722=(
item
)=>{
  proxy.$refs.gengjin_show_dialog_ref1212.closeDialog()
  proxy.$refs.gengjing_flow_show_list.refresh()

}
const fail_5722=(
)=>{
  uni.showToast({
  title: '提交失败，请重试',
  icon: 'error',
  duration: 2000
});

}
const click_3643=(
)=>{
  proxy.$refs.contact_show_dialog_ref1212.showDialog()

}
const success_9755=(
item
)=>{
  proxy.$refs.contact_show_dialog_ref1212.closeDialog()
  uni.showToast({
  title: '添加成功',
  icon: 'success',
  duration: 2000
});

}
const fail_9755=(
)=>{
  uni.showToast({
  title: '提交失败，请重试',
  icon: 'error',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>