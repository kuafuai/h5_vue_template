<template>
  <view>
    <fui-checkbox :checked="props.data[changeName]" @change="check_change"></fui-checkbox>
  </view>
</template>

<script setup>
const {proxy} = getCurrentInstance();
const props = defineProps({
  api: {type: String, default: ''},
  data: {type: Array, default: () => {}},
  changeName: {type: String, default: ''}
});

function check_change(e){
  props.data[props.changeName] = e.checked;
  if (!props.api) {
    return;
  }
  props.api.split('.').reduce((acc, item) => acc[item], proxy.$api)(props.data);
}

</script>