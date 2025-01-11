<template>
  <VCard>
    <v-card-item>
      <v-card-title class="text-h6 me-2 font-weight-bold">{{ this.title }}</v-card-title>
    </v-card-item>
    <v-card-text>
      <div v-if="this.isLoading">
        <p class="text-h6 me-2"><span class="font-weight-bold">All: </span>{{ this.data.data_all_count }}</p>
        <p class="text-h6 me-2"><span class="font-weight-bold">Today: </span>{{ this.data.data_today }}</p>
      </div>
      <PulseLoader v-else/>
    </v-card-text>
  </VCard>
</template>

<script>
import PulseLoader from "vue-spinner/src/PulseLoader.vue";
export default {
  components: {PulseLoader},
  name: "BasicCard",
  props:["isLoading", "data","title"]
}
</script>