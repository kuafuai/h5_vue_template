<template>
  <view class="all">
    <uni-forms ref="formRef" :modelValue="form" :rules="rules" label-width="auto" style="
  background-color: white;padding: 20px ;box-sizing:border-box;border-radius: 10px 10px;" @submit.prevent="onSubmit">
      <slot name="form-items"></slot>
      <uni-forms-item>
        <view style="display:flex;flex-direction:row">
          <button type="primary" @click="onSubmit" class="up">{{ $t('form.submit') }}</button>
          <button @click="onResetForm" class="reset">{{ $t('form.reset') }}</button>
        </view>
      </uni-forms-item>
    </uni-forms>
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
import {getCurrentInstance, onMounted, ref} from "vue"
import {defineProps, defineEmits, toRefs} from 'vue';
import {onLoad, onReady, onShow} from "@dcloudio/uni-app";
const {proxy} = getCurrentInstance();
// Importing to define props and emit
// import {ElMessage} from 'element-plus';
// Defining props to receive form and rules from parent
const props = defineProps({
  form: {
    type: Object,
    required: true
  },
  rules: {
    type: Object,
    required: true,
    default: {}
  },
  table_module: {
    type: String,
    required: true
  },
  query_module: {
    type: String,
    required: true
  },
  model: {
    type: String,
    required: true,
    default: "add"
  },
  id: {
    type: String,
    required: true
  },
  params: {
    type: Object,
    required: false,
    default: {}
  },
  // 缓存哪个字段的值
  cache_field: {
    type: Array,
    required: false,
    default: []
  },
  unique_id: {
    type: String,
    required: false,
    default: ""
  }

});
const emit = defineEmits(['success', "fail", "message_perfect"]);
defineExpose({
  load_form,
});
const isEmpty = (obj) => Object.keys(obj).length === 0;

// 保留其实的form
const origin_form = JSON.parse(JSON.stringify(props.form))

// Defining the reset function
const onResetForm = () => {
  for (const key in props.form) {
    if (origin_form.hasOwnProperty(key)) {
      props.form[key] = origin_form[key];
    } else {
      props.form[key] = null;
    }
  }
  // proxy.$refs.formRef.value.resetFields();
  // ElMessage.info('Form reset successfully!');
};

// watch(() => props.params, (tal,val) => {
//   console.log("watch",tal, val);
//   if(!isEmpty(tal)){
//     getFormData();
//   }
// },{immediate: true});

// Handle form submission
async function handleSubmit() {
  try {

    var apiName = "add";
    if (props.model != "add") {
      apiName = "update"
    }


    let res = await proxy.$api[props.table_module][apiName](props.form);
    console.log(res.data)
    if (props.cache_field != null && props.cache_field.length > 0) {
      let cache_value = {}
      for (var i = 0; i < props.cache_field.length; i++) {
        var item = props.cache_field[i]
        cache_value[item] = props.form[item]
      }
      console.log("cache_value", cache_value)
      // 保存到缓存中
      uni.setStorageSync(props.unique_id, cache_value);
    }


    let form_message = await proxy.$api[props.query_module]["get"](res.data);
    console.log(form_message)


    if (props.model == "add") {
      onResetForm()
    } else {
      if (form_message.data != null) {
        for (let key in form_message.data) {
          if (form_message.data.hasOwnProperty(key)) {
            props.form[key] = form_message.data[key]
            // console.log(`${key}: ${person[key]}`);
          }
        }
      }
    }
    emit("success", form_message.data)
  } catch (e) {
    console.log(e)
    emit("fail")
  }
}

onMounted(async () => {
  setTimeout(()=>{
    getFormData();
  },800)

})

async function getFormData() {
  if (props.model != "add") {

    let form = null

    // 如果id不为null，使用id查询
    if (props.id != null && props.id !== '') {
      let res = await proxy.$api[props.query_module]["get"](props.id);
      form = res.data
    }
    // 如果param不为null，使用param查询,只取第一个
    else if (!isEmpty(props.params)) {
      let res = await proxy.$api[props.query_module]["page"](props.params);
      form = res.data.records[0]
    }
    //
    if (form != null) {
      for (let key in form) {
        if (form.hasOwnProperty(key)) {
          props.form[key] = form[key]
        }
      }
    }
  }
}

