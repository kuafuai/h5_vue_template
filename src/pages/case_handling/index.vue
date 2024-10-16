<template>
  <base-wrapper>
    <base-wrapper
>
<base-description
ref="case_description_ref"
:params="{'caseId':allParams.caseId,
}"
api="case_info_all.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.caseId"
type="base"
title="案件ID"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.caseNumber"
type="base"
title="案件编号"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.caseTitle"
type="base"
title="案件标题"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.caseDescription"
type="base"
title="案件描述"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.caseStatus"
type="base"
title="案件状态"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.username"
type="base"
title="关联用户ID"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.createdAt"
type="base"
title="创建时间"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.updatedAt"
type="base"
title="更新时间"
class="mb-10"
>
</uni-section>


</template>
</base-description>
<base-form
@success="success_881"
:params="{'caseId':allParams.caseId,
}"
:id="allParams.null"
table_module="case_progress"
random_int="1200"
model="add"
:rules="{progressId:{
rules:[
    {required: true,
    errorMessage: '请输入进展ID'}],

},caseId:{
rules:[
    {required: true,
    errorMessage: '请输入关联案件ID'}],

},progressDescription:{
rules:[
    {required: true,
    errorMessage: '请输入进展描述'}],

},progressStatus:{
rules:[
    {required: true,
    errorMessage: '请输入进展状态'}],

},createdAt:{
rules:[
    {required: true,
    errorMessage: '请输入记录时间'}],

},}"
@fail="fail_881"
primary_key="progressId"
:form="form_1200"
>
<template #form-items>
<uni-forms-item v-show="!allParams.caseId" label="关联案件ID" name="caseId" required>
    <base-select  v-model="form_1200.caseId" api="case_progress_all.get_case_info_list" title="关联案件ID"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.progressDescription" label="进展描述" name="progressDescription" required>
    <base-textarea   placeholder="请输入进展描述" v-model="form_1200.progressDescription" class="custom-textarea"/>
</uni-forms-item>


<uni-forms-item v-show="!allParams.progressStatus" label="进展状态" name="progressStatus" required>
    <uni-easyinput   placeholder="请输入进展状态" v-model="form_1200.progressStatus" />
</uni-forms-item>


<uni-forms-item  v-show="!allParams.createdAt"  label="记录时间" name="createdAt" required>
    <uni-datetime-picker   placeholder="请输入记录时间" v-model="form_1200.createdAt" type="datetime"
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















const form_1200 = ref({...allParams.value});


const success_881=(
item
)=>{
  uni.showToast({
  title: '案件进展更新成功',
  icon: 'none',
  duration: 2000
});

}
const fail_881=(
)=>{
  uni.showToast({
  title: '案件进展更新失败，请重试。',
  icon: 'none',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>