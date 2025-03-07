<template>
  <base-layout display="flex" x="between" y="center" :w_full="true">
    <view class="flex-start-center">
      <img :src="getIcon()" style="width: 16pt;height: 16pt;" alt=""/>
      <span class="m-l-4">{{ label }}</span>
    </view>
    <view>{{ text }}</view>
  </base-layout>
</template>

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


const {proxy} = getCurrentInstance();
const props = defineProps({
  type: {type: String, default: "text"},
  label: {type: String, default: ""},
  text: {type: String, required: false},
});

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

</style>