<template>
  <base-wrapper>
    <base-list-header nickname="提交物管理" description="提交物设置"></base-list-header>
    <base-layout class="m-t-20 p-t-20" display="flex" direction="c">
      <view class="w-full flex-between-start">
        <view style="width: 200px">
          <fui-button btnSize="small" @click="handle_add_sub" radius="96rpx">新建提交物</fui-button>
        </view>
        <base-search firstSearchData="submissionName" :searchData="base_search"
                     firstSearchPlaceholder="请输入要搜索的提交物名称"
                     @refreshTableData="search_click"
        ></base-search>
      </view>

      <view class="w-full m-b-20">
        <base-table ref="refTableUserInfo" class="m-r-20" api="submission_material.page" :columns="[
            { prop: 'name', label: '提交物名称', width: '20' },
            { prop: 'name', label: '审批方式', width: '40' },
            { prop: 'name', label: '审批人', width: '100' },
        ]">
          <template #default="{item}">
            <uni-td align="center">
              <fui-text :text="item.submissionName" type="primary" @click="jump_edit(item)"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text v-if="item.submissionType === 1" text="多人会审" :size="28"></fui-text>
              <fui-text v-else text="逐级审批" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">{{ item.submissionUserName }}</uni-td>
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

function handle_add_sub() {
  let url = '/pages/submission/detail';
  proxy.$navigate(url)
}

function jump_edit(item) {
  let url = '/pages/submission/detail?submissionId=' + item.submissionId;
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