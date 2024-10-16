<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="商机管理"
description="展示商机"
>

</base-list-header>
<base-button
        ref="button_ref"
    @click="click_2262"
    model="float"
    type="primary"
    title="添加商机"
>

</base-button>
<base-list
ref="business_opportunity_show_list"
:isPage="true"
@click="click_3168"
:params="{'salesmanId':allParams.currentUser.userId,
}"
is_click="'true'"
api="business_opportunity_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.opportunityStatusName?item.opportunityStatusName:item.opportunityStatus"
title="商机状态:"
>

</uni-list-item>
<uni-list-item
:rightText="item.salesmanName?item.salesmanName:item.salesmanId"
title="销售人员:"
>

</uni-list-item>
<uni-list-item
:rightText="item.customerName?item.customerName:item.customerId"
title="拜访客户:"
>

</uni-list-item>
<uni-list-item
:rightText="item.visitCustomerName?item.visitCustomerName:item.visitId"
title="所属拜访:"
>

</uni-list-item>
<uni-list-item
:rightText="item.customerStageName?item.customerStageName:item.customerStage"
title="拜访客户阶段:"
>

</uni-list-item>
<uni-list-item
:rightText="item.signingAmount?item.signingAmount:item.signingAmount"
title="预计签单金额:"
>

</uni-list-item>
<uni-list-item
:rightText="item.signingProbability?item.signingProbability:item.signingProbability"
title="签单概率:"
>

</uni-list-item>
</template>
</base-list>
<base-dialog
ref="show_dialog"
type="bottom"
>
<template #dialog>
<base-form
@success="success_8976"
:id="allParams.null"
table_module="business_opportunity"
random_int="5066"
model="add"
:rules="{id:{
rules:[
    {required: true,
    errorMessage: '请输入主键'}],

},opportunityStatus:{
rules:[
    {required: true,
    errorMessage: '请输入商机状态'}],

},salesmanId:{
rules:[
    {required: true,
    errorMessage: '请输入销售人员'}],

},customerId:{
rules:[
    {required: true,
    errorMessage: '请输入拜访客户'}],

},visitId:{
rules:[
    {required: true,
    errorMessage: '请输入所属拜访'}],

},customerStage:{
rules:[
    {required: true,
    errorMessage: '请输入拜访客户阶段'}],

},signingAmount:{
rules:[
    {required: true,
    errorMessage: '请输入预计签单金额'}],

},signingProbability:{
rules:[
    {required: true,
    errorMessage: '请输入签单概率'}],

},}"
@fail="fail_8976"
primary_key="id"
:form="form_5066"
>
<template #form-items>
<uni-forms-item v-show="!allParams.opportunityStatus" label="商机状态" name="opportunityStatus" required>
    <base-select  v-model="form_5066.opportunityStatus" api="business_opportunity_all.get_opportunity_status_list" title="商机状态"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.salesmanId" label="销售人员" name="salesmanId" required>
    <base-select  v-model="form_5066.salesmanId" api="business_opportunity_all.get_salesman_list" title="销售人员"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.customerId" label="拜访客户" name="customerId" required>
    <base-select  v-model="form_5066.customerId" api="business_opportunity_all.get_customer_list" title="拜访客户"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.visitId" label="所属拜访" name="visitId" required>
    <base-select  v-model="form_5066.visitId" api="business_opportunity_all.get_visit_management_list" title="所属拜访"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.customerStage" label="拜访客户阶段" name="customerStage" required>
    <base-select  v-model="form_5066.customerStage" api="business_opportunity_all.get_customer_stage_list" title="拜访客户阶段"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.signingAmount" label="预计签单金额" name="signingAmount" required>
    <uni-easyinput   placeholder="请输入预计签单金额" v-model="form_5066.signingAmount" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.signingProbability" label="签单概率" name="signingProbability" required>
    <uni-easyinput   placeholder="请输入签单概率" v-model="form_5066.signingProbability" />
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
























const form_5066 = ref({...allParams.value});



const click_2262=(
)=>{
  proxy.$refs.show_dialog.showDialog()

}
const click_3168=(
item
)=>{
  proxy.$navigate( "/pages/business_opportunity_detail/index?opportunityId=" + item.id,false)

}
const success_8976=(
)=>{
  proxy.$refs.business_opportunity_show_list.refresh()
  proxy.$refs.show_dialog.closeDialog()

}
const fail_8976=(
)=>{
  uni.showToast({
  title: '提交失败,请重试',
  icon: 'error',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>