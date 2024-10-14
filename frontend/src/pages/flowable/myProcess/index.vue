<template>
  <base-wrapper>
    <base-list-header nickname="我的流程" description="我的流程"></base-list-header>
    <base-layout class="m-t-20 p-t-20" display="flex" direction="c">
      <base-search firstSearchData="name" :searchData="base_search"
                   firstSearchPlaceholder="请输入要搜索的流程名称"
                   @refreshTableData="search_click"
      ></base-search>
      <view class="w-full m-b-20">
        <base-table ref="refTableUserInfo" class="m-r-20" api="flowable_task.myProcess" :columns="[
            { prop: 'name', label: '流程编号', width: '10' },
            { prop: 'name', label: '流程名称', width: '10' },
            { prop: 'name', label: '流程分类', width: '10' },
            { prop: 'name', label: '流程版本', width: '10' },

            { prop: 'name', label: '提交时间', width: '10' },
            { prop: 'name', label: '流程状态', width: '10' },

            { prop: 'name', label: '耗时', width: '10' },
            { prop: 'name', label: '当前节点', width: '10' },
            { prop: 'name', label: '办理人', width: '10' },
            { prop: 'name', label: '操作', width: '60' },
        ]">
          <template #default="{item}">
            <uni-td align="center">
              <fui-text :text="item.procInsId" type="primary"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.procDefName" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.category" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <uni-tag :text="item.procDefVersion" type="success"/>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.createTime" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <uni-tag v-if="item.finishTime === null" text="进行中" type="warning"/>
              <uni-tag v-if="item.finishTime !== null" text="已完成" type="success"/>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.duration" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.taskName" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.assigneeName" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <button size="mini" type="default" class="m-r-10"
                      style="color:#ffffff;backgroundColor:#63b463;borderColor:#1AAD19"
                      @click="handle_task_info(item)">详情</button>
              <button size="mini" type="default" class="m-r-10"
                      style="color:#ffffff;backgroundColor:#63b463;borderColor:#1AAD19"
              @click="handle_stop(item)">取消申请</button>
              <button size="mini" type="default" class="m-r-10"
                      style="color:#ffffff;backgroundColor:#ce6b87;borderColor:#1AAD19"
              @click="handle_delete(item)">删除</button>
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

function handle_task_info(item) {
  let url = '/pages/flowable/myProcess/detail?deployId=' + item.deployId + "&procInsId=" + item.procInsId + "&taskId=" + item.taskId;
  proxy.$navigate(url)
}

async function handle_stop(item){
  const params = {
    instanceId: item.procInsId
  }
  let res = await proxy.$api.flowable_task.stopProcess(params);
  proxy.$refs.refTableUserInfo.refresh({});
}

async  function handle_delete(item){
  const ids = item.procInsId;
  let res = await  proxy.$api.flowable_instance.delete(ids);
  proxy.$refs.refTableUserInfo.refresh({});
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