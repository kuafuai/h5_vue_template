<template>
  <view class="uni-padding-wrap uni-common-mt">
    <uni-segmented-control :current="current" :values="items" :style-type="'text'" inActiveColor='#999'
                           activeColor='#5D5FEF' @clickItem="onClickItem"/>
  </view>
  <view class="content">
    <view :class="'content-text' + 0" v-if="current == 0"><slot name="content-0" ></slot></view>
    <view :class="'content-text' + 1" v-if="current == 1"><slot name="content-1" ></slot></view>
    <view :class="'content-text' + 2" v-if="current == 2"><slot name="content-2" ></slot></view>
    <view :class="'content-text' + 3" v-if="current == 3"><slot name="content-3" ></slot></view>
    <view :class="'content-text' + 4" v-if="current == 4"><slot name="content-4" ></slot></view>
    <view :class="'content-text' + 5" v-if="current == 5"><slot name="content-5" ></slot></view>
    <view :class="'content-text' + 6" v-if="current == 6"><slot name="content-6" ></slot></view>
    <view :class="'content-text' + 7" v-if="current == 7"><slot name="content-7" ></slot></view>
    <view :class="'content-text' + 8" v-if="current == 8"><slot name="content-8" ></slot></view>
    <view :class="'content-text' + 9" v-if="current == 9"><slot name="content-9" ></slot></view>
    <view :class="'content-text' + 10" v-if="current == 10"><slot name="content-10" ></slot></view>
    <view :class="'content-text' + 11" v-if="current == 11"><slot name="content-11" ></slot></view>
    <view :class="'content-text' + 12" v-if="current == 12"><slot name="content-12" ></slot></view>
    <view :class="'content-text' + 13" v-if="current == 13"><slot name="content-13" ></slot></view>
    <view :class="'content-text' + 14" v-if="current == 14"><slot name="content-14" ></slot></view>
    <view :class="'content-text' + 15" v-if="current == 15"><slot name="content-15" ></slot></view>
    <view :class="'content-text' + 16" v-if="current == 16"><slot name="content-16" ></slot></view>
    <view :class="'content-text' + 17" v-if="current == 17"><slot name="content-17" ></slot></view>
    <view :class="'content-text' + 18" v-if="current == 18"><slot name="content-18" ></slot></view>
    <view :class="'content-text' + 19" v-if="current == 19"><slot name="content-19" ></slot></view>
    <view :class="'content-text' + 20" v-if="current == 20"><slot name="content-20" ></slot></view>
    <view :class="'content-text' + 21" v-if="current == 21"><slot name="content-21" ></slot></view>
    <view :class="'content-text' + 22" v-if="current == 22"><slot name="content-22" ></slot></view>
    <view :class="'content-text' + 23" v-if="current == 23"><slot name="content-23" ></slot></view>
    <view :class="'content-text' + 24" v-if="current == 24"><slot name="content-24" ></slot></view>
    <view :class="'content-text' + 25" v-if="current == 25"><slot name="content-25" ></slot></view>
    <view :class="'content-text' + 26" v-if="current == 26"><slot name="content-26" ></slot></view>
    <view :class="'content-text' + 27" v-if="current == 27"><slot name="content-27" ></slot></view>
    <view :class="'content-text' + 28" v-if="current == 28"><slot name="content-28" ></slot></view>
    <view :class="'content-text' + 29" v-if="current == 29"><slot name="content-29" ></slot></view>
    <view :class="'content-text' + 30" v-if="current == 30"><slot name="content-30" ></slot></view>

<!--    <view v-for="(item, index) in items" class="cont" :key="index">-->


<!--      <slot name="'content-0' + "></slot>-->


<!--      <template v-slot:scopedSlot=" 'content-' + index">-->
<!--&lt;!&ndash;        <p>{{ slotProps.user.name }}</p>&ndash;&gt;-->
<!--      </template>-->

<!--      <view :class="'content-text' + index" v-show="current == index">-->
<!--          <slot :name="'content-' + index"></slot>-->
<!--&lt;!&ndash;                    <slot name="content-0"></slot>&ndash;&gt;-->

<!--&lt;!&ndash;        <template v-slot:content_+index>&ndash;&gt;-->

<!--&lt;!&ndash;        </template>&ndash;&gt;-->

<!--      </view>-->

<!--    </view>-->
  </view>
</template>

<script setup>
import {ref} from "vue";

const props = defineProps({
  items: {
    type: Array,
    required: true,
  },
});
const content_=ref("content_")
const current = ref(0);
const onClickItem = (e) => {
  console.log(e);
  current.value = e.currentIndex;
};
</script>

<style scoped lang="scss">
::v-deep .uni-padding-wrap,
.uni-common-mt {
  background: rgb(245, 247, 250);
  position: relative;
}

.content,
.cont,
.content-text {
  height: 100%;
  overflow: auto;
  background: rgb(245, 247, 250);
}

::v-deep .segmented-control__text {
  font-size: 1rem !important;
  font-weight: 700 !important;
}

::v-deep .segmented-control__text {
  span {
    color: rgba(166, 166, 166, 1)
  }
}

::v-deep .segmented-control__item--text {
  span {
    color: rgba(93, 95, 239, 1)
  }
}

::v-deep .segmented-control {
  height: 2.25rem !important;
  color: rgba(93, 95, 239, 1)
}

::v-deep .uni-section__content-title {
  display: flex;
  justify-content: flex-start;
  font-family: Montserrat;
  font-size: 12px !important;
  color: rgba(153, 153, 153, 1) !important;
}

::v-deep .uni-section .uni-section-header__content-sub {
  display: flex;
  justify-content: center !important;
  font-size: 26px !important;
  font-weight: 500 !important;
  color: rgba(51, 51, 51, 1) !important;
}

/* Add any custom styles here */
</style>