<template>
  <base-layout class="app" display="flex" x="between" y="start" :w_full="true">
    <view class="flex-start-center" style="min-width: 120px;">
      <img :src="getIcon()" style="width: 16pt;height: 16pt;" alt=""/>
      <span class="m-l-4">{{ label }}</span>
    </view>
    <view class="flex-end-start">
      <span ref="textContainer" :class="{'truncate': !show_expand}"> {{ text }} </span>
      <img v-if="show_tab" v-show="!show_expand" :src="show_1" style="width: 16pt;height: 16pt; cursor: pointer" alt=""
           @click.stop="toggleExpand"/>
      <img v-if="show_tab" v-show="show_expand" :src="show_2" style="width: 16pt;height: 16pt; cursor: pointer" alt=""
           @click.stop="toggleExpand"/>
    </view>
  </base-layout>
</template>
<script>
export default {
  options: {
    styleIsolation: 'shared', // 解除样式隔离
  }
};
</script>
<script setup>
import ai_icon from '../static/ai.png'
import location_icon from '../static/location.png'
import decimal_icon from '../static/decimal.png'
import boolean_icon from '../static/boolean.png'
import email_icon from '../static/email.png'
import number_icon from '../static/number.png'
import longtext_icon from '../static/longtext.png'
import phone_icon from '../static/phone_icon.png'
import enum_icon from '../static/enum.png'
import time_icon from '../static/time.png'
import contact_icon from '../static/contact.png'
import show_1 from '../static/show_1.png'
import show_2 from '../static/show_2.png'


const {proxy} = getCurrentInstance();
const props = defineProps({
  type: {type: String, default: "text"},
  label: {type: String, default: ""},
  text: {type: String, required: false},
});

onMounted(() => {
  checkEllipsis();
});

function checkEllipsis() {
  const textContainer = proxy.$refs.textContainer;
  if (textContainer) {
    // 比较实际高度和显示高度
    show_tab.value = textContainer.scrollHeight > textContainer.clientHeight;
  }
}

const show_tab = ref(false)
const show_expand = ref(false)

function toggleExpand() {
  show_expand.value = !show_expand.value;
}

function getIcon() {
  if (props.type === 'text' || props.type === 'fulltext') {
    return longtext_icon
  } else if (props.type === 'phone') {
    return phone_icon
  } else if (props.type === 'email') {
    return email_icon
  } else if (props.type === 'boolean') {
    return boolean_icon
  } else if (props.type === 'datetime' || props.type === 'date' || props.type === 'time') {
    return time_icon
  } else if (props.type === 'number') {
    return number_icon
  } else if (props.type === 'enum' || props.type === 'quote') {
    return enum_icon
  } else if (props.type === 'user' || props.type === 'contact') {
    return contact_icon
  } else if (props.type === 'decimal') {
    return decimal_icon
  } else if (props.type === 'location') {
    return location_icon
  } else if (props.type === 'ai') {
    return ai_icon
  } else {
    return longtext_icon
  }
}

</script>

<style scoped lang="scss">

.app {
  &:first-child {
    margin-bottom: 5px;
  }

  &:not(:first-child) {
    margin-bottom: 5px;
  }

  &:last-child {
    margin-bottom: 0;
  }
}

.truncate {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
}

</style>