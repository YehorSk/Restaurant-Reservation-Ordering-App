import axios from "axios";
import {useStorage} from "@vueuse/core";
import {defineStore} from "pinia";

axios.defaults.baseURL = "http://localhost/SavchukBachelor/backend/public/api/";

export const UseHomeStore = defineStore("home", {
   state:() => ({
       user: useStorage('user',{}),
       token: useStorage('token',{}),
       order_stats: {
           isLoading: false,
           data: [],
       },
       reservation_stats: {
           isLoading: false,
           data: [],
       },
   }),
    getters: {
       getOrderStats(){
           return this.order_stats;
       },
        getReservationStats(){
            return this.reservation_stats;
        }
    },
    actions: {
        async getToken(){
            await axios.get('/sanctum/csrf-cookie');
        },
        async fetchOrderStats(){
            this.order_stats.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('order/stats',{
                    headers: {
                        'Accept': 'application/vnd.api+json',
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin":"*",
                        'Authorization': `Bearer ${this.token}`
                    }
                });
                console.log(response.data.data)
                this.order_stats.data = response.data.data;
            }catch (error) {
                console.log(error);
            }finally {
                this.order_stats.isLoading = false;
            }
        },
        async fetchReservationStats(){
            this.reservation_stats.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('reservation/stats',{
                    headers: {
                        'Accept': 'application/vnd.api+json',
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin":"*",
                        'Authorization': `Bearer ${this.token}`
                    }
                });
                console.log(response.data.data)
                this.reservation_stats.data = response.data.data;
            }catch (error) {
                console.log(error);
            }finally {
                this.reservation_stats.isLoading = false;
            }
        },
    }
});