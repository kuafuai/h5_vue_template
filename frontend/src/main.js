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
import { createI18n } from 'vue-i18n';
import en from '@/i18n/en.js';
import zh from '@/i18n/zh.js';

export function createApp() {
    const lange = import.meta.env.VITE_APP_LANGE;
    const i18n = createI18n({
        locale: lange,         // 默认语言
        fallbackLocale: 'en', // 备用语言
        messages: {
            en,
            zh
        }
    });

    const app = createSSRApp(App);
    
    app.config.globalProperties.$axios = service;
    app.config.globalProperties.$api = api;
    app.config.globalProperties.$navigate = navigate;
    app.config.globalProperties.$back = back;
    app.component("login",login)
    app.component("login",login);
    app.use(i18n);
    app.config.globalProperties.$tt = i18n.global.t;
    return {
        app,
    };
}