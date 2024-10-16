<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="客户表"
description="展示客户"
>

</base-list-header>
<base-layout
>
<base-statistics
group_result_show_display="text"
ref="customer_base_statistics_1"
model="customer_base_statistics_1_count"
api="customer_statistic"
title="全部客户"
>

</base-statistics>
<base-statistics
group_result_show_display="text"
ref="customer_statistics_2"
:params="{'customerHierarchy':'1',
}"
model="customer_statistics_2_count"
api="customer_statistic"
title="战略客户"
>

</base-statistics>
<base-statistics
group_result_show_display="text"
ref="customer_statistics_3"
:params="{'customerHierarchy':'2',
}"
model="customer_statistics_3_count"
api="customer_statistic"
title="重要客户"
>

</base-statistics>
<base-statistics
group_result_show_display="text"
ref="customer_statistics_4"
:params="{'customerHierarchy':'3',
}"
model="customer_statistics_4_count"
api="customer_statistic"
title="一般客户"
>

</base-statistics>
</base-layout>
<base-button
        ref="button_ref"
    @click="click_2745"
    model="float"
    type="primary"
    title="添加客户"
>

</base-button>
<base-list
ref="customer_show_list"
:isPage="true"
@click="click_9095"
is_click="'true'"
api="customer_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.customerName?item.customerName:item.customerName"
title="客户名称:"
>

</uni-list-item>
<uni-list-item
:rightText="item.industryName?item.industryName:item.customerIndustry"
title="客户行业:"
>

</uni-list-item>
<uni-list-item
:rightText="item.personnelScaleName?item.personnelScaleName:item.customerPersonnelScale"
title="客户人员规模:"
>

</uni-list-item>
<uni-list-item
:rightText="item.customerHierarchyName?item.customerHierarchyName:item.customerHierarchy"
title="客户分层:"
>

</uni-list-item>
<uni-list-item
:rightText="item.customerAddress?item.customerAddress:item.customerAddress"
title="客户地址:"
>

</uni-list-item>
<uni-list-item
:rightText="item.customerInformation?item.customerInformation:item.customerInformation"
title="客户介绍:"
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
@success="success_2020"
:id="allParams.null"
table_module="customer"
random_int="2370"
model="add"
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
@fail="fail_2020"
primary_key="id"
:form="form_2370"
>
<template #form-items>
<uni-forms-item v-show="!allParams.customerName" label="客户名称" name="customerName" required>
    <uni-easyinput   placeholder="请输入客户名称" v-model="form_2370.customerName" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.customerIndustry" label="客户行业" name="customerIndustry" required>
    <base-select  v-model="form_2370.customerIndustry" api="customer_all.get_industry_list" title="客户行业"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.customerPersonnelScale" label="客户人员规模" name="customerPersonnelScale" required>
    <base-select  v-model="form_2370.customerPersonnelScale" api="customer_all.get_personnel_scale_list" title="客户人员规模"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.customerHierarchy" label="客户分层" name="customerHierarchy" required>
    <base-select  v-model="form_2370.customerHierarchy" api="customer_all.get_customer_hierarchy_list" title="客户分层"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.customerAddress" label="客户地址" name="customerAddress" required>
    <uni-easyinput   placeholder="请输入客户地址" v-model="form_2370.customerAddress" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.customerInformation" label="客户介绍" name="customerInformation" required>
    <uni-easyinput   placeholder="请输入客户介绍" v-model="form_2370.customerInformation" />
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






















const form_2370 = ref({...allParams.value});



const click_2745=(
)=>{
  proxy.$refs.show_dialog.showDialog()

}
const click_9095=(
item
)=>{
  proxy.$navigate( "/pages/customer_detail/index?customerId=" + item.id,false)

}
const success_2020=(
)=>{
  proxy.$refs.customer_show_list.refresh()
  proxy.$refs.show_dialog.closeDialog()

}
const fail_2020=(
)=>{
  uni.showToast({
  title: '提交失败,请重试',
  icon: 'error',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>