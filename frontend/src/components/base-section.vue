<template>
    <uni-section :sub-title="subTitle" :type="type" :title="title" class="mb-10 title">
      <image class="img"
             :src="getIcon()"
             mode="widthFix"
      />
      <view>
        <slot name="item">
        </slot>
      </view>
    </uni-section>

</template>
<script>
export default {
    options: {
        styleIsolation: 'shared', // 解除样式隔离
    }
};
</script>
<script setup>
import { getCurrentInstance } from "vue"
import { onShow } from "@dcloudio/uni-app";
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
import photo from '../static/photo.png'
import show_1 from '../static/show_1.png'
import show_2 from '../static/show_2.png'
const props = defineProps({
    subTitle: { type: String, default: "" },
    type: { type: String, default: "" },
    title: { type: String, default: "" },
    itemType:{type:String,default:"ai"}
})

function getIcon() {
  if (props.itemType === 'text' || props.itemType === 'fulltext') {
    return longtext_icon
  } else if (props.itemType === 'phone') {
    return phone_icon
  } else if (props.itemType === 'email') {
    return email_icon
  } else if (props.itemType === 'boolean') {
    return boolean_icon
  } else if (props.itemType === 'datetime' || props.itemType === 'date' || props.itemType === 'time') {
    return time_icon
  } else if (props.itemType === 'number') {
    return number_icon
  } else if (props.itemType === 'enum' || props.itemType === 'quote') {
    return enum_icon
  } else if (props.itemType === 'user' || props.itemType === 'contact') {
    return contact_icon
  } else if (props.itemType === 'decimal') {
    return decimal_icon
  } else if (props.itemType === 'location') {
    return location_icon
  } else if (props.itemType === 'ai') {
    return ai_icon
  }else if (props.itemType === 'image' || props.itemType === 'file' || props.itemType === 'video'){
    return photo
  }
  else {
    return longtext_icon
  }
}

</script>

<style scoped lang="scss">
::v-deep .uni-section__content-title{
    padding-left: 40rpx;

}
.title{
  position: relative;
}

.img{
    position: absolute;
    top: 26rpx;
    left: 2%;
    width: 30rpx;
    height: 30rpx;
}
</style>