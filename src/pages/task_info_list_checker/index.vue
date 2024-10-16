<template>
  <base-wrapper>
    <base-one-show
ref="base_one_show_d66bea"
firstSearchDataComment="姓名"
v-model:more_data="more_result_list"
@show_search="base_one_show_d66bea_search"
list_ref="base_list_d66bea"
type="more"
select_text_key="userName"
select_value_key="userInfoId"
firstSearchData="userName"
>
<template #list>
<base-list
ref="base_list_d66bea"
:isPage="true"
:is_click="true"
api="user_info_all.page"
@check_group_change="check_group_change_5496"
>
<template #default="{item}">
<uni-list-item
:rightText="item.userName!=null && item.userName!=undefined ?item.userName+'':item.userName"
title="姓名:"
>

</uni-list-item>
<uni-list-item
:rightText="item.phoneNumber!=null && item.phoneNumber!=undefined ?item.phoneNumber+'':item.phoneNumber"
title="联系电话:"
>

</uni-list-item>
<uni-list-item
:rightText="item.personnelInformation!=null && item.personnelInformation!=undefined ?item.personnelInformation+'':item.personnelInformation"
title="人员信息:"
>

</uni-list-item>
<uni-list-item
:rightText="item.userRoleStatusDescription!=null && item.userRoleStatusDescription!=undefined ?item.userRoleStatusDescription+'':item.userRole"
title="用户角色:"
>

</uni-list-item>
<uni-list-item
:rightText="item.createTime!=null && item.createTime!=undefined ?item.createTime+'':item.createTime"
title="开始日期:"
>

</uni-list-item>
<uni-list-item
:rightText="item.updateTime!=null && item.updateTime!=undefined ?item.updateTime+'':item.updateTime"
title="更新时间:"
>

</uni-list-item>
</template>
<template #right="{ item }">
<checkbox
:value="{text: item.userName,value: item.userInfoId}"
multiple
:checked="more_result_list.map(item => item.value).includes(item.userInfoId)"
>

</checkbox>
</template>
</base-list>
</template>
</base-one-show>
  </base-wrapper>

</template>

<script setup>
import {getCurrentInstance, ref} from "vue"
const {proxy} = getCurrentInstance();
import {onLoad} from "@dcloudio/uni-app";
const allParams = ref({})
// onLoad(() => {
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  const urlParams = currentPage.options; // 获取页面传递的参数

  const localStorageKey = "/" + currentPage.route; // 当前页面的路径作为 key
  const localStorageParams = uni.getStorageSync(localStorageKey) || null;
  let currentUser = uni.getStorageSync("currentUser") || null;
  if (localStorageParams != null && urlParams != null) {
    allParams.value = {
      ...urlParams,
      ...JSON.parse(localStorageParams)
    };
  } else if (localStorageParams != null && urlParams == null) {
    allParams.value = {

      ...JSON.parse(localStorageParams)
    };
  } else if (localStorageParams == null && urlParams != null) {
    allParams.value = {
      ...urlParams, // 展开 URL 参数

    };
  }
// })
if (currentUser != null) {
  var parse = JSON.parse(currentUser);
  allParams.value["currentUser"] = parse;
  allParams.value[parse["relevanceTable"] + "Id"] = parse.userId
}


const base_one_show_d66bea_search = (item) => {
    proxy.$refs.base_list_d66bea.refresh(item)
}

let more_result_list = ref([])

const check_group_change_5496=(
item
)=>{
  proxy.$refs.base_one_show_d66bea.refresh_more(item)

}

</script>

<style lang="scss" scoped></style>