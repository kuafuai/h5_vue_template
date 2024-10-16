import {
    createSSRApp
} from "vue";
import api from "@/api"
import App from "./App.vue";
import service  from "@/utils/request";
import '@/styles/index.scss';
import login from "@/components/login.vue"
import navigate from "@/utils/navigate";
import back from "@/utils/back";
export function createApp() {
    const app = createSSRApp(App);
    
    app.config.globalProperties.$axios = service;
    app.config.globalProperties.$api = api;
    app.config.globalProperties.$navigate = navigate;
    app.config.globalProperties.$back = back;
    app.component("login",login)
    return {
        app,
    };
}