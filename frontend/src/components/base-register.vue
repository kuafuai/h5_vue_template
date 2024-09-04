<template>
  <view class="all_register">
    <h2>注册</h2>
    <uni-forms ref="formRef" :rules="rules" :modelValue="baseFormData">
      <uni-forms-item label="用户名" required name="username">
        <uni-easyinput v-model="baseFormData.username" placeholder="请输入用户名"/>
      </uni-forms-item>
      <uni-forms-item label="密码" required name="passwd">
        <uni-easyinput v-model="baseFormData.passwd" placeholder="请输入密码"/>
      </uni-forms-item>
      <uni-forms-item label="再次输入密码" required name="checkPassWd">
        <uni-easyinput v-model="baseFormData.checkPassWd" placeholder="请输入密码"/>
      </uni-forms-item>

      <uni-forms-item v-for="(item, index) in not_filed_list" :key="index" :label="item.description" required
                      :name="item.fieldName">
        <uni-easyinput @input="oninput12($event, item.fieldName)" :placeholder="'请输入' + item.description"/>
      </uni-forms-item>

      <uni-forms-item>
        <button type="primary" class="submit-btn" style="background-color: rgb(93, 95, 239);" @click="submitForm()">
          注册
        </button>
      </uni-forms-item>
    </uni-forms>
  </view>

</template>

<script setup>
import {onLoad} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance()
const baseFormData = ref({})
const prop = defineProps({
  relevanceTable: {type: String, required: true}
})
const not_filed_list = ref([])
const rules = ref({
  username: {
    rules: [
      {
        required: true,
        errorMessage: '请输入用户名'
      }
    ]
  },
  passwd: {
    rules: [
      {
        required: true,
        errorMessage: '请输入密码'
      }
    ]
  },
  checkPassWd: {
    rules: [
      {
        required: true,
        errorMessage: '请输入密码'
      }
    ]
  }
});
onLoad(async () => {
  var res = await proxy.$api.register.get_not_null_field(proxy.relevanceTable)
  if (res.code == 0) {
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

// 注册成功的方式
const emits = defineEmits(["success"])
const submitForm = async () => {

  proxy.$refs.formRef.validate().then(res => {
    console.log("121212")
    handleSubmit()
  }).catch(err => {
    console.log('表单错误信息：', err);
  })
  ;


}

const handleSubmit = async () => {
  baseFormData.value["relevanceTable"] = prop.relevanceTable
  var res = await proxy.$api.register.register(baseFormData.value)
  if (res.code == 0) {
    emits("success")
  }
}

const oninput12 = (value, fieldName) => {
  console.log(value, fieldName)
  baseFormData.value[fieldName] = value
}
</script>

<style scoped>
.all_register {
  text-align: center;
  padding: 5px;
}
</style>