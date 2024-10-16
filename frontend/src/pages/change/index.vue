<template>
  <base-wrapper>
    <base-list-header nickname="变更管理" description="变更管理"></base-list-header>
    <base-layout class="m-t-20 p-t-20" display="flex" direction="c">
      <view class="w-full flex-between-start">
        <view style="width: 200px">
          <fui-button btnSize="small" @click="handle_change" radius="96rpx">发起变更</fui-button>
        </view>
        <base-search firstSearchData="approveNodeName" :searchData="base_search"
                     firstSearchPlaceholder="请输入要搜索的环节名称"
                     @refreshTableData="search_click"
        ></base-search>
      </view>

      <view class="w-full m-b-20">
        <base-table ref="refTableUserInfo" class="m-r-20" api="change_manager.page" :columns="[
            { prop: 'name', label: '变更标题', width: '40' },
            { prop: 'name', label: '客户名称', width: '40' },
            { prop: 'name', label: '项目名称', width: '40' },
            { prop: 'name', label: '产品名称', width: '40' },
            { prop: 'name', label: '发起时间', width: '40' },
            { prop: 'name', label: '断点时间', width: '40' },
            { prop: 'name', label: '发起人', width: '40' },
        ]">
          <template #default="{item}">
            <uni-td align="center">
              <fui-text :text="item.changeTitle" @click="jump_edit(item)" type="primary"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.changeCustomer" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.changeProjectName" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.changeProductName" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.changeStartTime" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.changeEndTime" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.changePersonName" :size="28"></fui-text>
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

function handle_change() {
  let url = '/pages/change/change'
  proxy.$navigate(url)
}

function jump_edit(item) {
  let url = '/pages/change/info?changeId=' + item.changeId + "&procInsId=" + item.flowableInstanceId;
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