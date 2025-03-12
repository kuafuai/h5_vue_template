<template>
  <view class="section-container">
    <!-- 标题部分 -->
    <view class="title">
      <!-- 图标 -->
      <image class="img" :src="getIcon()" mode="widthFix" />
      <view class="title-content">
        <text class="title-text">{{ title }}</text>
        <base-loading class="loading" v-if="itemType == 'ai' && subTitle == null"></base-loading>
      </view>
    </view>

    <!-- 子标题区域 -->
    <view class="sub-title-container"  @click.stop="toggleExpand">
      <span
          ref="textContainer"
          class="sub-title truncate"
          v-if="subTitle"

      >
        {{ subTitle }}
      </span>
      <text class="sub-title" v-else-if="itemType == 'ai' && subTitle == null">
        正在生成中,请稍等...
      </text>

      <!-- 超出三行时显示展开/收起箭头 -->
      <image
          v-if="show_tab"
          class="expand-icon"
          :src="show_1"
      />
    </view>

    <!-- 内容部分 -->
    <view class="content">
      <slot name="item"></slot>
    </view>



    <base-dialog ref="show_dialog_ref" type="bottom" comp_type="base">
      <template #dialog>
        <view class="dialog-container">
          {{subTitle}}
        </view>
      </template>
    </base-dialog>
  </view>
</template>

<script setup>
import { ref, onMounted, nextTick } from "vue";
import ai_icon from "../static/ai.png";
import location_icon from "../static/location.png";
import decimal_icon from "../static/decimal.png";
import boolean_icon from "../static/boolean.png";
import email_icon from "../static/email.png";
import number_icon from "../static/number.png";
import longtext_icon from "../static/longtext.png";
import phone_icon from "../static/phone_icon.png";
import enum_icon from "../static/enum.png";
import time_icon from "../static/time.png";
import contact_icon from "../static/contact.png";
import photo from "../static/photo.png";
import show_1 from "../static/show_1.png"; // 右箭头（展开）
import show_2 from "../static/show_2.png";
import {onShow} from "@dcloudio/uni-app"; // 下箭头（收起）
const {proxy} = getCurrentInstance();
const props = defineProps({
  subTitle: { type: String, default: "" },
  title: { type: String, default: "" },
  itemType: { type: String, default: "text" },
});

// const textContainer = ref(null);
const show_tab = ref(false); // 是否显示展开箭头
const show_expand = ref(false); // 是否展开

// 切换展开/收起状态
const toggleExpand = () => {
  if (show_tab.value){
    proxy.$refs.show_dialog_ref.showDialog();
  }
  // show_expand.value = !show_expand.value;
  // if (show_expand.value){

  // }
};

// 计算是否超出三行
// 计算文本高度
const checkEllipsis = () => {
  if (props.itemType=='ai'){
    const textContainer = proxy.$refs.textContainer;
    if (textContainer) {
      console.log("文本实际高度:", textContainer.scrollHeight, textContainer.clientHeight)
      // 比较实际高度和显示高度
      show_tab.value = textContainer.scrollHeight > textContainer.clientHeight;
    }
    // nextTick(() => {
    //   setTimeout(() => {
    //     uni.createSelectorQuery()
    //         .in(this) // 确保查询在当前页面执行
    //         .select(".sub-title")
    //         .boundingClientRect((rect) => {
    //           // console.log("文本实际高度:", rect);
    //           if (rect) {
    //             console.log("文本实际高度:", rect.height);
    //             const lineHeight = 5; // 估算单行高度（rpx 转 px 可能有小差异）
    //             show_tab.value = rect.height > lineHeight * 3; // 超出3行显示箭头
    //           }
    //         })
    //         .exec();
    //   }, 50); // 确保渲染完成后再执行
    // });
  }

};

// 组件加载后计算文本高度
onMounted(()=>{
  checkEllipsis()
});

// 监听 subTitle 变化，确保动态检测
watch(() => props.subTitle, checkEllipsis);

function getIcon() {
  const iconMap = {
    text: longtext_icon,
    fulltext: longtext_icon,
    phone: phone_icon,
    email: email_icon,
    boolean: boolean_icon,
    datetime: time_icon,
    date: time_icon,
    time: time_icon,
    number: number_icon,
    enum: enum_icon,
    quote: enum_icon,
    user: contact_icon,
    contact: contact_icon,
    decimal: decimal_icon,
    location: location_icon,
    ai: ai_icon,
    image: photo,
    file: photo,
    video: photo,
  };
  return iconMap[props.itemType] || longtext_icon;
}
</script>

<style scoped lang="scss">
/* 外层容器 */
.section-container {
  padding: 20rpx;
  background: #fff;
}

/* 标题部分 */
.title {
  display: flex;
  align-items: center;
  padding-bottom: 10rpx;
}

/* 图标 */
.img {
  width: 30rpx;
  height: 30rpx;
  margin-right: 10rpx;
}

.title-content {
  display: flex;
  flex-direction: row;
}

/* 标题文本 */
.title-text {
  font-size: 28rpx;
  color: #333;
}

/* 子标题区域 */
.sub-title-container {
  display: flex;
  align-items: center;
}

/* 子标题文本 */
.sub-title {

  font-size: 24rpx;
  color: #888;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  width: 90%;
}

/* 省略号模式 */
.truncate {
  //width: 90%;
  -webkit-line-clamp: 3;
}

/* 展开/收起图标 */
.expand-icon {
  width: 30rpx;
  height: 30rpx;
  margin-left: 10rpx;
  cursor: pointer;
}

.dialog-container{
  display: flex;
  flex-direction: column;
  padding-top: 30rpx;
  //max-height: 80vh; /* 至少占据 80% 视口高度 */
  padding-bottom: 30rpx;
  padding-left: 19rpx;
  padding-right: 19rpx;
  text-align: left;
  background: white;
  white-space: pre-wrap;
  //text-indent: 2em;
  //overflow: auto; /* 仅在内容超过时滚动 */

}
.loading{
  margin-left: 20rpx;
}
</style>
