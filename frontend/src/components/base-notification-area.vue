<template>
  <view class="notice-bar-container">

    <uni-notice-bar class="custom-notice-bar" show-get-more show-icon scrollable
                    :text="data[0]?.content || '暂无通知内容' " @getmore="getMore"
    color="#000" background-color="#fff"/>
  </view>

</template>

<script setup>
import {onLoad} from "@dcloudio/uni-app";
import {defineProps} from "vue";

const {proxy} = getCurrentInstance()
const data = ref([])
const getMore = () => {
  uni.showToast({
    title: '点击查看更多',
    icon: 'none'
  })
}

const props = defineProps({
  api: {
    type: String,
    default: '',
  }
});


onLoad(async () => {
  const res = await props.api.split(".").reduce((acc, item) => acc[item], proxy.$api)({type: 'notification'})
  if(res.data) {
    data.value = res.data.records
  }
})
</script>
<style scoped lang="scss">
/* 外部容器，留出两侧间距 */
.notice-bar-container {
  margin: 0 10px; /* 距离两侧10px */
}

/* 自定义通知栏样式 */
.custom-notice-bar {
  border-radius: 20px !important; /* 圆角 */
  padding: 10px !important; /* 添加内部间距 */
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1); /* 添加阴影 */
}

</style>