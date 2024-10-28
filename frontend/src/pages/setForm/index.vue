<template>
  <view>
    <v-form-designer ref="vfDesigner" :designer-config="designerConfig">
      <template #customSaveButton>
        <el-button type="text" @click="saveFormJson">保存</el-button>
      </template>
    </v-form-designer>

    <uni-popup ref="popup" style="width: 200px"
               borderRadius="10px 10px 10px 10px"
               background-color="#fff">
      <view class="m-10 w-full">
        <uni-forms ref="refForm" style="max-width: 100%" :modelValue="form" label-position="top">
          <uni-forms-item label="表单名称">
            <uni-easyinput v-model="form.formName" placeholder="请输入表单名称"/>
          </uni-forms-item>
          <uni-forms-item label="备注">
            <uni-easyinput v-model="form.formDescription" placeholder="请输入备注"/>
          </uni-forms-item>
        </uni-forms>
        <view class="flex-center-center">
          <button type="primary" size="mini" @click="cancel">取 消</button>
          <button type="primary" size="mini" @click="submitForm">确 定</button>
        </view>
      </view>
    </uni-popup>

  </view>

</template>

<script setup>
import {onShow} from "@dcloudio/uni-app";

const {proxy} = getCurrentInstance();

const designerConfig = ref({
  generateSFCButton: false,
  exportCodeButton: false,  //是否显示导出代码按钮
  toolbarMaxWidth: 420,
  toolbarMinWidth: 400,  //设计器工具按钮栏最小宽度（单位像素）
  formHeader: false,
  formTemplates: false,
  importJsonButton: false,
  exportJsonButton: false,
  previewFormButton: false,
})

const allParams = ref({})

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

if (currentUser != null) {
  let parse = JSON.parse(currentUser);
  allParams.value["currentUser"] = parse;
  allParams.value[parse["relevanceTable"] + "Id"] = parse.userId
}

const form = ref({
  formId: null,
  formName: null,
  formDescription: null,
  formContent: null,
  remark: null
})

function saveFormJson() {
  let formJson = proxy.$refs.vfDesigner.getFormJson()
  form.value.formContent = JSON.stringify(formJson);
  proxy.$refs.popup.open();
}

function submitForm() {
  console.log(form.value)
  proxy.$api.form_setting.update(form.value).then(res => {
    console.log(res)
    if (res.code === 0) {
      proxy.$refs.popup.close();
      proxy.$navigate('/pages/form/index')
    } else {
      uni.showToast({
        'title': '操作失败',
        'position': 'center',
        'icon': 'error'
      });
    }
  })
}

function cancel() {
  proxy.$refs.popup.close();
}


onMounted(async () => {
  if (allParams.value.formId && allParams.value.formId !== '') {
    let res = await proxy.$api['form_setting']["get"](allParams.value.formId);
    proxy.$nextTick(() => {
      proxy.$refs.vfDesigner.setFormJson(JSON.parse(res.data.formContent))
    });
    form.value = res.data
  }
  else{
    proxy.$nextTick(() => {
      // 加载表单json数据
      proxy.$refs.vfDesigner.setFormJson({"widgetList":[],"formConfig":{"modelName":"formData","refName":"vForm","rulesName":"rules","labelWidth":80,"labelPosition":"left","size":"","labelAlign":"label-left-align","cssCode":"","customClass":"","functions":"","layoutType":"PC","onFormCreated":"","onFormMounted":"","onFormDataChange":"","onFormValidate":""}})
    })
  }
});

onShow(() => {
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  const route = currentPage.route;
  uni.$emit('updateRoute', route);
})
</script>

<style scoped lang="scss">
::v-deep .uni-left-window {
  height: 100%;
}

::v-deep .uni-popup__wrapper {
  width: 600px;
  height: 400px;
}

</style>