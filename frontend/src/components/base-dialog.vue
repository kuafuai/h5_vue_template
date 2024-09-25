<template>
  <view v-if="comp_type === 'base'">

    <view class="all_dialog">
      <uni-popup
          :showClose="true"
          ref="popup"
          :animation="true"
          :type="type"
          :class="popupClass"
      >
        <view style="height: 100%;overflow-y: auto;">
          <slot name="dialog"/>
        </view>
      </uni-popup>
    </view>
  </view>
  <view v-if="comp_type === 'popup' ">
    <!-- 提示窗示例 -->
    <uni-popup ref="alertDialog" type="dialog">
      <uni-popup-dialog :type="toast.msgType" cancelText="取消" confirmText="确定" title="提示" :content="props.content"
                        @confirm="dialogConfirm"
                        @close="dialogClose"></uni-popup-dialog>
    </uni-popup>
    <view class="example-body box">
      <button class="button popup-success" @click="dialogToggle('success')">
        <text
            class="button-text success-text">确定
        </text>
      </button>
    </view>

    <!--    <view>-->
    <!--      &lt;!&ndash; 提示信息弹窗 &ndash;&gt;-->
    <!--      <uni-popup ref="message" type="message">-->
    <!--        <uni-popup-message :type="toast.msgType" :message="toast.messageText" :duration="2000"></uni-popup-message>-->
    <!--      </uni-popup>-->
    <!--    </view>-->
  </view>


</template>

<script setup>
import {getCurrentInstance} from "vue"

const {proxy} = getCurrentInstance();

const props = defineProps({
  text: {
    type: String,
    required: true
  },
  type: {
    type: String,
    required: false,
    default: "center"
  },
  comp_type: {
    type: String,
    required: false,
    default: "base"
  },
  content: {
    type: String,
    required: false,
    default: "您确定要提交吗？"
  }
});
import {defineProps, ref} from "vue";
import {useRoute, useRouter} from "vue-router";

const toast = {
  type: 'center',
  msgType: 'success',
  messageText: '成功',
  value: ''
}


function dialogConfirm() {
  console.log('点击确认')
  proxy.messageText = `点击确认了 ${toast.msgType} 窗口`
  proxy.$refs.message.open()
}

function dialogToggle(type) {
  toast.msgType = type
  proxy.$refs.alertDialog.open()
}

function dialogClose() {
  console.log('点击关闭')
}

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

@mixin flex {
  /* #ifndef APP-NVUE */
  display: flex;
  /* #endif */
  flex-direction: row;
}

@mixin height {
  /* #ifndef APP-NVUE */
  height: 100%;
  /* #endif */
  /* #ifdef APP-NVUE */
  flex: 1;
  /* #endif */
}

.box {
  @include flex;
}

.button {
  @include flex;
  align-items: center;
  justify-content: center;
  flex: 1;
  height: 35px;
  margin: 0 5px;
  border-radius: 5px;
  width: 1rem;
}

.example-body {
  background-color: #fff;
  padding: 10px 0;
}

.button-text {
  color: #fff;
  font-size: 12px;
}

.popup-content {
  @include flex;
  align-items: center;
  justify-content: center;
  padding: 15px;
  height: 50px;
  background-color: #fff;
}

.popup-height {
  @include height;
  width: 200px;
}

.text {
  font-size: 12px;
  color: #333;
}

.popup-success {
  color: #fff;
  background-color: #e1f3d8;
}

.popup-warn {
  color: #fff;
  background-color: #faecd8;
}

.popup-error {
  color: #fff;
  background-color: #fde2e2;
}

.popup-info {
  color: #fff;
  background-color: #f2f6fc;
}

.success-text {
  color: #09bb07;
}

.warn-text {
  color: #e6a23c;
}

.error-text {
  color: #f56c6c;
}

.info-text {
  color: #909399;
}

.dialog,
.share {
  /* #ifndef APP-NVUE */
  display: flex;
  /* #endif */
  flex-direction: column;
}

.dialog-box {
  padding: 10px;
}

.dialog .button,
.share .button {
  /* #ifndef APP-NVUE */
  width: 100%;
  /* #endif */
  margin: 0;
  margin-top: 10px;
  padding: 3px 0;
  flex: 1;
}

.dialog-text {
  font-size: 14px;
  color: #333;
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