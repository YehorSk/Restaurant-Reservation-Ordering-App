import axios from "axios";
import {defineStore} from "pinia";
import {useStorage} from "@vueuse/core";

axios.defaults.baseURL = "http://localhost/SavchukBachelor/backend/public/api/";

export const UseOrdersStore = defineStore("orders", {
    state: () => ({
        user: useStorage('user', {}),
        token: useStorage('token', {}),
        orders: [],
        errors: '',
        isLoading: true,
        success: ''
    }),
    getters: {
        getOrders(){
            return this.orders;
        }
    },
    actions: {
        async getToken(){
            await axios.get('/sanctum/csrf-cookie');
        },
        async fetchOrders(page = 1, search = ''){
            this.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('order/admin/getAllOrders?page=' + page,{
                    headers: {
                        'Accept': 'application/vnd.api+json',
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin":"*",
                        'Authorization': `Bearer ${this.token}`
                    },
                    params:{
                        search: search
                    }
                });
                console.log(response.data.data)
                this.orders = response.data.data;
            }catch (error) {
                console.log(error);
            }finally {
                this.isLoading = false;
            }
        },
        async updateOrder(order){
            try{
                const response = await axios.put("order/admin/updateOrder/" + order.id,{
                        status: order.status,
                    },
                    {
                        headers: {
                            'Accept': 'application/vnd.api+json',
                            "Content-Type": "application/json",
                            "Access-Control-Allow-Origin":"*",
                            'Authorization': `Bearer ${this.token}`
                        }
                    });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchOrders();
            }catch (error) {
                console.log(error);
            }
        },
        async destroyOrder(id){
            try{
                const response = await axios.delete("order/admin/deleteOrder/" + id,
                    {
                        headers: {
                            'Accept': 'application/vnd.api+json',
                            "Content-Type": "application/json",
                            "Access-Control-Allow-Origin":"*",
                            'Authorization': `Bearer ${this.token}`
                        }
                    });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchOrders();
            }catch (error) {
                console.log(error)
            }
        },
    }
});