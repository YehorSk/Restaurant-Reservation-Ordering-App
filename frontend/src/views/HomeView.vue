<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <h2 class="text-4xl font-extrabold dark:text-white">Statistics</h2>
    <br>
    <v-row>
      <v-col cols="12" sm="4" md="3">
        <v-card
            color="indigo"
            variant="flat"
        >
          <v-card-item>
            <v-card-title class="text-h4 me-2">Orders</v-card-title>
          </v-card-item>

          <v-card-text>
            <div v-if="!homeStore.getOrderStats.isLoading">
              <p class="text-h5 me-2"><span class="font-weight-bold">All: </span>{{homeStore.getOrderStats.data[0].data_all_count}}</p>
              <p class="text-h5 me-2"><span class="font-weight-bold">Today: </span>{{homeStore.getOrderStats.data[0].data_today}}</p>
            </div>
            <PulseLoader v-else/>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" sm="4" md="3">
        <v-card
            color="indigo"
            variant="flat"
        >
          <v-card-item>
            <v-card-title class="text-h4 me-2">Reservations</v-card-title>
          </v-card-item>

          <v-card-text>
            <div v-if="!homeStore.getOrderStats.isLoading">
              <p class="text-h5 me-2"><span class="font-weight-bold">All: </span>{{homeStore.getReservationStats.data[0].data_all_count}}</p>
              <p class="text-h5 me-2"><span class="font-weight-bold">Today: </span>{{homeStore.getReservationStats.data[0].data_today}}</p>
            </div>
            <PulseLoader v-else/>
          </v-card-text>
        </v-card>
      </v-col>

    </v-row>
  </div>
</template>

<script>
import NavComponent from "@/components/SideBarComponent.vue";
import { UseAuthStore } from "@/stores/AuthStore.js";
import { UseHomeStore} from "@/stores/HomeStore.js";
import { useToast } from 'vue-toastification';
import PulseLoader from "vue-spinner/src/PulseLoader.vue";


export default {
  components: {
    NavComponent,
    PulseLoader
  },
  data(){
    return{
      authStore: UseAuthStore(),
      homeStore: UseHomeStore(),
      toast: useToast(),
    }
  },
  watch: {
    // Watch for changes in the success message
    "authStore.success": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.success(newValue);
          this.authStore.success = "";
        }
      },
      immediate: true,
    },
  },
  beforeMount(){
    this.homeStore.fetchOrderStats()
    this.homeStore.fetchReservationStats()
  },
  created() {
    this.homeStore.success = "";
  },
}
</script>