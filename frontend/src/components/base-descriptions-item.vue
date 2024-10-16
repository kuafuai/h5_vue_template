<!--仅在外键关系是一对多个情况下渲染使用-->
<template>
  <uni-section
      :sub-title="sub_title_ref_value"
      :type="type"
      :title="title"
      class="mb-10"
  />
</template>

<script setup>
const prop = defineProps({
  type: {type: String},
  title: {type: String},
  sub_title: {type: String}
})
let sub_title_ref_value = ref("")
let more_result_list = ref([]
)
onMounted(() => {
  init()


})

watch(
    () => prop.sub_title, // 监听的目标是 props.show_value
    (newValue, oldValue) => {
      init()
      // console.log('show_value 变化:', oldValue, '=>', newValue);
      // show_value_one.value = props.show_value;

      // 在这里可以执行你想要的逻辑，比如数据处理
    }
);
const init=()=>{
  console.log("1111111111",prop.sub_title)
  if (prop.sub_title != undefined && prop.sub_title != null) {
    let obj = JSON.parse(prop.sub_title);
    if (typeof obj === 'object' && obj !== null && !Array.isArray(obj)) {
      // 获取对象的所有 value 列表
      var values = Object.values(obj);
      console.log(values); // 输出所有的值
      more_result_list.value = values;
    } else {
      // 是列表直接赋值
      more_result_list.value = s
    }
    sub_title_ref_value.value = more_result_list.value.map(item => item.text).join(',');

  }else {

  }
}
</script>

<style scoped>

</style>