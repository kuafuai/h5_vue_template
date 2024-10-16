<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="项目信息"
description="完善项目信息"
>

</base-list-header>
<base-form
@success="success_905"
:params="{'userInfoId':allParams.currentUser.userId,
}"
:id="allParams.null"
table_module="project_info"
random_int="4064"
model="update_or_add"
:rules="{projectInfoId:{
rules:[
    {required: true,
    errorMessage: '请输入编号'}],

},userInfoId:{
rules:[
    {required: true,
    errorMessage: '请输入负责人用户ID'}],

},projectName:{
rules:[
    {required: true,
    errorMessage: '请输入项目名称'}],

},projectOneWord:{
rules:[
    {required: false,
    errorMessage: '请输入项目一句话'}],

},projectDescription:{
rules:[
    {required: false,
    errorMessage: '请输入项目描述'}],

},companyEstablishedDate:{
rules:[
    {required: false,
    errorMessage: '请输入公司成立时间'}],

},market:{
rules:[
    {required: false,
    errorMessage: '请输入市场'}],

},product:{
rules:[
    {required: true,
    errorMessage: '请输入产品'}],

},competition:{
rules:[
    {required: false,
    errorMessage: '请输入竞争'}],

},coreCompetence:{
rules:[
    {required: true,
    errorMessage: '请输入核心竞争力'}],

},businessPlanFile:{
rules:[
    {required: false,
    errorMessage: '请输入商业计划书'}],

},reviewStatus:{
rules:[
    {required: false,
    errorMessage: '请输入审核状态'}],

},}"
@fail="fail_905"
primary_key="projectInfoId"
:form="form_4064"
>
<template #form-items>
<uni-forms-item v-show="!allParams.userInfoId" label="负责人用户ID" name="userInfoId" required>
    <base-select  v-model="form_4064.userInfoId" api="project_info_all.get_user_info_list" title="负责人用户ID"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.projectName" label="项目名称" name="projectName" required>
    <uni-easyinput   placeholder="请输入项目名称" v-model="form_4064.projectName" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.projectOneWord" label="项目一句话" name="projectOneWord" >
    <uni-easyinput   placeholder="请输入项目一句话" v-model="form_4064.projectOneWord" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.projectDescription" label="项目描述" name="projectDescription" >
    <base-textarea   placeholder="请输入项目描述" v-model="form_4064.projectDescription" class="custom-textarea"/>
</uni-forms-item>


<uni-forms-item  v-show="!allParams.companyEstablishedDate" label="公司成立时间" name="companyEstablishedDate" >
    <uni-datetime-picker    placeholder="请输入公司成立时间" v-model="form_4064.companyEstablishedDate" type="date"
                         :format="'yyyy-MM-dd'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>

<uni-forms-item v-show="!allParams.market" label="市场" name="market" >
    <base-textarea   placeholder="请输入市场" v-model="form_4064.market" class="custom-textarea"/>
</uni-forms-item>


<uni-forms-item v-show="!allParams.product" label="产品" name="product" required>
    <base-textarea   placeholder="请输入产品" v-model="form_4064.product" class="custom-textarea"/>
</uni-forms-item>


<uni-forms-item v-show="!allParams.competition" label="竞争" name="competition" >
    <base-textarea   placeholder="请输入竞争" v-model="form_4064.competition" class="custom-textarea"/>
</uni-forms-item>


<uni-forms-item v-show="!allParams.coreCompetence" label="核心竞争力" name="coreCompetence" required>
    <base-textarea   placeholder="请输入核心竞争力" v-model="form_4064.coreCompetence" class="custom-textarea"/>
</uni-forms-item>


<uni-forms-item v-show="!allParams.businessPlanFile" label="商业计划书" name="businessPlanFile" >
    <base-upload  v-model="form_4064.businessPlanFile"  />
</uni-forms-item>

<uni-forms-item v-show="!allParams.reviewStatus" label="审核状态" name="reviewStatus" >
    <base-select 
    disabled
 v-model="form_4064.reviewStatus" api="project_info_all.get_review_status_list" title="审核状态"></base-select>
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




























const form_4064 = ref({...allParams.value});


const success_905=(
item
)=>{
  uni.showToast({
  title: '提交成功',
  icon: 'success',
  duration: 2000
});

}
const fail_905=(
)=>{
  uni.showToast({
  title: '提交失败，请重试',
  icon: 'error',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>