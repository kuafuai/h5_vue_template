<template>
  <base-wrapper>
    <base-wrapper
>
<base-form
@success="success_850"
:params="{'id':allParams.visitId,
}"
:id="allParams.null"
table_module="visit_management"
random_int="2187"
model="update"
:rules="{id:{
rules:[
    {required: true,
    errorMessage: '请输入主键'}],

},leadStatus:{
rules:[
    {required: true,
    errorMessage: '请输入销售线索状态'}],

},salesmanId:{
rules:[
    {required: true,
    errorMessage: '请输入销售人员'}],

},customerId:{
rules:[
    {required: true,
    errorMessage: '请输入拜访客户'}],

},visitDate:{
rules:[
    {required: true,
    errorMessage: '请输入拜访日期'}],

},visitCustomerName:{
rules:[
    {required: true,
    errorMessage: '请输入拜访客户姓名'}],

},visitCustomerPost:{
rules:[
    {required: true,
    errorMessage: '请输入拜访客户职位'}],

},visitCustomerPhone:{
rules:[
    {required: true,
    errorMessage: '请输入拜访客户手机号'}],

},visitCustomerStage:{
rules:[
    {required: true,
    errorMessage: '请输入拜访客户阶段'}],

},visitCustomerInformation:{
rules:[
    {required: true,
    errorMessage: '请输入拜访客户记录'}],

},}"
@fail="fail_850"
primary_key="id"
:form="form_2187"
>
<template #form-items>
<uni-forms-item v-show="!allParams.leadStatus" label="销售线索状态" name="leadStatus" required>
    <base-select  v-model="form_2187.leadStatus" api="visit_management_all.get_lead_status_list" title="销售线索状态"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.salesmanId" label="销售人员" name="salesmanId" required>
    <base-select  v-model="form_2187.salesmanId" api="visit_management_all.get_salesman_list" title="销售人员"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.customerId" label="拜访客户" name="customerId" required>
    <base-select  v-model="form_2187.customerId" api="visit_management_all.get_customer_list" title="拜访客户"></base-select>
</uni-forms-item>

<uni-forms-item  v-show="!allParams.visitDate"  label="拜访日期" name="visitDate" required>
    <uni-datetime-picker   placeholder="请输入拜访日期" v-model="form_2187.visitDate" type="datetime"
                         :format="'yyyy-MM-dd HH:mm:ss'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>

<uni-forms-item v-show="!allParams.visitCustomerName" label="拜访客户姓名" name="visitCustomerName" required>
    <uni-easyinput   placeholder="请输入拜访客户姓名" v-model="form_2187.visitCustomerName" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.visitCustomerPost" label="拜访客户职位" name="visitCustomerPost" required>
    <uni-easyinput   placeholder="请输入拜访客户职位" v-model="form_2187.visitCustomerPost" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.visitCustomerPhone" label="拜访客户手机号" name="visitCustomerPhone" required>
    <uni-easyinput   placeholder="请输入拜访客户手机号" v-model="form_2187.visitCustomerPhone" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.visitCustomerStage" label="拜访客户阶段" name="visitCustomerStage" required>
    <base-select  v-model="form_2187.visitCustomerStage" api="visit_management_all.get_customer_stage_list" title="拜访客户阶段"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.visitCustomerInformation" label="拜访客户记录" name="visitCustomerInformation" required>
    <uni-easyinput   placeholder="请输入拜访客户记录" v-model="form_2187.visitCustomerInformation" />
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






















const form_2187 = ref({...allParams.value});


const success_850=(
)=>{
  uni.showToast({
  title: '更新成功',
  icon: 'success',
  duration: 2000
});

}
const fail_850=(
)=>{
  uni.showToast({
  title: '提交失败,请重试',
  icon: 'error',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>