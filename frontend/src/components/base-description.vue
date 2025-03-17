<template>
  <view>
    <view v-if="description != null && description != {}" class="margin-top" :column="4">
      <view class="all">
        {{ $t('description.all_info') }}：
      </view>
      <view class="all_content">
        <slot name="description-items" :description="description">

        </slot>
      </view>

    </view>

    <view v-else class="list_box">
      <view class="nodata">
        <img src="../static/noData.png" style="width:200px;height:auto" alt="">
        <view class="noText">{{ $t('description.no_text') }}</view>
      </view>
    </view>
  </view>

</template>

<script setup>
import {getCurrentInstance} from "vue"

const {proxy} = getCurrentInstance();
import {defineProps, ref} from 'vue';
import {onLoad, onShow} from "@dcloudio/uni-app";

const props = defineProps({
  api: {
    type: String,
    default: '',
  },
  params: {
    type: Object, default: () => {
    }
  },

  tableName: {
    type: String,
    default: null,
  },
  agentFieldName: {
    type: Array,
    default: [],
  },
  primaryName: {
    type: String,
    default: null,
  }
});
const description = defineModel()
const refresh = async () => {

  if (!isEmpty(props.params)) {
    let response = await props.api.split('.').reduce((acc, item) => acc[item], proxy.$api)(props.params);
    console.log("详情页面描述【response】", response)
    description.value = response.data.records[0];
    process_ai_agent()
  }

}

const isEmpty = (obj) => Object.keys(obj).length == 0;

watch(() => props.params, (value) => {
  console.log("watch", value)
  if (!isEmpty(value) && (description.value == null || isEmpty(description.value))) {
    refresh()
  }
}, {immediate: true, deep: true})


var interval = null; // 全局变量存储 interval 引用

/**
 * 开启定时器
 */
function startInterval() {
  if (interval) clearInterval(interval); // 避免重复启动
  interval = setInterval(async () => {
    var is_end = true;
    for (let i = 0; i < props.agentFieldName.length; i++) {
      var agentFieldNameElement = props.agentFieldName[i];
      var item = description.value[agentFieldNameElement];
      if (item == null) {
        is_end = false;
        //  说明正在生成中,发送一次请求
        let response = await proxy.$api.ai_agent_kuafu.isEnd({
          tableName: props.tableName,
          tableId: description.value[props.primaryName],
          agentFieldName: agentFieldNameElement
        });

        if (response.code === 0 && response.data === true) {
          refresh();
        }
      }
    }
    if (is_end) {
      // 如果全部ai字段都是不空，则结束
      clearInterval(interval);
      interval = null;
    }
  }, 5000);
}


/**
 * 处理ai类型的字段
 */
const process_ai_agent = () => {
  clearInterval(interval)
  if (props.agentFieldName != null && props.agentFieldName.length > 0 && props.tableName != null) {
    startInterval()
  }

}

onMounted(() => {
  refresh()


})
onShow(() => {
  refresh()
})

defineExpose({
  refresh
})
</script>

<style scoped lang="scss">
.margin-top {
  width: 100%;
  //height: 100%;
  //height: 92.3vh;
  padding: 0.9357rem 0.9357rem;
  box-sizing: border-box;

  .all_content {
    ::v-deep .uni-section:first-child {
      border-radius: 10px 10px 0px 0px;
    }

    ::v-deep .uni-section:last-child {
      border-radius: 0 0 10px 10px;
    }

    ::v-deep .uni-section__content-title {
      // font-size: 14px !important;
      font-size: 0.875rem !important;
      color: rgba(56, 56, 56, 1) !important;
    }


    ::v-deep .uni-section .uni-section-header__content-sub {
      //display: flex !important;
      justify-content: flex-start !important;
      font-size: 0.875rem !important;
      color: rgba(166, 166, 166, 1) !important;
    }

    ::v-deep .uni-section-header {
      display: block !important;
    }


  }

  ::v-deep .uni-section .uni-section-header {
    display: block !important;
  }

  .tops {
    ::v-deep .uni-section:last-child {
      border-radius: 0 0 0 0 !important;
    }
  }

  .all {
    height: 66 rpx;
    line-height: 66 rpx;
    margin-bottom: 10px;
    font-size: 0.875rem !important;
    color: rgba(128, 128, 128, 1)
  }
}


//.list_box {
//  width: 80%;
//  margin: 20px auto;
//  display: flex;
//  justify-content: center; /* 水平居中 */
//  align-items: center; /* 垂直居中 */
//  //height: 100vh; /* 高度设置为视口高度，确保内容在整个页面居中 */
//  flex-direction: column; /* 垂直排列图片和文字 */
//  //margin-top: 20px;
//}
//
//.nodata {
//  width: 100%;
//  display: flex;
//  flex-direction: column;
//  align-items: center;
//  justify-content: center;
//}
//.nodata img{
//  width: 80%;
//}
//
//.noText {
//  margin-top: 10px;
//  font-size: 16px;
//  color: #666;
//}

.list_box {
  width: 100%;
  height: 100%;
  //background: rgb(245, 247, 250);
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  position: static;
  z-index: 8;

  .nodata {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 60%;
    font-size: 20px;
    font-weight: 500;
    color: rgba(144, 150, 178, 1);

    .noText {
      margin-top: 18.8px;
    }
  }

  .list {
    // flex: 1;
    margin: 30 rpx 0;
    box-sizing: border-box;
    border-radius: 15 rpx;
    background: white;
    color: #fff !important;

    ::v-deep .uni-list-item {
      position: static;
      background: white !important;
    }
  }

  .flex-end-center {
    width: 100%;
  }
}
</style>