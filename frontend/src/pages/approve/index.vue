<template>
  <base-wrapper>
    <!-- <base-list-header nickname="审批人管理" description="审批流程节点的审批人设置"></base-list-header> -->
    <view style="width:98%;background:white;padding:25px 15px;display:flex;align-items: center;justify-content: space-between;">
      <view style="color:#6569F7;font-weight:600;font-size:18px;">
        审批人管理
      </view>
      <view style="width: 40px; height: 40px;line-height:40px;border-radius:30px;background:rgb(101,105,247);text-align: center;color:white;">
            {{ userInfo.userName }}
          </view>
    </view>
    <base-layout class="m-t-20 p-t-20" display="flex" direction="c">
      <view class="w-full flex-between-start">
        <view style="width: 200px">
          <fui-button v-if="false" btnSize="small" @click="handle_add_approve" radius="96rpx">新建审批人</fui-button>
        </view>
        <base-search firstSearchData="approveNodeName" :searchData="base_search"
                     firstSearchPlaceholder="请输入要搜索的环节名称"
                     :showMoreSearch="false"
                     @refreshTableData="search_click"
        ></base-search>
      </view>

      <view class="w-full m-y-20">
        <base-table ref="refTableUserInfo" class="m-r-20" api="approvenode.page" :columns="[
            { prop: 'name', label: '环节名称', width: '20' },
            { prop: 'name', label: '审批方式', width: '40' },
            { prop: 'name', label: '审批人', width: '100' },
        ]">
          <template #default="{item}">
            <uni-td align="center">
              <fui-text :text="item.approveNodeName" type="primary" @click="jump_edit(item)"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text v-if="item.approveType === 1" text="多人会审" :size="28"></fui-text>
              <fui-text v-else text="逐级审批" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">{{ item.approveUserName }}</uni-td>
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

function handle_add_approve() {
  let url = '/pages/approve/detail';
  proxy.$navigate(url)
}

function jump_edit(item) {
  let url = '/pages/approve/detail?approveNodeId=' + item.approveNodeId;
  proxy.$navigate(url)
}
// 用户信息
const userInfo = ref('')
let strUserInfo = uni.getStorageSync("userInfo") || null;
    if (strUserInfo != null) {
      userInfo.value = JSON.parse(strUserInfo);
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