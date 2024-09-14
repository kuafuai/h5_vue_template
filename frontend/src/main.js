import {
    createSSRApp
} from "vue";
import api from "@/api"
import App from "./App.vue";
import service  from "@/utils/request";
import '@/styles/index.scss';
import login from "@/components/login.vue"
import navigate from "@/utils/navigate";
export function createApp() {
    const app = createSSRApp(App);
    
    app.config.globalProperties.$axios = service;
    app.config.globalProperties.$api = api;
    app.config.globalProperties.$navigate = navigate;
    app.component("login",login)
    return {
        app,
    };
}