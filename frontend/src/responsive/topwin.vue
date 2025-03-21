<template>
    <view class="flex-start-center bg-color-purplb">
		

        <view class="m-l-10">
            <uni-icons type="settings" size="30" color="White" @click="showDrawer"></uni-icons>
        </view>
		
        <uni-drawer ref="showRight" mode="left" :width="320">
			<scroll-view style="height: 100%;" scroll-y="true">
				
                <view class="flex-c-between-start h-full">
                    
                    <view class="w-full"> 
                        <view class="flex-start-center m-30 ">
                            <img :src="get_resource_url('assets/logo/logo.png')" width="32px" height="34px" />
                            <span class="m-l-10" style="font-weight: bold; font-size: 24px;">CodeFlying</span>
                        </view>

                        <view class="flex-start-center m-y-30 m-l-22">
                            <h3 class="m-l-5">{{ left_title }}</h3>
                        </view>
                    </view>

                    <view class="h-full w-full text-left m-l-10">
                        <template v-for="item in dynamicRoutes" :key="item.path">
                            <navigator v-if="!item.meta.shownot"  :url="item.path" open-type="navigate" hover-class="navigator-hover" @click="closeDrawer">
                                <text type="default">{{item.meta.title}}</text>
                            </navigator>
                        </template>
                    </view>

                    
                    <view class="w-full m-b-30">
                        <view class="flex-end-center m-r-20">
                            <image :src="get_resource_url('assets/logo/logo.png')" style="width: 30px; height: 30px;"  mode="aspectFill">
                            </image>
                            
                            <uni-badge :is-dot="true" :text="1" absolute="rightTop" class="item m-l-20">
                                <uni-icons type="chatbubble" size="30" ></uni-icons>
                            </uni-badge>
                        </view>
                    </view>

                </view>

			</scroll-view>
		</uni-drawer>
	</view>
</template>

<script setup>

import get_resource_url from '../config/static_config';
import { dynamicRoutes } from '@/router/dynamic';
    const { proxy } = getCurrentInstance();

    let left_title = import.meta.env.VITE_APP_NAME;


    function showDrawer() {
		proxy.$refs.showRight.open();
	}

    function closeDrawer() {
        proxy.$refs.showRight.close();
    }

</script>