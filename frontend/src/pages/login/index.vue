<template>
  <base-wrapper>
    <login login_type="qywx_h5" relevanceTable="UserInfo" @loginSuccess="login_success"></login>
  </base-wrapper>
</template>

<script setup>
const { proxy } = getCurrentInstance();
function login_success(){
  proxy.$navigate("/pages/index/index", false);
}

</script>