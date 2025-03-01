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
                const response = await axios.get('timeSlots');
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
                    `timeSlots/${timeSlotId}`
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