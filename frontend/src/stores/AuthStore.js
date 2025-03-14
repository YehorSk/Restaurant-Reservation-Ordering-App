import axios from "axios";
import {defineStore} from "pinia";
import {useStorage} from "@vueuse/core";
import router from "@/router/index.js";

export const UseAuthStore = defineStore("auth",{
    state:() => ({
        user: useStorage('user',{}),
        token: useStorage('token',{}),
        errors:'',
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
                this.token = response.data.data[0].token;
                console.log("Auth "+this.token);
            } catch (error) {
                console.log(error.response.data.message);
                if (error.response.status === 422) {
                    this.errors = error.response.data.message;
                }
                if (error.response.status === 401) {
                    this.user = {};
                    this.token = null;
                }
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
                this.success = response.data.data.message;
                this.user = response.data.data[0].user;
                this.token = response.data.data[0].token;
                await router.push('/');
                console.log(this.token)
            } catch (error) {
                console.log(error.response);
                if(error.response.status === 422){
                    this.credentials = error.response.data.message;
                    console.log(this.credentials);
                }
                else if(error.response.status === 401){
                    this.errors = error.response.data.errors;
                    console.log(this.errors);
                }
            }
        },
        async logout() {
            try {
                await this.getToken();
                const response = await axios.post('logout', null);
                this.successLoggedOut = response.data.data.message;
                this.user = {};
                this.token = null;
                window.location.reload();
            } catch (error) {
                console.log(error.response.data.message);
                if (error.response.status === 422) {
                    this.errors = error.response.data.message;
                }
            }
        },
        async forgot_password(email){
            try {
                const response = await axios.post('forgot-password', {
                    email: email,
                });
                this.success = "A link to reset your password has been sent to your email. Please check your inbox and follow the instructions to reset your password. If you do not see the email, please check your spam or junk folder.";
            } catch (error) {
                if(error.response.status === 422){
                    this.errors = error.response.data.errors;
                }
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
                if(error.response.status === 422){
                    this.errors = error.response.data.errors.password[0];
                }
            }
        },
    }
})