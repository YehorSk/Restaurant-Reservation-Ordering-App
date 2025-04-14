import axios from "axios";
import {defineStore} from "pinia";
import {useStorage} from "@vueuse/core";
import {handleError} from "@/utils/errorHandler.js";

export const UseInfoStore = defineStore("info", {
    state:() => ({
        user: useStorage('user',{}),
        token: useStorage('token',{}),
        info: [],
        errors: '',
        isLoading: true,
        success: ''
    }),
    getters: {
        getInfo(){
            return this.info;
        }
    },
    actions: {
        async getToken(){
            await axios.get('/sanctum/csrf-cookie');
        },
        async fetchInfo(){
            this.isLoading = true;
            try {
                const response = await axios.get('get-restaurant-info');
                console.log(response.data.data)
                this.info = response.data.data;
            } catch (error) {
                console.log(error);
                handleError(error, this);
            }finally {
                this.isLoading = false;
            }
        },
        async updateInfo(name, description, address, phone, email, website, opening_hours){
            this.isLoading = true;
            try {
                const response = await axios.post('update-restaurant-info',{
                    name: name,
                    description: description,
                    phone: phone,
                    email: email,
                    address: address,
                    website: website,
                    opening_hours: opening_hours,
                });
                console.log(response.data.data)
                this.success = response.data.message;
                await this.fetchInfo();
            } catch (error) {
                console.log(error);
                handleError(error, this);
            }finally {
                this.isLoading = false;
            }
        }
    },
})