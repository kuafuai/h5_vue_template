<template>
  <base-layout display="flex" direction="c" :w_full="true">
    <base-layout display="flex" x="end" :w_full="true">
      <base-layout display="flex" y="center" class="m-r-20" style="width: 400px;">
        <uni-easyinput class="uni-mt-5" suffixIcon="search" v-model="props.searchData[firstSearchData]"
                       :placeholder="firstSearchPlaceholder" @iconClick="iconClick" @confirm="iconClick"></uni-easyinput>
        <view class="m-l-10" style="width: 120px;">
          <fui-button radius="96rpx" size="25" btnSize="small" @click="toggleAdvanced">
            高级筛选
            <a style="margin-left: 8px;">
              <uni-icons v-if="advanced" color="white" type="up"></uni-icons>
              <uni-icons v-else type="down" color="white"></uni-icons>
            </a>
          </fui-button>
        </view>
      </base-layout>
    </base-layout>
    <base-layout :w_full="true">
      <uni-collapse accordion>
        <uni-collapse-item :open="advanced" titleBorder="none">
          <uni-forms :inline="true" :modelValue="props.searchData" class="m-20">
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
  searchItems: {type: Array, default: () => []},
  searchData: {
    type: Object, default: () => {
    }
  },
  firstSearchData: {type: String},
  firstSearchPlaceholder: {type: String},
});
const advanced = ref(false);

// 定义事件
const emit = defineEmits(["refreshTableData"]);

function toggleAdvanced() {
  advanced.value = !advanced.value
}

function iconClick() {
  console.log(props.searchData)
  emit("refreshTableData", props.searchData);
  advanced.value = false;
}

</script>

<style scoped lang="scss">
::v-deep .uni-collapse-item__title {
  display: none !important;
}
</style>