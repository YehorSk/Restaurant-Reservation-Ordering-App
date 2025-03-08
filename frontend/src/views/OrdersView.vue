<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <h2 class="text-4xl font-extrabold dark:text-white">All Order's</h2>
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
    <div v-if="ordersStore.isLoading" class="text-center text-gray-500 py-6">
      <PulseLoader/>
    </div>
    <table v-else class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400 my-6">
      <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
      <tr>
        <th scope="col" class="px-6 py-3">
          Order Code
        </th>
        <th scope="col" class="px-6 py-3">
          Price
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
      <tr v-for="order in ordersStore.getOrders.data" :key="order.id" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
        <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
          {{ order.code }}
        </td>
        <td class="px-6 py-4">
          {{ order.price }}
        </td>
        <td class="px-6 py-4">
          {{ order.status }}
        </td>
        <td>
          <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline inline-block" @click="dialog = true, setOrder(order)">
            Update
          </v-btn>
        </td>
        <td>
          <form @submit.prevent class="inline-block">
            <v-btn @click="ordersStore.destroyOrder(order.id)"
                   color="red-lighten-2"
                   text="Delete"
            ></v-btn>
          </form>
        </td>
      </tr>
      </tbody>
    </table>
    <div class="text-center">
      <v-pagination
          v-model="ordersStore.current_page"
          :length="ordersStore.total_pages"
          rounded="circle"
      ></v-pagination>
    </div>
  </div>
  <v-dialog v-model="dialog" width="auto" persistent>
    <v-card
        min-width="600"
        prepend-icon="mdi-update"
        :title="'Update Reservation #' + edit_order.code"
    >
      <v-select
          v-model="edit_order.status"
          label="Status"
          :items="statuses"></v-select>
      <template v-slot:actions>
        <v-btn class="ms-auto" text="Close" @click="dialog = false"></v-btn>
        <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline" text="Update" @click="dialog = false, ordersStore.updateOrder(edit_order)"></v-btn>
      </template>
    </v-card>
  </v-dialog>
</template>

<script>
import NavComponent from "@/components/SideBarComponent.vue";
import {useToast} from "vue-toastification";
import PulseLoader from "vue-spinner/src/PulseLoader.vue";
import { UseOrdersStore } from "@/stores/OrdersStore.js";
import {watch} from "vue";

export default {
  name: "OrdersView",
  components: {NavComponent, PulseLoader},
  data(){
    return{
      dialog: false,
      search: "",
      page: 1,
      ordersStore: UseOrdersStore(),
      edit_order: [],
      statuses: [
        'Pending',
        'Confirmed',
        'Cancelled',
        'Preparing',
        'Completed',
        'Ready for Pickup',
      ],
    }
  },
  mounted() {
    initFlowbite();
    watch(() => this.ordersStore.current_page, (newValue, oldValue) => {
      if (newValue) {
        this.ordersStore.fetchOrders( this.search)
      }
    });
  },
  beforeMount(){
    this.ordersStore.fetchOrders();
  },
  watch: {
    "ordersStore.success": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.success(newValue);
          this.ordersStore.success = "";
        }
      },
      immediate: true,
    },
  },
  methods:{
    setOrder(order){
      this.edit_order = order;
    },
    onSearch(){
      this.ordersStore.current_page = 1;
      this.ordersStore.fetchOrders(this.search);
    }
  }
}
</script>

<style scoped>

</style>