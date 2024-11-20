<template>
  <view class="app flex w-full">
    <view class="flex-end-center p-y-10 bg-color-lightgrey" :style="{ width: computedLabelWidth}">
      <span class="m-r-10">{{ label }}</span>
    </view>

    <view class="flex-1 flex-start-center m-x-10 w-full">
      <slot />
    </view>
  </view>
</template>

<script setup>
const {proxy} = getCurrentInstance();
const props = defineProps({
  label: {
    type: String,
    default: '',
  },
  labelWidth: {
    type: String,
    default: '',
  },
});

const computedLabelWidth = computed(() => {
  return props.labelWidth || '150px'
});

</script>

<style lang="scss" scoped>
.app {
  border: 0.4px solid rgb(230, 233, 240);

  //&:first-child {
  //  border-radius: 10px 10px 0px 0px;
  //}
  //
  //&:not(:first-child) {
  //  border-top: none;
  //}
  //
  //&:last-child {
  //  border-radius: 0px 0px 10px 10px;
  //}

}
</style>