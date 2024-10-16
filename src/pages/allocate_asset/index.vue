<template>
  <base-wrapper>
    <base-wrapper
>
<base-form
@success="success_3823"
:id="allParams.null"
table_module="asset_allocation_info"
random_int="3441"
model="add"
:rules="{allocationId:{
rules:[
    {required: true,
    errorMessage: '请输入分配ID'}],

},assetId:{
rules:[
    {required: true,
    errorMessage: '请输入资产ID'}],

},departmentOrEmployee:{
rules:[
    {required: true,
    errorMessage: '请输入分配给部门或员工'}],

},allocationDate:{
rules:[
    {required: true,
    errorMessage: '请输入分配日期'}],

},}"
@fail="fail_3823"
primary_key="allocationId"
:form="form_3441"
>
<template #form-items>
<uni-forms-item v-show="!allParams.assetId" label="资产ID" name="assetId" required>
    <uni-easyinput   placeholder="请输入资产ID" v-model="form_3441.assetId" />
</uni-forms-item>


<uni-forms-item v-show="!allParams.departmentOrEmployee" label="分配给部门或员工" name="departmentOrEmployee" required>
    <uni-easyinput   placeholder="请输入分配给部门或员工" v-model="form_3441.departmentOrEmployee" />
</uni-forms-item>


<uni-forms-item  v-show="!allParams.allocationDate" label="分配日期" name="allocationDate" required>
    <uni-datetime-picker    placeholder="请输入分配日期" v-model="form_3441.allocationDate" type="date"
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










const form_3441 = ref({...allParams.value});


const success_3823=(
item
)=>{
  proxy.$navigate( "/pages/asset_detail/index?assetId=" + assetId,false)

}

</script>

<style lang="scss" scoped></style>