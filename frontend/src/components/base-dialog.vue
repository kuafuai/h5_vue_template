<template>
  <view class="all_dialog">
    <uni-popup
        :showClose="true"
        ref="popup"
        :animation="true"
        :type="type"
        :class="popupClass"
        @close="handleClose"
    >
      <view style="height: 100%;overflow-y: auto;">
        <slot name="dialog"/>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>

import {onHide} from "@dcloudio/uni-app";

const props = defineProps({
  text: {
    type: String,
    required: true
  },
  type: {
    type: String,
    required: false,
    default: "center"
  }
});
import {defineProps, ref} from "vue";
import {useRoute, useRouter} from "vue-router";

onUnmounted(() => {
  console.log(12345678909876543)
})
onHide(()=>{
  console.log("onhide")
})



console.log(props.type)
// 根据 type 动态设置不同的 class
const popupClass = computed(() => {
  return {
    'type-bottom-top': props.type === 'bottom' || props.type === 'top',
    'type-other': props.type !== 'bottom' && props.type !== 'top'
  };
});
// console.log(popupClass.value)
// const dialogVisible = ref(false)
// const showDialog = () => {
//   dialogVisible.value = true
// }
let router = useRouter()
let route = useRoute()

const dialogVisible = ref(false);
const popup = ref(null);

function showDialog() {
  popup.value.open();
}

function closeDialog() {
  popup.value.close();
}


// const closeDialog = () => {
//   // const { path, query, params } = route.value;
//   // location.reload()
//   // router.go(0)
//   dialogVisible.value = false
//
//   // router.push({path:"/sales_lead"})
//   // 先导航到一个空路径，然后再导航回原来的路径
//   //   router.push({ path, query, params });
// }
defineExpose({
  closeDialog,
  showDialog
})
</script>

<style lang="scss" scoped>
.all_dialog {
  width: 100vm;
  z-index: 99999;
}

//.dialog {
//  background-color: rgb(113, 116, 248);
//  position: fixed;
//  right: 0;
//  z-index: 99;
//  border-radius: 50%;
//  display: flex;
//  width: 44px;
//  bottom: 15%;
//  height: 44px;
//  .btnDialog {
//    width: 44px;
//    color: white;
//    height: 44px;
//    text-align: center;
//    line-height: 44px;
//    font-size: 24px;
//    border-radius: 50%;
//    border: none !important;
//    background: rgba(0, 0, 0, 0) !important;
//  }
//  .show_dialog {
//    /* padding: 20px; */
//  }
//}
button {
  /* 取消按钮的边框样式 */
  border: 0;
  /* 取消按钮的背景颜色 */
  background-color: transparent;
  /* 取消按钮的文本装饰（下划线） */
  text-decoration: none;
  /* 取消按钮的文字阴影 */
  text-shadow: none;
  /* 取消按钮的内边距 */
  padding: 0;
  /* 取消按钮的外边距 */
  margin: 0;
  /* 取消按钮的显示样式 */
  display: inline-block;
  /* 取消按钮的文字颜色 */
  color: inherit;
  /* 取消按钮的字体样式 */
  font: inherit;
  /* 设置按钮的光标样式为默认 */
  cursor: default;
  /* 设置按钮的用户选择样式为无 */
  user-select: none;
}

::v-deep .type-bottom-top .uni-popup__wrapper {
  //height: 80%;       /* 设置高度为80% */
  max-height: 500px;
  overflow: auto;
  width: 100%;
  border-radius: 20px 20px 0px 0px;
}

::v-deep .type-other .uni-popup__wrapper {
  height: 80%; /* 设置高度为80% */
  //height: 500px;
  //overflow: auto;
  //width: 100%;
}
</style>