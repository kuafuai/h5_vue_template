<template>
  <base-wrapper>
    <base-wrapper
>
<base-form
@success="success_4664"
:params="{'userId':allParams.currentUser.userId,
}"
:id="allParams.null"
table_module="case_info"
random_int="8852"
model="add"
:rules="{caseId:{
rules:[
    {required: true,
    errorMessage: '请输入案件ID'}],

},caseNumber:{
rules:[
    {required: true,
    errorMessage: '请输入案件编号'}],

},caseTitle:{
rules:[
    {required: true,
    errorMessage: '请输入案件标题'}],

},caseDescription:{
rules:[
    {required: false,
    errorMessage: '请输入案件描述'}],

},caseStatus:{
rules:[
    {required: true,
    errorMessage: '请输入案件状态'}],

},userId:{
rules:[
    {required: true,
    errorMessage: '请输入关联用户ID'}],

},createdAt:{
rules:[
    {required: true,
    errorMessage: '请输入创建时间'}],

},updatedAt:{
rules:[
    {required: true,
    errorMessage: '请输入更新时间'}],

},}"
@fail="fail_4664"
primary_key="caseId"
:form="form_8852"
>
<template #form-items>
<uni-forms-item v-show="!allParams.caseNumber" label="案件编号" name="caseNumber" required>
    <uni-easyinput   placeholder="请输入案件编号" v-model="form_8852.caseNumber" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.caseTitle" label="案件标题" name="caseTitle" required>
    <uni-easyinput   placeholder="请输入案件标题" v-model="form_8852.caseTitle" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.caseDescription" label="案件描述" name="caseDescription" >
    <base-textarea   placeholder="请输入案件描述" v-model="form_8852.caseDescription" class="custom-textarea"/>
</uni-forms-item>


<uni-forms-item v-show="!allParams.caseStatus" label="案件状态" name="caseStatus" required>
    <uni-easyinput   placeholder="请输入案件状态" v-model="form_8852.caseStatus" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.userId" label="关联用户ID" name="userId" required>
    <base-select  v-model="form_8852.userId" api="case_info_all.get_user_info_list" title="关联用户ID"></base-select>
</uni-forms-item>

<uni-forms-item  v-show="!allParams.createdAt"  label="创建时间" name="createdAt" required>
    <uni-datetime-picker   placeholder="请输入创建时间" v-model="form_8852.createdAt" type="datetime"
                         :format="'yyyy-MM-dd HH:mm:ss'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>

<uni-forms-item  v-show="!allParams.updatedAt"  label="更新时间" name="updatedAt" required>
    <uni-datetime-picker   placeholder="请输入更新时间" v-model="form_8852.updatedAt" type="datetime"
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


















const form_8852 = ref({...allParams.value});


const success_4664=(
item
)=>{
  uni.showToast({
  title: '案件录入成功',
  icon: 'none',
  duration: 2000
});

}
const fail_4664=(
)=>{
  uni.showToast({
  title: '案件录入失败，请重试。',
  icon: 'none',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>