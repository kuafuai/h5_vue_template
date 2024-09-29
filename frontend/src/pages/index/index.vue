<template>
    <div class='page-container'>
        <base-search @search="search_4215"
            :other_search_condition="[{ 'name': '缺陷是否都关联依据', 'value': [{ 'text': '是', 'value': 'hazard_basic_info_count=0' }, { 'text': '否', 'value': 'hazard_basic_info_count!=0' }] }]"
            ref="base_search_ref" firstSearchDataComment="任务名称"
            :searchData="{ check_type: null, company_name: null, checker: null, }" class="m-b-10"
            firstSearchData="taskName">
            <!-- <base-dialog ref="show_dialog" text="添加客户" type="bottom"> -->
            <template #collapse="{ item }">

                <uni-forms-item label="检查类型" name="checkType">
                    <base-select v-model="item.checkType" api="task_info_all.get_check_type_enums_list"
                        title="检查类型"></base-select>
                </uni-forms-item>

                <uni-forms-item label="企业名称" name="companyName">
                    <uni-easyinput placeholder="请输入企业名称" v-model="item.companyName" />
                </uni-forms-item>


                <uni-forms-item label="检查人" name="checker">
                    <base-select v-model="item.checker" api="task_info_all.get_user_info_list"
                        title="检查人"></base-select>
                </uni-forms-item>

            </template>
            <!-- </base-dialog> -->
        </base-search>

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

// const search_4215 = (
// ) => {
//   proxy.$refs.show_dialog.showDialog()

// }



</script>
<style scoped>
.page-container {
    width: 100vw;
    height: 100vh;
}
</style>