import {
    createSSRApp
} from "vue";
import api from "@/api"
import App from "./App.vue";
import service from "@/utils/request";
import '@/styles/index.scss';
import login from "@/components/login.vue"
import navigate from "@/utils/navigate";
import ElementPlus from 'element-plus' //引入element-plus库
import 'element-plus/dist/index.css' //引入element-plus样式
import VForm3 from 'vform3-builds'
import 'vform3-builds/dist/designer.style.css'
export function createApp() {
    const app = createSSRApp(App);

    app.config.globalProperties.$axios = service;
    app.config.globalProperties.$api = api;
    app.config.globalProperties.$navigate = navigate;
    app.component("login", login)
    app.use(ElementPlus)
    app.use(VForm3)
    return {
        app,
    };
}