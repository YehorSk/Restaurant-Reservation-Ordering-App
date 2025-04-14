<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <h2 class="text-4xl font-extrabold dark:text-white">{{ $t('Reservation.All_Reservations') }}</h2>
    <br>
    <form class="flex items-center max-w-sm mx-auto" @submit.prevent="onSearch">
      <div class="relative w-full">
        <input type="text" v-model="search" id="simple-search" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" :placeholder="$t('Reservation.Search')" />
      </div>
      <button type="submit" class="p-2.5 ms-2 text-sm font-medium text-white bg-blue-700 rounded-lg border border-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
        <svg class="w-4 h-4" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
          <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
        </svg>
        <span class="sr-only">{{ $t('Reservation.Search') }}</span>
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
            {{ $t('Reservation.Id') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Reservation.Client_Id') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Reservation.Reservation_Code') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Reservation.Table_Number') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Reservation.Party_Size') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Reservation.Date') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Reservation.Start_Time') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Reservation.Status') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Reservation.Edit') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Reservation.Delete') }}
          </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="reservation in reservationStore.getReservations.data" :key="reservation.id" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
          <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
            {{ reservation.id }}
          </td>
          <td class="px-6 py-4">
            {{ reservation.client_id }}
          </td>
          <td class="px-6 py-4">
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
            {{ getTranslation(reservation.status) }}
          </td>
          <td>
            <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline inline-block" @click="dialog = true, setReservation(reservation)">
              {{ $t('Reservation.Update') }}
            </v-btn>
          </td>
          <td>
            <form @submit.prevent class="inline-block">
              <v-btn @click="reservationStore.destroyReservation(reservation.id)"
                     color="red-lighten-2"
                     :text="$t('Reservation.Delete')"
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
  <v-dialog v-model="dialog" max-width="900" persistent>
    <v-card
        prepend-icon="mdi-update"
        :title="$t('Reservation.Update')"
    >
      <v-select
          v-model="edit_reservation.status"
          :label="$t('Reservation.Status')"
          :items="statuses"
          item-title="label"
          item-value="value"
      ></v-select>
      <template v-slot:actions>
        <v-btn class="ms-auto" :text="$t('Reservation.Close')" @click="dialog = false"></v-btn>
        <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline" :text="$t('Reservation.Update')" @click="dialog = false, reservationStore.updateReservation(edit_reservation)"></v-btn>
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
import { initFlowbite } from 'flowbite'

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
  computed: {
    statuses() {
      return [
        { label: this.getTranslation("Pending"), value: "Pending" },
        { label: this.getTranslation("Confirmed"), value: "Confirmed" },
        { label: this.getTranslation("Cancelled"), value: "Cancelled" },
      ]
    }
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
    },
    getTranslation(status) {
      return this.$t(`Reservation.${status}`);
    }
  }
}
</script>

<style scoped>

</style>