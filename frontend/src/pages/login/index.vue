<template>
  <base-wrapper>
    <login login_type="qywx_h5" relevanceTable="UserInfo" @loginSuccess="login_success"></login>
  </base-wrapper>
</template>

<script setup>
const {proxy} = getCurrentInstance();

function isMobile() {
  const flag = navigator.userAgent.match(/(phone|pad|iPhone|Android|Mobile|iPad)/i);
  if (flag) {
    alert('手机端')
  }
}

isMobile();

function login_success() {
  proxy.$navigate("/pages/home/index", false);
}

</script>