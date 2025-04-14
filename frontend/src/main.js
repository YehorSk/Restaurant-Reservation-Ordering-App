import { createApp } from 'vue'
import { createPinia } from 'pinia'
import {useStorage} from "@vueuse/core";
import 'vuetify/styles';
import i18n from './i18n';
import { createVuetify } from 'vuetify';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';
import 'primeicons/primeicons.css';
import Toast from 'vue-toastification';
import 'vue-toastification/dist/index.css';
import '@mdi/font/css/materialdesignicons.css'
import { isAxiosError} from "axios";
import { UseAuthStore } from "@/stores/AuthStore.js";


import App from './App.vue'
import router from './router'
import axios from "axios";

const app = createApp(App);

axios.defaults.baseURL = "https://api.platea.site/backend/public/api";

const lang = useStorage('language','en');

axios.interceptors.request.use((config) => {
    const token = useStorage('token',{}).value;
    config.headers.Authorization = `Bearer ${token}`;
    if (!config.headers["Content-Type"]) {
        config.headers["Content-Type"] = "application/json";
    }
    config.headers["Access-Control-Allow-Origin"] = "*";
    config.headers["lang"] = lang.value;
    config.headers.Accept = "application/vnd.api+json";
    return config;
});
app.use(router);

axios.interceptors.response.use(
    response => response,
    error => {
        if (isAxiosError(error)) {
            const authStore = UseAuthStore();
            console.log("Global Axios Error", error.response?.status);
            if (error.response?.status === 401) {
                authStore.clear_user();
                router.push({ name: 'login' });
            }
        }
        return Promise.reject(error);
    }
);

const vuetify = createVuetify({
    components,
    directives,
});

i18n.global.locale = lang.value;

const pinia = createPinia().use(() => {
    const  t  = i18n.global.t
    return { t }
})

app.use(pinia);

app.use(vuetify);
app.use(i18n);
app.use(Toast)


app.mount('#app');
