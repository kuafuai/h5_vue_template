<template>
  <view :class='"text-"+horizontal'>
    <text>{{ text_show }}</text>
  </view>
</template>

<script setup>
const {proxy} = getCurrentInstance();
// 双向绑定变量
const model = defineModel();
const text_show = ref();

const props = defineProps({
  text: {
    type: String,
    default: '',
  },
  horizontal: {type: String, default: 'left'},
});

watch(model, (newValue, oldValue) => {
  if (newValue) {
    text_show.value = newValue
  } else {
    text_show.value = props.text
  }
}, {immediate: true, deep: true})


</script>