<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="客户拜访"
description="展示拜访"
>

</base-list-header>
<base-layout
>
<base-statistics
group_result_show_display="text"
ref="visit_management_statistics_1"
:params="{'salesmanId':allParams.currentUser.userId,
}"
model="visit_management_statistics_1_count"
api="visit_management_statistic"
title="全部线索"
>

</base-statistics>
<base-statistics
group_result_show_display="text"
ref="visit_management_statistics_2"
:params="{'leadStatus':'1',
'salesmanId':allParams.currentUser.userId,
}"
model="visit_management_statistics_2_count"
api="visit_management_statistic"
title="待处理线索"
>

</base-statistics>
<base-statistics
group_result_show_display="text"
ref="visit_management_statistics_3"
:params="{'leadStatus':'3',
'salesmanId':allParams.currentUser.userId,
}"
model="visit_management_statistics_3_count"
api="visit_management_statistic"
title="已转商机"
>

</base-statistics>
<base-statistics
group_result_show_display="text"
ref="visit_management_statistics_4"
:params="{'leadStatus':'4',
'salesmanId':allParams.currentUser.userId,
}"
model="visit_management_statistics_4_count"
api="visit_management_statistic"
title="无效线索"
>

</base-statistics>
</base-layout>
<base-button
        ref="button_ref"
    @click="click_9892"
    model="float"
    type="primary"
    title="添加拜访"
>

</base-button>
<base-list
ref="visit_management_show_list"
:isPage="true"
@click="click_7402"
:params="{'salesmanId':allParams.currentUser.userId,
}"
is_click="'true'"
api="visit_management_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.leadStatusName?item.leadStatusName:item.leadStatus"
title="销售线索状态:"
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
:rightText="item.visitDate?item.visitDate:item.visitDate"
title="拜访日期:"
>

</uni-list-item>
<uni-list-item
:rightText="item.visitCustomerName?item.visitCustomerName:item.visitCustomerName"
title="拜访客户姓名:"
>

</uni-list-item>
<uni-list-item
:rightText="item.visitCustomerPost?item.visitCustomerPost:item.visitCustomerPost"
title="拜访客户职位:"
>

</uni-list-item>
<uni-list-item
:rightText="item.visitCustomerPhone?item.visitCustomerPhone:item.visitCustomerPhone"
title="拜访客户手机号:"
>

</uni-list-item>
<uni-list-item
:rightText="item.customerStageName?item.customerStageName:item.visitCustomerStage"
title="拜访客户阶段:"
>

</uni-list-item>
<uni-list-item
:rightText="item.visitCustomerInformation?item.visitCustomerInformation:item.visitCustomerInformation"
title="拜访客户记录:"
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
@success="success_6175"
:id="allParams.null"
table_module="visit_management"
random_int="7506"
model="add"
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
@fail="fail_6175"
primary_key="id"
:form="form_7506"
>
<template #form-items>
<uni-forms-item v-show="!allParams.leadStatus" label="销售线索状态" name="leadStatus" required>
    <base-select  v-model="form_7506.leadStatus" api="visit_management_all.get_lead_status_list" title="销售线索状态"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.salesmanId" label="销售人员" name="salesmanId" required>
    <base-select  v-model="form_7506.salesmanId" api="visit_management_all.get_salesman_list" title="销售人员"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.customerId" label="拜访客户" name="customerId" required>
    <base-select  v-model="form_7506.customerId" api="visit_management_all.get_customer_list" title="拜访客户"></base-select>
</uni-forms-item>

<uni-forms-item  v-show="!allParams.visitDate"  label="拜访日期" name="visitDate" required>
    <uni-datetime-picker   placeholder="请输入拜访日期" v-model="form_7506.visitDate" type="datetime"
                         :format="'yyyy-MM-dd HH:mm:ss'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>

<uni-forms-item v-show="!allParams.visitCustomerName" label="拜访客户姓名" name="visitCustomerName" required>
    <uni-easyinput   placeholder="请输入拜访客户姓名" v-model="form_7506.visitCustomerName" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.visitCustomerPost" label="拜访客户职位" name="visitCustomerPost" required>
    <uni-easyinput   placeholder="请输入拜访客户职位" v-model="form_7506.visitCustomerPost" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.visitCustomerPhone" label="拜访客户手机号" name="visitCustomerPhone" required>
    <uni-easyinput   placeholder="请输入拜访客户手机号" v-model="form_7506.visitCustomerPhone" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.visitCustomerStage" label="拜访客户阶段" name="visitCustomerStage" required>
    <base-select  v-model="form_7506.visitCustomerStage" api="visit_management_all.get_customer_stage_list" title="拜访客户阶段"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.visitCustomerInformation" label="拜访客户记录" name="visitCustomerInformation" required>
    <uni-easyinput   placeholder="请输入拜访客户记录" v-model="form_7506.visitCustomerInformation" />
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




























const form_7506 = ref({...allParams.value});



const click_9892=(
)=>{
  proxy.$refs.show_dialog.showDialog()

}
const click_7402=(
item
)=>{
  proxy.$navigate( "/pages/visit_management_detail/index?visitId=" + item.id,false)

}
const success_6175=(
)=>{
  proxy.$refs.visit_management_show_list.refresh()
  proxy.$refs.show_dialog.closeDialog()

}
const fail_6175=(
)=>{
  uni.showToast({
  title: '提交失败,请重试',
  icon: 'error',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>