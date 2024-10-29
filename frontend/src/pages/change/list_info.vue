<template>
  <base-wrapper>
    <!-- <base-list-header nickname="变更列表" description="变更列表明细"></base-list-header> -->
    <view style="width:100%;background:white;padding:25px 15px;color:#6569F7;font-weight:600;font-size:18px">
      变更明细
      <uni-breadcrumb separator="/">
        <uni-breadcrumb-item>
          变更管理平台
        </uni-breadcrumb-item>
        <uni-breadcrumb-item>
          变更明细
        </uni-breadcrumb-item>
      </uni-breadcrumb>
    </view>
    <base-layout class="m-t-20 p-t-20" display="flex" direction="c">
      <view class="w-full flex-between-start" style="position: relative;">
        <view style="width: 200px;position: absolute;" class="flex-start-center">
          <fui-button btnSize="small" @click="handle_change" radius="96rpx" class="m-r-10">发起变更</fui-button>
          <fui-button btnSize="small" @click="handle_show_key" type="purple" radius="96rpx" class="m-r-10">
            设置展示字段
          </fui-button>
          <fui-button btnSize="small" @click="handle_export" type="success" radius="96rpx">导 出</fui-button>
        </view>
        <base-search firstSearchData="changeTitle" :searchData="base_search"
                     firstSearchPlaceholder="请输入要搜索的变更标题"
                     @refreshTableData="search_click"
        >
          <template #collapse>
            <uni-forms-item style="width: 300px" class="m-x-10" label="客户名称:" name="changeCustomer">
              <uni-easyinput type="text" v-model="base_search.changeCustomer" placeholder="请输入客户名称"/>
            </uni-forms-item>
            <uni-forms-item style="width: 300px" class="m-x-10" label="项目名称:" name="changeProjectName">
              <uni-easyinput type="text" v-model="base_search.changeProjectName" placeholder="请输入项目名称"/>
            </uni-forms-item>
          </template>
        </base-search>
      </view>

      <view class="m-b-20 overflow-x-scroll" :style="{width: table_width+'px'}">
        <base-table ref="refTableUserInfo" api="change_manager.page_info" :columns="showKeyColumns">
          <template #default="{item}">

            <uni-td v-if=" 'ECN编号(客户)' in showKeyMapColumns" align="center">
              <fui-text :text=" '客户ECN编号' in item.infoMap ? item.infoMap['客户ECN编号'].infoValue : default_value"
                        :size="28"></fui-text>
            </uni-td>

            <uni-td v-if=" '主机厂(客户)' in showKeyMapColumns" align="center">
              <fui-text :text="item.changeCustomer" :size="28"></fui-text>
            </uni-td>

            <uni-td v-if=" 'ECR编号(内部)' in showKeyMapColumns" align="center">
              <fui-text :text="'ECR编号' in item.infoMap ? item.infoMap['ECR编号'].infoValue : default_value"
                        :size="28"></fui-text>
            </uni-td>

            <uni-td v-if=" 'ECR批准' in showKeyMapColumns" align="center">
              <fui-text :text="default_value" :size="28"></fui-text>
            </uni-td>

            <uni-td v-if=" 'ECN编号(内部)' in showKeyMapColumns" align="center">
              <fui-text :text="'ECN编号' in item.infoMap ? item.infoMap['ECN编号'].infoValue : default_value"
                        :size="28"></fui-text>
            </uni-td>
            <uni-td v-if=" '项目名称' in showKeyMapColumns" align="center">
              <fui-text :text="item.changeProjectName" :size="28"></fui-text>
            </uni-td>

            <uni-td v-if=" '项目阶段' in showKeyMapColumns" align="center">
              <fui-text :text="item.changeProjectStage" :size="28"></fui-text>
            </uni-td>

            <uni-td v-if=" '零件编号' in showKeyMapColumns" align="center">
              <fui-text :text="default_value" :size="28"></fui-text>
            </uni-td>

            <uni-td v-if=" '产品名称' in showKeyMapColumns" align="center">
              <fui-text :text="item.changeProductName" :size="28"></fui-text>
            </uni-td>

            <uni-td v-if=" '变更前' in showKeyMapColumns" align="center">
              <fui-text :text="'更改前说明' in item.infoMap ? item.infoMap['更改前说明'].infoValue : default_value"
                        :size="28"></fui-text>
            </uni-td>

            <uni-td v-if=" '变更后' in showKeyMapColumns" align="center">
              <fui-text :text="'更改后说明' in item.infoMap ? item.infoMap['更改后说明'].infoValue : default_value"
                        :size="28"></fui-text>
            </uni-td>

            <uni-td v-if=" '项目负责人' in showKeyMapColumns" align="center">
              <fui-text :text="item.changePersonName" :size="28"></fui-text>
            </uni-td>

            <uni-td v-if=" '发行日期' in showKeyMapColumns" align="center">
              <fui-text :text="item.changeStartTime" :size="28"></fui-text>
            </uni-td>

            <uni-td v-if=" '断点日期' in showKeyMapColumns" align="center">
              <fui-text :text="item.changeEndTime" :size="28"></fui-text>
            </uni-td>

            <uni-td v-if=" '3D图纸' in showKeyMapColumns" align="center">
              <uni-icons v-if="'3D图纸' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td v-if=" '3D图纸' in showKeyMapColumns" align="center">
              <fui-text :text="'3D图纸' in item.infoMap ? item.infoMap['3D图纸'].createTime : default_value"
                        :size="28"></fui-text>
            </uni-td>


            <uni-td v-if=" '2D图纸' in showKeyMapColumns" align="center">
              <uni-icons v-if="'2D图纸' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td v-if=" '2D图纸' in showKeyMapColumns" align="center">
              <fui-text :text="'2D图纸' in item.infoMap ? item.infoMap['2D图纸'].createTime : default_value"
                        :size="28"></fui-text>
            </uni-td>


            <uni-td v-if=" '特殊特性清单' in showKeyMapColumns" align="center">
              <uni-icons v-if="'特殊特性清单' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td v-if=" '特殊特性清单' in showKeyMapColumns" align="center">
              <fui-text :text="'特殊特性清单' in item.infoMap ? item.infoMap['特殊特性清单'].createTime : default_value"
                        :size="28"></fui-text>
            </uni-td>


            <uni-td v-if=" 'DFEMA' in showKeyMapColumns" align="center">
              <uni-icons v-if="'DFEMA' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td v-if=" 'DFEMA' in showKeyMapColumns" align="center">
              <fui-text :text="'DFEMA' in item.infoMap ? item.infoMap['DFEMA'].createTime : default_value"
                        :size="28"></fui-text>
            </uni-td>


            <uni-td v-if=" 'PFC' in showKeyMapColumns" align="center">
              <uni-icons v-if="'PFC' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td v-if=" 'PFC' in showKeyMapColumns" align="center">
              <fui-text :text="'PFC' in item.infoMap ? item.infoMap['PFC'].createTime : default_value"
                        :size="28"></fui-text>
            </uni-td>


            <uni-td v-if=" 'PFEMA' in showKeyMapColumns" align="center">
              <uni-icons v-if="'PFEMA' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td v-if=" 'PFEMA' in showKeyMapColumns" align="center">
              <fui-text :text="'PFEMA' in item.infoMap ? item.infoMap['PFEMA'].createTime : default_value"
                        :size="28"></fui-text>
            </uni-td>


            <uni-td v-if=" 'CP' in showKeyMapColumns" align="center">
              <uni-icons v-if="'CP' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td v-if=" 'CP' in showKeyMapColumns" align="center">
              <fui-text :text="'CP' in item.infoMap ? item.infoMap['CP'].createTime : default_value"
                        :size="28"></fui-text>
            </uni-td>


            <uni-td v-if=" '作业指导书' in showKeyMapColumns" align="center">
              <uni-icons v-if="'作业指导书' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td v-if=" '作业指导书' in showKeyMapColumns" align="center">
              <fui-text :text="'作业指导书' in item.infoMap ? item.infoMap['作业指导书'].createTime : default_value"
                        :size="28"></fui-text>
            </uni-td>


            <uni-td v-if=" '检验指导书' in showKeyMapColumns" align="center">
              <uni-icons v-if="'检验指导书' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>
            <uni-td v-if=" '检验指导书' in showKeyMapColumns" align="center">
              <fui-text :text="'检验指导书' in item.infoMap ? item.infoMap['检验指导书'].createTime : default_value"
                        :size="28"></fui-text>
            </uni-td>

            <uni-td v-if=" '验收单' in showKeyMapColumns" align="center">
              <uni-icons v-if="'验收报告' in item.infoMap" type="smallcircle-filled" size="20"></uni-icons>
              <uni-icons v-else type="circle" size="20"></uni-icons>
            </uni-td>

            <uni-td v-if=" '备注' in showKeyMapColumns" align="center">
              <fui-text :text="default_value" :size="28"></fui-text>
            </uni-td>

          </template>
        </base-table>
      </view>

    </base-layout>
  </base-wrapper>

  <uni-popup ref="submitPopup" borderRadius="10px 10px 10px 10px" background-color="#fff">
    <view class="m-10 w-full">
      <uni-forms ref="refSubmitForm" style="max-width: 100%" :modelValue="showKeyForm" label-position="top"
                 :rules="{
                    showKeys: {
                      rules: [{
                        required: true,
                        errorMessage: '展示字段不能为空'
                      }]
                    }
                 }">
        <uni-forms-item label="展示字段" required name="showKeys">
          <el-checkbox-group v-model="showKeyForm.showKeys">
            <div class="flex-wrap">
              <el-checkbox v-for="(item,index) in allShowKeys" :key="index" :label="item.label" :value="item.value"/>
            </div>
          </el-checkbox-group>
        </uni-forms-item>
      </uni-forms>
      <view class="flex-center-center">
        <button type="primary" size="mini" @click="proxy.$refs.submitPopup.close();">取 消</button>
        <button type="primary" size="mini" @click="submitShowKeyForm">确 定</button>
      </view>
    </view>
  </uni-popup>

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

