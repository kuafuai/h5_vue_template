<template>
  <web-view id="foo" v-if="isTrue" :src="previewUrl" style="width:640px; height:480px"></web-view>
  <view v-if="login_type === 'passwd'" class="login">
    <view class="hello">
      <view class="nihao">{{ $t('login.nihao') }}</view>
      <view class="code"> {{ $t('login.code') }} {{left_title}}</view>
    </view>
    <view class="title">
      <h2>{{ show_title }}</h2>
    </view>
    <!-- <uni-forms style="max-width: 320px; width: 100%; margin: 0 auto;" :="" :rules="" ref="loginForm_sms" label-width="auto" status-icon> -->
    <uni-forms ref="formRef" style="max-width: 320px; width: 100%; margin: 0 auto;" :modelValue="form" :rules="rules"
               label-width="auto" status-icon>

      <uni-forms-item name="phone">
        <view class="icon-input-container">
          <uni-easyinput :placeholder="$t('login.enter_user_name')" v-model="form.phone" class="input-field"/>
          <view class="icon">
            <img src="../static/peo.png" style="width:13px;height:13px;" alt="">
          </view>
        </view>
      </uni-forms-item>
      <uni-forms-item name="password">
        <view class="icon-input-container">
          <uni-easyinput :placeholder="$t('login.enter_pass_wd')" type="password" v-model="form.password" class="input-field"/>
          <view class="icon">
            <img src="../static/pass.png" style="width:13px;height:13px;" alt="">
          </view>
        </view>
      </uni-forms-item>
      <uni-forms-item>
        <button class="submit-btn" @click="submitForm(formRef)">
          {{ $t('login.login_btn') }}
        </button>
      </uni-forms-item>
    </uni-forms>
    <!-- <h5>默认账户密码：admin / 123456</h5> -->
    <h5 v-show="is_register" @click="to_page" style="color: rgba(93, 95, 239, 1);font-size: 13px;font-weight:450">
      <text style="color:rgba(52, 57, 101, 1);font-weight:450">{{ $t('login.login_msg') }}</text>
      {{ $t('login.login_reg') }}
    </h5>
    <view>

    </view>
  </view>
  <view v-else-if="login_type === 'sms'" class="login">
    <view class="hello">
      <view class="nihao">{{ $t('login.nihao') }}</view>
      <view class="code"> {{ $t('login.code') }}</view>
    </view>
    <view class="title">
      <h2>{{ show_title }}</h2>
    </view>
    <uni-forms style="max-width: 320px; width: 100%; margin: 0 auto;" :modelValue="form_sms" :rules="rules_sms"
               ref="loginForm_sms" label-width="auto" status-icon>
      <!-- 手机号输入框 -->
      <uni-forms-item name="phone">
        <view class="icon-input-container">
          <uni-easyinput v-model="form_sms.phone" :placeholder="$t('login.enter_phone')" type="number" maxlength="11"/>
          <view class="icon">
            <img src="../static/phone.png" style="width:11px;height:16px; margin-top:3px" alt="">
          </view>
        </view>
      </uni-forms-item>

      <!-- 验证码输入框 -->

      <uni-forms-item name="code">
        <view class="code-input-container">
          <uni-easyinput style="width:50px;" v-model="form_sms.code" :placeholder="$t('login.enter_verification_code')"/>
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
        {{ $t('login.login_btn') }}
      </button>
    </uni-forms>

    <h5 v-show="is_register" @click="to_page" style="color: rgba(93, 95, 239, 1);font-size: 13px;font-weight:450">
      <text style="color:rgba(52, 57, 101, 1);font-weight:450">{{ $t('login.login_msg') }}</text>
      {{ $t('login.login_reg') }}
    </h5>
  </view>


  <view v-else-if="login_type === 'h5'" class="login">
    <fui-button type="success" round size="large" @click="login_click">{{ $t('login.wx_login') }}</fui-button>

  </view>
  <view v-else class="wxlogin">
    <view class="h5Login">
      {{props.title}}
    </view>
    <button class="identity" open-type="getPhoneNumber" @getphonenumber="getPhoneNumber" v-if="radio1">
      {{ $t('login.wx_login') }}
    </button>
    <button class="identity" v-else @click="btnF1">
      {{ $t('login.wx_login') }}
    </button>
    <view class="radios">
      <label class="radio">
        <radio style="zoom: 74%;margin:0.15rem 18rpx 0rpx 0rpx" @click="isCheck" value="radio1" :checked="radio1"/>
        <text style="color: rgba(169,169,169,1)">{{$t('login.read_and_agree')}}
          <text class="agreement" @click.stop="services">《{{$t('login.service_agreement')}}》</text>
          <text
              class="agreement">及
          </text>
          <text class="agreement" @click.stop="policy">《{{$t('login.privacy_agreement')}}》</text>
        </text>
      </label>
    </view>
    <view class="add">
      {{end_content_show}}
    </view>
  </view>

  <view class="bottom" v-if="login_type === 'passwd' || login_type === 'sms' || login_type === 'h5'">
    {{end_content_show}}
  </view>
