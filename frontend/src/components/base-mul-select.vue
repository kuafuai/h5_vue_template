<template>
  <base-wrapper class="w-full borderradio">
    <view class="example-body m-y-20">

      <uni-badge v-for="(item, index) in fileList" :key="index"
                 class="uni-badge-left-margin m-x-10 tag_box" text="X" @click="remove_tag(item)" absolute="rightTop"
                 size="small">
        <uni-tag :circle="true" :inverted="true" :text="item.text" type="primary" size="small"/>
      </uni-badge>
    </view>
<!--    <uni-data-select :clear="false" :placeholder="'请选择'+title" :localdata="selectData"-->
<!--                     :filter="true" @change="select_change">-->
<!--    </uni-data-select>-->
    <el-select  filterable remote :remote-method="load_site"
               :loading="site_loading"
                @change="select_change"
               placeholder="通过用户名称模糊查询">
      <el-option v-for="item in site_options" :key="item.value" :label="item.label" :value="item.value">
      </el-option>
    </el-select>
  </base-wrapper>
</template>
<script setup>
const {proxy} = getCurrentInstance();
import {onLoad} from "@dcloudio/uni-app";

const props = defineProps({
  ids: [String],
  texts: [String],
  api: {type: String, default: ''},
  title: {type: String, default: ""}
});

const emits = defineEmits(["change"]);

const fileList = ref([])

watch(() => props.texts, val => {
  if (val) {
    // 首先将值转为数组
    const ids_list = Array.isArray(val) ? val : props.ids.split(",");
    const text_list = Array.isArray(val) ? val : props.texts.split(",");
    fileList.value = ids_list.map((key, index) => ({'id': key, 'text': text_list[index]}));
  } else {
    fileList.value = [];
  }
}, {deep: true, immediate: true});

const selectData = ref([]);

onLoad(() => {
  // refresh();
});

function refresh() {
  getApiData();
}

async function getApiData() {
  let response = await apiMethod();
  selectData.value = response.data;
}

function apiMethod() {
  return props.api.split('.').reduce((acc, item) => acc[item], proxy.$api)();
}

const site_loading = ref(false);
const site_options = ref([]);

function load_site(query) {
  if (query !== '') {
    site_loading.value = true;
    setTimeout(() => {

      site_loading.value = false;

      let params = {
        userName: query
      }

      proxy.$api.userinfo.select_list(params).then(res => {
        if (res.code === 0 && res.data.length > 0) {
          site_options.value = res.data;

        } else {
          site_options.value = [];
        }
      })

    }, 200);
  } else {
    site_options.value = [];
  }
}

function select_change(value) {
  console.log(value);
  console.log(site_options.value)
  const selectedOption = site_options.value.find(option => option.value === value);
  if (selectedOption) {
    let exists = fileList.value.some(item => item.id === String(selectedOption.value));
    if (!exists) {
      fileList.value.push({'id': String(selectedOption.value), 'text': selectedOption.text})
      emits("change", fileList.value);
    } else {
      uni.showToast({
        'title': '该数据已经添加！',
        'position': 'center',
        'icon': 'error'
      });
    }
  }
}

function remove_tag(item) {
  fileList.value = fileList.value.filter(p => p.id !== item.id)
  emits("change", fileList.value);
}

</script>
<style>
.example-body {
  display: flex;
  flex-wrap: wrap;
}

.tag_box {
  margin: .5rem;
}
.borderradio{
  border-radius: 11px
}
::v-deep .uni-select__selector{
  height: 8rem;
  overflow: auto;
}
</style>