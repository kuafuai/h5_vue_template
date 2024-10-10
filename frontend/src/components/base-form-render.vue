<template>
    <view>
        <v-form-render :form-json="formJson" :form-data="formData" :option-data="optionData" ref="vFormRef">
        </v-form-render>
        <el-button type="primary" @click="submitForm">提交</el-button>
    </view>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const formJson = reactive({
    "widgetList": [], "formConfig": {
        "modelName": "formData", "refName": "vForm", "rulesName": "rules",
        "labelWidth": 80, "labelPosition": "left", "size": "", "labelAlign": "label-left-align", "cssCode": "", "customClass": "",
        "functions": "", "layoutType": "PC", "jsonVersion": 3, "onFormCreated": "", "onFormMounted": "", "onFormDataChange": "",
        "onFormValidate": ""
    }
})
const formData = reactive({})
const optionData = reactive({})
const vFormRef = ref(null)

const submitForm = () => {
    vFormRef.value.getFormData().then(formData => {
        // Form Validation OK
        alert(JSON.stringify(formData))
    }).catch(error => {
        // Form Validation failed
        ElMessage.error(error)
    })
}
</script>
<style lang="scss" scoped>
::v-deep .no-widget-hint{
    display: none;
}
</style>