import axios from "axios";
import {defineStore} from "pinia";
import {useStorage} from "@vueuse/core";

export const UseTableStore = defineStore("table",{
    state:() => ({
        isLoading: false,
        user: useStorage('user',{}),
        token: useStorage('token',{}),
        tables: [],
        error: '',
        success: '',
        failure: ''
    }),
    getters: {
        getTables(){
            return this.tables;
        }
    },
    actions: {
        async getToken(){
            await axios.get('/sanctum/csrf-cookie');
        },
        async fetchTables() {
            this.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('tables');
                console.log(response.data.data)
                this.tables = response.data.data;
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
        async insertTable(number, capacity) {
            this.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.post('tables', {
                    number,
                    capacity
                });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchTables();
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
        async updateTable(table) {
            this.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.put(`tables/${table.id}`, {
                    number: table.number,
                    capacity: table.capacity
                });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchTables();
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
        async deleteTable(id) {
            this.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.delete(`tables/${id}`);
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchTables();
            }catch (error) {
                console.log(error);
                if(error.response.status === 422){
                    this.errors = error.response.data.errors;
                    this.failure = error.response.data.message;
                }
            } finally {
                this.isLoading = false;
            }
        }
    }
});