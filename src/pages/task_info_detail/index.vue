<template>
  <base-wrapper>
    <base-wrapper
>
<base-list-header
nickname="任务信息管理"
description="任务详情"
>

</base-list-header>
<base-description
ref="task_info_detail_ref"
:params="{'taskInfoId':allParams.taskInfoId,
}"
api="task_info_all.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.taskInfoId"
type="base"
title="编号"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.taskName"
type="base"
title="任务名称"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.taskStatusDescription"
type="base"
title="任务状态"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.enterpriseName"
type="base"
title="被检查企业"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.userName"
type="base"
title="发起人"
class="mb-10"
>
</uni-section>


<base-descriptions-item
:sub_title="description.checker"
type="base"
title="检查人"
class="mb-10"
>

</base-descriptions-item>
<uni-section
:sub-title="description.startTime"
type="base"
title="开始日期"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.endTime"
type="base"
title="截止时间"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.checkRequire"
type="base"
title="检查要求"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.tenantId"
type="base"
title="租户ID"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.hazardInfoCount?description.hazardInfoCount:'0'"
type="base"
title="隐患数量"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.hazardBasicInfoCount?description.hazardBasicInfoCount:'0'"
type="base"
title="未绑定依据的隐患数量"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.hazardNotBasicInfoCount?description.hazardNotBasicInfoCount:'0'"
type="base"
title="绑定依据的隐患数量"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.hazardAcceptanceCount?description.hazardAcceptanceCount:'0'"
type="base"
title="验收的隐患"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.hazardNotAcceptanceCount?description.hazardNotAcceptanceCount:'0'"
type="base"
title="未验收的隐患"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.createTime"
type="base"
title="创建时间"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.updateTime"
type="base"
title="更新时间"
class="mb-10"
>
</uni-section>


</template>
</base-description>
<base-button
        ref="update_task_info_button"
    @click="click_1617"
    model="float"
    type="primary"
    title="修改任务内容"
>
修改任务详情
</base-button>
<base-dialog
ref="end_check_popup"
@success="success_4801"
comp_type="popup"
@fail="fail_4801"
content="请确认所有隐患已关联隐患依据"
>
<template #dialog>

</template>
</base-dialog>
<base-dialog
ref="delete_task_popup"
@success="success_8863"
comp_type="popup"
@fail="fail_8863"
content="您确定要删除这个排查任务吗？"
>
<template #dialog>

</template>
</base-dialog>
<base-layout
>
<base-button
        ref="hazard_info_list_button"
    @click="click_4433"
    model="base"
    type="primary"
    title="隐患列表"
>

</base-button>
<base-button
        ref="end_check_button"
    v-if="allParams.userRole==1"
    @click="click_526"
    model="base"
    type="primary"
    title="结束检查"
>

</base-button>
<base-button
        ref="delete_task_button"
    @click="click_3230"
    model="base"
    type="primary"
    title="删除任务"
>

</base-button>
</base-layout>
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














const click_1617=(
)=>{
  proxy.$navigate( "/pages/update_task_info/index?taskInfoId=" + allParams.value.taskInfoId,false)

}
const click_4433=(
)=>{
  proxy.$navigate( "/pages/hazard_info_list/index?taskInfoId=" + allParams.value.taskInfoId,false)

}
const click_526=(
)=>{
  proxy.$refs.end_check_popup.showDialog()

}
const success_4801=(
)=>{
  proxy.$refs.end_check_popup.closeDialog()
  proxy.$api.task_info.update({taskInfoId:allParams.value.taskInfoId,taskStatus:'2',}).then(res=>{uni.showToast({
  title: '结束检查成功',
  icon: 'success',
  duration: 2000
});})
  uni.showToast({
  title: '提交成功',
  icon: 'success',
  duration: 2000
});
  proxy.$refs.task_info_detail_ref.refresh()

}
const click_3230=(
)=>{
  proxy.$refs.delete_task_popup.showDialog()

}
const success_8863=(
)=>{
  proxy.$refs.delete_task_popup.closeDialog()
  proxy.$api.task_info.delete(allParams.value.taskInfoId).then(res=>{uni.showToast({
  title: '删除任务成功',
  icon: 'success',
  duration: 2000
});})
  proxy.$navigate( "/pages/task_info_list/index",false)
  proxy.$refs.show_task_info_list.refresh()

}

</script>

<style lang="scss" scoped></style>