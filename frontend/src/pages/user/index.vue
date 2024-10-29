<template>
  <base-wrapper>
    <!-- <base-list-header nickname="人员管理" description="企业微信人员列表，可以设置人员权限"></base-list-header> -->
    <view style="width:100%;background:white;padding:25px 15px;color:#6569F7;font-weight:600;font-size:18px">
      人员管理
      <uni-breadcrumb separator="/">
        <uni-breadcrumb-item>
          变更管理平台
        </uni-breadcrumb-item>
        <uni-breadcrumb-item>
          人员管理
        </uni-breadcrumb-item>
      </uni-breadcrumb>
    </view>
    <base-layout class="m-t-20 p-t-20" display="flex" direction="c">
      <base-search firstSearchData="userName" :searchData="base_search"
                   firstSearchPlaceholder="请输入要搜索的人员名称"
                   :showMoreSearch="false"
                   @refreshTableData="search_click"
      ></base-search>

      <view class="w-full m-y-20">
        <base-table ref="refTableUserInfo" class="m-r-20" :columns="[
            { prop: 'name', label: '人员姓名', width: '40' },
            { prop: 'name', label: '所属部门', width: '100' },
            { prop: 'name', label: '管理员', width: '1' },
            { prop: 'name', label: '只读管理员', width: '1' },
            { prop: 'name', label: '变更发起人', width: '1' },
            { prop: 'name', label: '交接任务', width: '1' }]" api="userinfo.page">
          <template #default="{item}">
            <uni-td align="center">{{ item.userName }}</uni-td>
            <uni-td align="center">{{ item.departmentName }}</uni-td>
            <uni-td align="center">
              <base-checkbox :data="item" changeName="admin" api="userinfo.update"></base-checkbox>
            </uni-td>
            <uni-td align="center">
              <base-checkbox :data="item" changeName="adminReadOnly" api="userinfo.update"></base-checkbox>
            </uni-td>
            <uni-td align="center">
              <base-checkbox :data="item" changeName="changePerson" api="userinfo.update"></base-checkbox>
            </uni-td>

            <uni-td>
              <view>
                <fui-button type="link" text="交接" color="red" size="28" @click="handle_jiaojie(item)"></fui-button>
              </view>
            </uni-td>
          </template>
        </base-table>

      </view>

    </base-layout>


  </base-wrapper>

  <uni-popup ref="submitPopup" borderRadius="10px 10px 10px 10px" background-color="#fff">
    <view class="m-10 w-full">
      <el-radio-group class="m-20" v-model="submitCheckForm.assigner">
        <el-radio v-for="(item,index ) in submitPersons"
                  :key="index" :value="item.value"> {{ item.label }}
        </el-radio>
      </el-radio-group>
      <view class="flex-center-center">
        <button type="primary" size="mini" @click="submitSubmitPersonForm">确 定</button>
      </view>
    </view>
  </uni-popup>

</template>

<script setup>
import {onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();

const base_search = ref({})

function search_click(item) {
  proxy.$refs.refTableUserInfo.refresh(item);
}

const submitPersons = ref([]);
const submitCheckForm = ref({});

function handle_jiaojie(item) {
  submitCheckForm.value = {userInfoId: item.userInfoId};
  proxy.$refs.submitPopup.open();
  proxy.$api.userinfo.select_list({}).then(res => {
    submitPersons.value = res.data;
  })
}

async function submitSubmitPersonForm() {
  uni.showLoading({
    title: '处理中'
  });

  let res = await proxy.$api.userinfo.assignTask(submitCheckForm.value)

  uni.hideLoading();

  if (res.code === 0) {

    proxy.$refs.submitPopup.close();

    uni.showToast({
      title: "交接完成",
      icon: "none"
    });

  }
}

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


::v-deep .uni-popup__wrapper {
  width: 600px;
  max-width: 80%;
}

</style>