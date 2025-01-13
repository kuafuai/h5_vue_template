<template>
  <div class="marks" v-if="markdowns" v-html="renderedMarkdown"></div>
</template>

<script setup>
import { ref, watch, computed } from 'vue';  // computed 需要从 Vue 导入
import {marked} from 'marked';

const markdowns = ref(null);

const props = defineProps({
  markdown: {
    type: String,
    required: true,
  },
});

// 监听 markdown 的变化并更新 markdowns
watch(
    () => props.markdown,
    (newVal) => {
      markdowns.value = newVal;
    },
    {immediate: true}
);

// 计算渲染的 Markdown 内容
const renderedMarkdown = computed(() => {
  return marked(markdowns.value);
});
</script>

<style lang="scss">
::v-deep .language-java {
  width: 100% !important;
  white-space: pre-wrap !important;
}

.marks {
  h1,
  h2,
  h3,
  p,
  ul li,
  ol li {
    line-height: 38px !important;
  }

  h1 {
    margin-bottom: 20px;
    font-size: 26px !important;
  }

  h2 {
    margin-bottom: 18px;
    font-size: 24px !important;
  }

  h3 {
    font-size: 22px !important;
    margin-bottom: 16px;
  }

  h4 {
    margin-bottom: 14px;
    font-size: 20px !important;
  }

  h5 {
    margin-bottom: 10px;
    font-size: 18px !important;
  }

  h6 {
    margin-bottom: 10px;
    font-size: 16px !important;
  }

  ul {
    margin-bottom: 10px;
    padding-left: 20px;
  }

  ul li {
    margin-left: 20px;
    list-style-type: square;
  }

}
</style>
