<template>
  <base-wrapper>
    <base-list-header nickname="首页" description="首页统计信息"></base-list-header>
    <base-layout class="m-y-20" display="flex" x="around" :w_full="true" :h_full="true">

      <view style="width: 5%"></view>

      <view style="width: 20%;height: 100%; background-color: #FFFFFF; border-radius: 10px"
            class="flex-c-between-start">
        <view class="w-full flex-center-center" style="height: 80px;">
          <fui-icon name="piechart" class="m-r-10"></fui-icon>
          <fui-text text="数据统计"></fui-text>
        </view>
        <view class="w-full h-full">
          <button>22</button>
        </view>
        <view class="w-full flex-center-start">
          <fui-button width="80%" height="40px" size="26" @click="handle_change">+ 发起变更申请</fui-button>
        </view>
      </view>
      <view class="flex-column overflow-x-scroll"
            style="width: 60%; height: 100%; background-color: #FFFFFF; border-radius: 10px">
        <view style="height: 110px" v-for="(item,index) in todoList" :key="index">
          <uni-card>
            <template #title>
              <fui-text class="m-y-4" :text='item.changeTitle' type="black" size="36"></fui-text>
            </template>
            <view class="flex-c-start-start">
              <view>
                <uni-icons type="person" size="18"></uni-icons>
                <fui-text text="发起人：" class="m-x-6" size="28"></fui-text>
                <text>{{ item.startUserName }}</text>
              </view>
              <view class="flex-between-start w-full">
                <view>
                  <fui-icon name="warning" size="34" color="#e58276"></fui-icon>
                  <fui-text text="需要您：" class="m-x-6" size="28"></fui-text>
                  <text style="cursor: pointer" @click="handle_change_task(item)">{{ item.taskName }}</text>
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

      <view style="width: 5%"></view>
    </base-layout>
  </base-wrapper>
</template>

<script setup>
import {onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();


function handle_change() {
  let url = '/pages/change/change'
  proxy.$navigate(url, true)
}

function handle_change_task(item) {
  let url = '/pages/change/info?changeId=' + item.changeId + "&procInsId=" + item.procInsId;
  proxy.$navigate(url)
}

const todoList = ref([]);

async function getMyTodo() {
  uni.showLoading({
    title: '加载中'
  });
  let res = await proxy.$api.change_manager.myTodo();
  console.log(res);
  if (res.code === 0) {
    todoList.value = res.data;
  }
  uni.hideLoading();
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
</style>