const showKeyForm = ref({});
const showKeyColumns = ref([])
const showKeyMapColumns = ref({})

function handle_show_key() {
  proxy.$refs.submitPopup.open();
}

function submitShowKeyForm() {

  proxy.$refs.refSubmitForm.validate().then(async (r) => {
    uni.showLoading({
      title: '处理中'
    });
    showKeyForm.value.showKeys.sort();
    let res = await proxy.$api.change_manager.saveShowKey(showKeyForm.value)
    uni.hideLoading();
    proxy.$refs.submitPopup.close();
    if (res.code === 0) {
      //getShowKeySetting();
      proxy.$router.go(0);
    }
  });
}

async function getShowKeySetting() {
  let res = await proxy.$api.change_manager.getMyShowKeys()
  if (res.code === 0) {
    showKeyForm.value.showKeys = res.data.selectKey;

    if (res.data.selectKey.length > 0) {
      const result = res.data.selectKey.reduce((obj, item) => {
        // 如果包含 "-"，只保留第一个部分
        const key = item.split('-')[0];
        obj[key.substring(4)] = "";
        return obj;
      }, {});

      showKeyMapColumns.value = result;
    } else {
      showKeyMapColumns.value = allShowMapKeys.value;
    }

    if (res.data.showKey.length > 0) {
      let result = res.data.showKey.map(item => {
        // 用 "-" 分割 showKey
        const keys = item.showKey.split('-');
        // 转换成列
        const columns = keys.map(key => ({
          prop: key.substring(4),        // 每个分割部分作为 prop
          label: key.substring(4),       // 每个分割部分作为 label
          width: item.showWidth // 使用 showWidth 作为 width
        }));
        return columns;
      });

      result = result.flat();

      showKeyColumns.value = result;

    } else {
      showKeyColumns.value = allTableColumns.value;
    }
  }
}

