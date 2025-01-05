<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <h2 class="text-4xl font-extrabold dark:text-white">All Reservation's</h2>
    <div v-if="reservationStore.isLoading" class="text-center text-gray-500 py-6">
      <RingLoader/>
    </div>
    <table v-else class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400 my-6">
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
      <tr v-for="reservation in reservationStore.getReservations" :key="reservation.id" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
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

export default {
  name: "ReservationsView",
  components: {NavComponent},
  data(){
    return{
      reservationStore: UseReservationStore(),
      dialog: false,
      edit_reservation: [],
      statuses: [
          'Pending',
          'Confirmed',
          'Cancelled',
      ]
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
  },
  beforeMount(){
    this.reservationStore.fetchReservations();
  },
  created() {
    this.reservationStore.success = "";
  },
  methods:{
    setReservation(reservation){
      this.edit_reservation = reservation;
    }
  }
}
</script>

<style scoped>

</style>