</template>

<script setup>
import {getCurrentInstance, ref} from "vue";
import service from "@/utils/request.js";
const radio1 = ref(false)
const isCheck = () => {
  radio1.value = !radio1.value;
}


const isTrue = ref(false)
const {proxy} = getCurrentInstance();

const services = () => {
  proxy.$navigate( "/pages/services/index",false)
}
const policy = () => {
  proxy.$navigate( "/pages/policy/index",false)
}
const btnF1 = () => {
  uni.showToast({
    icon: "none",
    title: proxy.$tt('login.agreement_toast'),
    duration: 2000,
  });
  return;
}
const emit = defineEmits(["loginSuccess", "loginFail"]);

const props = defineProps({
  login_type: {type: String, default: null},
  show_title: {type: String, default: "登陆"},
  relevanceTable: {type: String, required: true,},
  is_register: {type: String, required: false,},
  register_page: {type: String, required: false, default: ""},
  title: {type: String, required: false, default: "码上飞CodeFlying"},
  end_content: {type: String, required: false, default: "本应用由AI智能软件开发平台CodeFlying自动开发"}
});


const end_content_show = ref('')
let left_title = import.meta.env.VITE_APP_NAME;
let app_vip = import.meta.env.VITE_APP_VIP === 'true'
if(app_vip){
  end_content_show.value=''
}
else{
  end_content_show.value=props.end_content
  left_title = "CodeFlying!"
}



const to_page = () => {
  proxy.$navigate(props.register_page)
}
const form = ref({
  phone: "18852718858",
  password: "123456",
});
// 验证码登陆的信息
const form_sms = ref({
  phone: '18852718858',
  code: null
});

const rules_sms = {
  phone: {
    rules: [
      {
        required: true,
        errorMessage: proxy.$tt('login.enter_phone')
      }
    ]
  },
  code: {
    rules: [
      {
        required: true,
        errorMessage: proxy.$tt('login.enter_verification_code')
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
      title: proxy.$tt('login.phone_number_format_error_text'),
      icon: "none"
    })
    return;
  }

  try {
    let res = await proxy.$api.login.getCodeByPhone(form_sms.value.phone)
    uni.showToast({
      title: proxy.$tt('login.verification_code_success_text'),
      icon: "success"
    })
    console.log(res);
    if(res.code === 0 && res.data){
      form_sms.value.code = res.data;
    }
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
      title: proxy.$tt('login.verification_code_fail_text'),
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
        errorMessage: proxy.$tt('login.enter_user_name')
      },
      {
        minLength: 1,
        maxLength: 100,
        errorMessage: proxy.$tt('login.username_length_rule_text')
      }
    ]
  },
  password: {
    rules: [
      {required: true, errorMessage: proxy.$tt('login.enter_pass_wd')},
      {minLength: 3, maxLength: 18, errorMessage: proxy.$tt('login.password_length_rule_text')}
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
    uni.showToast({title: proxy.$tt('login.please_enter_correct_info'), icon: 'none'});

  });
};


function login_error(err) {
  emit("loginFail");
}

