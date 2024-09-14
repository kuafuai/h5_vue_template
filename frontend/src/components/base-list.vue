<template>
  <view v-if="pageRes.records.length" class="list_box">
    <view class="box" v-for="(item, index) in pageRes.records" :key="index">
      <view @click="click_ok(item)" class="content">
        <view class="indexBox">
          <view>{{ index + 1 }}</view>
        </view>
        <uni-list class="list" :border="false">
          <slot name="default" :item="item">
            <uni-list-item :title="item.title"> old {{ item }} </uni-list-item>
          </slot>
        </uni-list>
        <view v-show="is_click" class="imgs">
          <image src="../static/toRight.png" mode="scaleToFill" />
        </view>
      </view>
<!--      <view class="operate">-->
<!--        <view class="detail">-->
<!--          <image-->
<!--              style="width: 30rpx; height: 30rpx; margin-right: 10rpx"-->
<!--              src="../static/detail.png"-->
<!--              mode="scaleToFill"-->
<!--          />-->
<!--          详情-->
<!--        </view>-->
<!--        <view class="edit">-->
<!--          <image-->
<!--              style="width: 30rpx; height: 30rpx; margin-right: 10rpx"-->
<!--              src="../static/edit.png"-->
<!--              mode="scaleToFill"-->
<!--          />编辑-->
<!--        </view>-->
<!--        <view class="del">-->
<!--          <image-->
<!--              style="width: 30rpx; height: 30rpx; margin-right: 10rpx"-->
<!--              src="../static/del.png"-->
<!--              mode="scaleToFill"-->
<!--          />删除-->
<!--        </view>-->
<!--      </view>-->
    </view>
    <view v-if="isPage" class="flex-end-center m-t-10 m-r-10">
      <fui-pagination
          :total="pageRes.total"
          :pageSize="pageParams.pageSize"
          :current="pageParams.current"
          @change="handleCurrentChange"
          :pageType="2"
      ></fui-pagination>
    </view>
  </view>
  <view v-else class="list_box">
    <view class="nodata">
      <img src="../static/noData.png" alt="" />
      <view class="noText">暂无数据～</view>
    </view>
  </view>
</template>
<script setup>
import { getCurrentInstance, ref } from "vue";
import { onLoad } from "@dcloudio/uni-app";
const { proxy } = getCurrentInstance();

const props = defineProps({
  params: {
    type: Object,
    default: () => {},
  },
  api: { type: String, default: "" },
  //是否分页
  isPage: { type: Boolean, default: () => false },
  path: { type: String },
  is_route: { type: Boolean, default: () => false },
  query: {
    type: Object,
    default: () => {},
  },
  is_click:{
    type: Boolean,
    default: () => false,
  }
});

// let route_query = proxy.$route.query
//
// var p = props.params
// // 如果查询参数没有，那么使用路由中的参数
// if (p==null || p==undefined) {
//   if (route_query!=null){
//     for (let key in route_query){
//       props.params.params[key] = route_query[key];
//     }
//   }
//
// }
// console.log(p,route_query)

let isLoading = ref(true);
// 分页响应数据
let pageRes = ref({ current: 1, pages: 1, size: 10, total: 0, records: [] });
let pageParams = ref({ current: 1, pageSize: 10 });

const emits = defineEmits(["click"]);
// 暴露方法
defineExpose({
  refresh,
});

onLoad(() => {
  refresh();
});

// 刷新
function refresh() {
  isLoading.value = true;
  // 情况2：走api接口数据
  if (props.isPage) {
    pageRes.value = {
      current: 1,
      pages: 1,
      size: 10,
      total: 0,
      records: [],
    };
    pageParams.value.current = 1;
  }
  getApiData();

  isLoading.value = false;
}

async function getApiData(pageObj) {
  if (!props.api) {
    return;
  }

  isLoading.value = true;

  if (props.isPage) {
    // 处理分页参数
    if (pageObj) {
      // 从分页组件中拿到数据
      pageParams.value.current = pageObj.page;
      pageParams.value.pageSize = pageObj.limit;
    }

    let response = await apiMethod(props.params, pageParams);
    pageRes.value = response.data;
  } else {
    let response = await apiMethod(props.params);
    pageRes.value.records = response.data;
  }

  isLoading.value = false;
}

function apiMethod(params, headers) {
  let data = { ...params };
  if (headers) {
    data = Object.assign(data, headers.value);
  }
  console.log("params", props.params);
  console.log("data", data);
  return props.api.split(".").reduce((acc, item) => acc[item], proxy.$api)(
      data
  );
}

// 分页组件参数变更时触发
function handleCurrentChange(val) {
  console.log(val);
  getApiData({ page: val.current, limit: pageParams.value.pageSize });
}

function handleSizeChange(val) {
  getApiData({ page: pageParams.value.current, limit: val });
}

function click_ok(item) {
 emits("click", item);
}
</script>

<style lang="scss" scoped>
::v-deep .uni-list-item__content {
  flex: none;
}
::v-deep .uni-list-item__content-title {
  color: rgba(113, 142, 191, 1);
}
::v-deep .uni-list-item__extra-text {
  color: rgba(0, 0, 0, 1);
}
::v-deep .uni-list {
  width: 70%;
}
.list_box {
  width: 100%;
  //height: 100%;
  background: rgb(250, 251, 255);
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  position: static;
  //overflow: auto;
  overflow-x: hidden;
  z-index: 8;

  .box {
    background: white;
    margin-bottom: 40rpx;
  }

  .content {
    display: flex;
    margin: 30rpx 0 0 0;
    box-sizing: border-box;
    background: white;
    font-size: 16px;
    justify-content: space-between;
    ::v-deep .uni-list--border::after{
      height: 0 !important;
    }

    .indexBox {
      width: 20%;

      view {
        margin: 0 auto;
        margin-top: 40rpx;
        width: 80rpx;
        height: 80rpx;
        opacity: 1;
        border-radius: 100px;
        background: rgba(93, 95, 239, 1);
        color: white;
        line-height: 80rpx;
        text-align: center;
      }
    }
  }

  .operate {
    width: 90%;
    background: white;
    display: flex;
    border-top: 1rpx solid #ccc;
    margin: 0 auto;
    justify-content: space-between;
    padding: 30rpx 0;
    color: rgba(166, 166, 166, 1);
    font-size: 15px;

    .detail {
      color: rgba(93, 95, 239, 1);
      display: flex;
      align-items: center;
    }

    .edit {
      display: flex;
      align-items: center;
    }

    .del {
      display: flex;
      align-items: center;
    }
  }

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
  .imgs {
    display: flex;
    align-items: center;
    width: 10%;
    justify-content: center;
    image {
      width: 50rpx;
      height: 50rpx;
    }
  }
  .list:first-child {
    margin: 0rpx 0rpx 30rpx 0px;
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
</style>