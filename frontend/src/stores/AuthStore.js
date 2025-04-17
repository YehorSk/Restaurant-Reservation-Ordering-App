import axios from "axios";
import {defineStore} from "pinia";
import {useStorage} from "@vueuse/core";
import router from "@/router/index.js";
import {handleError} from "@/utils/errorHandler.js";

export const UseAuthStore = defineStore("auth",{
    state:() => ({
        user: useStorage('user',{}),
        token: useStorage('token',{}),
        lang: useStorage('language','en'),
        errors:'',
        isLoading: true,
        credentials:'',
        success: '',
        failure: '',
        loggedOut: ''
    }),
    getters: {
          getSuccess(){
              return this.success;
          },
          getCredentials(){
              return this.credentials;
          },
          getSuccessLoggedOut(){
              return this.loggedOut;
          }
    },
    actions:{
        async getToken(){
            await axios.get('/sanctum/csrf-cookie');
        },
        async authenticate() {
            try {
                await this.getToken();
                const response = await axios.post('user', null);
                this.user = response.data.data[0].user;
                this.lang = response.data.data[0].user.language;
                this.token = response.data.data[0].token;
                console.log("Auth "+this.token);
            } catch (error) {
                console.log(error.response.data.message);
                handleError(error, this);
            }
        },
        async login(email,password){
            await this.getToken();
            try {
                const response = await axios.post('login', {
                    email: email,
                    password: password,
                });
                console.log(response)
                this.lang = response.data.data[0].user.language;
                this.success = response.data.data.message;
                this.user = response.data.data[0].user;
                this.token = response.data.data[0].token;
                window.location.reload();
                console.log(this.token)
            } catch (error) {
                console.log(error.response);
                handleError(error, this);
            }
        },
        async logout() {
            try {
                await this.getToken();
                const response = await axios.post('logout', null);
                this.success = response.data.data.message;
                this.user = {};
                this.lang = 'en';
                this.token = null;
                window.location.reload();
            } catch (error) {
                console.log(error.response.data.message);
                handleError(error, this);
            }
        },
        async reset_password(new_pwd,confirm_new_pwd,email,token){
            try {
                await this.getToken();
                const response = await axios.post('update-password', {
                    email: email,
                    token:token,
                    password: new_pwd,
                    password_confirmation:confirm_new_pwd
                });
                this.success = "Updated successfully";
            } catch (error) {
                console.log(error);
                handleError(error, this);
            } finally {
                this.isLoading = false;
            }
        },
        clear_user(){
            this.token = {};
            this.lang = 'en';
            this.user = {};
        }
    }
})