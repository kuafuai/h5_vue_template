<template>
  <base-wrapper>
    <base-list-header nickname="变更列表" description="变更列表明细"></base-list-header>
    <base-layout class="m-t-20 p-t-20" display="flex" direction="c">
      <view class="w-full flex-between-start">
        <view style="width: 200px">
          <fui-button btnSize="small" @click="handle_change" radius="96rpx">发起变更</fui-button>
        </view>
        <base-search firstSearchData="approveNodeName" :searchData="base_search"
                     firstSearchPlaceholder="请输入要搜索的环节名称"
                     @refreshTableData="search_click"
        ></base-search>
      </view>

      <view class="m-b-20 overflow-x-scroll" :style="{width: table_width+'px'}">
        <base-table ref="refTableUserInfo" api="change_manager.page_info" :columns="[
            { prop: 'name', label: 'ECN编号(客户)', width: '100' },
            { prop: 'name', label: '主机厂(客户)', width: '150' },
            { prop: 'name', label: 'ECR编号(内部)', width: '150' },
            { prop: 'name', label: 'ECR批准', width: '80' },
            { prop: 'name', label: 'ECN编号(内部)', width: '120' },
            { prop: 'name', label: '项目名称', width: '150' },
            { prop: 'name', label: '项目阶段', width: '150' },
            { prop: 'name', label: '零件编号', width: '150' },
            { prop: 'name', label: '产品名称', width: '120' },
            { prop: 'name', label: '变更前', width: '200' },
            { prop: 'name', label: '变更后', width: '200' },
            { prop: 'name', label: '项目负责人', width: '150' },
            { prop: 'name', label: '发行日期', width: '150' },
            { prop: 'name', label: '断点日期', width: '150' },
            { prop: 'name', label: '3D图纸', width: '80' },
            { prop: 'name', label: '关闭时间', width: '120' },
            { prop: 'name', label: '2D图纸', width: '80' },
            { prop: 'name', label: '关闭时间', width: '120' },
            { prop: 'name', label: '特殊特性清单', width: '80' },
            { prop: 'name', label: '关闭时间', width: '120' },
            { prop: 'name', label: 'DFEMA', width: '80' },
            { prop: 'name', label: '关闭时间', width: '120' },
            { prop: 'name', label: 'PFC(工艺流程图)', width: '80' },
            { prop: 'name', label: '关闭时间', width: '120' },
            { prop: 'name', label: 'PFEMA', width: '80' },
            { prop: 'name', label: '关闭时间', width: '120' },
            { prop: 'name', label: 'CP(控制计划)', width: '80' },
            { prop: 'name', label: '关闭时间', width: '120' },
            { prop: 'name', label: '作业指导书', width: '80' },
            { prop: 'name', label: '关闭时间', width: '120' },
            { prop: 'name', label: '检验指导书', width: '80' },
            { prop: 'name', label: '关闭时间', width: '120' },
            { prop: 'name', label: '验收单', width: '80' },
            { prop: 'name', label: '备注', width: '120' },
        ]">
          <template #default="{item}">
            <uni-td align="center">
              <fui-text :text=" '客户ECN编号' in item.infoMap ? item.infoMap['客户ECN编号'].infoValue : default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.changeCustomer" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="'ECR编号' in item.infoMap ? item.infoMap['ECR编号'].infoValue : default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="'ECN编号' in item.infoMap ? item.infoMap['ECN编号'].infoValue : default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.changeProjectName" :size="28"></fui-text>
            </uni-td>

            <uni-td align="center">
              <fui-text :text="default_value" :size="28"></fui-text>
            </uni-td>

            <uni-td align="center">
              <fui-text :text="default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.changeProductName" :size="28"></fui-text>
            </uni-td>

            <uni-td align="center">
              <fui-text :text="'更改前说明' in item.infoMap ? item.infoMap['更改前说明'].infoValue : default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="'更改后说明' in item.infoMap ? item.infoMap['更改后说明'].infoValue : default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.changePersonName" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.changeStartTime" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="item.changeEndTime" :size="28"></fui-text>
            </uni-td>

            <uni-td align="center">
              <uni-icons v-if="'3D图纸' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="'3D图纸' in item.infoMap ? item.infoMap['3D图纸'].createTime : default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <uni-icons v-if="'2D图纸' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="'2D图纸' in item.infoMap ? item.infoMap['2D图纸'].createTime : default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <uni-icons v-if="'特殊特性清单' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="'特殊特性清单' in item.infoMap ? item.infoMap['特殊特性清单'].createTime : default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <uni-icons v-if="'DFEMA' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="'DFEMA' in item.infoMap ? item.infoMap['DFEMA'].createTime : default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <uni-icons v-if="'PFC' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="'PFC' in item.infoMap ? item.infoMap['PFC'].createTime : default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <uni-icons v-if="'PFEMA' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="'PFEMA' in item.infoMap ? item.infoMap['PFEMA'].createTime : default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <uni-icons v-if="'CP' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="'CP' in item.infoMap ? item.infoMap['CP'].createTime : default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <uni-icons v-if="'作业指导书' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="'作业指导书' in item.infoMap ? item.infoMap['作业指导书'].createTime : default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <uni-icons v-if="'检验指导书' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="'检验指导书' in item.infoMap ? item.infoMap['检验指导书'].createTime : default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="default_value" :size="28"></fui-text>
            </uni-td>
            <uni-td align="center">
              <fui-text :text="default_value" :size="28"></fui-text>
            </uni-td>

          </template>
        </base-table>
      </view>

    </base-layout>
  </base-wrapper>
</template>

<script setup>
import {onShow, onResize} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();

const base_search = ref({})

function search_click(item) {
  proxy.$refs.refTableUserInfo.refresh(item);
}

const default_value = ref('/')

function handle_change() {
  let url = '/pages/change/change'
  proxy.$navigate(url)
}

const table_width = ref(800);

onResize(() => {
  console.log(window.innerWidth, window.innerHeight)
  table_width.value = window.innerWidth - 300;
  console.log('=====',table_width.value);
})

onMounted(() => {
  console.log(window.innerWidth, window.innerHeight)
  table_width.value = window.innerWidth - 300;
  console.log('=====',table_width.value);
})

onShow(() => {
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  const route = currentPage.route;
  uni.$emit('updateRoute', route);
})
</script>

<style scoped lang="scss">
::v-deep .uni-left-window {
  height: 100%;
}
</style>