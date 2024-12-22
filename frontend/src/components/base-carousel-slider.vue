<template>

  <uni-swiper-dot :v-show="data.length > 0" class="uni-swiper-dot-box" :info="displayData" :current="current" :mode="mode"
                  :dots-styles="dotsStyles" field="content">
    <swiper class="swiper-box"
            @change="change"
            :current="swiperDotIndex"
            :interval="3000"
            :autoplay="true">
      <swiper-item v-for="(item, index) in displayData" :key="index">
        <view class="swiper-item" :class="'swiper-item' + index">
          <!--                    <text style="color: #fff; font-size: 32px;">{{ item.content }}</text>-->
          <image class="swiper-image" :src="item.imageUrl" mode="aspectFill"
                 style="width: 100%; height: 200px;" @click="clickItem(item)"></image>
        </view>
      </swiper-item>
    </swiper>
  </uni-swiper-dot>
</template>

<script setup>
import {onLoad} from "@dcloudio/uni-app";
import {defineProps} from "vue";

const emits = defineEmits(['click_item'])
const {proxy} = getCurrentInstance()

const data = ref([])
const current = ref(0);
const mode = ref('dot');
const dotsStyles = {};
const swiperDotIndex = ref(0);
const change = (e) => {
  current.value = e.detail.current;
}

const props = defineProps({
  api: {
    type: String,
    default: '',
  },
  displayCount: {
    type: Number,
    default: 3
  }
});

// 限制显示的数据条数
const displayData = computed(() => {
  return data.value.slice(0, props.displayCount);
});

onLoad(async () => {
  const res = await props.api.split(".").reduce((acc, item) => acc[item], proxy.$api)({type: 'carousel'})
  if (res.data) {
    data.value = res.data.records
  }
})
const clickItem = (item) => {
  emits('click_item', item)
}

</script>
<style scoped lang="scss">
.swiper-box {
  height: 150px;
  border-radius: 8px; /* 圆角 */
  overflow: hidden; /* 确保内容不会溢出圆角 */
  /* 两侧留出间距 */
  margin: 0 10px 10px;
}

.swiper-item {
  /* #ifndef APP-NVUE */
  display: flex;
  /* #endif */
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 200px;
  color: #fff;
  border-radius: 8px; /* 圆角 */
}

/* 设置图片的圆角 */
.swiper-image {
  width: 100%;
  height: 100%;
  border-radius: 8px; /* 圆角 */
  object-fit: cover; /* 确保图片填充且保持圆角 */
}

.swiper-item0 {
  background-color: #cee1fd;
}

.swiper-item1 {
  background-color: #b2cef7;
}

.swiper-item2 {
  background-color: #cee1fd;
}

.image {
  width: 750rpx;
}

/* #ifndef APP-NVUE */
::v-deep .image img {
  -webkit-user-drag: none;
  -khtml-user-drag: none;
  -moz-user-drag: none;
  -o-user-drag: none;
  user-drag: none;
}

/* #endif */

@media screen and (min-width: 500px) {
  .uni-swiper-dot-box {
    width: 400px;
    margin: 0 auto;
    margin-top: 8px;
  }

  .image {
    width: 100%;
  }
}

.uni-bg-red {
  background-color: #ff5a5f;
}

.uni-bg-green {
  background-color: #09bb07;
}

.uni-bg-blue {
  background-color: #007aff;
}

.example-body {
  /* #ifndef APP-NVUE */
  display: flex;
  /* #endif */
  flex-direction: row;
  padding: 20rpx;
}

.example-body-item {

  flex-direction: row;
  justify-content: center;
  align-items: center;
  margin: 15rpx;
  padding: 15rpx;
  height: 60rpx;
  /* #ifndef APP-NVUE */
  display: flex;
  padding: 0 15rpx;
  /* #endif */
  flex: 1;
  border-color: #e5e5e5;
  border-style: solid;
  border-width: 1px;
  border-radius: 5px;
}

.example-body-item-text {
  font-size: 28rpx;
  color: #333;
}

.example-body-dots {
  width: 16rpx;
  height: 16rpx;
  border-radius: 50px;
  background-color: #333333;
  margin-left: 10rpx;
}

.active {
  border-style: solid;
  border-color: #007aff;
  border-width: 1px;
}
</style>