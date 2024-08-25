<template>
    <base-layout  display="flex" direction="c" :w_full="true">
        <base-layout display="flex" x="end" :w_full="true">
            <base-layout display="flex" y="center" class="m-r-20" style="width: 80%;">
                <uni-easyinput class="uni-mt-5" suffixIcon="search" v-model="props.searchData[firstSearchData]" placeholder="请输入" @iconClick="iconClick"></uni-easyinput>
                <button size="mini" class="m-l-10"  @click="toggleAdvanced">
                    高级搜索
                    <a style="margin-left: 8px" >
                        <uni-icons  v-if="advanced" type="up" ></uni-icons>
                        <uni-icons v-else type="down" ></uni-icons>
                    </a>
                </button>
            </base-layout>
        </base-layout>
        <base-layout :w_full="true">
            <uni-collapse accordion>
                <uni-collapse-item :open="advanced" titleBorder="none">
                    <uni-forms :inline="true" :modelValue="props.searchData"  class="m-20">
                        <slot name="collapse">
                        </slot> 

                        <uni-forms-item>
                            <button size="mini" style="float: right;" @click="iconClick"> 
                                搜索
                            </button>
                        </uni-forms-item>
                    </uni-forms>
                    
                </uni-collapse-item>
            </uni-collapse>
        </base-layout>
    </base-layout>
</template>

<script setup>

const props = defineProps({
  searchItems: { type: Array, default: () => [] },
  searchData: { type: Object, default: () => {} },
  firstSearchData: { type: String}
});
const advanced = ref(false);

// 定义事件
const emit = defineEmits(["refreshTableData"]);

function toggleAdvanced() {
  advanced.value = !advanced.value
}

function iconClick(){
    emit("refreshTableData");
    advanced.value=false;
}

</script>

<style scoped lang="scss">
::v-deep .uni-collapse-item__title {
    display: none !important;
}
</style>