<template>
  <base-wrapper>
    <base-wrapper
>
<base-form
:id="allParams.null"
table_module="book"
random_int="7649"
model="search"
:rules="{bookId:{
rules:[
    {required: true,
    errorMessage: '请输入图书ID'}],

},title:{
rules:[
    {required: true,
    errorMessage: '请输入书名'}],

},author:{
rules:[
    {required: true,
    errorMessage: '请输入作者'}],

},isbn:{
rules:[
    {required: true,
    errorMessage: '请输入ISBN'}],

},publicationDate:{
rules:[
    {required: true,
    errorMessage: '请输入出版日期'}],

},totalCopies:{
rules:[
    {required: true,
    errorMessage: '请输入总数量'}],

},availableCopies:{
rules:[
    {required: true,
    errorMessage: '请输入可借数量'}],

},}"
primary_key="bookId"
:form="form_7649"
>
<template #form-items>
<uni-forms-item v-show="!allParams.title" label="书名" name="title" required>
    <uni-easyinput   placeholder="请输入书名" v-model="form_7649.title" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.author" label="作者" name="author" required>
    <uni-easyinput   placeholder="请输入作者" v-model="form_7649.author" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.isbn" label="ISBN" name="isbn" required>
    <uni-easyinput   placeholder="请输入ISBN" v-model="form_7649.isbn" />
</uni-forms-item>


<uni-forms-item  v-show="!allParams.publicationDate" label="出版日期" name="publicationDate" required>
    <uni-datetime-picker    placeholder="请输入出版日期" v-model="form_7649.publicationDate" type="date"
                         :format="'yyyy-MM-dd'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>

<uni-forms-item v-show="!allParams.totalCopies" label="总数量" name="totalCopies" required>
    <uni-easyinput   placeholder="请输入总数量" v-model="form_7649.totalCopies" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.availableCopies" label="可借数量" name="availableCopies" required>
    <uni-easyinput   placeholder="请输入可借数量" v-model="form_7649.availableCopies" />
</uni-forms-item>


</template>
</base-form>
<base-list
ref="search_results_ref"
:isPage="true"
@click="click_8922"
:params="{'title':allParams.searchQuery,
'author':allParams.searchQuery,
}"
api="book.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.title?item.title:item.title"
title="书名"
>

</uni-list-item>
<uni-list-item
:rightText="item.author?item.author:item.author"
title="作者"
>

</uni-list-item>
<uni-list-item
:rightText="item.isbn?item.isbn:item.isbn"
title="ISBN"
>

</uni-list-item>
<uni-list-item
:rightText="item.totalCopies?item.totalCopies:item.totalCopies"
title="总数量"
>

</uni-list-item>
<uni-list-item
:rightText="item.availableCopies?item.availableCopies:item.availableCopies"
title="可借数量"
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
















const form_7649 = ref({...allParams.value});





const 搜索成功=(
)=>{

}
const 搜索失败=(
)=>{
  uni.showToast({
  title: '未找到相关图书',
  icon: 'none',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>