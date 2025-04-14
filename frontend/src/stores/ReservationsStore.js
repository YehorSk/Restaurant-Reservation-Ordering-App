import axios from "axios";
import {defineStore} from "pinia";
import {useStorage} from "@vueuse/core";
import {handleError} from "@/utils/errorHandler.js";

export const UseReservationStore = defineStore("reservations", {
    state:() => ({
        user: useStorage('user',{}),
        token: useStorage('token',{}),
        reservations: [],
        errors: '',
        isLoading: true,
        success: '',
        failure: '',
        current_page: 1,
        total_pages: 1,
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
        async fetchReservations(search = ''){
            this.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('reservation/admin/getAllReservations?page=' + this.current_page,{
                    params:{
                        search: search
                    }
                });
                console.log(response.data.data)
                this.total_pages = response.data.data.last_page;
                this.current_page = this.current_page <= this.total_pages ? this.current_page : this.total_pages;
                this.reservations = response.data.data;
            }catch (error) {
                console.log(error);
                handleError(error, this);
            }finally {
                this.isLoading = false;
            }
        },
        async updateReservation(reservation){
            try{
                const response = await axios.put("reservation/admin/updateReservation/" + reservation.id,{
                        status: reservation.status,
                    });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchReservations();
            }catch (error) {
                console.log(error);
                handleError(error, this);
            }
        },
        async destroyReservation(id){
            try{
                const response = await axios.delete("reservation/admin/deleteReservation/" + id);
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchReservations();
            }catch (error) {
                console.log(error);
                handleError(error, this);
            }
        },
    }
})