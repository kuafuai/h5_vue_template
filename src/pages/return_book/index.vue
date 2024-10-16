<template>
  <base-wrapper>
    <base-wrapper
>
<base-description
ref="borrow_record_description_ref"
:params="{'borrowId':allParams.borrowId,
}"
api="borrow_record_all.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.borrowId"
type="base"
title="借阅ID"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.username"
type="base"
title="用户ID"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.title"
type="base"
title="图书ID"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.borrowDate"
type="base"
title="借阅日期"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.returnDate"
type="base"
title="归还日期"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.status"
type="base"
title="状态"
class="mb-10"
>
</uni-section>


</template>
</base-description>
<base-button
        ref="return_button_ref"
    @click="click_1992"
    type="primary"
    title="归还"
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






const click_1992=(
)=>{
  uni.showToast({
  title: '归还成功',
  icon: 'none',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>