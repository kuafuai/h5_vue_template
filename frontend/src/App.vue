<script setup>

console.log("stepup")
</script>

<style>
/*每个页面公共css */
::v-deep .uni-popup__wrapper{
  width: 80%;
}
/* 禁止透传 */
::v-deep uni-popup{
  z-index: 99999 !important;
}
html, body {
  touch-action: manipulation; /* 禁用双击放大 */
}
page{
  height: 100vh !important;
}
</style>
