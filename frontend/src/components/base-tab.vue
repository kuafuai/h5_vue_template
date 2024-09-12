<template>
  <view class="uni-padding-wrap uni-common-mt">
    <uni-segmented-control :current="current" :values="items" :style-type="'text'" inActiveColor='#999'
                           activeColor='#5D5FEF'
                           @clickItem="onClickItem" />
  </view>
  <view class="content">
    <view v-for="(item, index) in items" class="cont" :key="index" v-show="current === index">
      <text :class="'content-text' + index">
        <slot :name="'content-' + index"></slot>
      </text>
    </view>
  </view>
</template>

<script setup>
import { ref } from "vue";

const props = defineProps({
  items: {
    type: Array,
    required: true,
  },
});

const current = ref(0);
const onClickItem = (e) => {
  console.log(e);
  current.value = e.currentIndex;
};
</script>

<style scoped lang="scss">
::v-deep .uni-padding-wrap,
.uni-common-mt {
  background: rgb(245, 247, 250);
  position: relative;
}

.content,
.cont,
.content-text {
  height: 100%;
  overflow: auto;
  background: rgb(245, 247, 250);
}

::v-deep .segmented-control__text {
  font-size: 16px !important;
  font-weight: 700 !important;
}

::v-deep .uni-section__content-title {
  display: flex;
  justify-content: flex-start;
  font-family: Montserrat;
  font-size: 12px !important;
  color: rgba(153, 153, 153, 1) !important;
}

::v-deep .uni-section .uni-section-header__content-sub {
  display: flex;
  justify-content: center !important;
  font-size: 26px !important;
  font-weight: 500 !important;
  color: rgba(51, 51, 51, 1) !important;
}

/* Add any custom styles here */
</style>