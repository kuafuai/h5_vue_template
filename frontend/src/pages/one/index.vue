<template>
  <!--  <view>1111</view>-->

  <!--  <base-search :searchData="listQuery" firstSearchData="courseName" class="m-b-10" @refreshTableData="searchList">-->
  <!--    <template #collapse>-->
  <!--      <uni-forms-item label="姓名" name="courseName">-->
  <!--        <uni-easyinput type="text" v-model="listQuery.courseName" placeholder="请输入姓名"/>-->
  <!--      </uni-forms-item>-->
  <!--      <uni-forms-item label="年龄" name="teacherName">-->
  <!--        <input type="text" v-model="listQuery.teacherName" placeholder="请输入年龄"/>-->
  <!--      </uni-forms-item>-->
  <!--    </template>-->
  <!--  </base-search>-->

  <!--  <base-table ref="refTableList" :columns="[{label:'学科'},{label:'老师'},{label:'年级'},{label:'操作'}]"-->
  <!--              :selection="true" :params="listQuery" api="course_management.page">-->
  <!--    <template #default="{item}">-->
  <!--      <uni-td align="center">{{ item.courseName }}</uni-td>-->
  <!--      <uni-td align="center">{{ item.teacherName }}</uni-td>-->
  <!--      <uni-td align="center">{{ item.grade }}</uni-td>-->
  <!--      <uni-td align="center">-->
  <!--        <view style="display: flex; align-items: center;">-->
  <!--          <button class="uni-button" size="mini" type="primary" @click="update(item)">修改</button>-->
  <!--          <button class="uni-button" size="mini" type="warn" @click="remove(item)">删除</button>-->
  <!--        </view>-->
  <!--      </uni-td>-->
  <!--    </template>-->
  <!--  </base-table>-->

  <view>
    11
  </view>
  <view>
    22
  </view>
  <view>
    33
  </view>
  <text>1</text>
  <text>2</text>
  <text>3</text>
  <view>===</view>

  <view>
    <base-text-display v-for="(item, index) in test_list"  :text="index">
    </base-text-display>
  </view>

  <base-text-display v-model="test_obj.name" horizontal="left"></base-text-display>

  <base-image-display v-model="test_obj.image" radius="10"></base-image-display>

</template>
<script setup>

const {proxy} = getCurrentInstance();
onMounted(() => {
});

const listQuery = ref({});

// console.log(proxy.$page) 
// console.log(proxy.$route);

const test_obj = ref({
  name: 'tt',
  image: [
    "https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/shuijiao.jpg"
  ]
})

const test_list = ref([
  {
    name: '1121111'
  },
  {
    name: '21222233'
  },
])


function update(item) {
  console.log('update', item);
}

function remove(item) {
  console.log('remove', item);
}

function searchList() {

  console.log(listQuery)

  proxy.$refs.refTableList.refresh();

}

</script>