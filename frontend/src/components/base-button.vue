<template>
  <view class="container">
    <button v-if="model=='float'" class="btnDialog" :type="type" @click="click"><img style="width:100%"
                                                                                     src="../static/options.png" alt="">
    </button>
    <button v-if="model=='flex'" class="btnDialog" :type="type" @click="click"><img style="width:100%"
                                                                                    src="../static/options.png" alt="">
    </button>
    <button v-if="model=='slot'" class="btnDialog" :type="type" @click="click"><img style="width:100%"
                                                                                    src="../static/options.png" alt="">
    </button>
  </view>
  <button v-if="model=='base'" :type="type" @click="click">{{ title }}</button>
</template>

<script setup>
import {defineProps, ref} from "vue";

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
.container {
  background-color: rgb(113, 116, 248);
  position: fixed;
  right: 3%;
  z-index: 99999;
  border-radius: 50%;
  display: flex;
  width: 44px;
  bottom: 15%;
  height: 44px;

  .btnDialog {
    width: 44px;
    color: white;
    height: 44px;
    text-align: center;
    line-height: 44px;
    font-size: 24px;
    border-radius: 50%;
    border: none !important;
    background: rgba(0, 0, 0, 0) !important;
  }
}
</style>
