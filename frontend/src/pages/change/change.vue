<template>
  <base-wrapper>
<!--    <base-list-header nickname="变更申请" description="变更申请"></base-list-header>-->
    <view style="width:100%;background:white;padding:25px 15px;color:#6569F7;font-weight:600;font-size:18px">
      变更申请
      <uni-breadcrumb separator="/">
        <uni-breadcrumb-item>
          变更管理平台
        </uni-breadcrumb-item>
        <uni-breadcrumb-item>
          变更列表
        </uni-breadcrumb-item>
        <uni-breadcrumb-item>
          变更申请
        </uni-breadcrumb-item>
      </uni-breadcrumb>
    </view>
    <base-layout class="m-t-20 p-t-20" display="flex" x="between">
<!--      <view style="width: 1%"></view>-->
      <view style="width: 50%">
        <uni-section class="p-10" title="变更基础信息" type="line">
          <uni-forms class="w-full" style="max-width: 90%" ref="baseForm" :rules="baseFormRules"
                     :modelValue="baseFormData"
                     label-width="120px">
            <uni-forms-item label="变更类型" required name="changeType">
              <uni-data-checkbox v-model="baseFormData.changeType" :localdata="changeTypes" @change="type_change"/>
            </uni-forms-item>
            <uni-forms-item label="变更标题" required name="changeTitle">
              <uni-easyinput v-model="baseFormData.changeTitle" placeholder="请输入变更标题"/>
            </uni-forms-item>
            <uni-forms-item label="客户名称" required name="changeCustomer">
              <uni-easyinput v-model="baseFormData.changeCustomer" placeholder="请输入客户名称"/>
            </uni-forms-item>

            <uni-forms-item label="项目名称" required name="changeProjectName">
              <uni-easyinput v-model="baseFormData.changeProjectName" placeholder="请输入项目名称"/>
            </uni-forms-item>

            <uni-forms-item label="产品名称" required name="changeProductName">
              <uni-easyinput v-model="baseFormData.changeProductName" placeholder="请输入产品名称"/>
            </uni-forms-item>
            <uni-forms-item label="项目阶段" required name="changeProjectStage">
              <el-select v-model="baseFormData.changeProjectStage" placeholder="请选择项目阶段">
                <el-option v-for="stage in projectStages"
                          :key="stage.value"
                          :label="stage.text"
                          :value="stage.value"
                />
              </el-select>
            </uni-forms-item>

            <uni-forms-item label="零件编号" required name="partNumber">
              <uni-easyinput type="textarea"  v-model="baseFormData.partNumber" placeholder="请输入零件编号"/>
            </uni-forms-item>

            <!-- <uni-forms-item label="断点时间" required name="changeEndTime">
              <uni-datetime-picker type="date" v-model="baseFormData.changeEndTime"
                                   :format="'yyyy-MM-dd'" :value-format="'yyyy-MM-dd'"/>
            </uni-forms-item> -->

            <uni-forms-item label="断点时间" required name="changeEndTimeOption">
              <uni-data-checkbox v-model="baseFormData.changeEndTimeOption" :localdata="changeEndTimeOptions" />
            </uni-forms-item>
            <uni-forms-item v-if="baseFormData.changeEndTimeOption.includes('breakpoint')" label="选择断点时间" required name="changeEndTime">
              <uni-datetime-picker type="date" v-model="baseFormData.changeEndTime"
                                   :format="'yyyy-MM-dd'" :value-format="'yyyy-MM-dd'"/>
            </uni-forms-item>



          </uni-forms>
          <view class="flex-center-center">
            <fui-button type="success" text="发起变更申请"
                        btnSize="small" size="25"
                        @click="submitForm"></fui-button>
          </view>
        </uni-section>
      </view>
<!--      <view style="width: 1%"></view>-->
      <view style="width: 48%">
        <uni-section title="变更扩展信息" type="line" class="p-10">
          <v-form-render :form-data="formRenderData" ref="vFormRef"/>
        </uni-section>
      </view>
      <view style="width: 1%"></view>
    </base-layout>
  </base-wrapper>
</template>

<script setup>
import {onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();

const projectStages = ref([
  { text: '样件制造阶段', value: '样件制造阶段' },
  { text: 'OTS工装样件阶段', value: 'OTS工装样件阶段' },
  { text: 'SOP量产阶段', value: 'SOP量产阶段' }
]);

const formRenderData = ref({});
const formJson = ref({});
const baseFormData = ref({
  changeEndTimeOption: [], // 初始化为空数组，以便可以存储选择的选项
  changeEndTime: ''
});
const changeEndTimeOptions = ref([
  {
    text: '自然切换',
    value: 'natural'
  },
  {
    text: '断点时间',
    value: 'breakpoint'
  }
]);
const baseFormRules = ref({
  changeType: {
    rules: [{
      required: true,
      errorMessage: '变更类型不能为空'
    }]
  },
  changeTitle: {
    rules: [{
      required: true,
      errorMessage: '变更标题不能为空'
    }]
  },
  changeCustomer: {
    rules: [{
      required: true,
      errorMessage: '客户名称不能为空'
    }]
  },
  changeProjectName: {
    rules: [{
      required: true,
      errorMessage: '项目名称不能为空'
    }]
  },
  changeProductName: {
    rules: [{
      required: true,
      errorMessage: '产品名称不能为空'
    }]
  },
  changeProjectStage: {
    rules: [{
      required: true,
      errorMessage: '项目阶段不能为空'
    }]
  },
  partNumber: {
    rules: [{
      required: true,
      errorMessage: '项目阶段不能为空'
    }]
  },
  changeEndTime: {
    rules: [{
      required: true,
      errorMessage: '断点时间不能为空'
    }]
  }
})


const changeTypes = ref([
  {
    text: 'ECR',
    value: 1
  },
  {
    text: 'ECN',
    value: 2
  }
])

async function type_change(e) {

  const params = {changeType: e.detail.value}
  let res = await proxy.$api.change_manager.flowFormData(params);
  if (res.code === 0) {
    baseFormData.value.deployId = res.data.deployId;
    baseFormData.value.procDefId = res.data.procDefId;

    proxy.$nextTick(() => {
      proxy.$refs.vFormRef.setFormJson(res.data.formJson);
      formJson.value = res.data.formJson;
    })
  }
}

async function submitForm() {
  proxy.$refs.baseForm.validate().then(async (res) => {

    uni.showLoading({
      title: '发起变更中'
    });

    let formDataMap = await proxy.$refs.vFormRef.getFormData();
    const params = {
      formJson: formJson.value,
    }
    Object.assign(params, formDataMap);
    baseFormData.value.variables = params;
    console.log(baseFormData.value)

    let resAdd = await proxy.$api.change_manager.add(baseFormData.value)
    console.log(resAdd)

    uni.hideLoading();

    if (resAdd.code === 0) {
      proxy.$navigate('/pages/change/index')
    } else {
      uni.showToast({
        'title': '操作失败',
        'position': 'center',
        'icon': 'error'
      });
    }
  }).catch(err => {

  })

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