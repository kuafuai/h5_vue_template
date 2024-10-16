<template>
    <div class='page-container'>
        1111111111111111
    </div>
</template>
<script setup>
/**
 * 模块引入
 */
import { reactive, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
const router = useRouter();
const route = useRoute();
/**
 * 数据
 */
const state = reactive({});
/**
 * 功能方法
 */
</script>
<style lang='scss' scoped>
.page-container {
    width: 100vw;
    height: 100vh;
}
</style>