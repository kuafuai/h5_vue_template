<template>
  <base-wrapper>
    <!-- <base-list-header nickname="首页" description="首页统计信息"></base-list-header> -->
    <view
      style="width:100%;background:white;padding:25px 15px;box-sizing:border-box;display:flex;align-items: center;justify-content: space-between;">
      <view style="color:#6569F7;font-weight:600;font-size:18px">
        首页
        <uni-breadcrumb separator="/">
          <uni-breadcrumb-item>
            变更管理平台
          </uni-breadcrumb-item>
          <uni-breadcrumb-item>
            首页
          </uni-breadcrumb-item>
        </uni-breadcrumb>
      </view>
      <view
        style="width: 40px; height: 40px;line-height:40px;border-radius:30px;background:rgb(101,105,247);text-align: center;color:white;">
        {{ userInfo.userName }}
      </view>
    </view>
    <base-layout class="m-y-20" display="flex" x="around" :w_full="true" :h_full="true"
      style="display:flex;justify-content: space-evenly;">

      <!-- <view style="width: 5%"></view> -->

      <view style="width: 22%;height: 100%; background-color: #FFFFFF; border-radius: 10px"
        class="flex-c-between-start">
        <view class="w-full flex-center-center" style="height: 80px;">
          <fui-icon name="piechart" class="m-r-10"></fui-icon>
          <fui-text text="数据统计"></fui-text>
        </view>
        <view class="w-full h-full">
          <view v-for="(item, index) in staticsList" :key="index" class="flex-c-center-center m-y-40">
            <fui-text :text="item.number" :color="setColor(item.change_status)" :size="60" :fontWeight="900"
              class="m-b-10"></fui-text>
            <fui-text v-if="item.change_status === 1" text="审批中" :size="40"></fui-text>
            <fui-text v-if="item.change_status === 2" text="已通过" :size="40"></fui-text>
            <fui-text v-if="item.change_status === 3" text="已驳回" :size="40"></fui-text>
            <fui-text v-if="item.change_status === 0" text="已关闭" :size="40"></fui-text>
          </view>
        </view>
        <view class="w-full flex-center-start" style="margin-bottom:45px;">
          <fui-button width="80%" height="49px" size="26" @click="handle_change" style="
    line-height: 49px !important;
    ">
          <view style="font-size: 1rem !important;">
            <text style="display:inline-block;font-size:1.5rem;">
              +</text> 发起变更申请
          </view>
    </fui-button>
        </view>
      </view>
      <view class="flex-c-start-start" style="width: 68%; height: 100%; background-color: #FFFFFF; border-radius: 10px">

        <view class="w-full flex-start-center" style="height: 80px;">
          <fui-icon name="info" class="m-x-10"></fui-icon>
          <fui-text :text='"您当前有 " + todoList.length + " 个任务需要处理"'></fui-text>
        </view>

        <view v-if="todoList.length > 0" class="flex-column overflow-x-scroll w-full h-full">
          <view style="height: 110px" v-for="(item, index) in todoList" :key="index">
            <uni-card>
              <template #title>
                <fui-text class="m-y-4" :text='item.changeTitle' type="black" size="36"></fui-text>
              </template>
              <view class="flex-c-start-start" @click="handle_change_task(item)">
                <view>
                  <uni-icons type="person" size="18"></uni-icons>
                  <fui-text text="发起人：" class="m-x-6" size="28"></fui-text>
                  <text>{{ item.startUserName }}</text>
                </view>
                <view class="flex-between-start w-full">
                  <view>
                    <fui-icon name="warning" size="34" color="#e58276"></fui-icon>
                    <fui-text text="需要您：" class="m-x-6" size="28"></fui-text>
                    <text style="cursor: pointer">{{ item.taskName }}</text>
                  </view>
                  <view>
                    <fui-icon name="wait" size="34"></fui-icon>
                    <text class="m-x-6">{{ item.finishTime }}</text>
                    <fui-text text="截止，已用时" class="m-x-6" size="28"></fui-text>
                    <text class="m-x-6">{{ item.duration }}</text>

                  </view>
                </view>
              </view>
            </uni-card>
          </view>
        </view>

        <view v-else class="flex-column overflow-x-scroll h-full w-full">
          <view class="nodata">
            <img src="@/static/noData.png" style="width:12.5rem;height:auto" alt="" />
            <view class="noText">暂无数据～</view>
          </view>
        </view>

      </view>

      <!-- <view style="width: 5%"></view> -->
    </base-layout>
  </base-wrapper>
</template>

<script setup>
import { onShow } from "@dcloudio/uni-app";

const { proxy } = getCurrentInstance();


function handle_change() {
  let url = '/pages/change/change'
  proxy.$navigate(url, true)
}

function handle_change_task(item) {
  let url = '/pages/change/info?changeId=' + item.changeId + "&procInsId=" + item.procInsId;
  proxy.$navigate(url)
}

const todoList = ref([]);
const staticsList = ref([]);

async function getMyTodo() {
  uni.showLoading({
    title: '加载中'
  });
  let res = await proxy.$api.change_manager.myTodo();

  if (res.code === 0) {
    todoList.value = res.data;
  }

  let resStatics = await proxy.$api.change_manager.myStatics();
  if (resStatics.code === 0) {
    staticsList.value = resStatics.data;
  }

  uni.hideLoading();
}
const userInfo = ref('')
let strUserInfo = uni.getStorageSync("userInfo") || null;
if (strUserInfo != null) {
  userInfo.value = JSON.parse(strUserInfo);
}
function setColor(val) {
  if (val === 2) {
    return "#2bc418";
  } else if (val === 1) {
    return "#0f74e8";
  } else if (val === 3) {
    return "#e58276";
  } else {
    return "#646060";
  }
}

onMounted(() => {
  getMyTodo();
});

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

.nodata {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 37%;
  font-size: 20px;
  font-weight: 500;
  color: rgba(144, 150, 178, 1);

  .noText {
    margin-top: 18.8px;
    font-size: 0.875rem;
  }
}
</style>