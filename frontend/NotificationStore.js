import axios from "axios";
import {useStorage} from "@vueuse/core";
import {defineStore} from "pinia";

export const UseNotificationStore = defineStore("notifications", {
   state:() => ({
       user: useStorage('user',{}),
       token: useStorage('token',{}),
       errors: '',
       isLoading: true,
       success: ''
   }),
    actions: {
        async getToken(){
            await axios.get('/sanctum/csrf-cookie');
        },
        async sendNotificationToEveryone(title = "", body = ""){
            this.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.post('admin/sendToEveryone',{
                        title: title,
                        body: body,
                    },
                    {
                        headers: {
                            'Accept': 'application/vnd.api+json',
                            "Content-Type": "application/json",
                            "Access-Control-Allow-Origin":"*",
                            'Authorization': `Bearer ${this.token}`
                        }
                    });
                console.log(response.data.data)
                this.users = response.data.data;
            }catch (error) {
                console.log(error);
            }finally {
                this.isLoading = false;
            }
        },
    }
});