<template>
  <base-wrapper>
    <!-- <base-list-header nickname="表单配置" description="审批流表单配置"></base-list-header> -->
    <view style="width:100%;background:white;padding:25px 15px;color:#6569F7;font-weight:600;font-size:18px">
      表单配置
    </view>
    <base-layout class="m-t-20 p-t-20" display="flex" direction="c">
      <view class="w-full flex-between-start">
        <view style="width: 200px">
          <fui-button v-if="false" btnSize="small" @click="handle_add_form" radius="96rpx">新建表单</fui-button>
        </view>
        <base-search firstSearchData="formName" :searchData="base_search"
                     firstSearchPlaceholder="请输入要搜索的表单名称"
                     :showMoreSearch="false"
                     @refreshTableData="search_click"
        ></base-search>
      </view>

      <view class="w-full m-y-20">
        <base-table ref="refTableUserInfo" class="m-r-20" api="form_setting.page" :columns="[
            { prop: 'name', label: '表单名称', width: '20' },
            { prop: 'name', label: '说明', width: '100' },
        ]">
          <template #default="{item}">
            <uni-td align="center">
              <fui-text :text="item.formName" type="primary" @click="jump_edit(item)"></fui-text>
            </uni-td>
            <uni-td align="center">{{ item.formDescription }}</uni-td>
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

function handle_add_form() {
  proxy.$navigate("/pages/setForm/index")
}

function jump_edit(item) {
  // let url = '/pages/approve/detail?approveNodeId=' + item.approveNodeId;
  // proxy.$navigate(url)
  proxy.$navigate("/pages/setForm/index?formId=" + item.formId)
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