<template>
  <base-wrapper>
    <base-one-show
ref="base_one_show_933288"
firstSearchDataComment="企业名称"
@show_search="base_one_show_933288_search"
list_ref="base_list_933288"
select_text_key="enterpriseName"
select_value_key="enterpriseInformationId"
firstSearchData="enterpriseName"
>
<template #list>
<base-list
ref="base_list_933288"
:isPage="true"
api="enterprise_information.page"
>
<template #default="{item}">
<uni-list-item
:rightText="item.enterpriseName!=null && item.enterpriseName!=undefined ?item.enterpriseName+'':item.enterpriseName"
title="企业名称:"
>

</uni-list-item>
<uni-list-item
:rightText="item.personInCharge!=null && item.personInCharge!=undefined ?item.personInCharge+'':item.personInCharge"
title="主要负责人:"
>

</uni-list-item>
<uni-list-item
:rightText="item.phoneNumber!=null && item.phoneNumber!=undefined ?item.phoneNumber+'':item.phoneNumber"
title="联系电话:"
>

</uni-list-item>
<uni-list-item
:rightText="item.enterpriseAddress!=null && item.enterpriseAddress!=undefined ?item.enterpriseAddress+'':item.enterpriseAddress"
title="企业地址:"
>

</uni-list-item>
<uni-list-item
:rightText="item.sector!=null && item.sector!=undefined ?item.sector+'':item.sector"
title="所属行业:"
>

</uni-list-item>
</template>
<template #op="{item}">
<base-button
        ref="base_button_933288"
    @click="click_1096(item)"
    model="base"
    type="primary"
    title="选择"
    op_param="item"
>

</base-button>
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


const base_one_show_933288_search = (item) => {
    proxy.$refs.base_list_933288.refresh(item)
}

let more_result_list = ref([])

const click_1096=(
item
)=>{
  proxy.$refs.base_one_show_933288.refresh(item)

}

</script>

<style lang="scss" scoped></style>