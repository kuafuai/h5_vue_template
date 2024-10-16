<template>
  <uni-forms-item label="一级分类">
    <base-select v-model="data[0]" v-model:data="select_value[0]"
                 title="一级分类"></base-select>
  </uni-forms-item>

  <uni-forms-item label="二级分类">
    <base-select v-model="data[1]" v-model:data="select_value[1]"
                 title="二级分类"></base-select>
  </uni-forms-item>
</template>

<script setup>

import {getCurrentInstance, ref, toRefs} from 'vue';
import {onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();

const result = defineModel()
const data = ref([])
const fullData = ref([])
const select_value = ref({}) // 列表的值
const props = defineProps({
  category_size: {type: Number, default: 2}
})


/**
 * 查询值
 * @param index
 */
async function getSelectValue(index) {
  let parentId = null;
  if (index > 0) {

    parentId = data.value[index - 1];
    console.log("11111111", parentId)
  }
  let res = await proxy.$api.category.list({
    parentId: parentId,
    relevanceTable: "hazard_basic",
    relevanceTableColumn: "first_menu"
  })
  console.log(res)
  select_value.value[index] = res.data
  console.log(index, select_value.value[index])
}

getSelectValue(0)
// onShow(()=>{
//   getSelectValue(0)
// })

// 监听数据变化
watch(data.value, (newValue, oldValue) => {
  let  update_index=null
  console.log('数据发生变化，新的值为:', newValue);
  console.log('旧的值为:', oldValue);
  for (let index in data.value) {
    if (data.value[index] === '') {
      update_index=index
      data.value.splice(index, 1); // 删除值为空的元素
    }
    //  触发分类表的请求
  }
  result.value = data.value.map(i => "<" + i + ">").join(','); // 使用< > 包裹，like查询时包括
  if (update_index!=null){
    // result.value = JSON.stringify(data.value)
    getSelectValue(update_index,data.value.length)
  }


});

</script>

<style scoped>

</style>