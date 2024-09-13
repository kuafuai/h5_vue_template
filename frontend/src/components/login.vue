<template>
  <view v-if="login_type === 'passwd'" class="login">
    <view class="hello">
      <view class="nihao">嘿！你好</view>
      <view class="code"> 欢迎来到CodeFlying!</view>
    </view>
    <view class="title">
      <h2>{{ show_title }}</h2>
    </view>
    <!-- <uni-forms style="max-width: 320px; width: 100%; margin: 0 auto;" :="" :rules="" ref="loginForm_sms" label-width="auto" status-icon> -->
    <uni-forms ref="formRef" style="max-width: 320px; width: 100%; margin: 0 auto;" :modelValue="form" :rules="rules"
               label-width="auto" status-icon>

      <uni-forms-item name="phone">
        <view class="icon-input-container">
          <uni-easyinput placeholder="请输入用户名" v-model="form.phone" class="input-field" />
          <view class="icon">
            <img src="../static/peo.png" style="width:13px;height:13px;" alt="">
          </view>
        </view>
      </uni-forms-item>
      <uni-forms-item name="password">
        <view class="icon-input-container">
          <uni-easyinput placeholder="请输入密码" type="password" v-model="form.password" class="input-field" />
          <view class="icon">
            <img src="../static/pass.png" style="width:13px;height:13px;" alt="">
          </view>
        </view>
      </uni-forms-item>
      <uni-forms-item>
        <button class="submit-btn" @click="submitForm(formRef)">
          登录
        </button>
      </uni-forms-item>
    </uni-forms>
    <!-- <h5>默认账户密码：admin / 123456</h5> -->
    <h5 v-show="is_register" @click="to_page" style="color: rgba(93, 95, 239, 1);font-size: 13px;font-weight:450">
      <text style="color:rgba(52, 57, 101, 1);font-weight:450">没有账号? </text>
      现在去注册
    </h5>
    <view>

    </view>
  </view>
  <view v-else-if="login_type === 'sms'" class="login">
    <view class="hello">
      <view class="nihao">嘿！你好</view>
      <view class="code"> 欢迎来到CodeFlying!</view>
    </view>
    <view class="title">
      <h2>{{ show_title }}</h2>
    </view>
    <uni-forms style="max-width: 320px; width: 100%; margin: 0 auto;" :modelValue="form_sms" :rules="rules_sms"
               ref="loginForm_sms" label-width="auto" status-icon>
      <!-- 手机号输入框 -->
      <uni-forms-item name="phone">
        <view class="icon-input-container">
          <uni-easyinput v-model="form_sms.phone" placeholder="请输入手机号" type="number" maxlength="11" />
          <view class="icon">
            <img src="../static/phone.png" style="width:11px;height:16px; margin-top:3px" alt="">
          </view>
        </view>
      </uni-forms-item>

      <!-- 验证码输入框 -->

      <uni-forms-item name="code">
        <view class="code-input-container">
          <uni-easyinput style="width:50px;" v-model="form_sms.code" placeholder="请输入验证码" />
          <view class="icon">
            <img src="../static/safe.png" style="width:22px;height:24px; margin-bottom:2px" alt="">
          </view>
          <button class="send-code-button" @click="sendcode" :disabled="countdown > 0">
            {{ countdown > 0 ? `${countdown}s后重发` : '获取验证码' }}
          </button>
        </view>

      </uni-forms-item>

      <!-- 登录按钮 -->
      <button class="submit-btn" @click="submitForm_sms">
        登录
      </button>
    </uni-forms>

    <h5 v-show="is_register" @click="to_page" style="color: rgba(93, 95, 239, 1);font-size: 13px;font-weight:450">
      <text style="color:rgba(52, 57, 101, 1);font-weight:450">没有账号? </text>
      现在去注册
    </h5>
  </view>

  <view v-else>
    <fui-button type="success" round size="large" @click="login_click">点击微信授权登录</fui-button>
  </view>
  <view class="bottom">
    本应用由AI智能软件开发平台CodeFlying自动开发
  </view>
</template>

<script setup>
import { getCurrentInstance, ref } from "vue";

const { proxy } = getCurrentInstance();
const emit = defineEmits(["loginSuccess", "loginFail"]);

const props = defineProps({
  login_type: { type: String, default: null },
  show_title: { type: String, default: "登陆" },
  relevanceTable: { type: String, required: true, },
  is_register: { type: String, required: false, },
  register_page: { type: String, required: false, default: "" }
});
const to_page = () => {
  proxy.$navigate(props.register_page)
}
const form = ref({
  phone: "",
  password: "",
});
// 验证码登陆的信息
const form_sms = ref({
  phone: '',
  code: null
});

const rules_sms = {
  phone: {
    rules: [
      {
        required: true,
        errorMessage: '请输入手机号'
      }
    ]
  },
  code: {
    rules: [
      {
        required: true,
        errorMessage: '请输入验证码'
      }
    ]
  }
};
const countdown = ref(0);
const isphoneValid = ref(false);

// 校验手机号
const validatephone = () => {
  if (form_sms.value.phone != null) {
    isphoneValid.value = /^1[3-9]\d{9}$/.test(form_sms.value.phone);
  }

};

