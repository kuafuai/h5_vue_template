<template>
  <uni-data-select v-model="select_item" @change="handleInput" :disabled="disabled" :clear="clear" v-bind="$attrs"
    :placeholder="'请选择' + title" :localdata="selectData" :filter="true">
  </uni-data-select>


  <!-- <view class="list_box">
    <view class="box" v-for="(item, index) in pageRes.records" :key="index">
      <view @click="click_ok(item)" class="content">
        <view class="indexBox">
          <view>{{ index + 1 }}</view>
        </view>
        <uni-list class="list" :border="false">
          <slot name="default" :item="item">
            <uni-list-item :title="item.title"> old{{ item }}</uni-list-item>
          </slot>
        </uni-list>
        <view v-show="is_click" class="imgs">
          <image src="../static/toRight.png" style="
          width:0.625rem;height:1.25rem;" mode="widthFill" />
        </view>
        <view class="imgs">
          <checkbox-group v-model="checkboxGroup" @change="checkboxChange(item)">
            <checkbox :value="item.taskInfoId"></checkbox>
          </checkbox-group>
        </view>
      </view>
      <view class="op_button_list">
        <slot name="op" :item="item">

        </slot>
      </view>

    </view>
    <view class="sure">
      <button class="identity" :disabled="checkboxGroup.length === 0" @click="choose">确认选择</button>
    </view>
  </view> -->

</template>

<script setup>
// import { getCurrentInstance, ref } from "vue";
// import { onLoad } from "@dcloudio/uni-app";
// const { proxy } = getCurrentInstance();
// const checkboxGroup = ref([])
// function checkboxChange(int) {
//   console.log(checkboxGroup.value)
//   if (checkboxGroup.value.includes(int.taskInfoId)) {
//     checkboxGroup.value = checkboxGroup.value.filter(item => item !== int.taskInfoId)
//   } else {
//     checkboxGroup.value.push(int.taskInfoId)
//   }
// }
// const choose = () => {
//   // 把已选项的id存储起来
//   uni.setStorageSync('myData', checkboxGroup);
//   uni.navigateBack({
//     delta: 1
//   });
// }

// const props = defineProps({
//   params: {
//     type: Object,
//     default: () => {
//     },
//   },
//   api: { type: String, default: "" },
//   //是否分页
//   isPage: { type: Boolean, default: () => false },
//   path: { type: String },
//   is_route: { type: Boolean, default: () => false },
//   query: {
//     type: Object,
//     default: () => {
//     },
//   },
//   is_click: {
//     type: Boolean,
//     default: () => false,
//   }
// });

// // let route_query = proxy.$route.query
// //
// // var p = props.params
// // // 如果查询参数没有，那么使用路由中的参数
// // if (p==null || p==undefined) {
// //   if (route_query!=null){
// //     for (let key in route_query){
// //       props.params.params[key] = route_query[key];
// //     }
// //   }
// //
// // }
// // console.log(p,route_query)

// let isLoading = ref(true);
// // 分页响应数据
// let pageRes = ref({ current: 1, pages: 1, size: 10, total: 0, records: [] });
// let pageParams = ref({ current: 1, pageSize: 10 });

// const emits = defineEmits(["click"]);
// // 暴露方法
// defineExpose({
//   refresh,
// });

// onLoad(() => {
//   refresh();
// });

// // 刷新
// function refresh(query_param) {
//   console.log("list refresh", query_param)
//   isLoading.value = true;
//   // 情况2：走api接口数据
//   if (props.isPage) {
//     pageRes.value = {
//       current: 1,
//       pages: 1,
//       size: 10,
//       total: 0,
//       records: [],
//     };
//     pageParams.value.current = 1;
//   }
//   // if (query_param == null) {
//   //   query_param = {page: 1, limit: pageParams.value.pageSize}
//   // } else {
//   //   query_param.page = 1;
//   //   query_param.limit = pageParams.value.pageSize
//   // }
//   if (query_param != null && query_param != undefined) {
//     for (var item in query_param) {
//       pageParams.value[item] = query_param[item]
//     }
//   }

//   getApiData();

//   isLoading.value = false;
// }

// async function getApiData(pageObj) {
//   if (!props.api) {
//     return;
//   }

//   isLoading.value = true;

//   if (props.isPage) {
//     // 处理分页参数
//     if (pageObj) {
//       // 从分页组件中拿到数据
//       pageParams.value.current = pageObj.page;
//       pageParams.value.pageSize = pageObj.limit;
//     }


//     let response = await apiMethod(props.params, pageParams);
//     pageRes.value = response.data;
//   } else {
//     let response = await apiMethod(props.params);
//     pageRes.value.records = response.data;
//   }

//   isLoading.value = false;
// }

// function apiMethod(params, headers) {
//   let data = { ...params };
//   if (headers) {
//     data = Object.assign(data, headers.value);
//   }
//   console.log("params", props.params);
//   console.log("data", data);
//   return props.api.split(".").reduce((acc, item) => acc[item], proxy.$api)(
//     data
//   );
// }

// // 分页组件参数变更时触发
// function handleCurrentChange(val) {
//   console.log(val);
//   getApiData({ page: val.current, limit: pageParams.value.pageSize });
// }

// function handleSizeChange(val) {
//   getApiData({ page: pageParams.value.current, limit: val });
// }

// function click_ok(item) {
//   emits("click", item);
// }

    import {onLoad} from "@dcloudio/uni-app";

    const select_item = defineModel()
    const {proxy} = getCurrentInstance();
    const props = defineProps({
      api: {type: String, default: ''},
    disabled: {type: Boolean, default: false},
    title: {type: String, default: ""},
    clear: {type: Boolean, default: true},
    data: {type: Array, default: []}
});
    const selectData = ref([]);
    const emits = defineEmits(["change"])
const handleInput = (e) => {
      console.log(678, e)

  emits("change", e,)
}
onLoad(() => {

});
onMounted(()=>{
  refresh();
})

    function refresh() {
      getApiData();
}

    async function getApiData() {
  if (props.api == null || props.api == '') {
      selectData.value = props.data
    return
  }
    let response = await apiMethod();
    selectData.value = response.data;
}

    function apiMethod() {
  return props.api.split('.').reduce((acc, item) => acc[item], proxy.$api)();
}

</script>
