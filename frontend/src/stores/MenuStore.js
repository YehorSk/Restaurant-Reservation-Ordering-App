import axios from "axios";
import {defineStore} from "pinia";
import {useStorage} from "@vueuse/core";

axios.defaults.baseURL = "http://localhost/SavchukBachelor/backend/public/api/";

export const UseMenuStore = defineStore("menu",{
    state:() => ({
        user: useStorage('user',{}),
        token: useStorage('token',{}),
        menus: [],
        errors: ''
    }),
    getters: {
        getMenus(){
            return this.menus;
        }
    },
    actions: {
        async getToken(){
            await axios.get('/sanctum/csrf-cookie');
        },
        async fetchMenus() {
            await this.getToken();
            try {
                const response = await axios.get('menu',{
                    headers: {
                        'Accept': 'application/vnd.api+json',
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin":"*",
                        'Authorization': `Bearer ${this.token}`
                    }
                });
                console.log(response.data)
                this.menus = response.data;
            } catch (error) {
                if (error.response.status === 422) {
                    this.errors.value = error.response.data.errors;
                }
            }
        },
    },
});
