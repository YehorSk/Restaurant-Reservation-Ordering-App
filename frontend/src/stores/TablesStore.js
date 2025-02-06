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
        success: ''
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
                const response = await axios.get('tables', {
                    headers: {
                        'Accept': 'application/vnd.api+json',
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin": "*",
                        'Authorization': `Bearer ${this.token}`
                    }
                });
                console.log(response.data.data)
                this.tables = response.data.data;
            } catch (error) {
                console.log(error);
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
                }, {
                    headers: {
                        'Accept': 'application/vnd.api+json',
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin": "*",
                        'Authorization': `Bearer ${this.token}`
                    }
                });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchTables();
            } catch (error) {
                console.error(error);
                if (error.response?.status === 422) {
                    this.error = error.response.data.errors;
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
                }, {
                    headers: {
                        'Accept': 'application/vnd.api+json',
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin": "*",
                        'Authorization': `Bearer ${this.token}`
                    }
                });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchTables();
            } catch (error) {
                console.error(error);
                if (error.response?.status === 422) {
                    this.error = error.response.data.errors;
                }
            } finally {
                this.isLoading = false;
            }
        },
        async deleteTable(id) {
            this.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.delete(`tables/${id}`, {
                    headers: {
                        'Accept': 'application/vnd.api+json',
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin": "*",
                        'Authorization': `Bearer ${this.token}`
                    }
                });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchTables();
            } catch (error) {
                console.error(error);
            } finally {
                this.isLoading = false;
            }
        }
    }
});