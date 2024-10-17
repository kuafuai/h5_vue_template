<template>
<!--  v-bind="$attrs"-->
  <view :class="wrapper_class" >
    <slot/>
  </view>
</template>
<script setup>

const {proxy} = getCurrentInstance();

const props = defineProps({
  display: {type: String, default: ''},
  direction: {type: String, default: ''},
  x: {type: String, default: ''},
  y: {type: String, default: ''},
  h_full: {type: Boolean, default: false},
  w_full: {type: Boolean, default: false},
  bg_color: {type: String, default: ''}
});

const wrapper_class = ref('');

watch(
    () => props,
    (newPath, oldPath) => {
      get_wrapper_class();
    },
    {deep: true}
);

function get_wrapper_class() {
  let c = '';
  if (props.h_full) {
    c += ' h-full ';
  }
  if (props.w_full) {
    c += ' w-full ';
  }

  if (props.display === 'flex') {

    let t = 'flex'

    if (props.direction != '') {
      t += '-' + props.direction;
    } else {
      t += '-r';
    }

    if (props.x != '') {
      t += '-' + props.x;
    } else {
      t += '-start';
    }

    if (props.y != '') {
      t += '-' + props.y;
    } else {
      t += '-start';
    }
    c += ' ' + t + ' ';
  } else {
    c = "content"
  }

  if (props.bg_color != '') {
    c += ' ' + props.bg_color + ' ';
  }

  wrapper_class.value = c;
}

get_wrapper_class();

</script>

<style scoped lang="scss">
.content {
  width: 100%;
  display: flex;
  justify-content: space-around;
  margin: 10px 0;
  border-radius: 17px;
  background-color: rgba(255, 255, 255, 1);
}

</style>