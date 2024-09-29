<template>
  <uni-data-select v-model="select_item" @change="handleInput" :disabled="disabled" :clear="clear" v-bind="$attrs" :placeholder="'请选择'+title"
                   :localdata="selectData"
                   :filter="true">

  </uni-data-select>
</template>

<script setup>
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
  refresh();
});

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