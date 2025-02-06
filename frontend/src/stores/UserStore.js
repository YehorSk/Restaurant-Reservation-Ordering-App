import axios from "axios";
import {useStorage} from "@vueuse/core";
import {defineStore} from "pinia";

export const UseUserStore = defineStore("users",{
   state:() => ({
       user: useStorage('user',{}),
       token: useStorage('token',{}),
       users: [],
       errors: '',
       isLoading: true,
       success: ''
   }) ,
    getters: {
       getUsers(){
           return this.users;
       }
    },
    actions: {
        async getToken(){
            await axios.get('/sanctum/csrf-cookie');
        },
        async fetchUsers(page = 1, search = ''){
            this.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('users?page=' + page,{
                    headers: {
                        'Accept': 'application/vnd.api+json',
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin":"*",
                        'Authorization': `Bearer ${this.token}`
                    },
                    params: {
                        search: search
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
        async updateUser(user){
            try{
                const response = await axios.put("users/" + user.id,{
                        name: user.name,
                        email: user.email,
                        role: user.role,
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
                await this.fetchUsers();
            }catch (error) {
                console.log(error);
            }
        },
        async destroyUser(id){
            try{
                const response = await axios.delete("users/" + id,
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
                await this.fetchUsers();
            }catch (error) {
                console.log(error)
            }
        },
    }
});