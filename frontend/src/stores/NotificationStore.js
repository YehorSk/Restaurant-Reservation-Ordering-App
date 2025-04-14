import axios from "axios";
import {useStorage} from "@vueuse/core";
import {defineStore} from "pinia";
import {handleError} from "@/utils/errorHandler.js";

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
                const response = await axios.post('notifications/admin/sendToEveryone',{
                        title: title,
                        body: body,
                    });
                console.log(response.data.data)
                this.success = response.data.message;
                this.users = response.data.data;
            }catch (error) {
                console.log(error);
                handleError(error, this);
            }finally {
                this.isLoading = false;
            }
        },
    }
});