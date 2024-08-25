<template>

</template>

<script setup>
import {getCurrentInstance, ref, toRefs} from 'vue';

const {proxy} = getCurrentInstance();
// 定义 props 并解构
const props = defineProps({
  table_model: {
    type: String,
    default: ''
  },
  not_null_field: {
    type: Array,
    default: () => []
  },
  id: {
    type: [String, Number],
    required: true
  },
  dataset: {
    type: String,
    default: ""
  }
});
console.log(props.table_model)
proxy.$api[props.table_model]["get"](props.id).then(res => {
  var form = res.data
  // var notNullField1 = JSON.parse(props.not_null_field);
  const notNullField1 = JSON.parse(props.not_null_field.replace(/'/g, '"'));

  for (var i = 0; i < notNullField1.length; i++) {
    console.log(notNullField1[i])
    console.log(form[notNullField1[i]]);
    if (form[notNullField1[i]] == null || form[notNullField1[i]] == "") {
      console.log("失败")
      triggerMessageFail(form)
      return
    }
  }
  triggerMessageSuccess(form)
})
const {tableModel, notNullField, id, dataset} = toRefs(props);

// 定义 emit 事件
const emit = defineEmits(['message_fail', 'message_success']);

// 方法定义
const triggerMessageFail = (form) => {
  emit('message_fail', form);
};

const triggerMessageSuccess = (form) => {
  emit('message_success', form);
};
</script>

<style scoped>

</style>