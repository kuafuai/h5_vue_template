<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="投资人信息"
description="投资人个人信息"
>

</base-list-header>
<base-description
:params="{'buinessPersonId':allParams.currentUser.userId,
}"
api="buiness_person_all.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.buinessPersonId"
type="base"
title="编号"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.bWechatLoginId"
type="base"
title="小程序openid"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.bPhoneNumber"
type="base"
title="手机号"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.buinessPersonName"
type="base"
title="姓名"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.bProjectOrganization"
type="base"
title="组织"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.bTitle"
type="base"
title="职位头衔"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.bWechatNumber"
type="base"
title="微信号"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.statusDescription"
type="base"
title="认证状态"
class="mb-10"
>
</uni-section>


</template>
</base-description>
<base-wrapper
>
<base-button
        ref="form_button_ref"
    @click="click_8586"
    model="base"
    type="primary"
    title="查看所有项目列表"
>

</base-button>
<base-button
        ref="lianxi_button_ref"
    @click="click_8303"
    model="base"
    type="primary"
    title="我的联系人"
>

</base-button>
</base-wrapper>
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









const click_8586=(
)=>{
  proxy.$navigate( "/pages/project_all/index",false)

}
const click_8303=(
)=>{
  proxy.$navigate( "/pages/buiness_person_follow_communicate/index",false)

}

</script>

<style lang="scss" scoped></style>