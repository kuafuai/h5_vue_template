<template>
  <view class="load-more">
    <text @click="click">{{ text }}</text>
  </view>
</template>

<script setup>

import {defineProps} from "vue";


const emits = defineEmits(['click'])
const click = (item) => {
  emits('click',item)
}
const props = defineProps({
  text: {
    type: String,
    default: '查看更多',
  }
});
</script>

<style scoped>
.load-more {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
 }
.load-more text{
  color: #5D5FEF;
  margin-right: 4%;
}
</style>