import axios from "axios";
import {defineStore} from "pinia";
import {useStorage} from "@vueuse/core";

export const UseTimeSlotStore = defineStore("timeslot",{
    state:() => ({
        isLoading: false,
        user: useStorage('user',{}),
        token: useStorage('token',{}),
        timeSlots: [],
        error: '',
        success: '',
        failure: ''
    }),
    getters: {
        getTimeSlots(){
            return this.timeSlots;
        }
    },
    actions: {
        async getToken(){
            await axios.get('/sanctum/csrf-cookie');
        },
        async fetchTimeSlots() {
            this.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('timeSlots');
                console.log(response.data.data)
                this.timeSlots = response.data.data;
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
        async insertTimeSlot(start_time, end_time) {
            this.isLoading = true;
            try {
                console.log(start_time + " " + end_time);
                const response = await axios.post(
                    'timeSlots',
                    {
                        start_time: start_time,
                        end_time: end_time,
                    }
                );
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchTimeSlots();
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

        async updateTimeSlot(timeSlotId, timeSlot) {
            this.isLoading = true;
            try {
                const response = await axios.put(
                    `timeSlots/${timeSlotId}`,
                    {
                        start_time: timeSlot.start_time,
                        end_time: timeSlot.end_time,
                    }
                );
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchTimeSlots();
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

        async deleteTimeSlot(timeSlotId) {
            this.isLoading = true;
            try {
                const response = await axios.delete(
                    `timeSlots/${timeSlotId}`
                );
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchTimeSlots();
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