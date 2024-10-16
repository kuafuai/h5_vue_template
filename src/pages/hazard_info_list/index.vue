<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="隐患信息管理"
description="隐患信息列表"
>

</base-list-header>
<base-search
@search="search_3990"
ref="hazard_info_base_search_ref"
firstSearchDataComment="隐患描述"
:searchData="{acceptanceStatus:null,correlationBasisStatus:null,}"
class="m-b-10"
firstSearchData="hazardDescription"
>
<template #collapse="{item}">
<uni-forms-item  label="验收状态" name="acceptanceStatus" >
    <base-select  v-model="item.acceptanceStatus" api="hazard_info_all.get_hazard_acceptance_status_list" title="验收状态"></base-select>
</uni-forms-item>

<uni-forms-item  label="隐患是否关联依据" name="correlationBasisStatus" >
    <base-select  v-model="item.correlationBasisStatus" api="hazard_info_all.get_correlation_basis_status_list" title="隐患是否关联依据"></base-select>
</uni-forms-item>

</template>
</base-search>
<base-list
ref="hazard_info_show_list"
:isPage="true"
@click="click_5791"
:params="{'taskInfoId':allParams.taskInfoId,
}"
:is_click="true"
api="hazard_info_all.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.hazardLocation!=null && item.hazardLocation!=undefined ?item.hazardLocation+'':item.hazardLocation"
title="隐患区域:"
>

</uni-list-item>
<uni-list-item
:rightText="item.hazardDescription!=null && item.hazardDescription!=undefined ?item.hazardDescription+'':item.hazardDescription"
title="隐患描述:"
>

</uni-list-item>
<uni-list-item
:rightText="item.startTime!=null && item.startTime!=undefined ?item.startTime+'':item.startTime"
title="开始时间:"
>

</uni-list-item>
<uni-list-item
:rightText="item.endTime!=null && item.endTime!=undefined ?item.endTime+'':item.endTime"
title="结束时间:"
>

</uni-list-item>
<uni-list-item
:rightText="item.hazardAcceptanceStatusDescription!=null && item.hazardAcceptanceStatusDescription!=undefined ?item.hazardAcceptanceStatusDescription+'':item.acceptanceStatus"
title="验收状态:"
>

</uni-list-item>
<uni-list-item
:rightText="item.taskName!=null && item.taskName!=undefined ?item.taskName+'':item.taskInfoId"
title="所属任务:"
>

</uni-list-item>
</template>
</base-list>
</base-wrapper>
<base-button
        ref="add_hazard_info_button_ref"
    @click="click_8050"
    model="float"
    type="primary"
    title="添加隐患信息"
>

</base-button>
<base-dialog
ref="hazard_info_show_dialog"
comp_type="base"
text="添加隐患信息"
type="bottom"
content="您确定要提交吗？"
>
<template #dialog>
<base-form
@success="success_3253"
:id="allParams.null"
table_module="hazard_info"
random_int="3752"
model="add"
:rules="{hazardInfoId:{
rules:[
    {required: true,
    errorMessage: '请输入编号'}],

},hazardLocation:{
rules:[
    {required: true,
    errorMessage: '请输入隐患区域'}],

},hazardDescription:{
rules:[
    {required: true,
    errorMessage: '请输入隐患描述'}],

},hazardImage:{
rules:[
    {required: false,
    errorMessage: '请输入隐患图片'}],

},hazardBasis:{
rules:[
    {required: false,
    errorMessage: '请输入隐患依据'}],

},rectificationSuggestions:{
rules:[
    {required: true,
    errorMessage: '请输入整改建议'}],

},startTime:{
rules:[
    {required: true,
    errorMessage: '请输入开始时间'}],

},endTime:{
rules:[
    {required: true,
    errorMessage: '请输入结束时间'}],

},acceptanceStatus:{
rules:[
    {required: false,
    errorMessage: '请输入验收状态'}],

},correlationBasisStatus:{
rules:[
    {required: true,
    errorMessage: '请输入隐患是否关联依据'}],

},taskInfoId:{
rules:[
    {required: false,
    errorMessage: '请输入所属任务'}],

},investigatedEnterprise:{
rules:[
    {required: false,
    errorMessage: '请输入被检查企业'}],

},tenantId:{
rules:[
    {required: false,
    errorMessage: '请输入租户ID'}],

},createTime:{
rules:[
    {required: false,
    errorMessage: '请输入创建时间'}],

},updateTime:{
rules:[
    {required: false,
    errorMessage: '请输入截止时间'}],

},}"
@fail="fail_3253"
primary_key="hazardInfoId"
:form="form_3752"
query_module="hazard_info_all"
>
<template #form-items>

