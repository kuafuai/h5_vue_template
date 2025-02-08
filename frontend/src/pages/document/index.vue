<template>
  <base-wrapper>
    <view
        style="width:100%;background:white;padding:25px 15px;box-sizing:border-box;display:flex;align-items: center;justify-content: space-between;">
      <view style="color:#6569F7;font-weight:600;font-size:18px">
        物料管理
        <uni-breadcrumb separator="/">
          <uni-breadcrumb-item>
            变更管理平台
          </uni-breadcrumb-item>
          <uni-breadcrumb-item>
            物料管理
          </uni-breadcrumb-item>
        </uni-breadcrumb>
      </view>
      <view
          style="width: 40px; height: 40px;line-height:40px;border-radius:30px;background:rgb(101,105,247);text-align: center;color:white;">
        {{ userInfo.userName }}
      </view>
    </view>

    <view>
      <el-link class="m-10" type="success"  @click="handle_download('/profile/产品验收单.xls')">
        下载“产品验收单”
      </el-link>
    </view>

  </base-wrapper>
</template>
<script setup>
import {onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();

// 用户信息
const userInfo = ref('')
let strUserInfo = uni.getStorageSync("userInfo") || null;
if (strUserInfo != null) {
  userInfo.value = JSON.parse(strUserInfo);
}

function handle_download(fileName) {
  console.log(fileName);
  proxy.$download.name(fileName);
}

onShow(() => {
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  const route = currentPage.route;
  uni.$emit('updateRoute', route);
})

</script>