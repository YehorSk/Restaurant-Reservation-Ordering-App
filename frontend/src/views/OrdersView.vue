<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <h2 class="text-4xl font-extrabold dark:text-white">{{ $t('Orders.All_Orders') }}</h2>
    <br>
    <form class="flex items-center max-w-sm mx-auto" @submit.prevent="onSearch">
      <div class="relative w-full">
        <input type="text" v-model="search" id="simple-search" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" :placeholder="$t('default.Search')" />
      </div>
      <button type="submit" class="p-2.5 ms-2 text-sm font-medium text-white bg-blue-700 rounded-lg border border-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
        <svg class="w-4 h-4" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
          <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
        </svg>
        <span class="sr-only">{{ $t('default.Search') }}</span>
      </button>
    </form>
    <br>
    <div v-if="ordersStore.isLoading" class="text-center text-gray-500 py-6">
      <PulseLoader/>
    </div>
    <div v-else class="overflow-x-auto">
      <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400 my-6">
        <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
        <tr>
          <th scope="col" class="px-6 py-3">
            {{ $t('Orders.Id') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Orders.Client_Id') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Orders.Waiter_Id') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Orders.Order_Code') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Orders.Price') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Orders.Status') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('default.Edit') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('default.Delete') }}
          </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="order in ordersStore.getOrders.data" :key="order.id" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
          <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
            {{ order.id }}
          </td>
          <td class="px-6 py-4">
            {{ order.client_id }}
          </td>
          <td class="px-6 py-4">
            {{ order.waiter_id }}
          </td>
          <td class="px-6 py-4">
            {{ order.code }}
          </td>
          <td class="px-6 py-4">
            {{ order.price }}
          </td>
          <td class="px-6 py-4">
            {{ getTranslation(order.status) }}
          </td>
          <td>
            <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline inline-block" @click="dialog = true, setOrder(order)">
              {{ $t('default.Update') }}
            </v-btn>
          </td>
          <td>
            <form @submit.prevent class="inline-block">
              <v-btn @click="ordersStore.destroyOrder(order.id)"
                     color="red-lighten-2"
                     :text="$t('default.Delete')"
              ></v-btn>
            </form>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="text-center">
      <v-pagination
          v-model="ordersStore.current_page"
          :length="ordersStore.total_pages"
          rounded="circle"
      ></v-pagination>
    </div>
  </div>
  <v-dialog v-model="dialog" max-width="900" persistent>
    <v-card
        prepend-icon="mdi-update"
        :title="$t('default.Update')"
    >
      <v-select
          v-model="edit_order.status"
          :label="$t('Orders.Status')"
          :items="statuses"
          item-title="label"
          item-value="value"
      ></v-select>
      <template v-slot:actions>
        <v-btn class="ms-auto" :text="$t('default.Close')" @click="dialog = false"></v-btn>
        <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline" :text="$t('default.Update')" @click="dialog = false, ordersStore.updateOrder(edit_order)"></v-btn>
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
import { initFlowbite } from 'flowbite'

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
    "ordersStore.failure": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.error(newValue);
          this.ordersStore.failure = "";
        }
      },
      immediate: true,
    },
  },
  computed: {
    statuses() {
      return [
        { label: this.getTranslation("Pending"), value: "Pending" },
        { label: this.getTranslation("Confirmed"), value: "Confirmed" },
        { label: this.getTranslation("Cancelled"), value: "Cancelled" },
        { label: this.getTranslation("Preparing"), value: "Preparing" },
        { label: this.getTranslation("Completed"), value: "Completed" },
        { label: this.getTranslation("Ready_for_Pickup"), value: "Ready for Pickup" }
      ]
    }
  },
  methods:{
    setOrder(order){
      this.edit_order = JSON.parse(JSON.stringify(order));
    },
    onSearch(){
      this.ordersStore.current_page = 1;
      this.ordersStore.fetchOrders(this.search);
    },
    getTranslation(status) {
      return this.$t(`Orders.${status}`);
    }
  }
}
</script>

<style scoped>

</style>