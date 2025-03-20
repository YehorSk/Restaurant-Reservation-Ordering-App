<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <h2 class="text-4xl font-extrabold dark:text-white">Statistics</h2>
    <br>
    <VRow>
      <VCol
          cols="12"
          md="4"
          sm="12"
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
          sm="12"
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
      <VCol
          cols="12"
          md="4"
          sm="12"
          order="2"
      >
        <VCard
            title="Sales by year"
        >
          <VCardText>
            <VList>
              <div v-if="!homeStore.getMenuItemsStats.isLoading">
                <p class="text-h6 me-2"><span class="font-weight-bold">Total orders created: </span>{{ homeStore.getOrderStats.data.data_all_count }}</p>
                <p class="text-h6 me-2"><span class="font-weight-bold">Today: </span>{{ homeStore.getOrderStats.data.data_today }}</p>
                <StatProfits :data="homeStore.getOrderStats.data"/>
                <div class="d-flex justify-center mt-4">
                  <VBtn v-for="year in homeStore.getReservationStats.data.years" :key="year" @click="onOrderYearChange(year)">
                    {{ year }}
                  </VBtn>
                </div>
              </div>
              <PulseLoader v-else/>
            </VList>
          </VCardText>
        </VCard>
      </VCol>
      <VCol
          cols="12"
          md="4"
          sm="12"
          order="2"
      >
        <VCard
            title="Reservations by year"
        >
          <VCardText>
            <VList>
              <div v-if="!homeStore.getReservationStats.isLoading">
                <p class="text-h6 me-2"><span class="font-weight-bold">Total reservations created: </span>{{ homeStore.getReservationStats.data.data_all_count }}</p>
                <p class="text-h6 me-2"><span class="font-weight-bold">Today: </span>{{ homeStore.getReservationStats.data.data_today }}</p>
                <StatProfits :data="homeStore.getReservationStats.data"/>
                <div class="d-flex justify-center mt-4">
                  <VBtn v-for="year in homeStore.getReservationStats.data.years" :key="year" @click="onReservationYearChange(year)">
                    {{ year }}
                  </VBtn>
                </div>
              </div>
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
import BasicCard from "@/components/StatBasicCard.vue";
import StatProfits from "@/components/StatProfits.vue";
import { initFlowbite } from 'flowbite'

export default {
  components: {
    StatProfits,
    BasicCard,
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
  mounted() {
    initFlowbite();
  },
  watch: {
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
    this.homeStore.fetchOrderStats(2025)
    this.homeStore.fetchReservationStats(2025)
    this.homeStore.fetchMenuItemsStats()
  },
  created() {
    this.homeStore.success = "";
  },
  methods: {
    onOrderYearChange(year){
      this.homeStore.fetchOrderStats(year)
    },
    onReservationYearChange(year){
      this.homeStore.fetchReservationStats(year)
    }
  }
}
</script>