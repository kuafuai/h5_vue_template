<template>
  <base-wrapper>
    <base-wrapper
>
<el-text
>

</el-text>
<base-form
@success="success_5425"
:params="{'partnerId':allParams.currentUser.userId,
}"
:id="allParams.null"
table_module="partner"
random_int="4820"
model="update"
:rules="{partnerId:{
rules:[
    {required: true,
    errorMessage: '请输入主键'}],

},partnerWechatLoginId:{
rules:[
    {required: false,
    errorMessage: '请输入微信绑定ID'}],

},partnerPhoneNumber:{
rules:[
    {required: true,
    errorMessage: '请输入手机号'}],

},partnerName:{
rules:[
    {required: true,
    errorMessage: '请输入姓名'}],

},idCard:{
rules:[
    {required: false,
    errorMessage: '请输入身份证号'}],

},}"
@fail="fail_5425"
primary_key="partnerId"
:form="form_4820"
>
<template #form-items>
<uni-forms-item v-show="!allParams.partnerWechatLoginId" label="微信绑定ID" name="partnerWechatLoginId" >
    <uni-easyinput   placeholder="请输入微信绑定ID" v-model="form_4820.partnerWechatLoginId" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.partnerPhoneNumber" label="手机号" name="partnerPhoneNumber" required>
    <uni-easyinput   placeholder="请输入手机号" v-model="form_4820.partnerPhoneNumber" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.partnerName" label="姓名" name="partnerName" required>
    <uni-easyinput   placeholder="请输入姓名" v-model="form_4820.partnerName" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.idCard" label="身份证号" name="idCard" >
    <uni-easyinput   placeholder="请输入身份证号" v-model="form_4820.idCard" />
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












const form_4820 = ref({...allParams.value});


const success_5425=(
item
)=>{
  proxy.$navigate( "/pages/sales_lead/index",false)

}

</script>

<style lang="scss" scoped></style>