const sendcode = async () => {
  validatephone()
  console.log(isphoneValid.value)
  if (!isphoneValid.value) {
    uni.showToast({
      title: "手机号格式错误",
      icon: "none"
    })
    return;
  }

  try {
    let res = await proxy.$api.login.getCodeByPhone(form_sms.value.phone)
    uni.showToast({
      title: "验证码发送成功",
      icon: "success"
    })
    countdown.value = 60;
    intervalId.value = setInterval(() => {
      if (countdown.value > 0) {
        countdown.value--;
      } else {
        clearInterval(intervalId.value);
      }
    }, 1000);
  } catch (e) {
    console.log(e)
    uni.showToast({
      title: "验证码发送失败",
      icon: "none"
    })
  }

};


const intervalId = ref(null);

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
      { required: true, errorMessage: '请输入密码' },
      { minLength: 3, maxLength: 18, errorMessage: '密码长度3-18位' }
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

/**
 * 手机号+验证码错误
 */
const submitForm_sms = () => {
  async function save_login_message() {
    var loin_sms_value = JSON.stringify({
      "phone": form_sms.value.phone,
      "code": form_sms.value.code,
    })
    var storage_key = await uni.setStorage({
      key: 'login_sms_key',      // 要存储的键
      data: loin_sms_value,    // 要存储的数据，可以是字符串、数字、对象等
    });
    return storage_key;
  }

  const loginForm = proxy.$refs.loginForm_sms
  loginForm.validate().then(() => {
    console.log(form_sms.value)

    form_sms.value.relevanceTable = props.relevanceTable
    proxy.$api.login.loginBySms(form_sms.value).then(async (res) => {

      var storage_key = await save_login_message();
      console.log(storage_key, "storage_key")
      login_success(res);

    }).catch(async err => {
      var storage_key = await save_login_message();
      login_error(err);
    });
  }).catch(error => {
    uni.showToast({ title: '请填写正确的信息', icon: 'none' });

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

  localStorage.setItem("h5_token", res.data);
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
::v-deep.uni-input-input {
  color: #999 !important;
}

::v-deep .uni-easyinput__content-input {
  padding-left: 20px !important;
}

::v-deep .uni-forms-item_error {
  width: 100%;
}

::v-deep.is-input-error-border .uni-easyinput__placeholder-class {
  color: #999;
}

::v-deep .code-input-container .is-input-border uni-input {
  width: 46%;
  position: relative;
  overflow: hidden;
  flex: none;
  line-height: 1;
  font-size: 14px;
  height: 35px;
}

.hello {
  max-width: 320px;
  width: 100%;
  height: 140px;
  font-size: 26px;
  letter-spacing: 1px;
  color: rgba(52, 57, 101, 1);
}

::v-deep .uni-forms-item {
  margin-bottom: 20px !important;
}

::v-deep .is-input-border {
  border-radius: 30px;
  padding-left: 15px;
  height: 58px;
  background: rgba(236, 242, 255, 1) !important;
  justify-content: flex-start;
  border: none;
}

::v-deep .uni-forms-item__label.no-label {
  display: none;
}

.title {
  width: 100%;
  max-width: 320px;
  font-size: 15px;
  display: flex;
  justify-content: flex-start;
  border-bottom: 1px solid rgba(230, 232, 240, 1);
  margin-bottom: 20px;

  h2 {
    color: rgba(93, 95, 239, 1);
    margin-bottom: 8px !important;
  }
}
h5 {
  color: #888;
  margin-top: 20px;
}


.login {
  width: 100%;
  max-width: 100%;
  height: 100vh;
  // background: #ffffff;
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



  .demo-form {
    width: 100%;
    margin-top: 20px;
  }

  .el-form-item {
    margin-bottom: 15px;
  }


  .input-field {
    // height: 45px;
    font-size: 16px;
  }

  .submit-btn {
    width: 100%;
    height: 55px;
    margin-top: 35px;
    background: rgba(93, 95, 239, 1);
    box-shadow: 0px 7px 40px rgba(0, 29, 176, 0.3);
    color: rgba(250, 251, 255, 1);
    border: none;
    border-radius: 30px;
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.3s;
    display: flex;
    align-items: center;
    justify-content: center;

    &:hover {
      background-color: rgba(93, 95, 239, 0.8);
    }
  }
}

.login-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  padding: 20px;
  box-sizing: border-box;
}

.input-group {
  margin-bottom: 20px;
}

.icon-input-container {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;

  .icon {
    position: absolute;
    left: 35rpx;
  }
}

.code-input-container {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;

  .send-code-button {
    position: absolute;
    right: 0;
    background: none;
    color: rgba(0, 4, 255, 1);
    font-size: 24rpx;
    border: none;

  }

  button::after {
    border: none;
  }

  .icon {
    position: absolute;
    display: flex;
    align-items: center;
    left: 27rpx;
  }
}

.verification-input {
  flex: 1;
  margin-right: 10px;
}

.login-button {
  width: 100%;
  background-color: #007aff;
  color: white;
  border-radius: 5px;
  //padding: 15px;
  text-align: center;
}

.bottom {
  width: 100%;
  display: flex;
  justify-content: center;
  font-size: 12px;
  color: rgba(166, 166, 166, 1);
  margin-bottom: 5px;
}
</style>