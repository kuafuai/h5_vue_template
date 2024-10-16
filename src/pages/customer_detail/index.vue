<template>
  <base-wrapper>
    <base-wrapper
>
<base-form
@success="success_2153"
:params="{'id':allParams.customerId,
}"
:id="allParams.null"
table_module="customer"
random_int="582"
model="update"
:rules="{id:{
rules:[
    {required: true,
    errorMessage: '请输入主键'}],

},customerName:{
rules:[
    {required: true,
    errorMessage: '请输入客户名称'}],

},customerIndustry:{
rules:[
    {required: true,
    errorMessage: '请输入客户行业'}],

},customerPersonnelScale:{
rules:[
    {required: true,
    errorMessage: '请输入客户人员规模'}],

},customerHierarchy:{
rules:[
    {required: true,
    errorMessage: '请输入客户分层'}],

},customerAddress:{
rules:[
    {required: true,
    errorMessage: '请输入客户地址'}],

},customerInformation:{
rules:[
    {required: true,
    errorMessage: '请输入客户介绍'}],

},}"
@fail="fail_2153"
primary_key="id"
:form="form_582"
>
<template #form-items>
<uni-forms-item v-show="!allParams.customerName" label="客户名称" name="customerName" required>
    <uni-easyinput   placeholder="请输入客户名称" v-model="form_582.customerName" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.customerIndustry" label="客户行业" name="customerIndustry" required>
    <base-select  v-model="form_582.customerIndustry" api="customer_all.get_industry_list" title="客户行业"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.customerPersonnelScale" label="客户人员规模" name="customerPersonnelScale" required>
    <base-select  v-model="form_582.customerPersonnelScale" api="customer_all.get_personnel_scale_list" title="客户人员规模"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.customerHierarchy" label="客户分层" name="customerHierarchy" required>
    <base-select  v-model="form_582.customerHierarchy" api="customer_all.get_customer_hierarchy_list" title="客户分层"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.customerAddress" label="客户地址" name="customerAddress" required>
    <uni-easyinput   placeholder="请输入客户地址" v-model="form_582.customerAddress" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.customerInformation" label="客户介绍" name="customerInformation" required>
    <uni-easyinput   placeholder="请输入客户介绍" v-model="form_582.customerInformation" />
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
















const form_582 = ref({...allParams.value});


const success_2153=(
)=>{
  uni.showToast({
  title: '更新成功',
  icon: 'success',
  duration: 2000
});

}
const fail_2153=(
)=>{
  uni.showToast({
  title: '提交失败,请重试',
  icon: 'error',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>