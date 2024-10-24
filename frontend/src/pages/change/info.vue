<template>
  <base-wrapper>
    <base-list-header nickname="变更详情" description="变更详情"></base-list-header>
    <base-layout class="m-t-20 p-t-20" display="flex" direction="c">
      <view class="flex-c-start-start m-b-20 w-full">
        <view class="flex-around-start m-b-20">
          <fui-text :text='baseInfo.changeType === 1 ? "[ECR]":"[ECN]"+" "+baseInfo.changeTitle' type="black"
                    size="36">

          </fui-text>
        </view>
        <view class="flex-between-start m-b-10 w-full">
          <view style="width: 30%">
            <fui-text text="发起人："></fui-text>
            <fui-text :text="baseInfo.changePersonName"></fui-text>
          </view>
          <view style="width: 30%">
            <fui-text text="产品名称："></fui-text>
            <fui-text :text="baseInfo.changeProductName"></fui-text>
          </view>
          <view style="width: 30%">
            <fui-text text="发起时间："></fui-text>
            <fui-text :text="baseInfo.changeStartTime"></fui-text>
          </view>
        </view>
        <view class="flex-between-start m-b-10 w-full">
          <view style="width: 30%">
            <fui-text text="项目名称："></fui-text>
            <fui-text :text="baseInfo.changeProjectName"></fui-text>
          </view>
          <view style="width: 30%">
            <fui-text text="项目阶段："></fui-text>
            <fui-text :text="baseInfo.changeProjectStage"></fui-text>
          </view>
          <view style="width: 30%">
            <fui-text text="断点时间："></fui-text>
            <fui-text :text="baseInfo.changeEndTime"></fui-text>
          </view>
        </view>
      </view>
      <view class=" m-20 w-full">
        <el-tabs tab-position="top" v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="变更流程" name="1">
            <el-timeline style="max-width: 800px; margin-top: 20px;">
              <el-timeline-item
                  v-for="(item,index ) in flowRecordList"
                  :key="index"
                  :color="setColor(item.finishTime)"
              >
                <view class="w-full flex-start-start">
                  <p style="font-weight: 700">{{ item.taskName }}</p>
                  <view class="m-l-20">
                    <uni-tag :text='item.finishTime?"完成":"处理中"' size="small"
                             :type='item.finishTime?"success":"warning"'
                             @click="handle_process(item)"/>
                  </view>

                  <view class="m-l-20" v-if="item.assigneeName">
                    <uni-icons type="person" size="18"></uni-icons>
                    <text>{{ item.assigneeName }}</text>
                  </view>

                  <view class="m-l-20" v-if="item.duration">

                    <uni-icons type="timer" size="18"></uni-icons>
                    <text>耗时 {{ item.duration }} , 于 {{ item.finishTime }} 完成</text>
                  </view>
                </view>
                <view v-if="item.hasChild" class="m-t-40">

                  <el-timeline style="max-width: 800px; margin-top: 20px;"
                               v-for="(sub,subIndex ) in item.subTask"
                               :key="subIndex">
                    <el-timeline-item v-for="(subItem, ssIndex) in sub.subTask"
                                      :key="ssIndex"
                                      :color="setColor(subItem.finishTime)" class="pass-node">

                      <view class="w-full flex-start-start">
                        <p style="font-weight: 700">{{ subItem.taskName }}</p>
                        <view class="m-l-20" v-if=" subItem.comment!=null && subItem.comment.type == 3">
                          <uni-tag v-show="subItem.comment.type == 3" text="驳回" type="error" size="small"/>
                        </view>

                        <view v-else class="m-l-20">
                          <uni-tag :text='subItem.finishTime?"完成":"处理中"' size="small"
                                   :type='subItem.finishTime?"success":"warning"'
                                   @click="handle_process_sub_task(subItem, item.taskName)">
                          </uni-tag>
                        </view>

                        <view class="m-l-20" v-if="subItem.assigneeName">
                          <uni-icons type="person" size="18"></uni-icons>
                          <text>{{ subItem.assigneeName }}</text>
                        </view>

                        <view class="m-l-20" v-if="subItem.duration">
                          <uni-icons type="timer" size="18"></uni-icons>
                          <text>耗时 {{ subItem.duration }} , 于 {{ subItem.finishTime }} 完成</text>
                        </view>
                      </view>

                    </el-timeline-item>
                  </el-timeline>

                </view>
                <view v-else>
                  <view class="w-full m-y-10 flex-start-start" v-for="(sub,subIndex) in item.subTask" :key="subIndex">
                    <p style="font-weight: 700">{{ sub.taskName }}</p>
                    <view class="m-l-20">
                      <uni-tag :text='sub.finishTime?"完成":"处理中"' size="small"
                               :type='sub.finishTime?"success":"warning"'
                               @click="handle_process(sub)"/>
                    </view>

                    <view class="m-l-20" v-if="sub.assigneeName">
                      <uni-icons type="person" size="18"></uni-icons>
                      <text>{{ sub.assigneeName }}</text>
                    </view>

                    <view class="m-l-20" v-if="sub.duration">

                      <uni-icons type="timer" size="18"></uni-icons>
                      <text>耗时 {{ sub.duration }} , 于 {{ sub.finishTime }} 完成</text>
                    </view>

                  </view>
                </view>
              </el-timeline-item>
            </el-timeline>
          </el-tab-pane>

          <el-tab-pane label="流程日志" name="2">
            <el-timeline style="max-width: 600px; margin-top: 20px">
              <el-timeline-item
                  v-for="(item,index ) in flowRecordAllList"
                  :key="index"
                  :color="setColor(item.finishTime)"
              >
                <p style="font-weight: 700">{{ item.taskName }}</p>

                <el-card v-if="item.createTime" :body-style="{ padding: '10px' }">
                  <el-descriptions class="margin-top" :column="1" size="small" border>
                    <el-descriptions-item v-if="item.assigneeName" label-class-name="my-label">
                      <template #label>办理人</template>
                      {{ item.assigneeName }}
                    </el-descriptions-item>
                    <el-descriptions-item v-if="item.createTime">
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
                    <el-descriptions-item v-if="item.infoValue">
                      <template #label>提交的文件</template>
                      <el-link class="m-x-4" type="success" v-for='(f,i) in file_split(item.infoValue)'
                               @click="handle_download(f)">
                        下载文件{{ i + 1 }}
                      </el-link>
                    </el-descriptions-item>
                  </el-descriptions>
                </el-card>

                <view v-if="item.hasChild">
                  <el-timeline style="max-width: 600px; margin-top: 20px;"
                               v-for="(sub,subIndex ) in item.subTask"
                               :key="subIndex">

                    <el-timeline-item v-for="(subItem, ssIndex) in sub.subTask"
                                      :key="ssIndex"
                                      :color="setColor(subItem.finishTime)" class="pass-node">

                      <p style="font-weight: 700">{{ subItem.taskName }}</p>

                      <el-card :body-style="{ padding: '10px' }">

                        <el-descriptions class="margin-top" :column="1" size="small" border>
                          <el-descriptions-item v-if="subItem.assigneeName" label-class-name="my-label">
                            <template #label>办理人</template>
                            {{ subItem.assigneeName }}
                          </el-descriptions-item>
                          <el-descriptions-item v-if="subItem.createTime">
                            <template #label>接收时间</template>
                            {{ subItem.createTime }}
                          </el-descriptions-item>
                          <el-descriptions-item v-if="subItem.finishTime" label-class-name="my-label">
                            <template #label><i class="el-icon-date"></i>处理时间</template>
                            {{ subItem.finishTime }}
                          </el-descriptions-item>
                          <el-descriptions-item v-if="subItem.duration" label-class-name="my-label">
                            <template #label><i class="el-icon-time"></i>耗时</template>
                            {{ subItem.duration }}
                          </el-descriptions-item>
                          <el-descriptions-item v-if="subItem.comment">
                            <template #label><i class="el-icon-tickets"></i>处理意见</template>
                            <el-tag v-if='subItem.comment.type === "3"' type="danger" class="m-r-10">驳回</el-tag>
                            <el-text :type='subItem.comment.type === "3"? "danger": "info" '>
                              {{ subItem.comment.comment }}
                            </el-text>
                          </el-descriptions-item>
                          <el-descriptions-item v-if="subItem.infoValue">
                            <template #label>提交的文件</template>
                            <el-link class="m-x-4" type="success" v-for='(f,i) in file_split(subItem.infoValue)'
                                     @click="handle_download(f)">
                              下载文件{{ i + 1 }}
                            </el-link>
                          </el-descriptions-item>
                        </el-descriptions>
                      </el-card>

                    </el-timeline-item>

                  </el-timeline>
                </view>
                <view v-else>
                  <el-card v-for="(sub,subIndex) in item.subTask" :key="subIndex">
                    <el-descriptions class="margin-top" :column="1" size="small" border>
                      <el-descriptions-item v-if="sub.assigneeName" label-class-name="my-label">
                        <template #label>办理人</template>
                        {{ sub.assigneeName }}
                      </el-descriptions-item>
                      <el-descriptions-item v-if="sub.createTime">
                        <template #label>接收时间</template>
                        {{ sub.createTime }}
                      </el-descriptions-item>
                      <el-descriptions-item v-if="sub.finishTime" label-class-name="my-label">
                        <template #label>处理时间</template>
                        {{ sub.finishTime }}
                      </el-descriptions-item>
                      <el-descriptions-item v-if="sub.duration" label-class-name="my-label">
                        <template #label>耗时</template>
                        {{ sub.duration }}
                      </el-descriptions-item>
                      <el-descriptions-item v-if="sub.comment" label-class-name="my-label">
                        <template #label>处理意见</template>
                        {{ sub.comment.comment }}
                      </el-descriptions-item>
                      <el-descriptions-item v-if="sub.infoValue">
                        <template #label>提交的文件</template>
                        <el-link class="m-x-4" type="success" v-for='(f,i) in file_split(sub.infoValue)' :href="f"
                                 target="_blank">
                          下载文件{{ i + 1 }}
                        </el-link>
                      </el-descriptions-item>
                    </el-descriptions>
                  </el-card>
                </view>

              </el-timeline-item>
            </el-timeline>
          </el-tab-pane>
        </el-tabs>
      </view>
    </base-layout>
  </base-wrapper>

  <uni-popup ref="approvePopup" style="width: 200px" borderRadius="10px 10px 10px 10px" background-color="#fff">
    <view class="m-10 w-full">
      <uni-forms ref="refApproveForm" style="max-width: 100%" :modelValue="approveForm" label-position="top"
                 :rules="{
                    comment: {
                      rules: [{
                        required: true,
                        errorMessage: '处理意见不能为空'
                      }]
                    }
                 }">
        <uni-forms-item label="处理意见" required name="comment">
          <uni-easyinput type="textarea" v-model="approveForm.comment" placeholder="请输入处理意见"/>
        </uni-forms-item>
      </uni-forms>
      <view class="flex-center-center">
        <button style="color:#ffffff;backgroundColor:#e87658;borderColor:#cb6455"
                v-if="approveForm.show_type === 1"
                size="mini" @click="submitApproveStopForm">驳回关闭
        </button>
        <button style="color:#ffffff;backgroundColor:#e87658;borderColor:#cb6455"
                v-if="approveForm.show_type === 2"
                size="mini" @click="submitApproveRejectForm">驳 回
        </button>
        <button style="color:#ffffff;backgroundColor:#989d98;borderColor:#6b736b" size="mini"
                @click="proxy.$refs.approvePopup.close();">取 消
        </button>
        <button type="primary" size="mini" @click="submitApproveForm">同 意</button>
      </view>
    </view>

  </uni-popup>

  <uni-popup ref="submitPopup" borderRadius="10px 10px 10px 10px" background-color="#fff">
    <view class="m-10 w-full">
      <view>
        <fui-text text='正在查看的环节：指定输出物提交人' type="black" size="30"></fui-text>
      </view>
      <view class="m-t-20">
        <fui-text text="开始时间："></fui-text>
        <fui-text :text="baseInfo.changeStartTime"></fui-text>
      </view>

      <view class="m-t-20">
        <fui-text text="处理状态："></fui-text>
        <uni-tag text='处理中' size="small" type='warning'/>
      </view>
      <view class="m-t-20">
        <uni-forms ref="refSubmitForm" style="max-width: 100%" :modelValue="submitForm" label-position="top"
                   :rules="{
                    comment: {
                      rules: [{
                        required: true,
                        errorMessage: '处理意见不能为空'
                      }]
                    },
                    checkSubmits: {
                      rules: [{
                        required: true,
                        errorMessage: '提交物不能为空'
                      }]
                    }
                 }">
          <uni-forms-item label="提交物" required name="checkSubmits">
            <el-checkbox-group v-model="submitForm.checkSubmits">
              <div class="flex-wrap">
                <div class="flex-start-center m-x-10" style="width: 200px"
                     v-for="(item,index ) in submitListRecord"
                     :key="index">
                  <el-checkbox :label="item.submissionName" :value="item.submissionName"/>
                  <view class="m-l-6" v-show="submitForm.checkSubmits.includes(item.submissionName)">
                    <el-text style="font-size: 12px; font-weight: 600; cursor: pointer;"
                             v-if="alreadySubmitPerson.hasOwnProperty(item.submissionName)"
                             @click="chooseSubmitPerson(item.submissionName)">
                      {{ alreadySubmitPerson[item.submissionName].text }}
                    </el-text>
                    <el-button v-else
                               type="warning" size="small" link
                               @click="chooseSubmitPerson(item.submissionName)">请设置提交人
                    </el-button>
                  </view>

                </div>
              </div>
            </el-checkbox-group>
          </uni-forms-item>
          <uni-forms-item label="处理意见" required name="comment">
            <uni-easyinput type="textarea" v-model="submitForm.comment" placeholder="请输入处理意见"/>
          </uni-forms-item>
        </uni-forms>
      </view>
      <view class="flex-center-center">
        <button type="primary" size="mini" @click="proxy.$refs.submitPopup.close();">取 消</button>
        <button type="primary" size="mini" @click="submitSubmitForm">确 定</button>
      </view>
    </view>
  </uni-popup>

  <uni-popup ref="submitPersonPopup" borderRadius="10px 10px 10px 10px" background-color="#fff">
    <el-radio-group class="m-20" v-model="submitCheckForm.choosePerson">
      <el-radio v-for="(item,index ) in submitPersons"
                :key="index" :value="item.value"> {{ item.label }}
      </el-radio>
    </el-radio-group>
    <view class="flex-center-center">
      <button type="primary" size="mini" @click="submitSubmitPersonForm">确 定</button>
    </view>
  </uni-popup>

  <uni-popup ref="submitUploadPopup" borderRadius="10px 10px 10px 10px"
             background-color="#fff">
    <view class="m-10 w-full">

      <uni-forms ref="refSubmitUploadForm" style="max-width: 100%" label-width="100px" :modelValue="submitUploadForm"
                 label-position="top"
                 :rules="{
                    comment: {
                      rules: [{
                        required: true,
                        errorMessage: '处理意见不能为空'
                      }]
                    },
                    fileUrl: {
                      rules: [{
                        required: true,
                        errorMessage: '提交物不能为空'
                      }]
                    }
                 }">
        <uni-forms-item label="上传提交物" required name="fileUrl">
          <base-upload v-model="submitUploadForm.fileUrl" :limit="3"/>
        </uni-forms-item>
        <uni-forms-item label="处理意见" required name="comment">
          <uni-easyinput type="textarea" v-model="submitUploadForm.comment" placeholder="请输入处理意见"/>
        </uni-forms-item>
      </uni-forms>

      <view class="flex-center-center">
        <button type="primary" size="mini" @click="proxy.$refs.submitUploadPopup.close();">取 消</button>
        <button type="primary" size="mini" @click="submitSubmitUploadForm">确 定</button>
      </view>
    </view>
  </uni-popup>

  <uni-popup ref="submitLastPopup" borderRadius="10px 10px 10px 10px"
             background-color="#fff">
    <view class="m-10 w-full">

      <uni-forms ref="refSubmitLastForm" style="max-width: 100%" :modelValue="submitLastForm" label-width="100px"
                 label-position="top"
                 :rules="{
                    comment: {
                      rules: [{
                        required: true,
                        errorMessage: '处理意见不能为空'
                      }]
                    },
                    checkFileUrl: {
                      rules: [{
                        required: true,
                        errorMessage: '提交物不能为空'
                      }]
                    }
                 }">
        <uni-forms-item label="上传提交物" required name="checkFileUrl">
          <base-upload v-model="submitLastForm.checkFileUrl" :limit="3"/>
        </uni-forms-item>
        <uni-forms-item label="处理意见" required name="comment">
          <uni-easyinput type="textarea" v-model="submitLastForm.comment" placeholder="请输入处理意见"/>
        </uni-forms-item>
      </uni-forms>

      <view class="flex-center-center">
        <button type="primary" size="mini" @click="proxy.$refs.submitLastPopup.close();">取 消</button>
        <button type="primary" size="mini" @click="submitLastFormAction">确 定</button>
      </view>
    </view>
  </uni-popup>

