<template>
  <view class="all_register">
    <view class="title">
      <h2>{{ $t('register.title') }}</h2>
    </view>
    <!--    <view>-->
    <view v-if="register_type === 'sms'">
      <uni-forms :modelValue="baseFormData" :rules="rules" ref="formRef" label-width="auto" status-icon>
        <!-- 手机号输入框 -->
        <uni-forms-item :label="$t('register.phone')" name="phone" required>
          <uni-easyinput v-model="baseFormData.phone" :placeholder="$t('register.enter_phone')"/>
        </uni-forms-item>

        <!-- 验证码输入框 -->
        <uni-forms-item :label="$t('register.phone_verification_code')" name="code" required>
          <view class="code-input-container">
            <uni-easyinput v-model="baseFormData.code" :placeholder="$t('register.enter_verification_code')"/>
            <button class="send-code-button" @click="sendcode" :disabled="countdown > 0">
              {{ countdown > 0 ? `${countdown}s后重发` : '获取验证码' }}
            </button>
          </view>


        </uni-forms-item>
        <uni-forms-item v-for="(item, index) in not_filed_list" :key="index" :label="item.description" required
                        :name="item.fieldName">
          <uni-easyinput @input="oninput12($event, item.fieldName)" :placeholder="$t('register.enter_prefix') + item.description"/>
        </uni-forms-item>

        <uni-forms-item>
          <button type="primary" class="submit-btn" style="background-color: rgb(93, 95, 239);" @click="submitForm()">
            {{ $t('register.title') }}
          </button>
        </uni-forms-item>
      </uni-forms>

    </view>
    <!--    </view>-->
    <view v-else-if="register_type === 'passwd'">
      <uni-forms ref="formRef" :rules="rules" :modelValue="baseFormData">

        <uni-forms-item :label="$t('register.user_name_label')" required name="username">

          <uni-easyinput v-model="baseFormData.username" :placeholder="$t('register.enter_user_name')"/>
        </uni-forms-item>
        <uni-forms-item :label="$t('register.pass_word')" required name="passwd">
          <uni-easyinput v-model="baseFormData.passwd" :placeholder="$t('register.enter_pass_word')"/>
        </uni-forms-item>
        <uni-forms-item :label="$t('register.again_pass_word')" required name="checkPassWd">
          <uni-easyinput v-model="baseFormData.checkPassWd" :placeholder="$t('register.enter_again_pass_word')"/>
        </uni-forms-item>

        <uni-forms-item v-for="(item, index) in not_filed_list" :key="index" :label="item.description" required
                        :name="item.fieldName">
          <view v-if="selectFiled && selectFiled.hasOwnProperty(item.fieldName)">
            <base-select :clear="false" class="  is-input-border" @change="oninput12($event, item.fieldName)"
                         :api="selectFiled[item.fieldName]"
                         :title="item.description"></base-select>
            <!--            <base-select></base-select>-->
          </view>
          <view v-else>
            <uni-easyinput @input="oninput12($event, item.fieldName)" :placeholder="$t('register.enter_prefix') + item.description"/>
          </view>


        </uni-forms-item>

        <uni-forms-item>
          <button type="primary" class="submit-btn" style="background-color: rgb(93, 95, 239);" @click="submitForm()">
            {{ $t('register.title') }}
          </button>
        </uni-forms-item>
      </uni-forms>
    </view>

  </view>

</template>
<script>
export default {
  options: {
    styleIsolation: 'shared', // 解除样式隔离
  }
};
</script>
<script setup>
import {onLoad} from "@dcloudio/uni-app";
import {ref} from "vue";

const {proxy} = getCurrentInstance()

const prop = defineProps({
  relevanceTable: {type: String, required: true},
  register_type: {
    type: String,
    required: false,
    default: "passwd"
  },
  selectMap: {
    type: String,
    required: false,
    default: ""
  },
  tableName: {
    type: String,
    required: false,
    default: ""
  }
})
let baseFormData = ref({
  "registerType": prop.register_type

})

const selectFiled = ref({})
if (prop.selectMap != null && prop.selectMap != '' && prop.selectMap != undefined) {
  var text = prop.selectMap.replaceAll("'", "\"");
  console.log(text)
  selectFiled.value = JSON.parse(text)
}


const not_filed_list = ref([])
const rules = ref({
  username: {
    rules: [
      {
        required: true,
        errorMessage: proxy.$tt('register.enter_user_name')
      }
    ]
  },
  passwd: {
    rules: [
      {
        required: true,
        errorMessage: proxy.$tt('register.enter_pass_word')
      }
    ]
  },
  checkPassWd: {
    rules: [
      {
        required: true,
        errorMessage: proxy.$tt('register.enter_again_pass_word')
      }
    ]
  },
  phone: {
    rules: [
      {
        required: true,
        errorMessage: proxy.$tt('register.enter_phone')
      }
    ]
  },
  code: {
    rules: [
      {
        required: true,
        errorMessage: proxy.$tt('register.enter_verification_code')
      }
    ]
  }
});

