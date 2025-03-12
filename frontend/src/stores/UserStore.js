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
       success: '',
       failure: '',
       current_page: 1,
       total_pages: 1,
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
        async fetchUsers(search = ''){
            this.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('users?page=' + this.current_page,{
                    params: {
                        search: search
                    }
                });
                console.log(response.data.data)
                this.total_pages = response.data.data.last_page;
                this.current_page = this.current_page <= this.total_pages ? this.current_page : this.total_pages;
                this.users = response.data.data;
            }catch (error) {
                console.log(error);
                if(error.response.status === 422){
                    this.errors = error.response.data.errors;
                    this.failure = error.response.data.message;
                }
            } finally {
                this.isLoading = false;
            }
        },
        async updateUser(user){
            this.isLoading = true;
            try{
                const response = await axios.put("users/" + user.id,{
                        name: user.name,
                        email: user.email,
                        role: user.role,
                    });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchUsers();
            }catch (error) {
                console.log(error);
                if(error.response.status === 422){
                    this.errors = error.response.data.errors;
                    this.failure = error.response.data.message;
                }
            } finally {
                this.isLoading = false;
            }
        },
        async destroyUser(id){
            this.isLoading = true;
            try{
                const response = await axios.delete("users/" + id);
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchUsers();
            }catch (error) {
                console.log(error);
                if(error.response.status === 422){
                    this.errors = error.response.data.errors;
                    this.failure = error.response.data.message;
                }
            } finally {
                this.isLoading = false;
            }
        },
    }
});