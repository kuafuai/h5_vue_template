<template>
  <view class="uni-padding-wrap uni-common-mt">
    <uni-segmented-control
        :current="current"
        :values="items"
        :style-type="'text'"
        @clickItem="onClickItem"
    />
  </view>
  <view class="content">
    <view
        v-for="(item, index) in items"
        class="cont"
        :key="index"
        v-show="current === index"
    >
      <text class="content-text">
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
.content,.cont,.content-text{
  height: 100%;
  overflow: auto;
}
/* Add any custom styles here */
</style>
