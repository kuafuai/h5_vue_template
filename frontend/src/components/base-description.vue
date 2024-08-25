<template>
  <view v-if="description!=null && description!={}" class="margin-top" :column="4">
    <view class="all">
      全部信息：
    </view>
    <slot name="description-items" :description="description">

    </slot>
  </view>
  <view v-else class="list_box">
    <view class="nodata">
      <img src="../static/noData.png" alt="">
      <view class="noText">暂无数据～</view>
    </view>
  </view>
</template>

<script setup>
import {getCurrentInstance} from "vue"

const {proxy} = getCurrentInstance();
import {defineProps, ref} from 'vue';
import {onLoad} from "@dcloudio/uni-app";

const props = defineProps({
  api: {
    type: String,
    default: '',
  },
  params: {
    type: Object, default: () => {
    }
  },
});
const description = ref({})
const refresh = async () => {
  console.log()
  var response = await props.api.split('.').reduce((acc, item) => acc[item], proxy.$api)(props.params);
  description.value = response.data.records[0];
}

onLoad(async () => {
  refresh()
});

// }
defineExpose({
  refresh
})
// export default {
//   name: "BaseDescription"
// }
</script>

<style scoped lang="scss">
.margin-top {
  width: 100%;
  height: 100%;
  height: 92.3vh;
  padding: 30px 25px;
  box-sizing: border-box;

  .all {
    background: white;
    height: 66 rpx;
    line-height: 66 rpx;
    padding-left: 10px;
    border-radius: 10px 10px 0 0;
    border-bottom: 1px solid #eee;
  }

  ::v-deep .uni-section:first-child {

  }

  ::v-deep .uni-section:last-child {
    border-radius: 0 0 10px 10px;
  }
  .list_box {
    width: 100%;
    height: 100%;
    background: rgb(245, 247, 250);
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    position: static;
    z-index: 8;

    .nodata{
      display: flex;
      flex-direction: column;
      align-items: center;
      margin-top: 25%;
      font-size: 24px;
      .noText{
        margin-top: 45px;
      }
    }

    .list {
      // flex: 1;
      margin: 30rpx 0;
      box-sizing: border-box;
      border-radius: 15rpx;
      background: white;
      color: #fff !important;

      ::v-deep .uni-list-item {
        position: static;
        background: white !important;
      }
    }

    .flex-end-center {
      width: 100%;
    }
  }
}
</style>