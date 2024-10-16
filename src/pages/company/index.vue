<template>
  <base-wrapper>
    <base-wrapper
>
<base-form
@success="success_3334"
:id="allParams.null"
table_module="company_select"
random_int="2280"
model="add"
:rules="{id:{
rules:[
    {required: true,
    errorMessage: '请输入编号'}],

},companyId:{
rules:[
    {required: true,
    errorMessage: '请输入公司名'}],

},}"
@fail="fail_3334"
primary_key="id"
:form="form_2280"
>
<template #form-items>
<uni-forms-item v-show="!allParams.companyId" label="公司名" name="companyId" required>
    <base-select  v-model="form_2280.companyId" api="company_select_all.get_company_list" title="公司名"></base-select>
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






const form_2280 = ref({...allParams.value});


const success_3334=(
item
)=>{
  proxy.$navigate( "/pages/survey_info/index?creatorId=" + item.companyId,false)

}
const fail_3334=(
)=>{
  uni.showToast({
  title: '提交失败,请重试',
  icon: 'none',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>