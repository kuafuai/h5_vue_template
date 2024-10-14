<template>
  <base-wrapper>
    <base-list-header nickname="代办任务" description="代办任务"></base-list-header>
    <base-layout class="m-t-20 p-t-20" display="flex" direction="c">
      <base-search firstSearchData="name" :searchData="base_search"
                   firstSearchPlaceholder="请输入要搜索的流程名称"
                   @refreshTableData="search_click"
      ></base-search>
      <view class="w-full m-b-20">
        <base-table ref="refTableUserInfo" class="m-r-20" api="flowable_task.todoList" :columns="[
            { prop: 'name', label: '任务编号', width: '10' },
            { prop: 'name', label: '流程名称', width: '10' },
            { prop: 'name', label: '当前节点', width: '10' },
            { prop: 'name', label: '流程版本', width: '10' },

            { prop: 'name', label: '流程发起人', width: '30' },
            { prop: 'name', label: '接收时间', width: '10' },
            { prop: 'name', label: '操作', width: '60' },
        ]">
          <template #default="{item}">
            <uni-td align="center">
              <fui-text :text="item.taskId" type="primary"></fui-text>
            </uni-td>

            <uni-td align="center">
              <fui-text :text="item.procDefName" :size="28"></fui-text>
            </uni-td>

            <uni-td align="center">
              <fui-text :text="item.taskName" :size="28"></fui-text>
            </uni-td>

            <uni-td align="center">
              <uni-tag :text="item.procDefVersion" type="success"/>
            </uni-td>

            <uni-td align="center">
              <fui-text :text="item.startUserName" :size="28"></fui-text>
            </uni-td>

            <uni-td align="center">
              <fui-text :text="item.createTime" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <button size="mini" type="default" class="m-r-10"
                      style="color:#ffffff;backgroundColor:#63b463;borderColor:#1AAD19"
                      @click="handle_task_process(item)">办理
              </button>
            </uni-td>

          </template>

        </base-table>
      </view>
    </base-layout>
  </base-wrapper>
</template>

<script setup>
import {onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();

const base_search = ref({})

function search_click(item) {
  proxy.$refs.refTableUserInfo.refresh(item);
}

function handle_task_process(item) {
  let url = '/pages/flowable/todo/detail?deployId='
      + item.deployId + "&procInsId=" + item.procInsId + "&taskId=" + item.taskId
      + "&executionId=" + item.executionId;
  proxy.$navigate(url)
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