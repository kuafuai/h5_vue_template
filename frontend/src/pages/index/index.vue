<template>
  <base-wrapper>
    <base-wrapper
    >
      <login
          @loginSuccess="loginSuccess_9102"
          ref="login_passwd_ref"
          login_type="passwd"
          show_title="登录"
          @loginFail="loginFail_9102"
          relevanceTable="userInformation"
      >

      </login>
    </base-wrapper>
  </base-wrapper>
</template>
<script setup>
import {getCurrentInstance, ref} from "vue"

const {proxy} = getCurrentInstance();
import {onLoad} from "@dcloudio/uni-app";

const allParams = ref({})
// onLoad(() => {
const pages = getCurrentPages();
const currentPage = pages[pages.length - 1];
const urlParams = currentPage.options; // 获取页面传递的参数

const localStorageKey = "/" + currentPage.route; // 当前页面的路径作为 key
const localStorageParams = uni.getStorageSync(localStorageKey) || null;
let currentUser = uni.getStorageSync("currentUser") || null;
if (localStorageParams != null && urlParams != null) {
  allParams.value = {
    ...urlParams,
    ...JSON.parse(localStorageParams)
  };
} else if (localStorageParams != null && urlParams == null) {
  allParams.value = {

    ...JSON.parse(localStorageParams)
  };
} else if (localStorageParams == null && urlParams != null) {
  allParams.value = {
    ...urlParams, // 展开 URL 参数

  };
}
// })
if (currentUser != null) {
  var parse = JSON.parse(currentUser);
  allParams.value["currentUser"] = parse;
  allParams.value[parse["relevanceTable"] + "Id"] = parse.userId
}


const loginSuccess_9102 = (
    item
) => {
  proxy.$navigate("/pages/one/index?userId=" + item.userId, false)

}
const loginFail_9102 = () => {
  uni.showToast({
    title: '认证失败，请确认用户名密码是否正确。',
    icon: 'none',
    duration: 2000
  });

}
</script>