import { createApp } from 'vue'
import { createPinia } from 'pinia'
import {useStorage} from "@vueuse/core";
import 'vuetify/styles';
import { createVuetify } from 'vuetify';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';
import 'primeicons/primeicons.css';
import Toast from 'vue-toastification';
import 'vue-toastification/dist/index.css';


import App from './App.vue'
import router from './router'
import axios from "axios";

const app = createApp(App);

axios.defaults.baseURL = "https://api.platea.site/backend/public/api/";

axios.interceptors.request.use((config) => {
    console.log(1);
    const token = useStorage('token',{}).value;
    config.headers.Authorization = `Bearer ${token}`;
    config.headers["Content-Type"] = "application/json";
    config.headers["Access-Control-Allow-Origin"] = "*";
    config.headers.Accept = "application/vnd.api+json";
    return config;
});

app.use(createPinia());
app.use(router);

const vuetify = createVuetify({
    components,
    directives,
});

app.use(vuetify);
app.use(Toast)


app.mount('#app');
