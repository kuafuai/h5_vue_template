<template>
  <uni-data-select v-model="select_item" @change="handleInput" :disabled="disabled" :clear="clear" v-bind="$attrs"
    :placeholder="'请选择' + title" :localdata="selectData" :filter="true">
  </uni-data-select>


  <view>
    <view class="imgs">
      <checkbox-group v-model="checkboxGroup" @change="checkboxChange(item)">
        <checkbox :value="item.taskInfoId"></checkbox>
      </checkbox-group>
    </view>
    <view class="sure">
      <button :class="{ 'identity1': checkboxGroup.length === 0, 'identity': checkboxGroup.length > 0 }"
        :disabled="checkboxGroup.length === 0" @click="choose">确认选择</button>
    </view>
  </view>

</template>

<script setup>
import { onLoad } from "@dcloudio/uni-app";



const checkboxGroup = ref([])
function checkboxChange(int) {
  console.log(checkboxGroup.value)
  if (checkboxGroup.value.includes(int.taskInfoId)) {
    checkboxGroup.value = checkboxGroup.value.filter(item => item !== int.taskInfoId)
  } else {
    checkboxGroup.value.push(int.taskInfoId)
  }
}
const choose = () => {
  console.log(checkboxGroup)
}

const select_item = defineModel()
const { proxy } = getCurrentInstance();
const props = defineProps({
  api: { type: String, default: '' },
  disabled: { type: Boolean, default: false },
  title: { type: String, default: "" },
  clear: { type: Boolean, default: true },
  data: { type: Array, default: [] }
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
<style>
.sure {
  position: fixed;
  bottom: 20rpx;
  width: 100%;
  z-index: 999;
}

.identity {
  color: rgba(250, 251, 255, 1) !important;
  background-color: rgba(93, 95, 239, 1);
  border-radius: 10px;
  line-height: 25px;
  width: 90%;
  height: 2.8125rem;
  font-size: 0.875rem;
  display: flex;
  justify-content: center;
  align-items: center;
}

.identity1 {
  color: rgba(229, 229, 229, 1) !important;
  background-color: rgba(206, 206, 206, 1);
  border: 1px solid rgba(206, 206, 206, 1);
  border-radius: 20rpx;
  line-height: 25px;
  width: 90%;
  height: 2.8125rem;
  font-size: 0.875rem;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>