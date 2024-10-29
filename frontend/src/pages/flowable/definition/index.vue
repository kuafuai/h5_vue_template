<template>
  <base-wrapper>
    <base-list-header nickname="流程定义管理" description="流程定义管理"></base-list-header>
    <base-layout class="m-t-20 p-t-20" display="flex" direction="c">
      <fui-button size="small" @click="handle_add">添 加</fui-button>
      <base-search firstSearchData="name" :searchData="base_search"
                   firstSearchPlaceholder="请输入要搜索的流程名称"
                   @refreshTableData="search_click"
      ></base-search>
      <view class="w-full m-b-20">
        <base-table ref="refTableUserInfo" class="m-r-20" api="flowable_definition.page" :columns="[
            { prop: 'name', label: '流程编号', width: '100' },
            { prop: 'name', label: '流程标识', width: '250' },
            { prop: 'name', label: '流程分类', width: '100' },
            { prop: 'name', label: '流程名称', width: '-' },
            { prop: 'name', label: '业务表单', width: '140' },
            { prop: 'name', label: '流程版本', width: '80' },
            { prop: 'name', label: '状态', width: '80' },
            { prop: 'name', label: '部署时间', width: '150' },
            { prop: 'name', label: '操作', width: '-' },
        ]">
          <template #default="{item}">
            <uni-td align="center">
              <fui-text :text="item.deploymentId" type="primary"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.flowKey" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.category" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.name" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text v-if="item.formId !== null" :text="item.formName" :size="28"></fui-text>
              <fui-text v-else text="暂无表单" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <uni-tag :text="item.version" type="success"/>
            </uni-td>
            <uni-td align="center">
              <uni-tag v-if="item.suspensionState === 1" text="激活" type="success"/>
              <uni-tag v-if="item.suspensionState === 2" text="挂起" type="warning"/>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.deploymentTime" :size="24"></fui-text>
            </uni-td>
            <uni-td align="center">
              <view class="flex-center-center">
                <fui-button v-if="item.formId !== null" type="link" text="发起任务"
                            btnSize="mini" color="green" size="28"
                            @click="handle_start_task(item)"></fui-button>

              </view>

            </uni-td>
          </template>
        </base-table>
      </view>
    </base-layout>

    <uni-popup ref="popup" borderRadius="10px 10px 10px 10px" background-color="#fff">
      <view class="m-10 w-full">
        <uni-forms ref="refForm" style="max-width: 100%" :modelValue="form" label-position="top">
          <uni-forms-item label="名称">
            <uni-easyinput v-model="form.name" placeholder="请输入名称"/>
          </uni-forms-item>
          <uni-forms-item label="分类">
            <uni-easyinput v-model="form.category" placeholder="请输入分类"/>
          </uni-forms-item>
          <uni-forms-item label="内容">
            <uni-easyinput type="textarea" :maxlength="-1" v-model="form.xml" placeholder="请输入内容"/>
          </uni-forms-item>
        </uni-forms>
        <view class="flex-center-center">
          <button type="primary" size="mini" @click="proxy.$refs.popup.close();">取 消</button>
          <button type="primary" size="mini" @click="submitForm">确 定</button>
        </view>
      </view>
    </uni-popup>

  </base-wrapper>
</template>

<script setup>
import {onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();

const base_search = ref({});
const form = ref({});

function search_click(item) {
  proxy.$refs.refTableUserInfo.refresh(item);
}

function handle_start_task(item) {
  let url = '/pages/flowable/send/index?deployId=' + item.deploymentId + "&procDefId=" + item.id;
  proxy.$navigate(url)
}

function handle_add(){
  proxy.$refs.popup.open();
}

async function submitForm(){
  uni.showLoading({
    title: '处理中'
  });

  let res = await proxy.$api.flowable_definition.add(form.value);

  uni.hideLoading();

  proxy.$refs.popup.close();
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

</style>