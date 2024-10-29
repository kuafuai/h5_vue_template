<template>
  <base-wrapper>
    <!-- <base-list-header nickname="提交物管理" description="提交物设置"></base-list-header> -->
    <view
      style="width:100%;background:white;padding:25px 15px;box-sizing:border-box;display:flex;align-items: center;justify-content: space-between;">
      <view style="color:#6569F7;font-weight:600;font-size:18px">
        提交物管理
        <uni-breadcrumb separator="/">
          <uni-breadcrumb-item>
            变更管理平台
          </uni-breadcrumb-item>
          <uni-breadcrumb-item>
            提交物管理
          </uni-breadcrumb-item>
        </uni-breadcrumb>
      </view>
      <view
        style="width: 40px; height: 40px;line-height:40px;border-radius:30px;background:rgb(101,105,247);text-align: center;color:white;">
        {{ userInfo.userName }}
      </view>
    </view>
    <base-layout class="m-t-20 p-t-20" display="flex" direction="c">
      <view class="w-full flex-between-start">
        <view style="width: 200px">
          <fui-button btnSize="small" @click="handle_add_sub" radius="96rpx">新建提交物</fui-button>
        </view>
        <base-search firstSearchData="submissionName" :searchData="base_search"
                     firstSearchPlaceholder="请输入要搜索的提交物名称"
                     :showMoreSearch="false"
                     @refreshTableData="search_click"
        ></base-search>
      </view>

      <view class="w-full m-y-20">
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
// 用户信息
const userInfo = ref('')
let strUserInfo = uni.getStorageSync("userInfo") || null;
    if (strUserInfo != null) {
      userInfo.value = JSON.parse(strUserInfo);
    }
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