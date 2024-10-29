<template>
  <base-wrapper>
    <!-- <base-list-header nickname="变更管理" description="变更管理"></base-list-header> -->
    <view style="width:100%;background:white;padding:25px 15px;color:#6569F7;font-weight:600;font-size:18px">
      变更列表
      <uni-breadcrumb separator="/">
        <uni-breadcrumb-item>
          变更管理平台
        </uni-breadcrumb-item>
        <uni-breadcrumb-item>
          变更列表
        </uni-breadcrumb-item>
      </uni-breadcrumb>
    </view>
    <base-layout class="m-t-20 p-t-20" display="flex" direction="c">
      <view class="w-full flex-between-start" style="position: relative;">
        <view style="width: 200px;position: absolute;">
          <fui-button btnSize="small" @click="handle_change" radius="96rpx">发起变更</fui-button>
        </view>
        <base-search firstSearchData="changeTitle" :searchData="base_search" firstSearchPlaceholder="请输入要搜索的变更标题"
          @refreshTableData="search_click">
          <template #collapse>
            <uni-forms-item style="width: 300px" class="m-x-10" label="客户名称:" name="changeCustomer">
              <uni-easyinput type="text" v-model="base_search.changeCustomer" placeholder="请输入客户名称"/>
            </uni-forms-item>
            <uni-forms-item style="width: 300px" class="m-x-10" label="项目名称:" name="changeProjectName">
              <uni-easyinput type="text" v-model="base_search.changeProjectName" placeholder="请输入项目名称" />
            </uni-forms-item>
          </template>
        </base-search>
      </view>

      <view class="m-b-20 overflow-x-scroll" :style="{ width: table_width+12*1 + 'px' }">
        <base-table ref="refTableUserInfo" class="m-r-20" api="change_manager.page" :columns="[
          { prop: 'name', label: '变更标题', width: '140' },
          { prop: 'name', label: '客户名称', width: '100' },
          { prop: 'name', label: '项目名称', width: '100' },
          { prop: 'name', label: '产品名称', width: '100' },
          { prop: 'name', label: '发起时间', width: '100' },
          { prop: 'name', label: '断点时间', width: '100' },
          { prop: 'name', label: '发起人', width: '60' },
          { prop: 'name', label: '状态', width: '60' },
          { prop: 'name', label: '操作', width: '150' },
        ]">
          <template #default="{ item }">
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
            <uni-td align="center">
              <uni-tag v-if="item.changeStatus === 0" text="关闭" />
              <uni-tag v-if="item.changeStatus === 1" text="进行中" type="warning" />
              <uni-tag v-if="item.changeStatus === 2" text="已完成" type="success" />
              <uni-tag v-if="item.changeStatus === 3" text="已驳回" type="error" />
            </uni-td>
            <uni-td align="center">
              <button size="mini" type="default" class="m-r-10"
                style="color:#ffffff;backgroundColor:#63b463;borderColor:#1AAD19" @click="handle_task_info(item)">详情
              </button>
              <!--              <button v-if="item.changeStatus === 1" size="mini" type="default" class="m-r-10"-->
              <!--                      style="color:#ffffff;backgroundColor:#d58867;borderColor:#ad3419"-->
              <!--                      @click="handle_stop(item)">取消申请-->
              <!--              </button>-->
            </uni-td>
          </template>
        </base-table>
      </view>
    </base-layout>
  </base-wrapper>
</template>

<script setup>
import { onResize, onShow } from "@dcloudio/uni-app";

const { proxy } = getCurrentInstance();

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

function handle_task_info(item) {
  let url = '/pages/change/info?changeId=' + item.changeId + "&procInsId=" + item.flowableInstanceId;
  proxy.$navigate(url)
}

async function handle_stop(item) {

  const params = {
    instanceId: item.flowableInstanceId
  }
  uni.showLoading({
    title: '加载中'
  });

  let flowRes = await proxy.$api.change_manager.stopProcess(params);
  if (flowRes.code === 0) {
    proxy.$refs.refTableUserInfo.refresh({});
  }
  uni.hideLoading();
}


const table_width = ref(800);

onResize(() => {
  table_width.value = window.innerWidth - 300 + 12;
})

onMounted(() => {
  table_width.value = window.innerWidth - 300 + 12;
})


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