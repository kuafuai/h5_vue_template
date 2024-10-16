<template>
  <base-wrapper>
    <base-tab
:items="['合作伙伴', '提现流水记录']"
>
<template #content-0>
<base-list-header
nickname="合作伙伴信息"
description="展示客户线索"
>

</base-list-header>
<base-button
        ref="button_ref"
    @click="click_3587"
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
@success="success_3528"
:params="{'partnerId':allParams.currentUser.userId,
}"
:id="allParams.null"
table_module="partner"
random_int="8724"
model="update"
:rules="{partnerId:{
rules:[
    {required: true,
    errorMessage: '请输入主键'}],

},partnerWechatLoginId:{
rules:[
    {required: false,
    errorMessage: '请输入微信绑定ID'}],

},partnerPhoneNumber:{
rules:[
    {required: true,
    errorMessage: '请输入手机号'}],

},partnerName:{
rules:[
    {required: true,
    errorMessage: '请输入姓名'}],

},idCard:{
rules:[
    {required: false,
    errorMessage: '请输入身份证号'}],

},}"
@fail="fail_3528"
primary_key="partnerId"
:form="form_8724"
>
<template #form-items>
<uni-forms-item v-show="!allParams.partnerWechatLoginId" label="微信绑定ID" name="partnerWechatLoginId" >
    <uni-easyinput   placeholder="请输入微信绑定ID" v-model="form_8724.partnerWechatLoginId" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.partnerPhoneNumber" label="手机号" name="partnerPhoneNumber" required>
    <uni-easyinput   placeholder="请输入手机号" v-model="form_8724.partnerPhoneNumber" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.partnerName" label="姓名" name="partnerName" required>
    <uni-easyinput   placeholder="请输入姓名" v-model="form_8724.partnerName" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.idCard" label="身份证号" name="idCard" >
    <uni-easyinput   placeholder="请输入身份证号" v-model="form_8724.idCard" />
</uni-forms-item>


</template>
</base-form>
</template>
</base-dialog>
<base-description
ref="user_center_base_description_ref"
:params="{'partnerId':allParams.currentUser.userId,
}"
api="partner.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.partnerId"
type="base"
title="主键"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.partnerWechatLoginId"
type="base"
title="微信绑定ID"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.partnerPhoneNumber"
type="base"
title="手机号"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.partnerName"
type="base"
title="姓名"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.idCard"
type="base"
title="身份证号"
class="mb-10"
>
</uni-section>


</template>
</base-description>
</template>
<template #content-1>
<base-list-header
nickname="提现金额表"
description="展示提现记录信息"
>

</base-list-header>
<base-button
        ref="button_ref_1"
    @click="click_7943"
    model="float"
    type="primary"
    title="提现申请"
>

</base-button>
<base-dialog
ref="show_dialog_withdrawal_transactions"
text="提交提现申请"
type="bottom"
>
<template #dialog>
<base-wrapper
>
<base-form
@success="success_7069"
:id="allParams.null"
table_module="withdrawal_transactions"
random_int="5742"
model="add"
:rules="{withdrawalTransactionsId:{
rules:[
    {required: true,
    errorMessage: '请输入主键'}],

},partnerId:{
rules:[
    {required: true,
    errorMessage: '请输入合作伙伴'}],

},salesLeadItemId:{
rules:[
    {required: true,
    errorMessage: '请输入客户项目'}],

},withdrawalAmount:{
rules:[
    {required: true,
    errorMessage: '请输入提现金额'}],

},status:{
rules:[
    {required: true,
    errorMessage: '请输入提现状态'}],

},endStatus:{
rules:[
    {required: true,
    errorMessage: '请输入结束状态(未处理/提现成功)'}],

},}"
@fail="fail_7069"
primary_key="withdrawalTransactionsId"
:form="form_5742"
>
<template #form-items>
<uni-forms-item v-show="!allParams.partnerId" label="合作伙伴" name="partnerId" required>
    <base-select  v-model="form_5742.partnerId" api="withdrawal_transactions_all.get_partner_list" title="合作伙伴"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.salesLeadItemId" label="客户项目" name="salesLeadItemId" required>
    <base-select  v-model="form_5742.salesLeadItemId" api="withdrawal_transactions_all.get_sales_lead_item_list" title="客户项目"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.withdrawalAmount" label="提现金额" name="withdrawalAmount" required>
    <uni-easyinput   placeholder="请输入提现金额" v-model="form_5742.withdrawalAmount" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.status" label="提现状态" name="status" required>
    <base-select  v-model="form_5742.status" api="withdrawal_transactions_all.get_amount_status_list" title="提现状态"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.endStatus" label="结束状态(未处理/提现成功)" name="endStatus" required>
    <base-select  v-model="form_5742.endStatus" api="withdrawal_transactions_all.get_end_amount_status_list" title="结束状态(未处理/提现成功)"></base-select>
</uni-forms-item>

</template>
</base-form>
</base-wrapper>
</template>
</base-dialog>
<base-layout
>
<base-statistics
group_result_show_display="text"
ref="withdrawal_transactions_statistics_3"
:params="{'partnerId':allParams.currentUser.userId,
'endStatus':'1',
}"
model="withdrawal_transactions_statistics_3_sum"
api="withdrawal_transactions_statistic"
title="共计佣金"
>

</base-statistics>
<base-statistics
group_result_show_display="text"
ref="withdrawal_transactions_statistics_1"
:params="{'partnerId':allParams.currentUser.userId,
'status':'1',
}"
model="withdrawal_transactions_statistics_1_sum"
api="withdrawal_transactions_statistic"
title="申请中的提现金额"
>

</base-statistics>
<base-statistics
group_result_show_display="text"
ref="withdrawal_transactions_statistics_2"
:params="{'partnerId':allParams.currentUser.userId,
'status':'4',
}"
model="withdrawal_transactions_statistics_2_sum"
api="withdrawal_transactions_statistic"
title="提现金额"
>

</base-statistics>
</base-layout>
<base-list
ref="withdrawal_transactions_show_list"
:isPage="true"
@click="click_8863"
:params="{'partnerId':allParams.currentUser.userId,
}"
api="withdrawal_transactions_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.partnerName?item.partnerName:item.partnerId"
title="合作伙伴"
>

</uni-list-item>
<uni-list-item
:rightText="item.salesItemName?item.salesItemName:item.salesLeadItemId"
title="客户项目"
>

</uni-list-item>
<uni-list-item
:rightText="item.withdrawalAmount?item.withdrawalAmount:item.withdrawalAmount"
title="提现金额"
>

</uni-list-item>
<uni-list-item
:rightText="item.amountStatusName?item.amountStatusName:item.status"
title="提现状态"
>

</uni-list-item>
<uni-list-item
:rightText="item.endAmountStatusName?item.endAmountStatusName:item.endStatus"
title="结束状态(未处理/提现成功)"
>

</uni-list-item>
</template>
</base-list>
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
















const form_8724 = ref({...allParams.value});





















const form_5742 = ref({...allParams.value});








const click_7943=(
)=>{
  proxy.$refs.show_dialog_withdrawal_transactions.showDialog()

}
const success_7069=(
item
)=>{
  proxy.$refs.show_dialog_withdrawal_transactions.closeDialog()
  proxy.$refs.withdrawal_transactions_show_list.refresh()

}
const click_3587=(
)=>{
  proxy.$refs.show_dialog_ref1212.showDialog()

}
const success_3528=(
item
)=>{
  proxy.$refs.user_center_base_description_ref.refresh()
  proxy.$refs.show_dialog_ref1212.closeDialog()

}

</script>

<style lang="scss" scoped></style>