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

// <style lang="scss" scoped>
// uni-button[disabled]:not([type]) {
//   color: rgba(229, 229, 229, 1) !important;
//   background-color: rgba(206, 206, 206, 1) !important;
// }

// .sure {
//   position: fixed;
//   bottom: 20rpx;
//   width: 100%;
//   z-index: 999;
// }

// .identity {
//   color: rgba(250, 251, 255, 1) !important;
//   background-color: rgba(93, 95, 239, 1);
//   border-radius: 10px;
//   line-height: 25px;
//   width: 95%;
//   height: 2.8125rem;
//   font-size: 0.875rem;
//   display: flex;
//   justify-content: center;
//   align-items: center;
// }

// .op_button_list {
//   display: flex;
//   justify-content: flex-end;
//   align-items: center;
// }

// ::v-deep .uni-list--border:after {
//   position: none !important;
//   height: 0px;
// }

// ::v-deep .uni-list-item__content {
//   flex: none;
// }

// ::v-deep .uni-list-item__container {
//   padding: 5px 15px !important;
// }

// ::v-deep .uni-list-item__content-title {
//   color: rgba(113, 142, 191, 1);
//   font-size: 1rem !important;
// }

// ::v-deep .uni-list-item__extra-text {
//   color: rgba(0, 0, 0, 1);
//   font-size: 1rem !important;
//   overflow: hidden;
//   text-overflow: ellipsis;
//   display: -webkit-box;
//   -webkit-line-clamp: 2;
//   -webkit-box-orient: vertical;
// }

// ::v-deep .uni-list {
//   width: 70%;
// }

// .list_box {
//   width: 100%;
//   background: rgb(245, 247, 250);
//   box-sizing: border-box;
//   display: flex;
//   flex-direction: column;
//   position: static;
//   overflow-x: hidden;
//   z-index: 8;

//   .box {
//     font-family: 'DemiLight';
//     font-weight: 400;
//     background: white;
//     margin-bottom: 40rpx;
//   }

//   .content {
//     display: flex;
//     margin: 30rpx 0 0 0;
//     box-sizing: border-box;
//     background: white;
//     font-size: 1rem;
//     justify-content: space-between;

//     .indexBox {
//       width: 20%;

//       view {
//         margin: 0 auto;
//         margin-top: 40rpx;
//         width: 80rpx;
//         height: 80rpx;
//         opacity: 1;
//         border-radius: 100px;
//         background: rgba(93, 95, 239, 1);
//         color: white;
//         line-height: 80rpx;
//         text-align: center;
//       }
//     }
//   }

//   .operate {
//     width: 90%;
//     background: white;
//     display: flex;
//     border-top: 1rpx solid #ccc;
//     margin: 0 auto;
//     justify-content: space-between;
//     padding: 30rpx 0;
//     color: rgba(166, 166, 166, 1);
//     font-size: 15px;

//     .detail {
//       color: rgba(93, 95, 239, 1);
//       display: flex;
//       align-items: center;
//     }

//     .edit {
//       display: flex;
//       align-items: center;
//     }

//     .del {
//       display: flex;
//       align-items: center;
//     }
//   }

//   .nodata {
//     display: flex;
//     flex-direction: column;
//     align-items: center;
//     margin-top: 37%;
//     font-size: 20px;
//     font-weight: 500;
//     color: rgba(144, 150, 178, 1);

//     .noText {
//       margin-top: 18.8px;
//       font-size: 0.875rem;
//     }
//   }

//   .list {
//     flex: 1;
//     margin: 30rpx 0;
//     box-sizing: border-box;
//     border-radius: 15rpx;
//     background: white;
//     color: #fff !important;

//     ::v-deep .uni-list-item {
//       position: static;
//       background: white !important;
//     }
//   }

//   .imgs {
//     display: flex;
//     align-items: center;
//     width: 18%;
//     justify-content: center;

//     image {
//       width: 50rpx;
//       height: 50rpx;
//     }
//   }

//   .list:first-child {
//     margin: 0rpx 0rpx 30rpx 0px;
//     box-sizing: border-box;
//     border-radius: 15rpx;
//     background: white;
//     color: #fff !important;

//     ::v-deep .uni-list-item {
//       position: static;
//       background: white !important;
//     }
//   }

//   .flex-end-center {
//     width: 100%;
//   }
// }
// </style>