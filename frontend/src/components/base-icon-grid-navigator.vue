<template>
  <!--  九宫格-->
  <view>
    <uni-section :title="title" titleFontSize="16px" stype="" padding>
      <uni-grid :column="column" :highlight="true" @change="change" :showBorder="false" :square="false">
        <uni-grid-item v-for="(item, index) in navigations" :index="index" :key="index" @click="goToPage(item.page)">
          <view class="grid-item-box" style="background-color: #fff;">
            <image :src="item.icon" mode="aspectFill" style="width: 40px; height: 40px;"></image>
            <text class="text">{{ item.title }}</text>
          </view>
        </uni-grid-item>
      </uni-grid>
    </uni-section>
  </view>
</template>

<script setup>
import {onLoad} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance()
const emits = defineEmits(["click_item", "click_item"]);


const props = defineProps({
  navigations: {
    type: Object,
    required: true
  },
  column: {
    type: Number,
    required: false,
    default: 3
  },
  title: {
    type: String,
    required: false,
    default: '导航标题'
  }
})


const goToPage = (route) => {
  if (route) {
    console.log("跳转到页面:", route);
    proxy.$navigate("/pages/" + route + '/index', false)
  } else {
    console.error("未指定页面地址");
  }l
}

defineExpose({
  goToPage
})

onLoad(async () => {
})
</script>
<style scoped lang="scss">
.image {
  width: 25px;
  height: 25px;
}
::v-deep .uni-section .uni-section-header__content {
  font-weight: bold;
}

.text {
  font-size: 14px;
  margin-top: 5px;
}

.example-body {
  /* #ifndef APP-NVUE */
  // display: block;
  /* #endif */
}

.grid-dynamic-box {
  margin-bottom: 15px;
}

.grid-item-box {
  flex: 1;
  // position: relative;
  /* #ifndef APP-NVUE */
  display: flex;
  /* #endif */
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 15px 0;
}

.grid-item-box-row {
  flex: 1;
  // position: relative;
  /* #ifndef APP-NVUE */
  display: flex;
  /* #endif */
  flex-direction: row;
  align-items: center;
  justify-content: center;
  padding: 15px 0;
}

.grid-dot {
  position: absolute;
  top: 5px;
  right: 15px;
}

.swiper {
  height: 420px;
}

/* #ifdef H5 */
@media screen and (min-width: 768px) and (max-width: 1425px) {
  .swiper {
    height: 630px;
  }
}

@media screen and (min-width: 1425px) {
  .swiper {
    height: 830px;
  }
}

/* #endif */
</style>