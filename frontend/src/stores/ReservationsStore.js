import axios from "axios";
import {defineStore} from "pinia";
import {useStorage} from "@vueuse/core";

axios.defaults.baseURL = "http://localhost/SavchukBachelor/backend/public/api/";

export const UseReservationStore = defineStore("reservations", {
    state:() => ({
        user: useStorage('user',{}),
        token: useStorage('token',{}),
        reservations: [],
        errors: '',
        isLoading: true,
        success: ''
    }),
    getters: {
        getReservations(){
            return this.reservations;
        }
    },
    actions: {
        async getToken(){
            await axios.get('/sanctum/csrf-cookie');
        },
        async fetchReservations(page = 1, search = ''){
            this.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('reservation/admin/getAllReservations?page=' + page,{
                    headers: {
                        'Accept': 'application/vnd.api+json',
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin":"*",
                        'Authorization': `Bearer ${this.token}`
                    },
                    params:{
                        search: search
                    }
                });
                console.log(response.data.data)
                this.reservations = response.data.data;
            }catch (error) {
                console.log(error);
            }finally {
                this.isLoading = false;
            }
        },
        async updateReservation(reservation){
            try{
                const response = await axios.put("reservation/admin/updateReservation/" + reservation.id,{
                        status: reservation.status,
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
                await this.fetchReservations();
            }catch (error) {
                console.log(error);
            }
        },
        async destroyReservation(id){
            try{
                const response = await axios.delete("reservation/admin/deleteReservation/" + id,
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
                await this.fetchReservations();
            }catch (error) {
                console.log(error)
            }
        },
    }
})