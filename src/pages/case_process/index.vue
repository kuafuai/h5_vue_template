<template>
  <base-wrapper>
    <base-wrapper
>
<base-form
@success="success_6457"
:params="{'caseId':allParams.caseId,
}"
:id="allParams.null"
table_module="case_process"
random_int="6559"
model="update"
:rules="{processId:{
rules:[
    {required: true,
    errorMessage: '请输入办理步骤编号'}],

},caseId:{
rules:[
    {required: false,
    errorMessage: '请输入关联案件ID'}],

},processStep:{
rules:[
    {required: false,
    errorMessage: '请输入办理步骤'}],

},processStatus:{
rules:[
    {required: false,
    errorMessage: '请输入办理状态'}],

},assignedTo:{
rules:[
    {required: false,
    errorMessage: '请输入负责人'}],

},createdAt:{
rules:[
    {required: false,
    errorMessage: '请输入创建时间'}],

},updatedAt:{
rules:[
    {required: false,
    errorMessage: '请输入更新时间'}],

},}"
@fail="fail_6457"
primary_key="processId"
:form="form_6559"
>
<template #form-items>
<uni-forms-item v-show="!allParams.caseId" label="关联案件ID" name="caseId" >
    <base-select  v-model="form_6559.caseId" api="case_process_all.get_case_info_list" title="关联案件ID"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.processStep" label="办理步骤" name="processStep" >
    <uni-easyinput   placeholder="请输入办理步骤" v-model="form_6559.processStep" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.processStatus" label="办理状态" name="processStatus" >
    <uni-easyinput   placeholder="请输入办理状态" v-model="form_6559.processStatus" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.assignedTo" label="负责人" name="assignedTo" >
    <uni-easyinput   placeholder="请输入负责人" v-model="form_6559.assignedTo" />
</uni-forms-item>


<uni-forms-item  v-show="!allParams.createdAt"  label="创建时间" name="createdAt" >
    <uni-datetime-picker   placeholder="请输入创建时间" v-model="form_6559.createdAt" type="datetime"
                         :format="'yyyy-MM-dd HH:mm:ss'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>

<uni-forms-item  v-show="!allParams.updatedAt"  label="更新时间" name="updatedAt" >
    <uni-datetime-picker   placeholder="请输入更新时间" v-model="form_6559.updatedAt" type="datetime"
                         :format="'yyyy-MM-dd HH:mm:ss'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
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
















const form_6559 = ref({...allParams.value});


const success_6457=(
item
)=>{
  uni.showToast({
  title: '案件办理状态更新成功',
  icon: 'none',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>