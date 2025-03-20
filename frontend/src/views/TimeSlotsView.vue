<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <v-sheet class="max-w-full" v-if="showAddMenu">
      <div class="flex flex-wrap">
        <div class="w-full md:w-1/2 p-2 max-w-md">
          <v-form fast-fail @submit.prevent>
            <v-text-field
                v-model="start_time"
                label="Start Time"
                color="orange"
                type="time"
            ></v-text-field>
            <v-text-field
                v-model="end_time"
                label="End Time"
                color="orange"
                type="time"
            ></v-text-field>
            <v-btn class="mt-2 mx-2" type="submit" @click="submitForm()" block>Save</v-btn>
          </v-form>
        </div>
      </div>
    </v-sheet>
    <br>
    <button type="submit" v-if="!showAddMenu" @click="showAddMenu = true" class="my-6 text-white inline-flex items-center justify-center bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
      <svg class="me-1 -ms-1 w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" clip-rule="evenodd"></path></svg>
      Add new Time Slot
    </button>
    <h2 class="text-4xl font-extrabold dark:text-white">All Time Slot's</h2>
    <div v-if="timeSlotStore.isLoading" class="text-center text-gray-500 py-6">
      <PulseLoader/>
    </div>
    <div v-else class="overflow-x-auto">
      <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400 my-6">
        <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
        <tr>
          <th scope="col" class="px-6 py-3">
            Start Time
          </th>
          <th scope="col" class="px-6 py-3">
            End Time
          </th>
          <th scope="col" class="px-6 py-3">
            Edit
          </th>
          <th scope="col" class="px-6 py-3">
            Delete
          </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="slot in timeSlotStore.getTimeSlots" :key="slot.id" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
          <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
            {{ slot.start_time }}
          </th>
          <td class="px-6 py-4">
            {{ slot.end_time }}
          </td>
          <td>
            <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline inline-block" @click="dialog = true, setTimeSlot(slot)">
              Update
            </v-btn>
          </td>
          <td>
            <form @submit.prevent class="inline-block">
              <v-btn @click="timeSlotStore.deleteTimeSlot(slot.id)"
                     color="red-lighten-2"
                     text="Delete"
              ></v-btn>
            </form>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
  <v-dialog v-model="dialog" max-width="900" persistent>
    <v-card prepend-icon="mdi-update" title="Update Time Slot">
      <v-text-field
          v-model="editTimeSlot.start_time"
          hide-details="auto"
          label="Start Time"
          type="time"
      ></v-text-field>
      <v-text-field
          v-model="editTimeSlot.end_time"
          hide-details="auto"
          label="End Time"
          type="time"
      ></v-text-field>
      <template v-slot:actions>
        <v-btn class="ms-auto" text="Close" @click="dialog = false"></v-btn>
        <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline" text="Update" @click="dialog = false, timeSlotStore.updateTimeSlot(editTimeSlot.id, editTimeSlot)"></v-btn>
      </template>
    </v-card>
  </v-dialog>
</template>

<script>
import {UseTimeSlotStore} from "@/stores/TimeSlotsStore.js";
import {useToast} from "vue-toastification";
import NavComponent from "@/components/SideBarComponent.vue";
import PulseLoader from "vue-spinner/src/PulseLoader.vue";
import { initFlowbite } from 'flowbite'

export default {
  components: {NavComponent, PulseLoader},
  data(){
    return {
      timeSlotStore: UseTimeSlotStore(),
      start_time: "",
      end_time: "",
      dialog: false,
      editTimeSlot: [],
      showAddMenu: false
    }
  },
  watch: {
    "timeSlotStore.success": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          this.showAddMenu = false;
          toast.success(newValue);
          this.timeSlotStore.success = "";
        }
      },
      immediate: true,
    },
    "timeSlotStore.failure": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.error(newValue);
          this.timeSlotStore.failure = "";
        }
      },
      immediate: true,
    },
  },
  mounted() {
    initFlowbite();
  },
  beforeMount(){
    this.timeSlotStore.fetchTimeSlots();
  },
  created() {
    this.timeSlotStore.success = "";
  },
  methods: {
    setTimeSlot(timeSlot) {
      this.editTimeSlot = JSON.parse(JSON.stringify(timeSlot));
    },
    submitForm() {
      this.timeSlotStore.insertTimeSlot(this.start_time, this.end_time);
      this.start_time = "";
      this.end_time = "";
    },
  }
}
</script>

<style scoped>

</style>