<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <h2 class="text-4xl font-extrabold dark:text-white">Statistics</h2>
    <br>
    <VRow>
      <VCol
          cols="12"
          md="8"
      >
        <VCard>
          <v-card-item>
            <v-card-title class="text-h6 me-2 font-weight-bold">Orders</v-card-title>
          </v-card-item>
          <v-card-text>
            <div v-if="!homeStore.getOrderStats.isLoading">
              <p class="text-h6 me-2"><span class="font-weight-bold">All: </span>{{homeStore.getOrderStats.data[0].data_all_count}}</p>
              <p class="text-h6 me-2"><span class="font-weight-bold">Today: </span>{{homeStore.getOrderStats.data[0].data_today}}</p>
            </div>
            <PulseLoader v-else/>
          </v-card-text>
        </VCard>
      </VCol>
      <VCol
          cols="12"
          sm="4"
      >
        <VRow>
          <VCol
              cols="12"
              md="6"
          >
            <VCard>
              <v-card-item>
                <v-card-title class="text-h6 me-2 font-weight-bold">Orders</v-card-title>
              </v-card-item>
              <v-card-text>
                <div v-if="!homeStore.getOrderStats.isLoading">
                  <p class="text-h6 me-2"><span class="font-weight-bold">All: </span>{{homeStore.getOrderStats.data[0].data_all_count}}</p>
                  <p class="text-h6 me-2"><span class="font-weight-bold">Today: </span>{{homeStore.getOrderStats.data[0].data_today}}</p>
                </div>
                <PulseLoader v-else/>
              </v-card-text>
            </VCard>
          </VCol>
          <VCol
              cols="12"
              md="6"
          >
            <VCard>
              <v-card-item>
                <v-card-title class="text-h6 me-2 font-weight-bold">Reservations</v-card-title>
              </v-card-item>
              <v-card-text>
                <div v-if="!homeStore.getOrderStats.isLoading">
                  <p class="text-h6 me-2"><span class="font-weight-bold">All: </span>{{homeStore.getReservationStats.data[0].data_all_count}}</p>
                  <p class="text-h6 me-2"><span class="font-weight-bold">Today: </span>{{homeStore.getReservationStats.data[0].data_today}}</p>
                </div>
                <PulseLoader v-else/>
              </v-card-text>
            </VCard>
          </VCol>
        </VRow>
      </VCol>
      <VCol
          cols="12"
          md="4"
          sm="6"
          order="2"
      >
        <VCard
            title="Top 10 Favorite Menu Items"
        >
          <VCardText>
            <VList>
              <VListItem
                  v-for="item in homeStore.getMenuItemsStats.data[0].favorites"
                  :key="item.id"
                  v-if="!homeStore.getMenuItemsStats.isLoading"
              >
                <VListItemTitle>
                  {{ item.name }}
                </VListItemTitle>
                <template #append>
                  <VListItemAction>
                    <span class="me-2">{{ item.favorited_by_users_count }}</span>
                    <span class="text-disabled">x</span>
                  </VListItemAction>
                </template>
              </VListItem>
              <PulseLoader v-else/>
            </VList>
          </VCardText>
        </VCard>
      </VCol>
      <VCol
          cols="12"
          md="4"
          sm="6"
          order="2"
      >
        <VCard
            title="Top 10 Ordered Menu Items"
        >
          <VCardText>
            <VList>
              <VListItem
                  v-for="item in homeStore.getMenuItemsStats.data[0].ordered"
                  :key="item.id"
                  v-if="!homeStore.getMenuItemsStats.isLoading"
              >
                <VListItemTitle>
                  {{ item.name }}
                </VListItemTitle>
                <template #append>
                  <VListItemAction>
                    <span class="me-2">{{ item.orders_count }}</span>
                    <span class="text-disabled">x</span>
                  </VListItemAction>
                </template>
              </VListItem>
              <PulseLoader v-else/>
            </VList>
          </VCardText>
        </VCard>
      </VCol>
    </VRow>
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
    this.homeStore.fetchMenuItemsStats()
  },
  created() {
    this.homeStore.success = "";
  },
}
</script>