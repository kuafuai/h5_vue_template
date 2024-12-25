<template>
  <view class="notice-bar-container">
    <uni-notice-bar :v-show="data.length > 0"
                    class="custom-notice-bar"
                    show-get-more
                    scrollable
                    show-icon
                    :speed="speed"
                    :text="data[0]?.content || '暂无通知内容' "
                    @getmore="getMore(data[0])"
                    color="#000" background-color="#fff"/>
  </view>

</template>

<script setup>
import {onLoad} from "@dcloudio/uni-app";
import {defineProps} from "vue";

const emits = defineEmits(['click_item'])
const {proxy} = getCurrentInstance()
const data = ref([])
const getMore = (item) => {
  emits('click_item',item)
}

const props = defineProps({
  api: {
    type: String,
    default: '',
  },
  speed: {
    type: Number,
    default: 100
  }
});


onLoad(async () => {
  const res = await props.api.split(".").reduce((acc, item) => acc[item], proxy.$api)({type: 'notification'})
  if (res.data) {
    data.value = res.data.records
  }
})
</script>
<style scoped lang="scss">
/* 外部容器，留出两侧间距 */
.notice-bar-container {
  margin: 5px 10px; /* 距离两侧10px */
}

::v-deep .uni-icons {
  color: black !important; /* 设置图标颜色为黑色 */
}

/* 自定义通知栏样式 */
.custom-notice-bar {
  border-radius: 20px !important; /* 圆角 */
  padding: 10px !important; /* 添加内部间距 */
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1); /* 添加阴影 */
}

/*!* 替换的小喇叭图标样式 *!
.custom-icon {
  width: 20px;
  height: 20px;
  margin-right: 8px; !* 图标与文字的间距 *!
  display: inline-block;
  vertical-align: middle;
}*/


</style>