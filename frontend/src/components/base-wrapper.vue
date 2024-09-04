<template>
  <view :class="wrapperClass+' box'">
    <slot />
  </view>
</template>

<script setup>
// import { ref, watch, getCurrentInstance } from 'vue';

const { proxy } = getCurrentInstance();
const type = ref(import.meta.env.VITE_APP_TYPE);

const props = defineProps({
  display: { type: String, default: '' },
  direction: { type: String, default: '' },
  x: { type: String, default: '' },
  y: { type: String, default: '' },
  h_full: { type: Boolean, default: false },
  w_full: { type: Boolean, default: false },
  bg_color: { type: String, default: '' }
});

const wrapperClass = ref('');

watch(
    () => props,
    () => {
      getWrapperClass();
    },
    { deep: true }
);

function getWrapperClass() {
  let c = '';
  if (props.h_full) {
    c += ' height-100 ';
  } else {
    if (type.value === 'PC') {
      c += ' padding-10 height-100 ';
    } else {
      c += ' height-100 ';
    }
  }

  if (props.w_full) {
    c += ' width-100 ';
  }

  if (props.display === 'flex') {
    let t = 'flex';

    if (props.direction !== '') {
      t += '-' + props.direction;
    } else {
      t += '-row';
    }

    if (props.x !== '') {
      t += '-' + props.x;
    } else {
      t += '-start';
    }

    if (props.y !== '') {
      t += '-' + props.y;
    } else {
      t += '-start';
    }
    c += ' ' + t + ' ';
  }

  if (props.bg_color !== '') {
    c += ' ' + props.bg_color + ' ';
  }

  wrapperClass.value = c;
}

getWrapperClass();
</script>

<style scoped>
.height-100 {
  height: 100%;
}

.width-100 {
  width: 100% ;
}
.box{
  display: flex;
  flex-direction: column;
  /*overflow: hidden;*/
}

/* .padding-10 {
  padding: 10px;
} */

/* Add your other custom styles here */
</style>

