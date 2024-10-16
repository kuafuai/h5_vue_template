<template>
  <base-wrapper>
    <base-list-header nickname="任务办理" description="任务办理"></base-list-header>
    <base-layout class="m-t-20 p-t-20" display="flex" x="between">
      <view style="width: 5%"></view>
      <view style="width: 40%">
        <v-form-render ref="vFormRef"/>

        <view class="flex-center-center">
          <fui-button type="success" text="审批"
                      btnSize="small" size="25"
                      @click="submitApprove"></fui-button>
        </view>
      </view>
      <view style="width: 55%">
        <el-timeline style="max-width: 600px">
          <el-timeline-item
              v-for="(item,index ) in flowRecordList"
              :key="index"
              :color="setColor(item.finishTime)"
          >
            <p style="font-weight: 700">{{ item.taskName }}</p>
            <el-card :body-style="{ padding: '10px' }">
              <el-descriptions class="margin-top" :column="1" size="small" border>
                <el-descriptions-item v-if="item.assigneeName" label-class-name="my-label">
                  <template #label><i class="el-icon-user"></i>办理人</template>
                  {{ item.assigneeName }}
                </el-descriptions-item>
                <el-descriptions-item v-if="item.candidate" label-class-name="my-label">
                  <template #label><i class="el-icon-user"></i>候选办理</template>
                  {{ item.candidate }}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template #label>接收时间</template>
                  {{ item.createTime }}
                </el-descriptions-item>
                <el-descriptions-item v-if="item.finishTime" label-class-name="my-label">
                  <template #label><i class="el-icon-date"></i>处理时间</template>
                  {{ item.finishTime }}
                </el-descriptions-item>
                <el-descriptions-item v-if="item.duration" label-class-name="my-label">
                  <template #label><i class="el-icon-time"></i>耗时</template>
                  {{ item.duration }}
                </el-descriptions-item>
                <el-descriptions-item v-if="item.comment" label-class-name="my-label">
                  <template #label><i class="el-icon-tickets"></i>处理意见</template>
                  {{ item.comment.comment }}
                </el-descriptions-item>
              </el-descriptions>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </view>
    </base-layout>
  </base-wrapper>
  <uni-popup ref="completePopup" style="width: 200px"
             borderRadius="10px 10px 10px 10px"
             background-color="#fff">
    <view class="m-10 w-full">
      <uni-forms ref="refTaskForm" style="max-width: 100%" :modelValue="taskForm" label-position="top">
        <uni-forms-item label="处理意见">
          <uni-easyinput type="textarea" v-model="taskForm.comment" placeholder="请输入处理意见"/>
        </uni-forms-item>
      </uni-forms>
      <view class="flex-center-center">
        <button type="primary" size="mini" @click="cancel">取 消</button>
        <button type="primary" size="mini" @click="submitForm">确 定</button>
      </view>
    </view>
  </uni-popup>
</template>

<script setup>
import {onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();

const allParams = ref({});

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

const taskForm = ref({});
const formJson = ref({});
const flowRecordList = ref([]);

onMounted(async () => {
  taskForm.value.deployId = allParams.value.deployId;
  taskForm.value.taskId = allParams.value.taskId;
  taskForm.value.procInsId = allParams.value.procInsId;
  taskForm.value.executionId = allParams.value.executionId;
  taskForm.value.instanceId = allParams.value.procInsId;

  if (taskForm.value.taskId) {
    getFlowTaskForm(taskForm.value.taskId)
  }
  getFlowRecordList(taskForm.value.procInsId, taskForm.value.deployId);
});

async function getFlowRecordList(procInsId, deployId) {
  let recordVar = await proxy.$api.flowable_task.flowRecord({procInsId: procInsId, deployId: deployId});
  flowRecordList.value = recordVar.data.flowList;
}

async function getFlowTaskForm(taskId) {
  let res = await proxy.$api.flowable_task.flowTaskForm({taskId: taskId})
  console.log(res);
  proxy.$refs.vFormRef.setFormJson(res.data.formJson);
  formJson.value = res.data.formJson;

  proxy.$nextTick(() => {
    proxy.$refs.vFormRef.setFormData(res.data);
  })
}

function setColor(val) {
  if (val) {
    return "#2bc418";
  } else {
    return "#b3bdbb";
  }
}


function submitApprove() {
  proxy.$refs.completePopup.open();
}

function cancel() {
  proxy.$refs.completePopup.close();
}

async function submitForm() {

  let formDataMap = await proxy.$refs.vFormRef.getFormData();
  taskForm.value.variables = formDataMap;
  taskForm.value.variables.formJson = formJson.value;
  console.log('====', taskForm.value)

  let res = await proxy.$api.flowable_task.complete(taskForm.value);
  if (res.code === 0) {
    proxy.$refs.completePopup.close();
    proxy.$navigate('/pages/flowable/todo/index')
  } else {
    uni.showToast({
      'title': '审批失败',
      'position': 'center',
      'icon': 'error'
    });
  }

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

::v-deep .uni-popup__wrapper {
  width: 600px;
  height: 210px;
}

</style>