<uni-forms-item v-show="!allParams.hazardLocation" label="隐患区域" name="hazardLocation" required>
    <uni-easyinput   placeholder="请输入隐患区域" v-model="form_3752.hazardLocation" />
</uni-forms-item>



<uni-forms-item v-show="!allParams.hazardDescription" label="隐患描述" name="hazardDescription" required>
    <uni-easyinput   placeholder="请输入隐患描述" v-model="form_3752.hazardDescription" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.hazardImage"   label="隐患图片" name="hazardImage" >
    <base-image-upload v-model="form_3752.hazardImage"  limit="3"/>
</uni-forms-item>

<uni-forms-item v-show="!allParams.hazardBasis" label="隐患依据" name="hazardBasis" >
    <base-belong  v-model="form_3752.hazardBasis" v-model:form="form_3752" :map="{'rectificationSuggestions':'rectificationSuggestions','hazardDescription':'hazardDescription','hazardBasis':'hazardBasicId'}"
title="隐患依据" show_name="hazardBasicDescription"
value_name="hazardBasicId" page_name="hazard_basic_list"> </base-belong>
</uni-forms-item>



<uni-forms-item v-show="!allParams.rectificationSuggestions" label="整改建议" name="rectificationSuggestions" required>
    <uni-easyinput   placeholder="请输入整改建议" v-model="form_3752.rectificationSuggestions" />
</uni-forms-item>


<uni-forms-item  v-show="!allParams.startTime" label="开始时间" name="startTime" required>
    <uni-datetime-picker    placeholder="请输入开始时间" v-model="form_3752.startTime" type="date"
                         :format="'yyyy-MM-dd'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>

<uni-forms-item  v-show="!allParams.endTime" label="结束时间" name="endTime" required>
    <uni-datetime-picker    placeholder="请输入结束时间" v-model="form_3752.endTime" type="date"
                         :format="'yyyy-MM-dd'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>

<uni-forms-item v-show="!allParams.acceptanceStatus" label="验收状态" name="acceptanceStatus" >
    <base-select  v-model="form_3752.acceptanceStatus" api="hazard_info_all.get_hazard_acceptance_status_list" title="验收状态"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.correlationBasisStatus" label="隐患是否关联依据" name="correlationBasisStatus" required>
    <base-select  v-model="form_3752.correlationBasisStatus" api="hazard_info_all.get_correlation_basis_status_list" title="隐患是否关联依据"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.taskInfoId" label="所属任务" name="taskInfoId" >
    <base-select  v-model="form_3752.taskInfoId" api="hazard_info_all.get_task_info_list" title="所属任务"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.investigatedEnterprise" label="被检查企业" name="investigatedEnterprise" >
    <base-select  v-model="form_3752.investigatedEnterprise" api="hazard_info_all.get_enterprise_information_list" title="被检查企业"></base-select>
</uni-forms-item>

</template>
</base-form>
</template>
</base-dialog>
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






































const form_3752 = ref({...allParams.value});



const search_3990=(
item
)=>{
  proxy.$refs.hazard_info_show_list.refresh(item)

}
const click_5791=(
item
)=>{
  proxy.$navigate( "/pages/hazard_info_detail/index?hazardInfoId=" + item.hazardInfoId,false)

}
const click_8050=(
)=>{
  proxy.$refs.hazard_info_show_dialog.showDialog()

}
const success_3253=(
)=>{
  proxy.$refs.hazard_info_show_dialog.closeDialog()
  proxy.$refs.hazard_info_show_list.refresh()
  uni.showToast({
  title: '添加成功',
  icon: 'success',
  duration: 2000
});

}
const fail_3253=(
)=>{
  uni.showToast({
  title: '提交失败,请重试',
  icon: 'error',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>