onShow(() => {
  console.log("onshow")
  // 保留之前的数据
  if (props.cache_field != null && props.cache_field.length > 0) {
    let cache_form = uni.getStorageSync(props.unique_id)
    console.log(cache_form)
    if (cache_form != null) {
      for (let key in cache_form) {
        console.log("onshow-for", key)
        var item = props.form[key]
        if (item == undefined || item == null || item == '') {
          props.form[key] = cache_form[key]
        }
      }
    }
    console.log("执行", props.form)
  }
})

// Defining the submit function
function onSubmit() {

  console.log(props.rules, 'sadsadsadsadsaaddsa')
  // Use the form reference to validate the form
  setTimeout(() => {
    proxy.$refs.formRef.validate(props.rules).then(res => {
      console.log("proxy.$refs.formRef.validate()", proxy.$refs.formRef.validate())
      console.log("表单验证成功：", res);

      console.log("121212")
      handleSubmit()

    }).catch(err => {
      console.log('表单错误信息：', err);
    })
  }, 100);
}



/*onReady(() => {
  console.log("onReady", props.rules)
proxy.$refs.formRef.value.setRules(props.rules)
})*/

function load_form(item){

  for (let key in item) {
    if (item.hasOwnProperty(key)) {
      props.params[key] = item[key]
    }
  }

  getFormData()
}

</script>


<style scoped lang="scss">
::v-deep uni-button {
  border-radius: 10px;
  background-color: rgba(255, 255, 255, 1);
  border: 1px solid #dcdfe6;
  color: rgb(166, 166, 166);
  font-size: 0.875rem;
  padding-left: 20px;
  display: flex;
  align-items: center;
  height: 100rpx;
  width: 100%;
}

::v-deep uni-button::after {
  border: none
}

::v-deep.uni-forms-item__content base-select {
  width: 100%;
}

::v-deep .uni-forms-item__label {
  width: 100% !important;
}

::v-deep .uni-forms {
  width: 100%;
  background: white;
  padding: 40rpx;
  margin-bottom: 0.4375rem !important;
  box-sizing: border-box
}

.all {
  //height: 100%;
  // overflow: auto;
  background: rgb(245, 247, 250) !important;
  padding: 0.9357rem 0.9357rem;
  box-sizing: border-box;
}

::v-deep .uni-forms-item__error {
  width: 100%;
  right: 0 !important;

  span {
    color: rgba(227, 60, 100, 1);
  }
}

/* Add any custom styles for your form here */
::v-deep .uni-forms-item {
  width: 100%;
  display: flex;
  flex-direction: column !important;
  margin-bottom: 12px !important;
}

::v-deep .uni-forms-item__content {
  display: flex !important;
  flex-direction: column;
}

::v-deep .uni-popup__wrapper {
  width: 80% !important;
}

::v-deep .uni-easyinput__content-input {
  height: 50px;
  padding-left: 20px !important;
  border-radius: 20px;
}

::v-deep .uni-select {
  height: 50px;
  padding-left: 20px;
  border-radius: 10px;

}

::v-deep .uni-select__input-placeholder {
  color: rgba(166, 166, 166, 1);
  font-size: 14px;
}

::v-deep.uni-easyinput__placeholder-class {
  color: rgba(166, 166, 166, 1);
  // font-size: 14px;
  font-size: 0.875rem;
}

::v-deep uni-text {
  color: rgba(0, 0, 0, 1);
  font-size: 14px;
}

::v-deep .is-input-border {
  border-radius: 10px;
}

.up,
.reset {
  border-radius: 100px;
  width: 9rem;
  height: 3.2rem;
  font-size: 0.9375rem;
  color: rgba(255, 255, 255, 1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.up {
  background: rgba(93, 95, 239, 1);
}

.reset {
  background: rgba(166, 166, 166, 1);
}


::v-deep .is-input-error-border .uni-easyinput__placeholder-class {
  color: rgb(166, 166, 166) !important;
}
</style>
base-upload里面的样式
<style scoped>
.example-body {
  padding: 4px !important;
  border-radius: 10px;
  width: 100% !important;
  /* margin-bottom: 20px; */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
}

.uni-file-picker {
  /*border: 2px dashed #007aff; !* 添加虚线边框并使用主题颜色 *!*/
  padding: 15px;
  border-radius: 8px;
  background-color: #fff; /* 确保背景为白色 */
  transition: border-color 0.3s ease; /* 鼠标悬停时边框颜色渐变 */
}

.uni-file-picker:hover {
  border-color: #005bb5; /* 鼠标悬停时边框颜色变为深蓝 */
}

/*::v-deep .uni-forms-item__content {*/
/*  display: flex !important;*/
/*}*/

::v-deep .uni-file-picker__files {
  align-items: center !important;
}
</style>