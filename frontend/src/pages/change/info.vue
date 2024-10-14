<template>
  info
</template>

<script setup>
import { onShow } from "@dcloudio/uni-app";
const { proxy } = getCurrentInstance();


onShow(()=>{
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  const route = currentPage.route;
  uni.$emit('updateRoute', route);
})
</script>

<style scoped lang="scss">
::v-deep .uni-left-window {
  height: 100% ;
}
</style>