<template>
  <base-layout display="flex" direction="c" :w_full="true">
    <base-layout display="flex" x="end" :w_full="true">
      <base-layout display="flex" y="center" class="m-r-20" style="width: 80%;">
        <uni-easyinput class="uni-mt-5" suffixIcon="search" v-model="props.searchData[firstSearchData]"
                       :placeholder="'请输入'+firstSearchDataComment" @iconClick="iconClick"></uni-easyinput>
        <button size="mini" class="m-l-10" @click="toggleAdvanced">
          高级搜索
          <a style="margin-left: 8px">
            <uni-icons v-if="advanced" type="up"></uni-icons>
            <uni-icons v-else type="down"></uni-icons>
          </a>
        </button>
      </base-layout>
    </base-layout>
    <base-dialog ref="diallog_slkdj" type="bottom">
      <base-layout :w_full="true">
        <uni-collapse accordion>
          <uni-collapse-item :open="advanced" titleBorder="none">
            <uni-forms :inline="true" :modelValue="props.searchData" class="m-20">
              <slot name="collapse" :item="props.searchData">
              </slot>

              <uni-forms-item v-for="(item,index) in other_search_condition" :label="item.name" name="">
                <base-select v-model="props.searchData.other_search_condition[index]" :data="item.value"
                             :title="item.name"></base-select>


              </uni-forms-item>

              <uni-forms-item>
                <button size="mini" style="float: right;" @click="iconClick">
                  搜索
                </button>
              </uni-forms-item>
            </uni-forms>

          </uni-collapse-item>
        </uni-collapse>
      </base-layout>
    </base-dialog>

  </base-layout>
</template>

<script setup>

import BaseDialog from "./base-dialog";
const props = defineProps({
  searchItems: {type: Array, default: () => []},
  searchData: {
    type: Object, default: () => {
    }
  },
  firstSearchData: {type: String},
  firstSearchDataComment: {type: String},
  other_search_condition: {
    type: Array,
    default: []
  }
});

props.searchData.other_search_condition = []
const advanced = ref(false);

// 定义事件
const emit = defineEmits(["search"]);

function toggleAdvanced() {
  advanced.value = !advanced.value

}

function iconClick() {
  console.log(props.searchData)
  emit("search", props.searchData);
  advanced.value = false;
}
</script>


<style scoped lang="scss">
::v-deep.uni-mt-5 {
  height: 72 rpx;
  border-radius: 10 ppx;

}

::v-deep.is-input-border {
  height: 100%;
}

::v-deep.content-clear-icon {
  color: rgba(93, 95, 239, 1) !important;

}

.m-l-10 {
  background: rgba(255, 255, 255, 1);
  border: 1px solid rgba(93, 95, 239, 1);
  border-radius: 100px;
  color: rgba(93, 95, 239, 1);
  font-size: 14px;
  font-weight: 550;
  height: 100% !important;

}

::v-deep .uni-collapse-item__title {
  display: none !important;
}

::v-deep uni-text {
  font-size: 12px;
  font-weight: 500;
  color: rgba(0, 0, 0, 1)
}

::v-deep .uni-select {
  padding-left: 19px;
  height: 100 rpx !important;
  border-radius: 10px;
  border: 1px solid rgba(229, 229, 229, 1);

}

::v-deep .uni-select__input-placeholder {
  font-size: 13px;
  color: rgb(166, 166, 166);
  font-weight: 500;
  letter-spacing: -0.15px;
}

::v-deep .uni-easyinput__content-input {
  padding-left: 19px !important;
  height: 100 rpx;
}

::v-deep.is-input-border {
  border-radius: 10px;
  border: 1px solid rgba(229, 229, 229, 1);
}

.uni-easyinput__placeholder-class {
  font-size: 13px;
  font-weight: 500;
  font-family: Inter;
}

::v-deep .uni-forms-item {
  align-items: center;
}

.button-botttom {
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 100px;
  background: rgba(93, 95, 239, 1);
  width: 100%;
  height: 100 rpx;
  color: rgba(255, 255, 255, 1);
  font-size: 14px !important;
  font-weight: 500;
}
</style>