<template>
  <view class="all">
    <uni-forms ref="formRef" :modelValue="form" :rules="rules" label-width="auto" style="max-width: 600px;
  background-color: white;padding: 20px"
               @submit.prevent="onSubmit">
      <slot name="form-items"></slot>
      <uni-forms-item>
        <button type="primary" @click="onSubmit">提交</button>
        <button @click="onResetForm">重置</button>
      </uni-forms-item>
    </uni-forms>
  </view>
</template>

<script setup>
import {getCurrentInstance, ref} from "vue"

const {proxy} = getCurrentInstance();

// Importing to define props and emit
import {defineProps, defineEmits, toRefs} from 'vue';
import {onLoad} from "@dcloudio/uni-app";
// import {ElMessage} from 'element-plus';
// Defining props to receive form and rules from parent
const props = defineProps({
  form: {
    type: Object,
    required: false
  },
  rules: {
    type: Object,
    required: false,
    default: {}
  },
  table_module: {
    type: String,
    required: false
  },
  model: {
    type: String,
    required: false,
    default: "add"
  },
  id: {
    type: String,
    required: false
  },
  params: {
    type: Object,
    required: false
  }

});
const emit = defineEmits(['success', "fail", "message_perfect"]);

// Defining the reset function
const onResetForm = () => {
  for (const key in props.form) {
    if (props.form.hasOwnProperty(key)) {
      props.form[key] = null;
    }
  }
  // proxy.$refs.formRef.value.resetFields();
  // ElMessage.info('Form reset successfully!');
};
// Handle form submission
const handleSubmit = async () => {
  try {

    var apiName = "add";
    if (props.model != "add") {
      apiName = "update"
    }
    let res = await proxy.$api[props.table_module][apiName](props.form);
    console.log(res.data)
    let form_message = await proxy.$api[props.table_module]["get"](res.data);
    console.log(form_message)
    if (props.model == "add") {
      onResetForm()
    }
    emit("success", form_message.data)
  } catch (e) {
    console.log(e)
    emit("fail")
  }
}
onLoad(async () => {
  console.log(1212131313)
//   根据查询条件搜索
  if (props.model != "add") {
    // console.log(121212)
    let form = null
    // 如果id不为null，使用id查询
    if (props.id != null && props.id != '') {
      let res = await proxy.$api[props.table_module]["get"](props.id);
      form = res.data
    }
    // 如果param不为null，使用param查询,只取第一个
    else if (props.params != null) {
      let res = await proxy.$api[props.table_module]["page"](props.params);
      form = res.data.records[0]
    }

    // var form = res.data
    // var flag = true;
    // for (let key in form) {
    //   if (form.hasOwnProperty(key)) {
    //     if (form[key] == null || form[key] == "") {
    //       flag = false;
    //       break
    //     }
    //   }
    // }

    if (form != null) {
      for (let key in form) {
        if (form.hasOwnProperty(key)) {
          props.form[key] = form[key]
          // console.log(`${key}: ${person[key]}`);
        }
      }
    }

  }
})
// Defining the submit function
const onSubmit = () => {

  // Use the form reference to validate the form
  proxy.$refs.formRef.validate().then(res => {
    console.log("121212")
    handleSubmit()
  }).catch(err => {
    console.log('表单错误信息：', err);
  })
  ;
};


</script>

<style scoped lang="scss">
.all {
  height: 100%;
  //overflow: auto;
}

/* Add any custom styles for your form here */
::v-deep .uni-forms-item {
  width: 100%;
  display: flex;
  flex-direction: column !important;
}

::v-deep .uni-forms-item__content {
  display: flex !important;;
}

::v-deep .uni-popup__wrapper {
  width: 80% !important;
}
</style>