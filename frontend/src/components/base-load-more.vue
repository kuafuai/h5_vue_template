<template>
  <view :class="'load-more-'+horizontal">
    <text @click="click" class="load_text">{{ text }}</text>
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
  },
  horizontal:{
    type: String,
    default: "right"
  }
});
</script>

<style scoped>
.load-more-right {
  width: 100vw;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
 }

.load-more-center {
  width: 100vw;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

.load-more-left {
  width: 100vw;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
}
 .load_text{
  /*color: #5D5FEF;*/
  color: rgb(154, 154, 154);
  margin-right: 4%;
  margin-left: 4%;
}
</style>