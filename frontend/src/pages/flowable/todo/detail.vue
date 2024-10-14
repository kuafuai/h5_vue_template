<template>
  <base-wrapper>
    <base-list-header nickname="任务办理" description="任务办理"></base-list-header>
    <base-layout class="m-t-20 p-t-20" display="flex" x="between">
      <view style="width: 5%"></view>
      <view style="width: 40%">
        <v-form-render ref="vFormRef"/>
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