<script>
import NavComponent from "@/components/NavComponent.vue";
import {UseMenuStore} from "@/stores/MenuStore.js";

export default{
  data(){
    return{
      menuStore: UseMenuStore()
    }
  },
  beforeMount(){
    this.menuStore.fetchMenus()
  }
}
</script>

<template>
  <NavComponent />
  <div class="p-4 sm:ml-64">


    <div class="relative overflow-x-auto">
      <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
        <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
        <tr>
          <th scope="col" class="px-6 py-3">
            Name
          </th>
          <th scope="col" class="px-6 py-3">
            Availability
          </th>
          <th scope="col" class="px-6 py-3">
            Description
          </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="menu in menuStore.getMenus" :key="menu.id" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
          <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
            {{ menu.name }}
          </th>
          <td class="px-6 py-4">
            {{ menu.availability }}
          </td>
          <td class="px-6 py-4">
            {{ menu.description }}
          </td>
        </tr>
        </tbody>
      </table>
    </div>

  </div>
</template>

<style scoped>

</style>