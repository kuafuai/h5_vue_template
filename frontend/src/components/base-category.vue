<template>
  <uni-forms-item v-for="item in category_size_list" :label="'分类'+(item+1)">
    <base-select v-model="data[item]" v-model:data="select_value[item]"
                 :title="'分类'+(item+1)" @change="handleChange(item)"></base-select>
  </uni-forms-item>

  <!--  <uni-forms-item label=>-->
  <!--    <base-select v-model="data[1]" v-model:data="select_value[1]"-->
  <!--                 title="二级分类"></base-select>-->
  <!--  </uni-forms-item>-->
</template>

<script setup>

import {getCurrentInstance, ref, toRefs} from 'vue';
import {onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();

const result = defineModel()
const data = ref([])
const category_size_list = ref([])
const fullData = ref([])
const select_value = ref({}) // 列表的值
const props = defineProps({
  category_size: {type: Number, default: 2},
  relevanceTable:{type:String,default:""},
  relevanceTableColumn:{type:String,default:""},
})

for (var i = 0; i < props.category_size; i++) {
  category_size_list.value.push(i);
}


/**
 * 查询值
 * @param index
 */
async function getSelectValue(update_index, index) {
  console.log("121212`1212")
  let parentId = null;
  if (index > 0) {
    parentId = data.value[index - 1];
    console.log("11111111", parentId)
  }
  let res = await proxy.$api.category.list({
    parentId: parentId,
    relevanceTable: props.relevanceTable,
    relevanceTableColumn: props.relevanceTableColumn
  })
  console.log(res)

  if (update_index == null) {
    update_index = index
  }
  select_value.value[update_index] = res.data
  console.log(update_index, select_value.value, select_value.value[index])
}

getSelectValue(0)
// onShow(()=>{
//   getSelectValue(0)
// })

// 监听数据变化
watch(data.value, (newValue, oldValue) => {
  let update_index = null
  console.log('数据发生变化，新的值为:', newValue);
  console.log('旧的值为:', oldValue);
  for (let index in data.value) {
    if (data.value[index] === '') {
      update_index = index
      data.value.splice(index, 1); // 删除值为空的元素
    }
    //  触发分类表的请求
  }
  result.value = data.value.map(i => "<" + i + ">").join(','); // 使用< > 包裹，like查询时包括

  // getSelectValue(update_index, data.value.length)


});

const handleChange = (item) => {
  console.log("change",item)
  for (var i = item+1; i <= data.value.length; i++) {
    getSelectValue(i, i)
  }
}

</script>

<style scoped>

</style>