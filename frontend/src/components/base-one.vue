<template>
  <view v-if="model=='one'">
    <!--    <view>数据的值id：{{ // show_value_one }}</view>-->
    <button @click="handleClick">{{ show_value_one ? show_value_one : '前去选择' + title }}

      <image src="../static/right.png"
             style="width:32rpx;position:absolute;right:24rpx;display: flex; justify-content:center; " mode="widthFix">
      </image>
    </button>

  </view>
  <view v-else-if="model=='more'">
    <!--    {{// JSON.parse(data_value)}}-->
    <view>

      <button  @click="handleClick">




        <text class="show_text" v-if="data_value_show!=null" v-for="(item, index) in data_value_show"
              :key="index">
          {{ item.text }}
          <!--          {{Object.keys(data_value_show).length}}-->
          <text v-if="index < (Object.keys(data_value_show).length-1)">,</text>
        </text>
        <text class="show_text"  v-else>前去选择{{ title }}</text>

        <image src="../static/right.png"
               style="width:32rpx;position:absolute;right:24rpx;display: flex; justify-content:center; "
               mode="widthFix">
        </image>
      </button>
    </view>

  </view>
</template>

<script setup>
import {onLoad, onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance()
const data_value = defineModel()
// const show_value_one = defineModel()
const props = defineProps({
  model: {type: String, default: "one"},
  show_value: {type: String, default: ""},
  origin_page_path: {type: String, default: ""},
  // api: {type: String, default: ''},
  page_path: {type: String, default: ""},
  title: {type: String, default: ""},
  ref_id: {type: String, default: ""}
});
let param;
let show_value_one = ref()
let data_value_show = ref(null)
onLoad((options) => {

  console.log('URL参数:', options)
  param = options;
})

onShow(() => {
  // for (let i = 0; i < 3; i++) {
  // 防止网络延迟无法成功赋值
  setTimeout(() => {


    // let value = param["select_result_" + props.ref_id];
    // let show = param["select_show_" + props.ref_id];
    if (props.model === 'one') {

      let res = uni.getStorageSync("select_storage_" + props.ref_id)
      //  单选的情况
      res = {...res}
      if (res != null && Object.keys(res).length != 0) {
        console.log("one," + props.ref_id, res)
        data_value.value = res["select_result_" + props.ref_id];
        show_value_one.value = res["select_show_" + props.ref_id];
        uni.removeStorageSync("select_storage_" + props.ref_id)
        // console.log("data_value",props.ref_id,data_value.value,show_value_one.value)
      } else {
        // show_value_one.value = props.show_value;
      }
    } else if (props.model === 'more') {

      let res = uni.getStorageSync("select_storage_" + props.ref_id)
      //  单选的情况
      res = {...res}


      if (data_value.value != null) {
        data_value_show.value = JSON.parse(data_value.value)
      }
      console.log(res)
      //  多选的数据赋值
      if (res != null && Object.keys(res).length != 0) {
        data_value.value = JSON.stringify(res)
        data_value_show.value = res
        // data_value_more_length.value =res.length
        // console.log("长度",data_value_more_length.value)
        uni.removeStorageSync("select_storage_" + props.ref_id)
      }

    }


  }, 150); // 每次延迟 i*100 毫秒，依次执行
  // }
})

onMounted(() => {
  show_value_one.value = props.show_value;
  console.log(show_value_one.value)
})
//  监听props.show_value的变化
watch(
    () => props.show_value, // 监听的目标是 props.show_value
    (newValue, oldValue) => {
      console.log('show_value 变化:', oldValue, '=>', newValue);
      show_value_one.value = props.show_value;
      // 在这里可以执行你想要的逻辑，比如数据处理
    }
);

const handleClick = () => {
  let path = props.page_path;
  let flag = false;
  if (param) {
    path += "?"
    for (let item in param) {
      path = path + item + "=" + param[item] + "&"
    }
    flag = true;
  }

  var select_result = null
  if (data_value.value) {
    if (!flag) {
      path += "?"
    }
    select_result = data_value.value
  }

  path = path + "select_result=" + select_result
      + "&" + "select_path=" + props.origin_page_path + "&" + "select_id=" + props.ref_id

  // 删除最后一个 "&"
  if (path.endsWith("&")) {
    path = path.slice(0, -1); // 移除最后一个字符（即 "&"）
  }
  console.log("one", path)
  proxy.$navigate(path, false)
}
</script>

<style scoped>
button{
  /* 清除默认边框 */
  border:0;
  outline:none;
  /*清除默认背景 */
  background-color: transparent;
  display: flex;
  align-items: center;
  font-size: 0.92rem;
  padding: 0.4rem 0 0.4rem 1.29rem;
  color: rgb(172,172,172);
}
::v-deep uni-forms-item__content >view:first-child{
  width:100%
}

::v-deep uni-button {
  border-radius: 10px;
  background-color: rgba(255, 255, 255, 1);
  border: 1px solid #dcdfe6;
  color: rgb(166, 166, 166);
  font-size: 0.875rem;
  padding-left: 20px;
  display: flex;
  align-items: center;
  height: 100rpx;
  width: 100%;
}

::v-deep uni-button::after {
  border: none;

}
.show_text{
  color: rgb(166, 166, 166);
}
</style>