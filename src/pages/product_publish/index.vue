<template>
  <base-wrapper
>
<base-form
:id="allParams.null"
table_module="product"
random_int="6900"
model="add"
:rules="{id:{
rules:[
    {required: true,
    errorMessage: '请输入主键'}],

},userId:{
rules:[
    {required: true,
    errorMessage: '请输入用户ID'}],

},title:{
rules:[
    {required: true,
    errorMessage: '请输入标题'}],

},description:{
rules:[
    {required: true,
    errorMessage: '请输入描述'}],

},price:{
rules:[
    {required: true,
    errorMessage: '请输入价格'}],

},imageUrl:{
rules:[
    {required: false,
    errorMessage: '请输入图片链接'}],

},createdAt:{
rules:[
    {required: true,
    errorMessage: '请输入创建时间'}],

},updatedAt:{
rules:[
    {required: true,
    errorMessage: '请输入更新时间'}],

},}"
primary_key="id"
:form="form_6900"
>
<template #form-items>
<uni-forms-item v-show="!allParams.userId" label="用户ID" name="userId" required>
    <base-select  v-model="form_6900.userId" api="product_all.get_user_list" title="用户ID"></base-select>
</uni-forms-item>

<uni-forms-item v-show="!allParams.title" label="标题" name="title" required>
    <uni-easyinput   placeholder="请输入标题" v-model="form_6900.title" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.description" label="描述" name="description" required>
    <base-textarea   placeholder="请输入描述" v-model="form_6900.description" class="custom-textarea"/>
</uni-forms-item>


<uni-forms-item v-show="!allParams.price" label="价格" name="price" required>
    <uni-easyinput   placeholder="请输入价格" v-model="form_6900.price" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.imageUrl"  label="图片链接" name="imageUrl" >
    <base-image-upload v-model="form_6900.imageUrl"  />
</uni-forms-item>

<uni-forms-item  v-show="!allParams.createdAt"  label="创建时间" name="createdAt" required>
    <uni-datetime-picker   placeholder="请输入创建时间" v-model="form_6900.createdAt" type="datetime"
                         :format="'yyyy-MM-dd HH:mm:ss'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>

<uni-forms-item  v-show="!allParams.updatedAt"  label="更新时间" name="updatedAt" required>
    <uni-datetime-picker   placeholder="请输入更新时间" v-model="form_6900.updatedAt" type="datetime"
                         :format="'yyyy-MM-dd HH:mm:ss'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>

</template>
</base-form>
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


















const form_6900 = ref({...allParams.value});


const success=(
)=>{
  uni.showToast({
  title: '商品发布成功',
  icon: '',
  duration: 2000
});

}
const failed=(
)=>{
  uni.showToast({
  title: '商品发布失败，请重试',
  icon: '',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>