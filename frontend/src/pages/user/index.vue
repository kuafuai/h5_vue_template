<template>
  <base-wrapper>
    <base-list-header nickname="人员管理" description="企业微信人员列表，可以设置人员权限"></base-list-header>

    <base-layout class="m-t-20 p-t-20" display="flex" direction="c">
      <base-search firstSearchData="userName" :searchData="base_search"
                   firstSearchPlaceholder="请输入要搜索的人员名称"
                   @refreshTableData="search_click"
      ></base-search>

      <view class="w-full m-b-20">
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
                <fui-button type="link" text="交接" color="red" size="28"></fui-button>
              </view>
            </uni-td>
          </template>
        </base-table>

      </view>

    </base-layout>


  </base-wrapper>

</template>

<script setup>
import {onShow} from "@dcloudio/uni-app";
const {proxy} = getCurrentInstance();

const base_search = ref({})

function search_click(item) {
  proxy.$refs.refTableUserInfo.refresh(item);
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
</style>