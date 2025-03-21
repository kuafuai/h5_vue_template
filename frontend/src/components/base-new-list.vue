<template>

  <view v-if="pageRes.records.length" class="h-full w-full overflow-y-auto">
    <view v-for="(item, index) in pageRes.records" :key="index">
      <uni-card :is-shadow="false" @click="click_ok(item)" :border="false" style="border-radius: 20rpx;"
                :title="getTitle(item)"
                :thumbnail="getThumbnail(item)">
        <base-layout display="flex" direction="c">
          <slot name="default" :item="item" :index="index + 1">
            <!--            old {{ index }} : {{ item }}-->
          </slot>
        </base-layout>

        <template #actions>
          <base-layout display="flex" x="end">
            <slot name="op" :item="item"/>
          </base-layout>
        </template>

      </uni-card>
    </view>

    <view v-if="isPage" class="flex-end-center m-t-10 m-r-10">
      <fui-pagination style="width:100%" :total="pageRes.total" :pageSize="pageParams.pageSize"
                      :current="pageParams.current"
                      @change="handleCurrentChange" :pageType="2"></fui-pagination>
    </view>

  </view>

  <view v-else class="list_no_data">
    <view class="nodata">
      <img :src="get_resource_url('static/noData.png')" style="width: 138pt;height: 93pt;" alt=""/>
      <view class="noText">{{ $t('list.empty_text') }}</view>
    </view>
  </view>

</template>

<script setup>
import get_resource_url from '../config/static_config';
import {onHide, onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();

const props = defineProps({
  params: {
    type: Object,
    default: () => {
    },
  },
  api: {type: String, default: ""},
  //是否分页
  isPage: {type: Boolean, default: () => false},
  query: {
    type: Object,
    default: () => {
    },
  },
  title: {type: String, required: false},
  thumbnail: {type: String, required: false},
  tableName: {
    type: String,
    default: null,
  },
  agentFieldName: {
    type: Array,
    default: [],
  },
  primaryName: {
    type: String,
    default: null,
  }
});


let isLoading = ref(true);
// 分页响应数据
let pageRes = ref({current: 1, pages: 1, size: 10, total: 0, records: []});
let pageParams = ref({current: 1, pageSize: 10});

const emits = defineEmits(["click", "check_group_change"]);
// 暴露方法
defineExpose({
  refresh,
});

onShow(()=>{
  refresh();
})

onHide(()=>{
  if (interval){
    clearInterval(interval);
  }

})

onMounted(() => {
  refresh();
});

watch(() => pageRes.value, (newVal) => {
  pageRes.value = newVal
}, {deep: true})

function refresh(query_param) {
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

  if (query_param != null && query_param != undefined) {
    for (var item in query_param) {
      pageParams.value[item] = query_param[item]
    }
  }

  getApiData();

  isLoading.value = false;

  process_ai_agent();
}


var interval=null

function startInterval() {
  if (interval) clearInterval(interval); // 避免重复启动
  interval = setInterval(async () => {
    var is_end = true;

    var tableIdList=[]
    for (let i = 0; i < props.agentFieldName.length; i++) {
      let agentFieldNameItem = props.agentFieldName[i]
      for (let j=0 ;j< pageRes.value.records.length; j++){
        if (pageRes.value.records[j][agentFieldNameItem] == null){
          tableIdList.push(pageRes.value.records[j][props.primaryName])
          is_end=false
        }
      }


      if (tableIdList.length>0){
        //  说明正在生成中,发送一次请求
        let response = await proxy.$api.ai_agent_kuafu.isEnd({
          tableName: props.tableName,
          tableIdList: tableIdList,
          agentFieldName: agentFieldNameItem
        });
        if (response.code!==0){
          clearInterval(interval)
          return
        }

        if (response.code === 0 && response.data === true) {
          refresh();
        }
      }
    }
    if (is_end) {
      // 如果全部ai字段都是不空，则结束
      clearInterval(interval);
      interval = null;
    }


  }, 8000);


}
const process_ai_agent=()=>{
  if (props.agentFieldName!=null && props.agentFieldName.length>0 && props.tableName!=null){
    startInterval()
  }
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
  return props.api.split(".").reduce((acc, item) => acc[item], proxy.$api)(
      data
  );
}

// 分页组件参数变更时触发
function handleCurrentChange(val) {
  getApiData({page: val.current, limit: pageParams.value.pageSize});
}

function handleSizeChange(val) {
  getApiData({page: pageParams.value.current, limit: val});
}

function click_ok(item) {
  console.log(item, "点击的值")
  emits("click", item);
}

function check_box(e) {
  console.log("选中", e)
  emits("check_group_change", e.detail.value)
}

function getTitle(item) {
  const field = props.title
  if (field) {
    return item[field]
  } else {
    return ''
  }
}

function getThumbnail(item) {
  const field = props.thumbnail
  if (field) {
    let t = item[field];
    if (Array.isArray(t)) {
      if (t.length > 0) {
        return t[0].url;
      } else {
        return ''
      }
    } else {
      return t;
    }
  } else {
    return ''
  }
}


</script>

<style scoped lang="scss">


.list_no_data {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  .nodata {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 40%;
    font-size: 20px;

    .noText {
      //margin-top: 18.8px;
      font-size: 0.875rem;
      color: rgba(154, 154, 154, 1);
    }
  }

}


</style>