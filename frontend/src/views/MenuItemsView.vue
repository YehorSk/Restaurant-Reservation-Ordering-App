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
    <div class="relative overflow-x-auto">
      <h2 class="text-4xl font-extrabold dark:text-white">All Menu Items</h2>
      <button type="submit" class="my-6 text-white inline-flex items-center justify-center bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
        <svg class="me-1 -ms-1 w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" clip-rule="evenodd"></path></svg>
        Add new Item Menu
      </button>
      <table class="my-6 w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
        <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
        <tr>
          <th scope="col" class="px-6 py-3">
            Name
          </th>
          <th scope="col" class="px-6 py-3">
            Short Description
          </th>
          <th scope="col" class="px-6 py-3">
            Recipe
          </th>
          <th scope="col" class="px-6 py-3">
            Price
          </th>
          <th scope="col" class="px-6 py-3">
            Show More
          </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="item in menuStore.menuItems" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
          <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
            {{ item.name }}
          </th>
          <td class="px-6 py-4">
            {{ item.short_description }}
          </td>
          <td class="px-6 py-4">
            {{ item.recipe }}
          </td>
          <td class="px-6 py-4">
            {{ item.price }}
          </td>
          <td class="px-6 py-4">
            <button class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800">
              Show
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

  </div>
</template>

<style scoped>

</style>