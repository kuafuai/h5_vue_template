<template>
  <base-wrapper>
    <base-list-header nickname="发起任务" description="发起任务"></base-list-header>
    <base-layout class="m-t-20 p-t-20" display="flex" x="between">
      <view style="width: 5%"></view>
      <view style="width: 60%">
        <v-form-render :form-data="formRenderData" ref="vFormRef"/>
        <view class="flex-center-center">
          <fui-button  type="success" text="发起任务"
                      btnSize="small"  size="25"
                      @click="submitForm"></fui-button>
        </view>
      </view>
      <view style="width: 30%">

      </view>
    </base-layout>
  </base-wrapper>
</template>

<script setup>
import {onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();

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

const formRenderData = ref({})
const formJson = ref({})

onMounted(async () => {
  const params = {deployId: allParams.value.deployId}
  let res = await proxy.$api.flowable_task.flowFormData(params);
  proxy.$nextTick(() => {
    proxy.$refs.vFormRef.setFormJson(res.data);
    formJson.value = res.data;
  })
})

async function submitForm(){
  let formDataMap = await proxy.$refs.vFormRef.getFormData();
  const params = {
    formJson:  formJson.value,
  }
  Object.assign(params, formDataMap);
  console.log(params)
  let res = await  proxy.$api.flowable_definition.definitionStart(allParams.value.procDefId, params)
  console.log(res);
}


onShow(() => {
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  const route = currentPage.route;
  uni.$emit('updateRoute', route);
})
</script>

<style scoped lang="scss">
::v-deep .uni-left-window {
  height: 100%;
}
</style>