</template>

<script setup>
import {onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();
const activeName = ref('1')
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


const baseInfo = ref({});
const flowRecordList = ref([])

onMounted(async () => {
  let baseRes = await proxy.$api.change_manager.get(allParams.value.changeId);
  if (baseRes.code === 0) {
    baseInfo.value = baseRes.data;
  }
  handle_load_records();
});

async function handle_load_records() {
  let params = {
    procInsId: allParams.value.procInsId
  }
  uni.showLoading({
    title: '加载中'
  });
  let flowRes = await proxy.$api.change_manager.changeRecords(params);
  if (flowRes.code === 0) {
    flowRecordList.value = flowRes.data;
  }
  uni.hideLoading();
}

function setColor(val) {
  if (val) {
    return "#2bc418";
  } else {
    return "#e58276";
  }
}

const approveForm = ref({});

const submitForm = ref({});
const submitListRecord = ref([]);
const submitPersons = ref([]);

const submitCheckForm = ref({});
const alreadySubmitPerson = ref({});

const submitUploadForm = ref({});

const submitLastForm = ref({});

function handle_process(item) {

  if (allParams.value.currentUser.userId !== item.assigneeId) {
    console.log('==========', '不是本人操作')
    return;
  }

  if (!item.finishTime) {

    if (item.taskName === '审批变更') {
      approveForm.value.taskId = item.taskId;
      approveForm.value.procInsId = allParams.value.procInsId;
      approveForm.value.instanceId = allParams.value.procInsId;
      approveForm.value.comment = '';
      approveForm.value.show_type = 1;
      proxy.$refs.approvePopup.open();

    } else if (item.taskName === '指定输出提交物') {

      alreadySubmitPerson.value = {};

      submitForm.value.checkSubmits = []
      submitForm.value.taskId = item.taskId;
      submitForm.value.procInsId = allParams.value.procInsId;
      submitForm.value.instanceId = allParams.value.procInsId;
      submitForm.value.comment = '';
      submitForm.value.choosePerson = {};

      proxy.$refs.submitPopup.open();

      proxy.$api.submission_material.list({}).then(res => {
        submitListRecord.value = res.data;
      })

    } else {

      submitLastForm.value.taskId = item.taskId;
      submitLastForm.value.procInsId = allParams.value.procInsId;
      submitLastForm.value.instanceId = item.procInsId;
      submitLastForm.value.comment = '';
      submitLastForm.value.checkFileUrl = '';
      submitLastForm.value.taskName = "验收报告";
      submitLastForm.value.variables = {};

      console.log('=====', submitLastForm.value)
      proxy.$refs.submitLastPopup.open();
    }
  }
}

function handle_process_sub_task(item, parentTaskName) {

  if (allParams.value.currentUser.userId !== item.assigneeId) {
    console.log('==========', '不是本人操作')
    return;
  }

  if (!item.finishTime) {
    if (parentTaskName === '上传提交物') {

      if (item.taskName === '审核') {

        approveForm.value.taskId = item.taskId;
        approveForm.value.procInsId = item.procInsId;
        approveForm.value.instanceId = item.procInsId;
        approveForm.value.comment = '';
        approveForm.value.show_type = 2;
        proxy.$refs.approvePopup.open();
        // console.log('=====', approveForm.value)

      } else {
        submitUploadForm.value.taskId = item.taskId;
        submitUploadForm.value.procInsId = item.procInsId;
        submitUploadForm.value.instanceId = item.procInsId;
        submitUploadForm.value.comment = '';
        submitUploadForm.value.fileUrl = '';
        submitUploadForm.value.taskName = item.taskName;
        submitUploadForm.value.variables = {};

        proxy.$refs.submitUploadPopup.open();
      }
    }
  }
}

async function submitApproveForm() {
  proxy.$refs.refApproveForm.validate().then(async (r) => {
    uni.showLoading({
      title: '处理中'
    });

    let res = await proxy.$api.change_manager.completeApprove(approveForm.value);

    uni.hideLoading();

    if (res.code === 0) {
      proxy.$refs.approvePopup.close();
      handle_load_records();
    }
  });
}

async function submitSubmitForm() {
  proxy.$refs.refSubmitForm.validate().then(async (r) => {
    submitForm.value.choosePerson = {...alreadySubmitPerson.value}
    console.log('====', submitForm.value)

    uni.showLoading({
      title: '处理中'
    });

    let res = await proxy.$api.change_manager.completeSubmit(submitForm.value)
    console.log(res);

    uni.hideLoading();

    if (res.code === 0) {
      proxy.$refs.submitPopup.close();
      handle_load_records();
    }
  });
}

function chooseSubmitPerson(submissionName) {
  submitCheckForm.value = {};
  submitCheckForm.value.chooseKey = submissionName;
  submitCheckForm.value.choosePerson = null;
  submitCheckForm.value.choosePersonObject = null;

  proxy.$refs.submitPersonPopup.open();
  proxy.$api.userinfo.select_list({}).then(res => {
    submitPersons.value = res.data;
  })
}

function submitSubmitPersonForm() {
  proxy.$refs.submitPersonPopup.close();
  if (submitCheckForm.value.choosePerson) {
    submitCheckForm.value.choosePersonObject =
        submitPersons.value.find(item => item.value === submitCheckForm.value.choosePerson);

    alreadySubmitPerson.value[submitCheckForm.value.chooseKey] = {...submitCheckForm.value.choosePersonObject}
    console.log('====11', alreadySubmitPerson.value);
  }
}


async function submitSubmitUploadForm() {
  proxy.$refs.refSubmitUploadForm.validate().then(async (r) => {
    submitUploadForm.value.variables[submitUploadForm.value.taskName] = submitUploadForm.value.fileUrl;
    console.log(submitUploadForm.value)

    uni.showLoading({
      title: '处理中'
    });

    let res = await proxy.$api.change_manager.completeSubmitUpload(submitUploadForm.value)

    uni.hideLoading();

    if (res.code === 0) {
      proxy.$refs.submitUploadPopup.close();
      handle_load_records();
    }
  });

}


async function submitLastFormAction() {

  proxy.$refs.refSubmitLastForm.validate().then(async (r) => {
    submitLastForm.value.variables[submitLastForm.value.taskName] = submitLastForm.value.checkFileUrl;
    console.log(submitLastForm.value)

    uni.showLoading({
      title: '处理中'
    });

    let res = await proxy.$api.change_manager.completeCheckFile(submitLastForm.value)
    uni.hideLoading();

    if (res.code === 0) {
      proxy.$refs.submitLastPopup.close();
      handle_load_records();
    }
  });
}

async function submitApproveStopForm() {

  proxy.$refs.refApproveForm.validate().then(async (r) => {
    uni.showLoading({
      title: '处理中'
    });

    let res = await proxy.$api.change_manager.stopProcess(approveForm.value);

    uni.hideLoading();

    if (res.code === 0) {
      proxy.$refs.approvePopup.close();
      proxy.$navigate('/pages/change/index', true);
    }
  });
}

async function submitApproveRejectForm() {
  proxy.$refs.refApproveForm.validate().then(async (r) => {

    uni.showLoading({
      title: '处理中'
    });

    let res = await proxy.$api.change_manager.rejectProcess(approveForm.value);

    uni.hideLoading();

    if (res.code === 0) {
      proxy.$refs.approvePopup.close();
      handle_load_records();
    }

  });
}

const flowRecordAllList = ref([])

async function handleClick(tab, event) {

  if (tab.props.name === '2') {
    let params = {
      procInsId: allParams.value.procInsId
    }
    uni.showLoading({
      title: '加载中'
    });
    let flowRes = await proxy.$api.change_manager.changeRecordsAll(params);
    if (flowRes.code === 0) {
      flowRecordAllList.value = flowRes.data;
    }
    uni.hideLoading();
  } else {
    handle_load_records();
  }
}

function file_split(val) {
  return val.split(",");
}

function handle_download(fileName) {
  console.log(fileName);
  proxy.$download.name(fileName);
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
  //height: 60%;
  max-width: 80%;
}

.example-body {
  padding: 4px !important;
  border-radius: 10px;
  width: 100% !important;
  /* margin-bottom: 20px; */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
}

.uni-file-picker {
  /*border: 2px dashed #007aff; !* 添加虚线边框并使用主题颜色 *!*/
  padding: 15px;
  border-radius: 8px;
  background-color: #fff; /* 确保背景为白色 */
  transition: border-color 0.3s ease; /* 鼠标悬停时边框颜色渐变 */
}

.uni-file-picker:hover {
  border-color: #005bb5; /* 鼠标悬停时边框颜色变为深蓝 */
}

::v-deep .uni-file-picker__files {
  align-items: center !important;
}

::v-deep .pass-node .el-timeline-item__tail {
  border-color: #cacbc9;
  display: block !important;
}

::v-deep .pass-node:last-child .el-timeline-item__tail {
  display: none !important;
}

.reject {
  background-color: red !important;
}

</style>