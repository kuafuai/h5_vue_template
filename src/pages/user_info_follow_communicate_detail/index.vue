<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="联系记录"
description="填写微信号，同意好友申请"
>

</base-list-header>
<base-form
@success="success_3319"
:params="{'contactInfoId':allParams.contactInfoId,
}"
:id="allParams.null"
table_module="contact_info"
random_int="1293"
model="update"
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
@fail="fail_3319"
primary_key="contactInfoId"
:form="form_1293"
>
<template #form-items>
<uni-forms-item v-show="!allParams.projectInfoId" label="项目id" name="projectInfoId" >
    <base-select  v-model="form_1293.projectInfoId" api="contact_info_all.get_project_info_list" title="项目id"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.userInfoId" label="项目负责人id" name="userInfoId" >
    <base-select  v-model="form_1293.userInfoId" api="contact_info_all.get_user_info_list" title="项目负责人id"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.prjectOwnerWechat" label="项目负责人wechat" name="prjectOwnerWechat" >
    <base-select 
    disabled
 v-model="form_1293.prjectOwnerWechat" api="contact_info_all.get_user_info_list" title="项目负责人wechat"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.buinessPersonId" label="投资人id" name="buinessPersonId" >
    <base-select  v-model="form_1293.buinessPersonId" api="contact_info_all.get_buiness_person_list" title="投资人id"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.investorWechat" label="投资人wechat" name="investorWechat" >
    <base-select 
    disabled
 v-model="form_1293.investorWechat" api="contact_info_all.get_buiness_person_list" title="投资人wechat"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.applicationStatus" label="申请状态" name="applicationStatus" >
    <base-select  v-model="form_1293.applicationStatus" api="contact_info_all.get_application_status_list" title="申请状态"></base-select>
</uni-forms-item>

</template>
</base-form>
<base-description
ref="base_description_ref"
:params="{'contactInfoId':allParams.contactInfoId,
'applicationStatus':'2',
}"
api="contact_info_all.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.contactInfoId"
type="base"
title="编号"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.projectName"
type="base"
title="项目id"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.userName"
type="base"
title="项目负责人id"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.wechatNumber"
type="base"
title="项目负责人wechat"
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
:sub-title="description.bWechatNumber"
type="base"
title="投资人wechat"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.statusDescription"
type="base"
title="申请状态"
class="mb-10"
>
</uni-section>


</template>
</base-description>
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


















const form_1293 = ref({...allParams.value});





const success_3319=(
item
)=>{
  proxy.$refs.base_description_ref.refresh()

}

</script>

<style lang="scss" scoped></style>