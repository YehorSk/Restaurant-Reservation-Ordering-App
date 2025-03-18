<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <h2 class="text-4xl font-extrabold dark:text-white">All Reservation's</h2>
    <br>
    <form class="flex items-center max-w-sm mx-auto" @submit.prevent="onSearch">
      <div class="relative w-full">
        <input type="text" v-model="search" id="simple-search" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="Search..." />
      </div>
      <button type="submit" class="p-2.5 ms-2 text-sm font-medium text-white bg-blue-700 rounded-lg border border-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
        <svg class="w-4 h-4" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
          <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
        </svg>
        <span class="sr-only">Search</span>
      </button>
    </form>
    <br>
    <div v-if="reservationStore.isLoading" class="text-center text-gray-500 py-6">
      <PulseLoader/>
    </div>
    <div v-else class="overflow-x-auto">
      <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400 my-6">
        <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
        <tr>
          <th scope="col" class="px-6 py-3">
            Reservation Code
          </th>
          <th scope="col" class="px-6 py-3">
            Table Number
          </th>
          <th scope="col" class="px-6 py-3">
            Party Size
          </th>
          <th scope="col" class="px-6 py-3">
            Date
          </th>
          <th scope="col" class="px-6 py-3">
            Start Time
          </th>
          <th scope="col" class="px-6 py-3">
            Status
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
        <tr v-for="reservation in reservationStore.getReservations.data" :key="reservation.id" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
          <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
            {{ reservation.code }}
          </td>
          <td class="px-6 py-4">
            {{ reservation.table_number }}
          </td>
          <td class="px-6 py-4">
            {{ reservation.party_size }}
          </td>
          <td class="px-6 py-4">
            {{ reservation.date }}
          </td>
          <td class="px-6 py-4">
            {{ reservation.start_time }}
          </td>
          <td class="px-6 py-4">
            {{ reservation.status }}
          </td>
          <td>
            <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline inline-block" @click="dialog = true, setReservation(reservation)">
              Update
            </v-btn>
          </td>
          <td>
            <form @submit.prevent class="inline-block">
              <v-btn @click="reservationStore.destroyReservation(reservation.id)"
                     color="red-lighten-2"
                     text="Delete"
              ></v-btn>
            </form>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="text-center">
      <v-pagination
          v-model="reservationStore.current_page"
          :length="reservationStore.total_pages"
          rounded="circle"
      ></v-pagination>
    </div>
  </div>
  <v-dialog v-model="dialog" width="auto" persistent>
    <v-card
        min-width="600"
        prepend-icon="mdi-update"
        :title="'Update Reservation #' + edit_reservation.code"
    >
      <v-select
          v-model="edit_reservation.status"
          label="Status"
          :items="statuses"></v-select>
      <template v-slot:actions>
        <v-btn class="ms-auto" text="Close" @click="dialog = false"></v-btn>
        <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline" text="Update" @click="dialog = false, reservationStore.updateReservation(edit_reservation)"></v-btn>
      </template>
    </v-card>
  </v-dialog>
</template>

<script>
import NavComponent from "@/components/SideBarComponent.vue";
import {UseReservationStore} from "@/stores/ReservationsStore.js";
import {useToast} from "vue-toastification";
import PulseLoader from "vue-spinner/src/PulseLoader.vue";
import {watch} from "vue";

export default {
  name: "ReservationsView",
  components: {NavComponent, PulseLoader},
  data(){
    return{
      reservationStore: UseReservationStore(),
      dialog: false,
      edit_reservation: [],
      statuses: [
          'Pending',
          'Confirmed',
          'Cancelled',
      ],
      search: "",
    }
  },
  watch: {
    "reservationStore.success": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.success(newValue);
          this.reservationStore.success = "";
        }
      },
      immediate: true,
    },
    "reservationStore.failure": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.error(newValue);
          this.reservationStore.failure = "";
        }
      },
      immediate: true,
    },
  },
  mounted() {
    initFlowbite();
    watch(() => this.reservationStore.current_page, (newValue, oldValue) => {
      if (newValue) {
        this.reservationStore.fetchReservations(this.search)
      }
    });
  },
  beforeMount(){
    this.reservationStore.fetchReservations();
  },
  created() {
    this.reservationStore.success = "";
  },
  methods:{
    setReservation(reservation){
      this.edit_reservation = JSON.parse(JSON.stringify(reservation));
    },
    onSearch(){
      this.reservationStore.current_page = 1;
      this.reservationStore.fetchReservations(this.search);
    }
  }
}
</script>

<style scoped>

</style>