<template>
  <view class="userBox">
    <view class="user-show">
      <image class="image" :src="avatar" mode="widthFix"></image>
      <view class="text-user">
        <view class="username">{{ username ? username : '小飞飞' }}</view>
        <view class="appName">{{  '您好，欢迎来到'+appName }}</view>
      </view>
    </view>
    <view class="refresh" @click="refreshFn">
      <image src="../static/refresh.png" mode="widthFix" />
    </view>
  </view>
</template>

<script setup>
import { getCurrentInstance } from "vue"

const { proxy } = getCurrentInstance();
const props = defineProps({
  table_module: {
    type: String,
    default: '',
  },
  api: {
    type: String,
    default: '',
  },
  id: {
    type: String,
    default: '',
  },
  usernameField: {
    type: String,
    default: 'username',
  },
  avatarField: {
    type: String,
    default: 'avatar',
  },
  appName: {
    type: String,
    default: '码上飞应用',
  }


});
var username = ref('')
var avatar = ref(null)
const user = ref({})

onMounted(async () => {
  let reponse = await proxy.$api[props.table_module][props.api](props.id)
  if (reponse.code === 0) {
    user.value = reponse.data
    console.log(user.value, "asdasdasd")
    username.value = user.value[props.usernameField]
    avatar.value = user.value[props.avatarField]
    console.log(username.value, avatar.value)
  }
})

// 刷新页面
const refreshFn=()=>{
  location.reload()
}

// });
</script>

<style scoped>
.userBox {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  /* 白色背景 */
  border-radius: 10px;
  /* 圆角 */
  padding: 10px;
  /* 内边距 */
  margin: 5px 10px;
  /* 外边距，控制左右距离 */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  /* 添加阴影效果 */
  box-sizing: border-box;
}

.image {
  min-width: 124rpx !important;
  border-radius: 50%;
  max-width: 124rpx;
  margin-right: 30rpx;
}

.user-show {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-around;
}

.username {
  color: rgba(51, 51, 51, 1);
  font-size: 28rpx;
  text-align: left;
  font-family: PingFangSC-regular;
  height: 40rpx;
  line-height: 40rpx;
}

.appName {
  color: rgba(154, 154, 154, 1);
  font-size: 28rpx;
  text-align: left;
  font-family: PingFangSC-regular;
  height: 40rpx;
  line-height: 40rpx;
}

.refresh {
  min-width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(82,127,244,1);
}

.refresh image {
  width: 70%;
  border-radius: 50%;
}
</style>