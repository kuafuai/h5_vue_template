<template>
  <base-wrapper>
    <base-wrapper
>
<base-list
ref="maintenance_record_list_ref"
:isPage="true"
:params="{'assetId':allParams.assetId,
}"
api="maintenance_record_info.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.assetId?item.assetId:item.assetId"
title="资产ID:"
>

</uni-list-item>
<uni-list-item
:rightText="item.maintenanceDate?item.maintenanceDate:item.maintenanceDate"
title="维护日期:"
>

</uni-list-item>
<uni-list-item
:rightText="item.maintenanceContent?item.maintenanceContent:item.maintenanceContent"
title="维护内容:"
>

</uni-list-item>
<uni-list-item
:rightText="item.maintenancePerson?item.maintenancePerson:item.maintenancePerson"
title="维护人员:"
>

</uni-list-item>
</template>
</base-list>
<base-button
        ref="add_maintenance_record_button_ref"
    @click="click_8149"
    model="float"
    type="primary"
    title="添加维护记录"
>

</base-button>
</base-wrapper>
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






const click_8149=(
)=>{
  proxy.$navigate( "/pages/add_maintenance_record/index?assetId=" + assetId,false)

}

</script>

<style lang="scss" scoped></style>