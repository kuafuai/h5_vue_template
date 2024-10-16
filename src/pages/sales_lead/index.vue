<template>
  <base-wrapper>
    <base-wrapper
>
<base-button
        ref="button_ref"
    @click="click_5066"
    model="float"
    type="primary"
    title="添加客户线索"
>

</base-button>
<base-dialog
ref="show_dialog"
text="添加客户线索"
type="bottom"
>
<template #dialog>
<base-wrapper
>
<base-form
@success="success_3300"
:id="allParams.null"
table_module="sales_lead"
random_int="5188"
model="add"
:rules="{salesLeadId:{
rules:[
    {required: true,
    errorMessage: '请输入主键'}],

},salesName:{
rules:[
    {required: true,
    errorMessage: '请输入客户线索名称'}],

},userId:{
rules:[
    {required: false,
    errorMessage: '请输入跟进人'}],

},salesDescription:{
rules:[
    {required: false,
    errorMessage: '请输入客户线索的描述'}],

},salesStatus:{
rules:[
    {required: false,
    errorMessage: '请输入客户线索状态'}],

},partnerId:{
rules:[
    {required: false,
    errorMessage: '请输入合作伙伴'}],

},}"
@fail="fail_3300"
primary_key="salesLeadId"
:form="form_5188"
>
<template #form-items>
<uni-forms-item v-show="!allParams.salesName" label="客户线索名称" name="salesName" required>
    <uni-easyinput   placeholder="请输入客户线索名称" v-model="form_5188.salesName" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.userId" label="跟进人" name="userId" >
    <base-select  v-model="form_5188.userId" api="sales_lead_all.get_user_info_list" title="跟进人"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.salesDescription" label="客户线索的描述" name="salesDescription" >
    <uni-easyinput   placeholder="请输入客户线索的描述" v-model="form_5188.salesDescription" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.salesStatus" label="客户线索状态" name="salesStatus" >
    <base-select  v-model="form_5188.salesStatus" api="sales_lead_all.get_sales_status_list" title="客户线索状态"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.partnerId" label="合作伙伴" name="partnerId" >
    <base-select  v-model="form_5188.partnerId" api="sales_lead_all.get_partner_list" title="合作伙伴"></base-select>
</uni-forms-item>

</template>
</base-form>
</base-wrapper>
</template>
</base-dialog>
<base-list-header
nickname="客户线索"
description="展示客户线索"
>

</base-list-header>
<base-list
ref="sales_lead_show_list"
:isPage="true"
@click="click_2789"
:params="{'partnerId':allParams.currentUser.userId,
}"
is_click="'true'"
api="sales_lead_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.salesName?item.salesName:item.salesName"
title="客户线索名称"
>

</uni-list-item>
<uni-list-item
:rightText="item.salesStatusName?item.salesStatusName:item.salesStatus"
title="客户线索状态"
>

</uni-list-item>
<uni-list-item
:rightText="item.partnerName?item.partnerName:item.partnerId"
title="合作伙伴"
>

</uni-list-item>
<uni-list-item
:rightText="item.salesLeadItemEqCount!=null?item.salesLeadItemEqCount+'':null"
title="客户项目数量"
>

</uni-list-item>
</template>
</base-list>
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















const form_5188 = ref({...allParams.value});








const click_5066=(
)=>{
  proxy.$refs.show_dialog.showDialog()

}
const click_2789=(
item
)=>{
  proxy.$navigate( "/pages/sales_lead_detail/index?salesLeadId=" + item.salesLeadId,false)

}
const success_3300=(
item
)=>{
  proxy.$refs.sales_lead_show_list.refresh()
  proxy.$refs.show_dialog.closeDialog()

}
const fail_3300=(
)=>{
  uni.showToast({
  title: '提交失败,请重试',
  icon: 'error',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>