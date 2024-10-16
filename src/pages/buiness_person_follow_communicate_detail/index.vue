<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="联系记录"
description="投资人好友申请详情"
>

</base-list-header>
<base-description
ref="base_description_ref"
:params="{'contactInfoId':allParams.contactInfoId,
'applicationStatus':'2',
}"
api="contact_info_all.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.contactInfoId"
type="base"
title="编号"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.projectName"
type="base"
title="项目id"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.userName"
type="base"
title="项目负责人id"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.wechatNumber"
type="base"
title="项目负责人wechat"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.buinessPersonName"
type="base"
title="投资人id"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.bWechatNumber"
type="base"
title="投资人wechat"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.statusDescription"
type="base"
title="申请状态"
class="mb-10"
>
</uni-section>


</template>
</base-description>
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








const 提交成功=(
)=>{

}

</script>

<style lang="scss" scoped></style>