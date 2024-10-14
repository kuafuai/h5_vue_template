<template>
  <base-wrapper>
    <login login_type="qywx_h5" relevanceTable="UserInfo" @loginSuccess="login_success"></login>
  </base-wrapper>
</template>

<script setup>
const { proxy } = getCurrentInstance();
function login_success(){
  console.log(1122222)
  proxy.$navigate("/pages/home/index", false);
}

</script>