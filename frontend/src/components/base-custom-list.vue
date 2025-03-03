<template>
  <view v-if="pageRes.records.length" :class="layout">
    <view v-for="(item, index) in pageRes.records" :key="index" style="border-radius: 20rpx;background-color: rgba(255,255,255,1);box-sizing:border-box;margin:10rpx 20rpx;">
      <uni-card :is-shadow="false" is-full @click="click_ok(item)" style="border-radius: 20rpx;">
        <slot name="default" :item="item" :index="index + 1">
          old {{ index }} : {{ item }}
        </slot>
      </uni-card>
    </view>
  </view>

  <view v-else class="list_box">
    <view class="nodata">
      <img src="../static/noData.png" style="width: 12.5rem;height: 8.5rem;" alt=""/>
      <view class="noText">{{ $t('list.empty_text') }}～</view>
    </view>
  </view>

</template>

<script setup>
const {proxy} = getCurrentInstance();
const props = defineProps({
  params: {
    type: Object, default: () => {
    }
  },
  api: {type: String, default: ""},
  isPage: {type: Boolean, default: () => false},
  direction: {type: String, default: 'vertical'},
});

const layout = ref('flex-column')

watch(
    () => props.direction,
    (newVal, oldVal) => {
      if (newVal) {
        refresh_direction();
      }
    },
    {deep: true}
);

function refresh_direction() {
  if (props.direction === 'vertical') {
    layout.value = 'flex-column';
  } else {
    layout.value = 'flex-wrap';
  }
}

// 分页响应数据
let pageRes = ref({current: 1, pages: 1, size: 10, total: 0, records: []});
let pageParams = ref({current: 1, pageSize: 10});
let searchParams = ref({})

const emits = defineEmits(["click"]);
// 暴露方法
defineExpose({
  refresh,
});

onMounted(() => {
  refresh_direction();
  refresh({});
});


function refresh(search) {
  searchParams.value = {}
  searchParams.value = {...search}

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

async function getApiData(pageObj) {
  if (!props.api) {
    return;
  }

  let api_search_params = {...props.params, ...searchParams.value}

  if (props.isPage) {
    // 处理分页参数
    if (pageObj) {
      // 从分页组件中拿到数据
      pageParams.value.current = pageObj.page;
      pageParams.value.pageSize = pageObj.limit;
    }
    let response = await apiMethod(api_search_params, pageParams);
    pageRes.value = response.data;
  } else {
    let response = await apiMethod(api_search_params);
    pageRes.value.records = response.data;
  }
}

function apiMethod(params, headers) {
  // eg: proxy.$api.sys_user.save(xx);
  let data = {...params};
  if (headers) {
    data = Object.assign(data, headers.value);
  }

  return props.api.split('.').reduce((acc, item) => acc[item], proxy.$api)(data);
}

function click_ok(item) {
  console.log(item, "点击的值")
  emits("click", item);
}

</script>

<style lang="scss" scoped>
::v-deep .uni-card__content,.uni-card{
  padding: 0px !important;
}

.list_box {
  width: 100%;
  background: rgb(245, 247, 250);
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  position: static;
  overflow-x: hidden;

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
}

</style>