function handle_export() {
  proxy.$download.download("/changeManager/info/export", {...base_search.value}, `变更明细_${new Date().getTime()}.xlsx`)
}


const table_width = ref(800);

onResize(() => {
  table_width.value = window.innerWidth - 300 - 12;
})

onMounted(() => {
  table_width.value = window.innerWidth - 300 - 12;

  getShowKeySetting();

})

onShow(() => {
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  const route = currentPage.route;
  uni.$emit('updateRoute', route);
})

const allShowKeys = ref([
  {label: "ECN编号(客户)", value: "(01)ECN编号(客户)"},
  {label: "主机厂(客户)", value: "(02)主机厂(客户)"},
  {label: "ECR编号(内部)", value: "(03)ECR编号(内部)"},
  {label: "ECR批准", value: "(04)ECR批准"},
  {label: "ECN编号(内部)", value: "(05)ECN编号(内部)"},
  {label: "项目名称", value: "(06)项目名称"},
  {label: "项目阶段", value: "(07)项目阶段"},
  {label: "零件编号", value: "(08)零件编号"},
  {label: "产品名称", value: "(09)产品名称"},
  {label: "变更前", value: "(10)变更前"},
  {label: "变更后", value: "(11)变更后"},
  {label: "项目负责人", value: "(12)项目负责人"},
  {label: "发行日期", value: "(13)发行日期"},
  {label: "断点日期", value: "(14)断点日期"},
  {label: "3D图纸", value: "(15)3D图纸-(00)关闭时间"},
  {label: "2D图纸", value: "(16)2D图纸-(00)关闭时间"},
  {label: "特殊特性清单", value: "(17)特殊特性清单-(00)关闭时间"},
  {label: "DFEMA", value: "(18)DFEMA-(00)关闭时间"},
  {label: "PFC", value: "(19)PFC-(00)关闭时间"},
  {label: "PFEMA", value: "(20)PFEMA-(00)关闭时间"},
  {label: "CP", value: "(21)CP-(00)关闭时间"},
  {label: "作业指导书", value: "(22)作业指导书-(00)关闭时间"},
  {label: "检验指导书", value: "(23)检验指导书-(00)关闭时间"},
  {label: "验收单", value: "(24)验收单"},
  {label: "备注", value: "(25)备注"}
]);

