<template>
  <base-wrapper>
    <base-wrapper
>
<base-form
@success="success_4248"
:params="{'bugInfoId':allParams.bugInfoId,
}"
:id="allParams.null"
table_module="bug_info"
random_int="7084"
model="update"
:rules="{bugInfoId:{
rules:[
    {required: true,
    errorMessage: '请输入编号'}],

},bugDiscoverer:{
rules:[
    {required: false,
    errorMessage: '请输入BUG发现人'}],

},bugSubmitter:{
rules:[
    {required: false,
    errorMessage: '请输入BUG提交人'}],

},bugHandler:{
rules:[
    {required: false,
    errorMessage: '请输入BUG处理人'}],

},bugTitle:{
rules:[
    {required: true,
    errorMessage: '请输入标题'}],

},bugDescription:{
rules:[
    {required: false,
    errorMessage: '请输入描述'}],

},bugImage:{
rules:[
    {required: false,
    errorMessage: '请输入问题图片'}],

},projectInfoId:{
rules:[
    {required: false,
    errorMessage: '请输入项目名称'}],

},bugDeadline:{
rules:[
    {required: false,
    errorMessage: '请输入截止时间'}],

},bugPriority:{
rules:[
    {required: true,
    errorMessage: '请输入优先级'}],

},bugHandleStatus:{
rules:[
    {required: true,
    errorMessage: '请输入审核状态'}],

},createdTime:{
rules:[
    {required: false,
    errorMessage: '请输入创建时间'}],

},}"
@fail="fail_4248"
primary_key="bugInfoId"
:form="form_7084"
>
<template #form-items>
<uni-forms-item v-show="!allParams.bugDiscoverer" label="BUG发现人" name="bugDiscoverer" >
    <base-select  v-model="form_7084.bugDiscoverer" api="bug_info_all.get_user_info_list" title="BUG发现人"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.bugSubmitter" label="BUG提交人" name="bugSubmitter" >
    <base-select  v-model="form_7084.bugSubmitter" api="bug_info_all.get_user_info_list" title="BUG提交人"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.bugHandler" label="BUG处理人" name="bugHandler" >
    <base-select  v-model="form_7084.bugHandler" api="bug_info_all.get_user_info_list" title="BUG处理人"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.bugTitle" label="标题" name="bugTitle" required>
    <uni-easyinput   placeholder="请输入标题" v-model="form_7084.bugTitle" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.bugDescription" label="描述" name="bugDescription" >
    <uni-easyinput   placeholder="请输入描述" v-model="form_7084.bugDescription" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.bugImage"  label="问题图片" name="bugImage" >
    <base-image-upload v-model="form_7084.bugImage"  />
</uni-forms-item>

<uni-forms-item v-show="!allParams.projectInfoId" label="项目名称" name="projectInfoId" >
    <base-select  v-model="form_7084.projectInfoId" api="bug_info_all.get_project_info_list" title="项目名称"></base-select>
</uni-forms-item>

<uni-forms-item  v-show="!allParams.bugDeadline" label="截止时间" name="bugDeadline" >
    <uni-datetime-picker    placeholder="请输入截止时间" v-model="form_7084.bugDeadline" type="date"
                         :format="'yyyy-MM-dd'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>

<uni-forms-item v-show="!allParams.bugPriority" label="优先级" name="bugPriority" required>
    <base-select  v-model="form_7084.bugPriority" api="bug_info_all.get_bug_priority_status_list" title="优先级"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.bugHandleStatus" label="审核状态" name="bugHandleStatus" required>
    <base-select  v-model="form_7084.bugHandleStatus" api="bug_info_all.get_bug_handle_status_list" title="审核状态"></base-select>
</uni-forms-item>

<uni-forms-item  v-show="!allParams.createdTime" label="创建时间" name="createdTime" >
    <uni-datetime-picker    placeholder="请输入创建时间" v-model="form_7084.createdTime" type="date"
                         :format="'yyyy-MM-dd'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
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


























const form_7084 = ref({...allParams.value});


const success_4248=(
)=>{
  uni.showToast({
  title: '更新成功',
  icon: 'success',
  duration: 2000
});

}
const fail_4248=(
)=>{
  uni.showToast({
  title: '提交失败,请重试',
  icon: 'error',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>