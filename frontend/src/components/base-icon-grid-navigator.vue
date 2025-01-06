<template>
  <!--  九宫格-->
  <view class="white-box">
    <uni-section :title="title" titleFontSize="16px" stype="" padding>
      <uni-grid :column="column" :highlight="true" @change="change" :showBorder="false" :square="false">
        <uni-grid-item v-for="(item, index) in navigations" :index="index" :key="index" @click="goToPage(item.page)">
          <view class="grid-item-box" style="background-color: #fff;">
            <image :src="item.icon" mode="aspectFill" style="width: 40px; height: 40px;"></image>
            <text class="text">{{ getTruncatedContent(item.title) }}</text>
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
    // 判断是否是完整的网址
    if (route.startsWith('http://') || route.startsWith('https://')) {
      // 使用 window.location.href 进行站外跳转
      window.location.href = route;
    } else {
      // 判断是否以 / 开头
      if (!route.startsWith('/')) {
        route = '/' + route;
      }
      console.log("跳转到页面:", route);
      proxy.$navigate("/pages" + route + '/index', false);
    }
  } else {
    console.error("未指定页面地址");
  }
}

// 文本截断函数，限制最大字符数
const getTruncatedContent = (content) => {
  const maxLength = 6;
  return content && content.length > maxLength ? content.slice(0, maxLength) + '...' : content;
};

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
  white-space: nowrap; /* 防止文本换行 */
  overflow: hidden; /* 隐藏溢出的内容 */
  text-overflow: ellipsis; /* 添加省略号 */
  width: 100%; /* 确保容器宽度足够 */
  display: inline-block; /* 必须设置为 inline-block 才能有效 */
  text-align: center; /* 水平居中 */
}

.white-box {
  width: 100%;
  background-color: #fff; /* 白色背景 */
  border-radius: 10px; /* 圆角 */
  padding: 10px; /* 内边距 */
  margin: 5px 10px; /* 外边距，控制左右距离 */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
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
  padding: 10px 0;
  margin: 8px 10px; /* 控制每个项之间的间距 */
  background-color: #fff;
  border-radius: 8px; /* 可选：设置圆角 */
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