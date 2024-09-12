<template>
  <view>
    <view v-if="description != null && description != {}" class="margin-top" :column="4">
      <view class="all">
        全部信息：
      </view>
      <view class="all_content">
        <slot name="description-items" :description="description">

        </slot>
      </view>

    </view>

    <view v-else class="list_box">
      <view class="nodata">
        <img src="../static/noData.png" alt="">
        <view class="noText">暂无数据～</view>
      </view>
    </view>
  </view>

</template>

<script setup>
import { getCurrentInstance } from "vue"

const { proxy } = getCurrentInstance();
import { defineProps, ref } from 'vue';
import { onLoad } from "@dcloudio/uni-app";

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
  //height: 100%;
  //height: 92.3vh;
  padding: 30px 15px;
  box-sizing: border-box;
  .all_content {
    ::v-deep .uni-section:first-child {
      border-radius: 10px 10px 0px 0px !important;
    }

    ::v-deep .uni-section:last-child {
      border-radius: 0 0 10px 10px;
    }

    ::v-deep .uni-section__content-title {
      font-size: 12px !important;
      color: rgba(56, 56, 56, 1) !important;
    }

    ::v-deep .uni-section .uni-section-header__content-sub {
      display: flex !important;
      justify-content: flex-start !important;
      font-size: 12px !important;
      color: rgba(166, 166, 166, 1) !important;
    }

  }

  .all {
    height: 66 rpx;
    line-height: 66 rpx;
    margin-bottom: 10px;
    font-size: 12px;
    color: rgba(128, 128, 128, 1)
  }
}



//.list_box {
//  width: 80%;
//  margin: 20px auto;
//  display: flex;
//  justify-content: center; /* 水平居中 */
//  align-items: center; /* 垂直居中 */
//  //height: 100vh; /* 高度设置为视口高度，确保内容在整个页面居中 */
//  flex-direction: column; /* 垂直排列图片和文字 */
//  //margin-top: 20px;
//}
//
//.nodata {
//  width: 100%;
//  display: flex;
//  flex-direction: column;
//  align-items: center;
//  justify-content: center;
//}
//.nodata img{
//  width: 80%;
//}
//
//.noText {
//  margin-top: 10px;
//  font-size: 16px;
//  color: #666;
//}

.list_box {
  width: 100%;
  height: 100%;
  //background: rgb(245, 247, 250);
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  position: static;
  z-index: 8;

  .nodata {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 25%;
    font-size: 24px;

    .noText {
      margin-top: 45px;
    }
  }

  .list {
    // flex: 1;
    margin: 30 rpx 0;
    box-sizing: border-box;
    border-radius: 15 rpx;
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
</style>