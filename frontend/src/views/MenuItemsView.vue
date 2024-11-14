<script>
import {RouterLink, useRoute} from "vue-router";
import {UseMenuStore} from "@/stores/MenuStore.js";

export default {
  name: "MenuItemsView",
  data(){
    return {
      route: useRoute(),
      menuStore: UseMenuStore()
    }
  },
  beforeMount(){
    this.menuStore.fetchMenuItems(this.route.params.id)
  }
}
</script>

<template>
  <div class="p-4 sm:ml-64">
    <p>{{menuStore.getMenuItems}}</p>
  </div>
</template>

<style scoped>

</style>