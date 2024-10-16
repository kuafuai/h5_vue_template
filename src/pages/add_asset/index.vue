<template>
  <base-wrapper>
    <base-wrapper
>
<base-form
@success="success_5695"
:id="allParams.null"
table_module="asset_info"
random_int="6041"
model="add"
:rules="{assetId:{
rules:[
    {required: true,
    errorMessage: '请输入资产ID'}],

},assetName:{
rules:[
    {required: true,
    errorMessage: '请输入资产名称'}],

},assetType:{
rules:[
    {required: true,
    errorMessage: '请输入资产类型'}],

},specification:{
rules:[
    {required: true,
    errorMessage: '请输入规格'}],

},purchaseDate:{
rules:[
    {required: true,
    errorMessage: '请输入购买日期'}],

},status:{
rules:[
    {required: true,
    errorMessage: '请输入状态'}],

},}"
@fail="fail_5695"
primary_key="assetId"
:form="form_6041"
>
<template #form-items>
<uni-forms-item v-show="!allParams.assetName" label="资产名称" name="assetName" required>
    <uni-easyinput   placeholder="请输入资产名称" v-model="form_6041.assetName" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.assetType" label="资产类型" name="assetType" required>
    <uni-easyinput   placeholder="请输入资产类型" v-model="form_6041.assetType" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.specification" label="规格" name="specification" required>
    <uni-easyinput   placeholder="请输入规格" v-model="form_6041.specification" />
</uni-forms-item>


<uni-forms-item  v-show="!allParams.purchaseDate" label="购买日期" name="purchaseDate" required>
    <uni-datetime-picker    placeholder="请输入购买日期" v-model="form_6041.purchaseDate" type="date"
                         :format="'yyyy-MM-dd'" :value-format="'yyyy-MM-dd HH:mm:ss'" />
</uni-forms-item>

<uni-forms-item v-show="!allParams.status" label="状态" name="status" required>
    <uni-easyinput   placeholder="请输入状态" v-model="form_6041.status" />
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














const form_6041 = ref({...allParams.value});


const success_5695=(
item
)=>{
  proxy.$navigate( "/pages/asset_management_home/index",false)

}

</script>

<style lang="scss" scoped></style>