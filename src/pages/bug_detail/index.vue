<template>
  <base-wrapper>
    <base-wrapper
>
<base-description
ref="bug_info_description"
:params="{'bugInfoId':allParams.bugInfoId,
}"
api="bug_info_all.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.bugInfoId"
type="base"
title="BUG ID"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.title"
type="base"
title="标题"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.description"
type="base"
title="描述"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.status"
type="base"
title="状态"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.priority"
type="base"
title="优先级"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.username"
type="base"
title="创建者ID"
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
@success="success_1062"
:params="{'bugInfoId':allParams.bugInfoId,
}"
:id="allParams.null"
table_module="bug_info"
random_int="5542"
model="update"
:rules="{bugInfoId:{
rules:[
    {required: true,
    errorMessage: '请输入BUG ID'}],

},title:{
rules:[
    {required: true,
    errorMessage: '请输入标题'}],

},description:{
rules:[
    {required: true,
    errorMessage: '请输入描述'}],

},status:{
rules:[
    {required: true,
    errorMessage: '请输入状态'}],

},priority:{
rules:[
    {required: true,
    errorMessage: '请输入优先级'}],

},createdBy:{
rules:[
    {required: true,
    errorMessage: '请输入创建者ID'}],

},createdAt:{
rules:[
    {required: true,
    errorMessage: '请输入创建时间'}],

},updatedAt:{
rules:[
    {required: true,
    errorMessage: '请输入更新时间'}],

},}"
@fail="fail_1062"
primary_key="bugInfoId"
:form="form_5542"
>
<template #form-items>
<uni-forms-item v-show="!allParams.title" label="标题" name="title" required>
    <uni-easyinput   placeholder="请输入标题" v-model="form_5542.title" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.description" label="描述" name="description" required>
    <base-textarea   placeholder="请输入描述" v-model="form_5542.description" class="custom-textarea"/>
</uni-forms-item>


<uni-forms-item v-show="!allParams.status" label="状态" name="status" required>
    <uni-easyinput   placeholder="请输入状态" v-model="form_5542.status" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.priority" label="优先级" name="priority" required>
    <uni-easyinput   placeholder="请输入优先级" v-model="form_5542.priority" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.createdBy" label="创建者ID" name="createdBy" required>
    <base-select  v-model="form_5542.createdBy" api="bug_info_all.get_user_info_list" title="创建者ID"></base-select>
</uni-forms-item>

<uni-forms-item  v-show="!allParams.createdAt"  label="创建时间" name="createdAt" required>
    <uni-datetime-picker   placeholder="请输入创建时间" v-model="form_5542.createdAt" type="datetime"
                         :format="'yyyy-MM-dd HH:mm:ss'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>

<uni-forms-item  v-show="!allParams.updatedAt"  label="更新时间" name="updatedAt" required>
    <uni-datetime-picker   placeholder="请输入更新时间" v-model="form_5542.updatedAt" type="datetime"
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





















const form_5542 = ref({...allParams.value});


const success_1062=(
item
)=>{
  uni.showToast({
  title: '更新成功',
  icon: 'success',
  duration: 2000
});

}
const fail_1062=(
)=>{
  uni.showToast({
  title: '更新失败，请重试',
  icon: 'error',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>