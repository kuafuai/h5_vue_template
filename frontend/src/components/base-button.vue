<template>
  <view class="container" v-if="model != 'base'">
    <button v-if="model == 'float'" class="btnDialog" :type="type" @click="click">
      <img style="height:81%" src="../static/add.png" alt="">
      <!--      <view style="display: flex;-->
      <!--  align-items: center;-->
      <!--  justify-content: center;">+</view>-->
    </button>
    <button v-if="model == 'flex'" class="btnDialog" :type="type" @click="click">
      <img style="width:100%" src="../static/options.png" alt="">
    </button>
    <button v-if="model == 'slot'" class="btnDialog" :type="type" @click="click"><img style="width:100%"
        src="../static/options.png" alt="">
    </button>
  </view>
  <button class="base-buttom" v-if="model == 'base'" :type="type" @click="click">{{ title }}</button>
</template>

<script setup>
import { defineProps, ref } from "vue";

const props = defineProps({
  model: {
    type: String,
    default: "",
  },
  title: {
    type: String,
    default: "",
  },
  type: {
    type: String,
    default: "",
  },
});
const emits = defineEmits(["click"]);
const click = () => {
  console.log(1111)
  emits("click");
};
// const nickname = ref("张三"); // 替换为实际昵称
// const description = ref("这是描述信息"); // 替换为实际描述
</script>

<style scoped lang="scss">
.base-buttom {
  background-color: rgb(113, 116, 248);
  margin: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  /* 使文字水平居中 */
  //height: auto;
  //height: 50px; /* 设置固定的高度，例如 40px */
  padding: 0 20px;
  /* 上下不再需要 padding，左右可以根据需要调整 */
  box-sizing: border-box;
  flex: none;
  font-size: 0.875rem;
}

.container {
  //background-color: rgba(93, 95, 239, .5);
  position: fixed;
  right: 3%;
  z-index: 99;
  border-radius: 50%;
  display: flex;
  width: 3.5rem;
  height: 3.5rem;
  bottom: 15%;
  //height: 44px;

  .btnDialog {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 3.5rem;
    color: white;
    height: 3.5rem;
    text-align: center;
    line-height: 44px;
    font-size: 24px;
    border-radius: 50%;
    border: none !important;
    background: rgba(93, 95, 239, .5) !important;
  }
}
</style>
