import axios from "axios";
import {defineStore} from "pinia";
import {useStorage} from "@vueuse/core";

axios.defaults.baseURL = "http://localhost/SavchukBachelor/backend/public/api/";

export const UseTimeSlotStore = defineStore("timeslot",{
    state:() => ({
        isLoading: false,
        user: useStorage('user',{}),
        token: useStorage('token',{}),
        timeSlots: [],
        error: '',
        success: ''
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
                const response = await axios.get('timeSlots',{
                    headers: {
                        'Accept': 'application/vnd.api+json',
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin":"*",
                        'Authorization': `Bearer ${this.token}`
                    }
                });
                console.log(response.data.data)
                this.timeSlots = response.data.data;
            } catch (error) {
                console.log(error);
            }finally {
                this.isLoading = false;
            }
        },
        async insertTimeSlot(start_time, end_time) {
            try {
                console.log(start_time + " " + end_time);
                const response = await axios.post(
                    'timeSlots',
                    {
                        start_time: start_time,
                        end_time: end_time,
                    },
                    {
                        headers: {
                            Accept: 'application/vnd.api+json',
                            'Content-Type': 'application/json',
                            'Access-Control-Allow-Origin': '*',
                            Authorization: `Bearer ${this.token}`,
                        },
                    }
                );
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchTimeSlots();
            } catch (error) {
                console.error(error);
            }
        },

        async updateTimeSlot(timeSlotId, timeSlot) {
            try {
                const response = await axios.put(
                    `timeSlots/${timeSlotId}`,
                    {
                        start_time: timeSlot.start_time,
                        end_time: timeSlot.end_time,
                    },
                    {
                        headers: {
                            Accept: 'application/vnd.api+json',
                            'Content-Type': 'application/json',
                            'Access-Control-Allow-Origin': '*',
                            Authorization: `Bearer ${this.token}`,
                        },
                    }
                );
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchTimeSlots();
            } catch (error) {
                console.error(error);
            }
        },

        async deleteTimeSlot(timeSlotId) {
            try {
                const response = await axios.delete(
                    `timeSlots/${timeSlotId}`,
                    {
                        headers: {
                            Accept: 'application/vnd.api+json',
                            'Content-Type': 'application/json',
                            'Access-Control-Allow-Origin': '*',
                            Authorization: `Bearer ${this.token}`,
                        },
                    }
                );
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchTimeSlots();
            } catch (error) {
                console.error(error);
            }
        },
    }
});