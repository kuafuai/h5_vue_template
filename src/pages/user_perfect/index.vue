<template>
  <base-wrapper>
    <base-wrapper
>
<el-text
>

</el-text>
<base-form
@success="success_3833"
:params="{'userInfoId':allParams.currentUser.userId,
}"
:id="allParams.null"
table_module="user_info"
random_int="1424"
model="update"
:rules="{userInfoId:{
rules:[
    {required: true,
    errorMessage: '请输入编号'}],

},wechatLoginId:{
rules:[
    {required: false,
    errorMessage: '请输入小程序openid'}],

},phoneNumber:{
rules:[
    {required: false,
    errorMessage: '请输入手机号'}],

},userName:{
rules:[
    {required: false,
    errorMessage: '请输入姓名'}],

},projectOrganization:{
rules:[
    {required: false,
    errorMessage: '请输入公司名称'}],

},title:{
rules:[
    {required: false,
    errorMessage: '请输入职位头衔'}],

},wechatNumber:{
rules:[
    {required: false,
    errorMessage: '请输入微信号'}],

},userInfoAuthStatus:{
rules:[
    {required: true,
    errorMessage: '请输入认证状态'}],

},}"
@fail="fail_3833"
primary_key="userInfoId"
:form="form_1424"
>
<template #form-items>
<uni-forms-item v-show="!allParams.wechatLoginId" label="小程序openid" name="wechatLoginId" >
    <uni-easyinput   placeholder="请输入小程序openid" v-model="form_1424.wechatLoginId" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.phoneNumber" label="手机号" name="phoneNumber" >
    <uni-easyinput   placeholder="请输入手机号" v-model="form_1424.phoneNumber" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.userName" label="姓名" name="userName" >
    <uni-easyinput   placeholder="请输入姓名" v-model="form_1424.userName" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.projectOrganization" label="公司名称" name="projectOrganization" >
    <uni-easyinput   placeholder="请输入公司名称" v-model="form_1424.projectOrganization" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.title" label="职位头衔" name="title" >
    <uni-easyinput   placeholder="请输入职位头衔" v-model="form_1424.title" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.wechatNumber" label="微信号" name="wechatNumber" >
    <uni-easyinput   placeholder="请输入微信号" v-model="form_1424.wechatNumber" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.userInfoAuthStatus" label="认证状态" name="userInfoAuthStatus" required>
    <base-select  v-model="form_1424.userInfoAuthStatus" api="user_info_all.get_auth_status_list" title="认证状态"></base-select>
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


















const form_1424 = ref({...allParams.value});


const success_3833=(
item
)=>{
  proxy.$navigate( "/pages/user_info/index",false)

}

</script>

<style lang="scss" scoped></style>