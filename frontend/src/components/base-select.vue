<template>
    <uni-data-select v-bind="$attrs" :localdata="selectData">
        
    </uni-data-select>
</template>

<script setup>
import { onLoad } from "@dcloudio/uni-app";
const { proxy } = getCurrentInstance();
const props = defineProps({
  api: { type: String, default: '' }
});
const selectData  = ref([]);

onLoad(() => {
  refresh();
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

</script>