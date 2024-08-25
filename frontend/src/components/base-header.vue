<template>
    <view class="flex-start-center" :style="{ 'padding-top': top + 'px', 'padding-bottom': bottom + 'px' }">
      <view class="flex-1">
        <slot></slot>
      </view>
      <slot name="right"></slot>
    </view>
</template>
<script setup>
  defineProps({
    top: {
      type: Number,
      default: 0,
    },
    bottom: {
      type: Number,
      default: 5,
    },
  });
</script>
<style lang="scss" scoped></style>
  