<template>
  <base-wrapper>
    <base-wrapper
>
<base-description
ref="book_description_ref"
:params="{'bookId':allParams.bookId,
}"
api="book.page"
>
<template #description-items={description}>
<uni-section
:sub-title="description.bookId"
type="base"
title="图书ID"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.title"
type="base"
title="书名"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.author"
type="base"
title="作者"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.isbn"
type="base"
title="ISBN"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.publicationDate"
type="base"
title="出版日期"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.totalCopies"
type="base"
title="总数量"
class="mb-10"
>
</uni-section>


<uni-section
:sub-title="description.availableCopies"
type="base"
title="可借数量"
class="mb-10"
>
</uni-section>


</template>
</base-description>
<base-button
        ref="borrow_button_ref"
    @click="click_1942"
    type="primary"
    title="借阅"
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






const click_1942=(
)=>{
  uni.showToast({
  title: '借阅成功',
  icon: 'none',
  duration: 2000
});

}

</script>

<style lang="scss" scoped></style>