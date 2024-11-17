<template>
  <base-layout display="flex" direction="c" :w_full="true">
    <base-layout display="flex" style="width: 100%;" x="end" :w_full="true">
      <view class="search">
        <uni-easyinput class="uni-mt-5" suffixIcon="search" v-model="searchDataValue[firstSearchData]"
                       :placeholder="'请输入' + firstSearchDataComment" @iconClick="iconClick"></uni-easyinput>
        <button v-if="is_show_advance" size="mini" class="m-l-10" @click="toggleAdvanced">
          {{ $t('search.advanced_search') }}
          <!--          <a style="margin-left: 8px">-->
          <!--            &lt;!&ndash;            <uni-icons v-if="advanced" type="up"></uni-icons>&ndash;&gt;-->
          <!--            &lt;!&ndash;            <uni-icons v-else type="down"></uni-icons>&ndash;&gt;-->
          <!--          </a>-->
        </button>
      </view>
    </base-layout>

    <base-dialog ref="base_search_dailog" type="bottom">
      <template #dialog>

        <base-layout :w_full="true">
          <!--          <uni-collapse accordion>-->
          <!--            <uni-collapse-item :open="advanced" titleBorder="none">-->
          <uni-forms :inline="true" :modelValue="searchDataValue" class="m-20">
            <slot name="collapse" :item="searchDataValue">
            </slot>

            <uni-forms-item v-for="(item, index) in other_search_condition" :label="item.name" name="" :key="index">
              <base-select v-model="searchDataValue.other_search_condition[index]" :data="item.value"
                           :title="item.name"></base-select>


            </uni-forms-item>

            <uni-forms-item>
              <button class="button-botttom" size="mini" style="float: right;" @click="iconClick">
                {{ $t('search.search_text') }}
              </button>
            </uni-forms-item>
          </uni-forms>

          <!--            </uni-collapse-item>-->
          <!--          </uni-collapse>-->
        </base-layout>
      </template>

    </base-dialog>

  </base-layout>
</template>
<script>
export default {
  options: {
    styleIsolation: 'shared', // 解除样式隔离
  }
};
</script>
<script setup>

const {proxy} = getCurrentInstance()

const props = defineProps({
  is_show_advance: {
    type: Boolean,
    default: true
  },
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
let searchDataValue = ref(JSON.parse(JSON.stringify(props.searchData)))
searchDataValue.value.other_search_condition = []
const advanced = ref(false);

// 定义事件
const emit = defineEmits(["search"]);

function toggleAdvanced() {
  // advanced.value = !advanced.value
  // if (advanced.value){
  console.log("searchData is ", searchDataValue.value.hazardDescription)
  searchDataValue.value.hazardDescription = null
  proxy.$refs.base_search_dailog.showDialog()
  // advanced.value=false
  // }else {
  //   proxy.$refs.base_search_dailog.closeDialog()
  // }

}

async function iconClick() {
  console.log("搜索组件", searchDataValue.value)

  // searchDataValue.value.orderConditions = []
  for (let i = 0; i < searchDataValue.value.other_search_condition.length; i++) {
    let condition = searchDataValue.value.other_search_condition[i];
    if (condition==undefined){
      continue
    }
    var text = condition.replaceAll("'", "\"").replaceAll("?", "\"");
    console.log(text)
    var json = JSON.parse(text);
    for (var item in json) {
      searchDataValue.value[item] = json[item]
    }
  }
  searchDataValue.value.other_search_condition = []

  console.log()
  proxy.$refs.base_search_dailog.closeDialog()
  // toggleAdvanced()
  await emit("search", searchDataValue.value);
  for (var item in json) {
    searchDataValue.value[item] = null
  }
  advanced.value = false;
}
</script>


<style scoped lang="scss">
::v-deep uni-transition > view:first-child {
  z-index: 999;
  padding-bottom: 0px !important;
}

::v-deep .uni-forms {
  margin: 40 rpx;
}

::v-deep .w-full base-layout {
  width: 100% !important;
}

::v-deep .m-r-20 {
  display: flex;
  justify-content: flex-end;
}

::v-deep .uni-forms{
  width: 80% !important;
}

::v-deep .uni-forms-item__label{
  width: 168rpx !important;
}

::v-deep.uni-mt-5 {
  height: 72 rpx;
  border-radius: 10px;

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
  font-weight: 500;
  //height: 100% !important;
  align-items: center;
}

::v-deep .uni-collapse-item__title {
  display: none !important;
}

::v-deep uni-text {
  font-size: 14px;
  font-weight: 500;
  color: rgba(0, 0, 0, 1)
}

.search{
  width: 90%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0.6rem auto;
}

::v-deep .uni-select {
  padding-left: 19px;
  height: 100 rpx !important;
  border-radius: 10px;
  border: 1px solid rgba(229, 229, 229, 1);

}

::v-deep .uni-select__input-placeholder {
  font-size: 15px;
  color: rgb(166, 166, 166);
  font-weight: 500;
  letter-spacing: -0.15px;
}

::v-deep .uni-easyinput__content-input {
  padding-left: 19px !important;
  height: 100 rpx !important;
}

::v-deep.is-input-border {
  border-radius: 10px;
  border: 1px solid rgba(229, 229, 229, 1);
}

.uni-easyinput__placeholder-class {
  // font-size: 15px;
  font-size: 0.9375rem;
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
  // font-size: 13px;
  font-size: 0.8125rem;
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
  margin-top: 10px;
  color: rgba(255, 255, 255, 1);
  font-size: 15px !important;
  font-weight: 500;
}

.uni-forms-item:last-child {
  margin-bottom: 0px;
}

::v-deep.content {
  margin: 0 !important;
  border-radius: 20px 20px 0px 0px !important;
  padding: 1rem 0;
}

.m-20 {
  width: 100%;
}
</style>