const allShowMapKeys = ref({
  "ECN编号(客户)": "", "主机厂(客户)": "", "ECR编号(内部)": "",
  "ECR批准": "", "ECN编号(内部)": "", "项目名称": "", "项目阶段": "",
  "零件编号": "", "产品名称": "", "变更前": "", "变更后": "",
  "项目负责人": "", "发行日期": "", "断点日期": "", "3D图纸": "",
  "2D图纸": "", "特殊特性清单": "", "DFEMA": "", "PFC": "",
  "PFEMA": "", "CP": "", "作业指导书": "", "检验指导书": "",
  "验收单": "", "备注": ""
});

const allTableColumns = ref([
  {prop: 'name', label: 'ECN编号(客户)', width: '180'},
  {prop: 'name', label: '主机厂(客户)', width: '150'},
  {prop: 'name', label: 'ECR编号(内部)', width: '150'},
  {prop: 'name', label: 'ECR批准', width: '80'},
  {prop: 'name', label: 'ECN编号(内部)', width: '120'},
  {prop: 'name', label: '项目名称', width: '150'},
  {prop: 'name', label: '项目阶段', width: '150'},
  {prop: 'name', label: '零件编号', width: '150'},
  {prop: 'name', label: '产品名称', width: '120'},
  {prop: 'name', label: '变更前', width: '200'},
  {prop: 'name', label: '变更后', width: '200'},
  {prop: 'name', label: '项目负责人', width: '150'},
  {prop: 'name', label: '发行日期', width: '150'},
  {prop: 'name', label: '断点日期', width: '150'},
  {prop: 'name', label: '3D图纸', width: '80'},
  {prop: 'name', label: '关闭时间', width: '120'},
  {prop: 'name', label: '2D图纸', width: '80'},
  {prop: 'name', label: '关闭时间', width: '120'},
  {prop: 'name', label: '特殊特性清单', width: '80'},
  {prop: 'name', label: '关闭时间', width: '120'},
  {prop: 'name', label: 'DFEMA', width: '80'},
  {prop: 'name', label: '关闭时间', width: '120'},
  {prop: 'name', label: 'PFC(工艺流程图)', width: '80'},
  {prop: 'name', label: '关闭时间', width: '120'},
  {prop: 'name', label: 'PFEMA', width: '80'},
  {prop: 'name', label: '关闭时间', width: '120'},
  {prop: 'name', label: 'CP(控制计划)', width: '80'},
  {prop: 'name', label: '关闭时间', width: '120'},
  {prop: 'name', label: '作业指导书', width: '80'},
  {prop: 'name', label: '关闭时间', width: '120'},
  {prop: 'name', label: '检验指导书', width: '80'},
  {prop: 'name', label: '关闭时间', width: '120'},
  {prop: 'name', label: '验收单', width: '80'},
  {prop: 'name', label: '备注', width: '120'},
]);

</script>

<style scoped lang="scss">
::v-deep .uni-left-window {
  height: 100%;
}

::v-deep .uni-popup__wrapper {
  width: 600px;
  max-width: 80%;
}

</style>