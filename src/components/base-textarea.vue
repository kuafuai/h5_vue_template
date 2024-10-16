<template>
  <fui-textarea :placeholder="placeholder" v-model="model" class="custom-textarea"/>
</template>

<script setup>
const model = defineModel()
defineProps({
  placeholder: {
    type: String,
    required: false
  }
})
</script>

<style scoped>
.custom-textarea {
  border: 1px solid #dcdfe6; /* 边框颜色，与 uni-easy-input 一致 */
  background-color: #ffffff; /* 内部颜色，通常是白色 */
  padding: 8px; /* 控制文本域内部的填充 */
  border-radius: 4px; /* 圆角边框，与 uni-easy-input 一样 */
  color: black; /* 字体颜色 */
  box-sizing: border-box; /* 确保 padding 和 border 包含在宽高内 */
}

.custom-textarea:focus {
  border-color: #007aff; /* 聚焦时的边框颜色，与 uni-easy-input 聚焦效果一致 */
}

.custom-textarea::placeholder {
  color: #999; /* placeholder 的颜色 */
}
</style>