const countdown = ref(0);
const isphoneValid = ref(false);

// 校验手机号
const validatephone = () => {
  if (baseFormData.value.phone != null) {
    isphoneValid.value = /^1[3-9]\d{9}$/.test(baseFormData.value.phone);
  }

};

// 注册成功的方式
const emits = defineEmits(["success"])
const intervalId = ref(null)
const sendcode = async () => {
  console.log(34343434)
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
    let res = await proxy.$api.login.getCodeByPhone(baseFormData.value.phone)
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

onLoad(async () => {
  uni.getStorage({
    key: "login_sms_key",
    success: function (res) {
      var data = res.data;
      var login_message = JSON.parse(data);
      baseFormData.value.phone = login_message.phone
      baseFormData.value.code = login_message.code
    },
    fail: function () {
      console.log('获取用户名失败');
    }
  })
  var res = await proxy.$api.register.get_not_null_field(proxy.relevanceTable, proxy.register_type)
  if (res.code == 0) {
    if (prop.register_type == 'sms' && (res.code == null || res.data.length == 0)) {
      emits("success")
    }
    not_filed_list.value = res.data
    for (var i = 0; i < not_filed_list.value.length; i++) {
      var element = not_filed_list.value[i];
      rules.value[element.fieldName] = {
        rules: [
          {
            required: true,
            errorMessage: '请输入' + element.description
          }
        ]
      }
    }
  }
})


const submitForm = async () => {

  proxy.$refs.formRef.validate().then(res => {
    console.log("121212")
    handleSubmit()
  }).catch(async err => {
    console.log('表单错误信息：', err);

  })
  ;


}

const handleSubmit = async () => {
  baseFormData.value["relevanceTable"] = prop.relevanceTable
  try {
    var res = await proxy.$api.register.register(baseFormData.value)
    if (res.code == 0) {
      var result = await uni.removeStorage({
        key: "login_sms_key"
      })
      baseFormData.value.phone = null
      baseFormData.value.code = null
      emits("success")

    }
  } catch (e) {
    var result = await uni.removeStorage({
      key: "login_sms_key"
    })
    baseFormData.value.phone = null
    baseFormData.value.code = null
  }

}

const oninput12 = (value, fieldName) => {
  console.log(value, fieldName)
  baseFormData.value[fieldName] = value
}
</script>

<style scoped lang="scss">
::v-deep .title view {
  width: 100%;
  max-width: 320px;
  font-size: 0.9375rem;
  display: flex;
  justify-content: flex-start;
  margin-bottom: 20px;
  color: rgba(93, 95, 239, 1);
  margin-bottom: 8px !important;
}
::v-deep.is-input-error-border .uni-easyinput__placeholder-class {
  color: #999;
}

::v-deep .uni-forms-item.is-direction-left {
  flex-direction: column;
}

::v-deep .uni-forms-item {
  margin-bottom: 10px;
}

::v-deep .is-input-border {
  border-radius: 30px;
  height: 50px;
  background: rgba(236, 242, 255, 1) !important;
}

::v-deep .uni-forms-item__label {
  width: 100% !important;
  color: rgba(52, 57, 101, 1);
  font-weight: 300;
}

::v-deep uni-text {
  margin-left: 2px !important;
}

::v-deep .uni-easyinput__placeholder-class {
  // font-size: 12px;
  font-size:0.75rem !important;
  color: rgba(166, 166, 166, 1)
}

.submit-btn {
  height: 50px;
  border-radius: 20px;
  background: rgba(93, 95, 239, 1);
  box-shadow: 0px 7px 40px rgba(0, 29, 176, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 15px;
  color: rgba(250, 251, 255, 1);
}

.title {
  width: 100%;
  font-size: 0.9375rem;
  display: flex;
  justify-content: flex-start;
  border-bottom: 1px solid rgba(230, 232, 240, 1);
  margin-bottom: 20px;

  h2 {
    color: rgba(93, 95, 239, 1);
    margin-bottom: 8px !important;
  }
}

.all_register {
  text-align: center;
  padding: 30px;
  height: 100vh;
  // background: #ffffff;
}

.send-code-button {
  background-color: #007aff;
  color: white;
  margin: 5px;
}

.code-input-container {
  display: flex;
  align-items: center;
  justify-content: center;
}

::v-deep .uni-select {
  border: none;
  padding: 0 5px 0 0px;
}

::v-deep .uni-select__input-text {
  width: 89%;
  text-align: left;
  padding-left: .8rem;
  color: rgb(166, 166, 166);
  font-size: 12px;
}
</style>