const login_success = (res) => {
  uni.showToast({
    title: proxy.$tt('login.login_success_text'),
    icon: "success",
    duration: 2000
  });
  console.log("123456765", "登陆成功")
  // #ifdef MP-WEIXIN
  // uni.setStorageSync('h5_token', res.data)
  console.log("=======================", res)
  proxy.$api.login.getLoginUser().then((res) => {
    const item = res.data;
    uni.setStorageSync("currentUser", JSON.stringify(item));
    emit("loginSuccess", item);
  }).catch(err => {
    console.log(err);
  });
  // #endif

  // #ifdef H5
  uni.setStorageSync("h5_token", res.data);
  proxy.$api.login.getLoginUser().then((res) => {
    const item = res.data;
    uni.setStorageSync("currentUser", JSON.stringify(item));
    emit("loginSuccess", item);
  }).catch(err => {
    console.log(err);
  });
  // #endif
}
const previewUrl = ref("")
const phone = ref("")


const getPhoneNumber = function (e) {
  console.log(e, "getPhoneNumber")
  if (e.detail.errMsg === "getPhoneNumber:fail user deny") {
    // 用户拒绝授权手机号
    uni.showToast({
      title: proxy.$tt('login.deny_authorization_text'),
      icon: "none",
      duration: 2000,
    });
  } else {
    // 用户同意授权手机号
    let token = uni.getStorageSync("token")
    uni.login({
      success: (res) => {
        console.log(res, "res============")

        service({
          url: "/login/phone",
          method: "get",
          data: {
            code: e.detail.code,
            relevanceTable: props.relevanceTable,
          }
        }).then((res) => {
          console.log("获取手机号之后的结果：", res)
          if (res.code === 0) {
            phone.value = res.data
            login_click();
          }

        })
      }
    })
  }
}

function login_click() {
  let url = `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=${encodeURIComponent(callback)}&response_type=code&scope=snsapi_base&state=codeflying#wechat_redirect`;
  // #ifdef MP-WEIXIN
  if (props.login_type === 'wechat') {
    console.log("进来了", import.meta.env.VITE_APP_SERVICE_API + '/login/wxApp')
    uni.login({
      "provider": "weixin",
      "onlyAuthorize": true, // 微信登录仅请求授权认证
      success: function (event) {
        const {code} = event
        //客户端成功获取授权临时票据（code）,向业务服务器发起登录请求。
        uni.request({
          url: import.meta.env.VITE_APP_SERVICE_API + '/login/wxApp', //仅为示例，并非真实接口地址。
          method: 'POST',
          data: {
            code: event.code,
            phone: phone.value,
            relevanceTable: props.relevanceTable,
          },
          success: (res) => {
            console.log(res, "loginSuccess")
            if (res.data.code === 0) {
              //获得token完成登录
              uni.setStorageSync('h5_token', res.data.data)
              // emit("loginSuccess")
              login_success(res)
            } else {
              login_error()
            }
          }
        });
      },
      fail: function (err) {
        login_error(err)
      }
    })
  }
  // #endif

  // #ifdef H5
  if (props.login_type === 'h5') {
    window.location.href = url;
  }
  // #endif
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
  padding: 1rem;
  justify-content: center;
  box-sizing: border-box;

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

.wxlogin {
  width: 100%;
  max-width: 100%;
  height: 100vh;
  // background: #ffffff;
  border-radius: 10px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1rem;
  justify-content: normal;
  box-sizing: border-box;
}


.h5Login {
  width: 100%;
  height: 11.75rem;
  display: flex;
  justify-content: center;
  align-items: center;
  line-height: 1.9375rem;
  font-size: 1.375rem;
  font-weight: 500;
  color: rgba(65, 67, 79, 1);
}

.identity {
  color: rgba(250, 251, 255, 1);
  background-color: rgba(93, 95, 239, 1);
  border-radius: 10px;
  line-height: 25px;
  width: 512rpx;
  height: 88rpx;
  font-size: 36rpx;
  display: flex;
  justify-content: center;
  align-items: center;
}

.radios {
  display: flex;
  justify-content: center;
  font-size: 28rpx;
  margin-top: 48rpx;
  height: 300rpx;

  .radio {
    display: flex;
    justify-content: center;

    .agreement {
      color: rgba(93, 95, 239, 1);
    }
  }
}

.add {
  line-height: 19px;
  color: rgba(169, 169, 169, 1);
  font-size: 0.8125rem;
  margin-top: 10.375rem;
}
</style>