import { createApp } from 'vue'
import { createPinia } from 'pinia'
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

app.use(createPinia());
app.use(router);

const vuetify = createVuetify({
    components,
    directives,
});

app.use(vuetify);
app.use(Toast)


app.mount('#app');
