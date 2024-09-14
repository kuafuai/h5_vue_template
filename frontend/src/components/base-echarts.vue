<template>
  <view class="all_echarts">
    <view v-if="is_echarts">
      <view class="chart" :id="'mychart-dom-bar' + randomInt" ref="mychart"></view>
    </view>
    <view v-else>

      <view v-for="item in option">
        <view v-if="item.children && item.children.length > 0" class="option-list">
          <!--          <view>有chailderen {{ item }}</view>-->
          <base-echarts :option="item.children" />

        </view>
        <view v-else>
          <!--          <view>没有chailderen {{ // item }}</view>-->
          <!--          <view v-for="(option_item, idx) in item" :key="idx" class="option-item">-->
          <view style="display: flex;flex-direction: row;align-items: center;justify-content: flex-start">
            <view class="option-text" v-if="item.name">{{ item.name }}</view>
            <view class="option-value">{{ item.value }}</view>
          </view>
          <!--          </view>-->
        </view>

      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted, defineProps } from 'vue';
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
  },
  group_result_show_display: {
    type: String,
    default: "text"
  }

});
console.log(props.option)
const is_echarts = ref(true)
const is_echarts_function = (tmp) => {
  // for (var i = 0; i < tmp.length; i++) {
  //   var op = tmp[i];
  //   if (op.hasOwnProperty("children")) {
  //     is_echarts_function(op.children)
  //   } else {
  //
  //     var t = Number(op["value"]);
  //     if (!isNaN(t)) {
  //       op["value"] = t;
  //     } else {
  //       console.log("操作模型", op.value)
  //       is_echarts.value = false
  //     }
  //   }
  // }
  if (props.group_result_show_display == 'text') {
    is_echarts.value = false
  } else if (props.group_result_show_display == 'chart') {
    is_echarts.value = true
  }
}
is_echarts_function(props.option)

// for () {
//
//   // var t = props.option[i]
//   // if (!t.hasOwnProperty("name") || t["name"] == null || t["name"] == "") {
//   //   is_echarts.value = false
//   //   break
//   // }
// }


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
import { getCurrentInstance } from "vue"
import { onReady, onShow } from "@dcloudio/uni-app";

const { proxy } = getCurrentInstance();
// debugger
onMounted(() => {
  let charBox = '#mychart-dom-bar' + randomInt
  var chartDom = document.querySelector(charBox)
  // debugger
  var myChart = echarts.init(chartDom);
  var option;

  option = {
    // tooltip: {
    //   trigger: 'item'
    // },
    legend: {
      top: '15%',
      left: 'center',
      itemWidth: 20,  // 图例标记的图形宽度
      itemHeight: 14,  // 图例标记的图形高度
      formatter: function (name) {
        return name.length > 10 ? name.slice(0, 10) + "..." : name;  // 控制图例显示的文本长度
      },
      orient: 'horizontal',  // 设置图例为水平排列
      itemGap: 20,  // 控制图例项之间的间距
      width: '100%',  // 设置图例容器的宽度为100%
      align: 'auto',  // 自动排列
      padding: [10, 50],  // 图例与容器边界的间距，防止内容溢出
      lineHeight: 30,  // 每行的高度
    },


    series: [
  {
    name: 'Access From',
    type: 'pie',
    radius: ['40%', '60%'],
    center: ['52%', '56%'],
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
      scale: false,  // 禁用鼠标移入时的放大效果
      label: {
        show: true,  // 保持强调状态下的标签显示
        fontSize: 14,  // 你可以自定义字体大小等样式
        fontWeight: 'normal'
      },
      labelLine: {
        show: true  // 保持标签引导线的显示
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
.all_echarts {
  /*padding: 5%;*/
  width: 100%;
  max-width: 90%;
  /* 确保容器不会超过页面宽度 */
  overflow: hidden;
  /* padding: 10px; */
  font-size: 0.875rem;

}

.chart {
  width: 300px;
  /* 确保图表容器占满宽度 */
  height: 50vh;
  /* 使用视口高度的 50% */
  margin: 0px auto;
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
