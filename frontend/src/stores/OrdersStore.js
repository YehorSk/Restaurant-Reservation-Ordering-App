import axios from "axios";
import {defineStore} from "pinia";
import {useStorage} from "@vueuse/core"
import {handleError} from "@/utils/errorHandler.js";

export const UseOrdersStore = defineStore("orders", {
    state: () => ({
        user: useStorage('user', {}),
        token: useStorage('token', {}),
        orders: [],
        errors: '',
        isLoading: true,
        success: '',
        failure: '',
        current_page: 1,
        total_pages: 1,
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
        async fetchOrders(search = ''){
            this.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('order/admin/getAllOrders?page=' + this.current_page,{
                    params:{
                        search: search
                    }
                });
                console.log(response.data.data)
                this.total_pages = response.data.data.last_page;
                this.current_page = this.current_page <= this.total_pages ? this.current_page : this.total_pages;
                this.orders = response.data.data;
            }catch (error) {
                console.log(error);
                handleError(error, this);
            }finally {
                this.isLoading = false;
            }
        },
        async updateOrder(order){
            try{
                const response = await axios.put("order/admin/updateOrder/" + order.id,{
                        status: order.status,
                });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchOrders();
            }catch (error) {
                console.log(error);
                handleError(error, this);
            }
        },
        async destroyOrder(id){
            try{
                const response = await axios.delete("order/admin/deleteOrder/" + id);
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchOrders();
            }catch (error) {
                console.log(error);
                handleError(error, this);
            }
        },
    }
});