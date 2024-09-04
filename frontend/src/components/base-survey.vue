<template>
  <view class="survey-container">
    <view class="survey-header">
      <text class="survey-title">{{ surveyData.surveyName }}</text>
      <br/>
      <text class="survey-description">{{ surveyData.surveyDescription }}</text>
    </view>
    <view class="survey-body">
      <view v-for="question in surveyData.questions" :key="question.qid" class="question-section">
        <text class="question-title">
          <text style="color: red">{{ question.requiredFlag ? '*' : '' }}</text>
          {{ question.text }}
        </text>
        <view v-if="question.questionType === '1'">
          <uni-data-checkbox v-model="answers[question.value].selectedOption"
                             :localdata="question.options"></uni-data-checkbox>
          <!-- 单选题使用 UniApp 的 Radio Group -->
          <!--          <radio-group v-model="answers[question.qid].selectedOption">-->
          <!--            <label v-for="option in question.options" :key="option.oid" class="radio-label">-->
          <!--              <radio :value="option.oid" />{{ option.optionName }}-->
          <!--            </label>-->
          <!--          </radio-group>-->
        </view>
        <view v-else-if="question.questionType === '2'">
          <!-- 多选题使用 UniApp 的 Checkbox Group -->
          <uni-data-checkbox multiple v-model="answers[question.value].selectedOptions"
                             :localdata="question.options"></uni-data-checkbox>
          <!--          <checkbox-group v-model="answers[question.qid].selectedOptions">-->
          <!--            <label v-for="option in question.options" :key="option.oid" class="checkbox-label">-->
          <!--              <checkbox :value="option.oid" />{{ option.optionName }}-->
          <!--            </label>-->
          <!--          </checkbox-group>-->
        </view>
        <view v-else-if="question.questionType === '3'">
          <!-- 输入框使用 UniApp 的 Input -->
          <input
              v-model="answers[question.value].answerText"
              placeholder="请输入内容"
              class="input-box"
          />
        </view>
        <text :id="'rule'+ question.value" class="rule_text" style="color: red;font-size: small;display: none">此题为必填项
        </text>
      </view>
    </view>
    <button type="primary" @click="submit">提交</button>
  </view>
</template>

<script setup>
import {getCurrentInstance, ref} from "vue";

const {proxy} = getCurrentInstance();
const props = defineProps({
  id: {
    type: String,
    required: true
  },
  userId: {
    type: String,
    required: true
  },
  op_model: {
    type: String,
    required: true
  }
});

const emits = defineEmits(['success', 'expired']);
const surveyData = ref({});
const answers = ref({});
const rule_question = []
// 初始化答案对象
const initializeAnswers = () => {
  surveyData.value.questions.forEach(question => {
    if (question.requiredFlag) {
      rule_question.push(question.value)
    }
    if (question.questionType === "1") {
      answers.value[question.value] = {
        selectedOption: null,
      };
    } else if (question.questionType === "2") {
      answers.value[question.value] = {
        selectedOptions: [],
      };
    } else if (question.questionType === "3") {
      answers.value[question.value] = {
        answerText: "",
      };
    }
  });
};

// 获取问卷数据并初始化答案结构
const get_survey_write = async () => {
  try {
    const res = await proxy.$api[props.op_model].get(props.id);
    console.log("12121212121212121212121212121")
    console.log(res)
    surveyData.value = res.data;
    initializeAnswers();
  } catch (e) {
    console.log(e)
    emits("expired")
  }

};

get_survey_write();

const submit = async () => {
  const formattedAnswers = [];
  console.log(answers.value)

  for (let qid in answers.value) {
    console.log(qid)
    const question = surveyData.value.questions.find(q => q.value == qid);
    if (question.questionType === "1") {
      formattedAnswers.push({
        // id: null,
        userId: props.userId,
        surveyInfoId: surveyData.value.sid,
        questionInfoId: qid,
        optionInfoId: answers.value[qid].selectedOption,
        answerText: null,
      });
    } else if (question.questionType === "2") {
      answers.value[qid].selectedOptions.forEach(optionId => {
        formattedAnswers.push({
          userId: props.userId,
          surveyInfoId: surveyData.value.sid,
          questionInfoId: qid,
          optionInfoId: optionId,
          answerText: null,
        });
      });
    } else if (question.questionType === "3") {
      formattedAnswers.push({
        // id: null,
        userId: props.userId,
        surveyInfoId: surveyData.value.sid,
        questionInfoId: qid,
        optionInfoId: null,
        answerText: answers.value[qid].answerText
      });
    }
  }

  console.log("Formatted Answers:", formattedAnswers);

  // 校验 是否必填项
  var flag = false
  console.log(rule_question)



  for (var item in answers.value) {
    var a = answers.value[item]
    console.log(item, a)
    console.log(a.hasOwnProperty("selectedOption"))
    if (rule_question.includes(parseInt(item))) {
      if (a.hasOwnProperty("selectedOption") && a.selectedOption == null) {
        var d = document.querySelector("#rule" + item)
        if (d) {
          d.style.display = 'block'
          console.log("121212121212121212")
        }
        flag = true
      } else if (a.hasOwnProperty("selectedOptions") && a.selectedOptions.length == 0) {
        var d = document.querySelector("#rule" + item)
        if (d) {
          d.style.display = 'block'
          console.log("121212121212121212")
        }
        flag = true
      } else if (a.hasOwnProperty("answerText") && (a.answerText == null || a.answerText == "")) {
        var d = document.querySelector("#rule" + item)
        if (d) {
          d.style.display = 'block'
          console.log("121212121212121212")
        }
        flag = true
      }
    }

  }


  if (flag) {
    uni.showToast({
      title: "问卷不完整,请填写完成",
      icon: "none"
    })
    return
  }

  let res = await proxy.$api[props.op_model].add(formattedAnswers);
  if (res.code == 0) {
    emits("success")
  }
}


</script>

<style scoped>
.survey-container {
  padding: 1rem;
  box-sizing: border-box;
}

.survey-header {
  margin-bottom: 1rem;
  text-align: center;
}

.survey-title {
  text-align: center;
  font-size: 1.5rem;
  font-weight: bold;
}

.survey-description {
  font-size: 1.2rem;
  color: #666;
  margin-top: 0.5rem;
}

.question-section {
  margin-bottom: 1.5rem;
}

.question-title {
  font-size: .9rem;
  margin-bottom: 0.5rem;
  color: #666;
  font-weight: 350;
}

.radio-label,
.checkbox-label {
  display: flex;
  align-items: center;
  margin-top: 0.5rem;
}

.input-box {
  width: 100%;
  box-sizing: border-box;
  height: 2.5rem;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 0.5rem;
  margin-top: 0.5rem;
}
</style>
