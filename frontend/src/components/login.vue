<template>
  <view v-if="login_type === 'passwd'" class="login">
    <h2>{{ show_title }}</h2>
    <uni-forms
        ref="formRef"
        style="max-width: 320px; width: 100%; margin: 0 auto;"
        :model="form"
        :rules="rules"
        label-width="auto"
        class="demo-form"
        status-icon
    >
      <uni-forms-item label="用户名" name="phone">
        <uni-easyinput placeholder="请输入用户名" v-model="form.phone" class="input-field"/>
      </uni-forms-item>
      <uni-forms-item label="密码" name="password">
        <uni-easyinput
            placeholder="请输入密码"
            type="password"
            v-model="form.password"
            class="input-field"
        />
      </uni-forms-item>
      <uni-forms-item>
        <button class="submit-btn" @click="submitForm(formRef)">
          登录
        </button>
      </uni-forms-item>
    </uni-forms>
    <h5>默认账户密码：admin / 123456</h5>
  </view>
  <view v-else>
    <fui-button type="success" round size="large" @click="login_click">点击微信授权登录</fui-button>
  </view>
</template>

<script setup>
import {getCurrentInstance, ref} from "vue";

const {proxy} = getCurrentInstance();
const emit = defineEmits(["loginSuccess", "loginFail"]);

const props = defineProps({
  login_type: {type: String, default: null},
  show_title: {type: String, default: "登陆"},
  relevanceTable: {type: String, required: true,}
});

const form = ref({
  phone: "admin",
  password: "123456",
});

const formRef = ref();

const rules = ref({
  phone: {
    rules: [
      {
        required: true,
        errorMessage: '请输入用户名'
      },
      {
        minLength: 1,
        maxLength: 100,
        errorMessage: '用户名长度1-100位'
      }
    ]
  },
  password: {
    rules: [
      {required: true, errorMessage: '请输入密码'},
      {minLength: 3, maxLength: 18, errorMessage: '密码长度3-18位'}
    ]
  }
});

const appId = import.meta.env.VITE_WX_APP_ID;
const callback = import.meta.env.VITE_WX_CALL_BACK;

const submitForm = async (formEl) => {
  if (!formEl) return;
  await proxy.$refs.formRef.validate().then(res => {
    console.log(form.value)
    form.value.relevanceTable = props.relevanceTable
    proxy.$api.login.loginPasswd(form.value).then((res) => {
      login_success(res);
    }).catch(err => {
      login_error(err);
    });
  }).catch(err => {
    console.log("error submit!", err);
  });
};

function login_error(err) {
  emit("loginFail");
}

const login_success = (res) => {
  uni.showToast({
    title: "登录成功",
    icon: "success",
    duration: 2000
  });

  localStorage.setItem("token", res.data);
  proxy.$api.login.getLoginUser().then((res) => {
    const item = res.data;
    uni.setStorageSync("currentUser", JSON.stringify(item));
    emit("loginSuccess", item);
  }).catch(err => {
    console.log(err);
  });
}

function login_click() {
  let url = `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=${encodeURIComponent(callback)}&response_type=code&scope=snsapi_base&state=codeflying#wechat_redirect`;
  window.location.href = url;
}
</script>

<style lang="scss" scoped>
.login {
  width: 100%;
  max-width: 100%;
  height: 100vh;
  background: #ffffff;
  border-radius: 10px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  h2 {
    color: rgb(93, 95, 239);
    margin-bottom: 20px;
  }

  h5 {
    color: #888;
    margin-top: 20px;
  }

  .demo-form {
    width: 100%;
    margin-top: 20px;
  }

  .el-form-item {
    margin-bottom: 15px;
  }

  .input-field {
    height: 45px;
    font-size: 16px;
  }

  .submit-btn {
    width: 100%;
    height: 45px;
    background-color: rgb(93, 95, 239);
    color: #fff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.3s;

    &:hover {
      background-color: rgba(93, 95, 239, 0.8);
    }
  }
}
</style>
