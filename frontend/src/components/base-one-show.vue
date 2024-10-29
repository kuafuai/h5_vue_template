<template>
  <base-wrapper>
    <base-wrapper
    >
      <base-search
          @search="search_875"
          ref="base_search_ref0000000"
          :firstSearchDataComment="firstSearchDataComment"
          :searchData="{}"
          class="m-b-10"
          :is_show_advance="false"
          :firstSearchData="firstSearchData"
      >
      </base-search>
      <slot name="list"/>

      <view v-if="type=='more'">
        <base-button model="base" title="选择" @click="click_select" :isChoose="more_result_list.length"/>
      </view>
    </base-wrapper>
  </base-wrapper>
</template>

<script setup>
import {getCurrentInstance} from "vue";
import {onLoad} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();
const props = defineProps({
  // 显示多选还是单选
  type: {String, default: "one"},
  select_text_key: {type: String, default: ""},
  select_value_key: {type: String, default: ""},
  firstSearchData: {type: String, default: ""},
  firstSearchDataComment: {type: String, default: ""},
  list_api: {type: String, default: ""},
});


let url_params

let more_result_list = defineModel("more_data")
onLoad((options) => {
  url_params = options

})

onMounted(() => {
  var select_result = url_params["select_result"]
  if (select_result != null && select_result != '' && select_result != "null") {
    var s = JSON.parse(select_result)
    if (typeof s === 'object' && s !== null && !Array.isArray(s)) {
      // 获取对象的所有 value 列表
      var values = Object.values(s);
      console.log(values); // 输出所有的值
      more_result_list.value = values;
    } else {
      // 是列表直接赋值
      more_result_list.value = s
    }
  } else {
    more_result_list.value = []
  }
})

/**
 * 触发页面的刷新
 */
const refresh = (item) => {

  function isEmptyObject(obj) {
    return Object.keys(obj).length === 0;
  }

  let ignoreKey = ["select_path", "select_id", "select_result"]

  let param = {...url_params};
  let path = param.select_path;
  // let result = param.select_result;
  let select_id = param.select_id


  let key = "select_result_" + select_id
  let show_key = "select_show_" + select_id

  param[key] = item[props.select_value_key]
  param[show_key] = item[props.select_text_key]

  // console.log(path, param)
  // if (!isEmptyObject(param)) {
  //   path += "?"
  //   for (let item in param) {
  //     if (!ignoreKey.includes(item)) {
  //       path = path + item + "=" + param[item] + "&"
  //     }
  //
  //   }
  //   console.log("full_path", path)
  //   // 删除最后一个 "&"
  //   if (path.endsWith("&")) {
  //     path = path.slice(0, -1);
  //   }
  // }


  uni.setStorageSync("select_storage_" + select_id, param)
  uni.navigateBack()
  // proxy.$navigate(path, true)
}


// 多选情况下 选中
const refresh_more = (item) => {

  more_result_list.value = item


}


// 暴露方法
defineExpose({
  refresh,
  refresh_more,
});

const emits = defineEmits(["show_search"])
const search_875 = (
    item
) => {
  emits("show_search", item)
}
const click_8259 = (
    item
) => {

}


// 多选情况下的点击按钮
const click_select = () => {

  let param = {...url_params};
  let select_id = param.select_id


  console.log("选中提交", more_result_list)

  uni.setStorageSync("select_storage_" + select_id, more_result_list.value)
  uni.navigateBack()
}

</script>

<!--<style scoped>-->
<style scoped>

</style>