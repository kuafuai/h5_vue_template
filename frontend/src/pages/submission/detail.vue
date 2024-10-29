<template>
  <base-wrapper>
<!--    <base-list-header nickname="提交物管理" description="提交物设置"></base-list-header>-->
    <view style="width:100%;background:white;padding:25px 15px;color:#6569F7;font-weight:600;font-size:18px">
      提交物管理
      <uni-breadcrumb separator="/">
        <uni-breadcrumb-item>
          变更管理平台
        </uni-breadcrumb-item>
        <uni-breadcrumb-item>
          提交物管理
        </uni-breadcrumb-item>
        <uni-breadcrumb-item>
          提交物设置
        </uni-breadcrumb-item>
      </uni-breadcrumb>
    </view>
    <base-layout class=" h-full">
      <base-form :model="allParams.submissionId ? 'update':'add'" :form="form_data" table_module="submission_material" :id="allParams.submissionId" @success="form_success">
        <template #form-items>
          <uni-forms-item label="显示名称:" name="submissionName">
            <uni-easyinput v-model="form_data.submissionName"></uni-easyinput>
          </uni-forms-item>
          <uni-forms-item label="审批方式:" name="submissionType">
            <uni-data-select v-model="form_data.submissionType" :localdata="[{ value: 1, text: '多人会审' },
            { value: 2, text: '逐级审批' }]"></uni-data-select>
          </uni-forms-item>
          <uni-forms-item label="审批人:" name="submissionUserId">
            <base-mul-select api="userinfo.select_list" :ids="form_data.submissionUserId"
                             :texts="form_data.submissionUserName" @change="mul_select_change"> </base-mul-select>
          </uni-forms-item>
        </template>
      </base-form>
    </base-layout>
  </base-wrapper>
</template>

<script setup>
import { onShow } from "@dcloudio/uni-app";
const { proxy } = getCurrentInstance();
const allParams = ref({})

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
if (currentUser != null) {
  let parse = JSON.parse(currentUser);
  allParams.value["currentUser"] = parse;
  allParams.value[parse["relevanceTable"] + "Id"] = parse.userId
}

const form_data = ref({...allParams.value});

function mul_select_change(data){
  const ids = data.map(item => item.id).join(',');
  const texts = data.map(item => item.text).join(',');
  form_data.value.submissionUserId = ids;
  form_data.value.submissionUserName = texts;
}

function form_success(data){
  proxy.$navigate('/pages/submission/index')
}

onShow(()=>{
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  const route = currentPage.route;
  uni.$emit('updateRoute', route);
})
</script>

<style scoped lang="scss">
::v-deep .uni-left-window {
  height: 100% ;
}
</style>