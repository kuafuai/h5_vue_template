<template>
  <view class="all_echarts">
  <view v-if="is_echarts">
    <view class="chart" :id="'mychart-dom-bar'+randomInt" ref="mychart"></view>
  </view>
  <view v-else>
    <view v-for="(option, idx) in option" :key="idx" class="option-item">
      <text class="option-text" v-if="option.name">{{ option.name }} -</text>
      <text class="option-value">{{ option.value }}</text>
    </view>
  </view>
  </view>
</template>

<script setup>
import {ref, onMounted, defineProps} from 'vue';
// import * as echarts from 'echarts';
// import ecCanvas from '@dcloudio/uni-ui/lib/ec-canvas/ec-canvas.vue';
const props = defineProps({
  option: {
    type: Object,
    default: '',
  },
  inx: {
    type: Number,
    default: ""
  }
});
console.log(props.option)
const is_echarts = ref(true)
for (var i = 0; i < props.option.length; i++) {
  var t = props.option[i]
  if (!t.hasOwnProperty("name")) {
    is_echarts.value = false
    break
  }
}

function getRandomInt(min, max) {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

const randomInt = getRandomInt(1, 999999);
console.log(randomInt); // 输出在 1 到 10 之间的整数，包括 1 和 10
// watch(
//     () => props.option,
//     (newVal) => {
//       // 当 option 发生变化时，更新 chartOption
//       chartOption.value = newVal;
//       console.log('option changed:', newVal);
//     },
//     { deep: true, immediate: true } // 深度监听，立即执行回调
// );
import * as echarts from 'echarts';
import {getCurrentInstance} from "vue"
import {onReady, onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();
// debugger
onMounted(() => {
  let charBox = '#mychart-dom-bar' + randomInt
  console.log(charBox, "charBoxcharBox")
  var chartDom = document.querySelector(charBox)

  console.log(chartDom, "akjf;lakdjf;lakdjfl;")
// debugger
  var myChart = echarts.init(chartDom);
  var option;

  option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: '5%',
      left: 'center'
    },
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '70%'],
        avoidLabelOverlap: true,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          position: 'outside',
          formatter: '{b|{b}: }{c}',  // 格式化标签，使用 rich 样式自定义
          rich: {
            b: {
              width: 100,  // 增加宽度
              overflow: 'break',  // 如果文字太长，则换行显示
            },
            c: {
              width: 50,
              align: 'right',
            }
          }
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold',
            formatter: '{b}: {c} ({d}%)',  // 在强调状态下显示百分比
          }
        },
        labelLine: {
          show: true  // 显示标签引导线
        },
        data: props.option
      }
    ]
  };

  option && myChart.setOption(option);
  // 监听窗口大小变化并调整图表大小
  // window.addEventListener('resize', () => {
  //   myChart.resize();
  // });
})

</script>

<style scoped>
.all_echarts{
  /*padding: 5%;*/
  width: 100%;
  max-width: 90%; /* 确保容器不会超过页面宽度 */
  overflow: hidden;
  padding: 10px;

}
.chart {
  width:300px; /* 确保图表容器占满宽度 */
  height: 50vh; /* 使用视口高度的 50% */
  margin: 20px auto;
}

.option-item {
  margin-top: 5px;
}

.option-value {
  color: #007aff;
}

.option-text {
  margin-left: 5px;
}
</style>
