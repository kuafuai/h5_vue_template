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
    </base-wrapper>
  </base-wrapper>
</template>

<script setup>
import {getCurrentInstance} from "vue";
import {onLoad} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();
const props = defineProps({
  select_text_key: {type: String, default: ""},
  select_value_key: {type: String, default: ""},
  firstSearchData: {type: String, default: ""},
  firstSearchDataComment: {type: String, default: ""},
  list_api: {type: String, default: ""},
});


let url_params
onLoad((options) => {
  url_params = options
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

  console.log(path, param)
  if (!isEmptyObject(param)) {
    path += "?"
    for (let item in param) {
      if (!ignoreKey.includes(item)) {
        path = path + item + "=" + param[item] + "&"
      }

    }
    console.log("full_path", path)
    // 删除最后一个 "&"
    if (path.endsWith("&")) {
      path = path.slice(0, -1);
    }
  }


  proxy.$navigate(path, true)
}

// 暴露方法
defineExpose({
  refresh,
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
</script>

<style scoped>

</style>