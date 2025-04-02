import axios from "axios";
import {useStorage} from "@vueuse/core";
import {defineStore} from "pinia";
import i18n from "@/i18n.js";

export const UseHomeStore = defineStore("home", {
   state:() => ({
       user: useStorage('user',{}),
       token: useStorage('token',{}),
       lang: useStorage('language','en'),
       order_stats: {
           isLoading: false,
           data: [],
       },
       reservation_stats: {
           isLoading: false,
           data: [],
       },
       menuItems_stats: {
           isLoading: false,
           data: [],
       }
   }),
    getters: {
       getOrderStats(){
           return this.order_stats;
       },
        getReservationStats(){
            return this.reservation_stats;
        },
        getMenuItemsStats(){
           return this.menuItems_stats;
        }
    },
    actions: {
        async getToken(){
            await axios.get('/sanctum/csrf-cookie');
        },
        async fetchOrderStats(year){
            this.order_stats.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('order/stats/' + year);
                console.log(response.data.data)
                this.order_stats.data = response.data.data;
            }catch (error) {
                console.log(error);
            }finally {
                this.order_stats.isLoading = false;
            }
        },
        async fetchReservationStats(year){
            this.reservation_stats.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('reservation/stats/'  + year);
                console.log(response.data.data)
                this.reservation_stats.data = response.data.data;
            }catch (error) {
                console.log(error);
            }finally {
                this.reservation_stats.isLoading = false;
            }
        },
        async fetchMenuItemsStats(){
            this.menuItems_stats.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('menuItems/stats',{
                    headers: {
                        'Accept': 'application/vnd.api+json',
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin":"*",
                        'Authorization': `Bearer ${this.token}`
                    }
                });
                console.log(response.data.data)
                this.menuItems_stats.data = response.data.data;
            }catch (error) {
                console.log(error);
            }finally {
                this.menuItems_stats.isLoading = false;
            }
        },
        async changeLanguage(language){
            this.menuItems_stats.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('update-language/'+language,{
                    headers: {
                        'Accept': 'application/vnd.api+json',
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin":"*",
                        'Authorization': `Bearer ${this.token}`
                    }
                });
                console.log(response.data.data)
                this.success = response.data.data.message;
                this.lang = response.data.data[0].language;
                i18n.global.locale = this.lang;
            }catch (error) {
                console.log(error.response);
                if(error.response.status === 422){
                    this.credentials = error.response.data.message;
                    console.log(this.credentials);
                }
                else if(error.response.status === 401){
                    this.errors = error.response.data.errors;
                    console.log(this.errors);
                }
            }finally {
                this.menuItems_stats.isLoading = false;
            }
        },
    }
});