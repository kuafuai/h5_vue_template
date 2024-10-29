<template>
  <view>
    <uni-table stripe :type="selection?'selection':''" border emptyText="暂无更多数据"
               ref="baseTableRef"
               :loading="isLoading"
               @selection-change="selectionChange">
      <uni-tr>
        <template v-for="item in columns" :key="item">
          <uni-th align="center">{{ item.label }}</uni-th>
        </template>
      </uni-tr>
      <uni-tr v-for="(item, index) in pageRes.records" :key="index">
        <slot name="default" :item="item">old {{  item }} </slot>
      </uni-tr>

    </uni-table>
    <view v-if="isPage"  class="uni-pagination-box">
      <uni-pagination
          v-if="pageRes && pageRes.total > 0"
          show-icon
          :page-size="pageParams.pageSize"
          :current="pageParams.current"
          :total="pageRes.total == null ? 0 : pageRes.total"
          @change="change"/>
    </view>
  </view>
</template>

<script setup>
import {onLoad} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();
const props = defineProps({
  selection: {type: Boolean, default: false},
  // 动态渲染的表格字段 :columns="[{ prop: 'name', label: '名称', width: '100' }]" =>  <el-table-column prop="name" label="名称" width="100" />
  columns: {type: Array, default: () => []},
  params: {
    type: Object, default: () => {
    }
  },
  api: {type: String, default: ''},
  data: {type: Array, default: () => []},
  //是否分页
  isPage: {type: Boolean, default: () => true},
});

// 暴露方法
defineExpose({
  refresh,
});

let isLoading = ref(false);
let pageParams = ref({current: 1, pageSize: 10});
// 分页响应数据
let pageRes = ref({current: 1, pages: 1, size: 10, total: 0, records: []});
let tableDataList = ref([]); // 列表响应数据

onLoad(() => {
  refresh();
});

// 刷新
function refresh() {
  isLoading.value = true;
  if (props.data && props.data.length > 0) {
    // 情况1：走父组件传值过来
    tableDataList.value = props.data;
  } else {
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
  }
  isLoading.value = false;
}

// 获取接口数据
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
    tableDataList.value = response.data;
  }

  isLoading.value = false;
}

function apiMethod(params, headers) {
  // eg: proxy.$api.sys_user.save(xx);
  let data = {...params};
  if (headers) {
    data = Object.assign(data, headers.value);
  }
  return props.api.split('.').reduce((acc, item) => acc[item], proxy.$api)(data);
}

function selectionChange(e) {
  console.log(e)
}

function change(e) {
  getApiData({page: e.current, limit: pageParams.value.pageSize});
}

</script>