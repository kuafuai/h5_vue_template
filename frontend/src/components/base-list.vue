<template>
  <view v-if="pageRes.records.length" class="list_box" >
    <uni-list
        class="list"
        v-for="(item, index) in pageRes.records"
        :key="index"
        @click="click_ok(item)"
    >
      <slot name="default" :item="item">
        <uni-list-item :title="item.title"> old {{ item }} </uni-list-item>
      </slot>
    </uni-list>

    <view v-if="isPage" class="flex-end-center m-t-10 m-r-10">
      <fui-pagination :total="pageRes.total" :pageSize="pageParams.pageSize" :current="pageParams.current" @change="handleCurrentChange" :pageType="2"></fui-pagination>
    </view>
  </view>
  <view v-else class="list_box">
    <view class="nodata">
      <img src="../static/noData.png" alt="">
      <view class="noText">暂无数据～</view>
    </view>
  </view>

</template>
<script setup>
import {getCurrentInstance, ref} from "vue"
import {onLoad} from "@dcloudio/uni-app";
const {proxy} = getCurrentInstance();

const props = defineProps({
  params: {
    type: Object, default: () => {
    }
  },
  api: {type: String, default: ''},
  //是否分页
  isPage: {type: Boolean, default: () => false},
  path: {type: String},
  is_route: {type: Boolean, default: () => false},
  query: {
    type: Object, default: () => {
    }
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
let pageRes = ref({current: 1, pages: 1, size: 10, total: 0, records: []});
let pageParams = ref({current: 1, pageSize: 10});

const emits = defineEmits(['click']);

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
  let data = {...params};
  if (headers) {
    data = Object.assign(data, headers.value);
  }
  console.log("params", props.params)
  console.log("data", data)
  return props.api.split('.').reduce((acc, item) => acc[item], proxy.$api)(data);
}

// 分页组件参数变更时触发
function handleCurrentChange(val) {
  console.log(val)
  getApiData({page: val.current, limit: pageParams.value.pageSize});
}

function handleSizeChange(val) {
  getApiData({page: pageParams.value.current, limit: val});
}

function click_ok(item) {
  emits("click", item)


}

</script>

<style lang="scss" scoped>
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


</style>
