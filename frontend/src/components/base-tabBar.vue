<template>
    <view class="big_box">
        <view class="cont">
            <slot />
        </view>
        <view class="tabbar">
            <view :class="currentRoute == item.url ? 'tab_box on' : 'tab_box'" v-for="(item, index) in tabLists" :key="index"
                :style="{ width: 100 / tabList.length + '%' }">
                <view class="tab_top" @click="tabFn(item.url)">
                    <image class="tab_icon" :style="{ width: '48rpx' }"
                        :src="currentRoute == item.url ? item.onicon : item.icon" mode="widthFix" />
                </view>
                <view class="tab_bot">
                    {{ item.title }}
                </view>
            </view>
        </view>
    </view>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue';

const { proxy } = getCurrentInstance();
const type = ref(import.meta.env.VITE_APP_TYPE);

const props = defineProps({
    tabList: {
        type: Array,
        default: [],
    },
});
console.log(props.tabList, "props.value.tabList");

const tabLists = ref([])

tabLists.value = props.tabList

function tabFn(url) {
	console.log(url)
  proxy.$navigate(url, true)
}

function getCurrentRoute() {
    const pages = getCurrentPages(); // 获取当前页面栈
    if (pages.length) {
        const currentPage = pages[pages.length - 1]; // 获取栈顶页面
        return `/${currentPage.route}`; // 返回当前页面路径
    }
    return '';
}

// 示例使用
const currentRoute = getCurrentRoute();
console.log("当前页面路径：", currentRoute);

</script>

<style scoped>
.big_box {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
}

.cont {
	flex: 1;
	/* overflow: auto; */
}

.tabbar {
    width: 100%;
    height: 10%;
    background: white;
    display: flex;
}

.tab_box {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

.on {
    /*color: orange;*/
    color: #527FF4;
}

.tab_top {
    width: 100%;
    text-align: center;
}

.tab_icon {
    width: 50%;
}

.tab_bot {
    width: 100%;
    text-align: center;
}
</style>