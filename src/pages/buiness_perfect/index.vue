<template>
  <base-wrapper>
    <base-wrapper
>
<el-text
>

</el-text>
<base-form
@success="success_6000"
:params="{'buinessPersonId':allParams.currentUser.userId,
}"
:id="allParams.null"
table_module="buiness_person"
random_int="2740"
model="update"
:rules="{buinessPersonId:{
rules:[
    {required: true,
    errorMessage: '请输入编号'}],

},bWechatLoginId:{
rules:[
    {required: false,
    errorMessage: '请输入小程序openid'}],

},bPhoneNumber:{
rules:[
    {required: true,
    errorMessage: '请输入手机号'}],

},buinessPersonName:{
rules:[
    {required: true,
    errorMessage: '请输入姓名'}],

},bProjectOrganization:{
rules:[
    {required: true,
    errorMessage: '请输入组织'}],

},bTitle:{
rules:[
    {required: true,
    errorMessage: '请输入职位头衔'}],

},bWechatNumber:{
rules:[
    {required: true,
    errorMessage: '请输入微信号'}],

},buinessAuthStatus:{
rules:[
    {required: false,
    errorMessage: '请输入认证状态'}],

},}"
@fail="fail_6000"
primary_key="buinessPersonId"
:form="form_2740"
>
<template #form-items>
<uni-forms-item v-show="!allParams.bWechatLoginId" label="小程序openid" name="bWechatLoginId" >
    <uni-easyinput   placeholder="请输入小程序openid" v-model="form_2740.bWechatLoginId" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.bPhoneNumber" label="手机号" name="bPhoneNumber" required>
    <uni-easyinput   placeholder="请输入手机号" v-model="form_2740.bPhoneNumber" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.buinessPersonName" label="姓名" name="buinessPersonName" required>
    <uni-easyinput   placeholder="请输入姓名" v-model="form_2740.buinessPersonName" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.bProjectOrganization" label="组织" name="bProjectOrganization" required>
    <uni-easyinput   placeholder="请输入组织" v-model="form_2740.bProjectOrganization" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.bTitle" label="职位头衔" name="bTitle" required>
    <uni-easyinput   placeholder="请输入职位头衔" v-model="form_2740.bTitle" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.bWechatNumber" label="微信号" name="bWechatNumber" required>
    <uni-easyinput   placeholder="请输入微信号" v-model="form_2740.bWechatNumber" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.buinessAuthStatus" label="认证状态" name="buinessAuthStatus" >
    <base-select  v-model="form_2740.buinessAuthStatus" api="buiness_person_all.get_auth_status_list" title="认证状态"></base-select>
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


















const form_2740 = ref({...allParams.value});


const success_6000=(
item
)=>{
  proxy.$navigate( "/pages/buiness_person/index",false)

}

</script>

<style lang="scss" scoped></style>