<script>
import {UseMenuStore} from "@/stores/MenuStore.js";

export default{
  data(){
    return{
      menuStore: UseMenuStore(),
      dialog: false,
      current_menu: []
    }
  },
  beforeMount(){
    this.menuStore.fetchMenus()
  },
  methods:{
    setMenu(menu){
      this.current_menu = menu;
    }
  }
}
</script>

<template>
  <div class="p-4 sm:ml-64">
    <div class="relative overflow-x-auto">
      <h2 class="text-4xl font-extrabold dark:text-white">All Menu's</h2>
      <button type="submit" class="my-6 text-white inline-flex items-center justify-center bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
        <svg class="me-1 -ms-1 w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" clip-rule="evenodd"></path></svg>
        Add new Menu
      </button>
      <div v-if="menuStore.isLoading" class="text-center text-gray-500 py-6">
        <RingLoader/>
      </div>
      <table v-else class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400 my-6">
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
          <th scope="col" class="px-6 py-3">
            Edit
          </th>
          <th scope="col" class="px-6 py-3">
            Items
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
          <td>
            <button @click="dialog = true, setMenu(menu)" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800">
              Edit
            </button>
          </td>
          <td>
            <RouterLink :to="'/menu/'+menu.id" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800">
              Items
            </RouterLink>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
  <v-dialog v-model="dialog" width="auto" persistent>
    <v-card min-width="600" prepend-icon="mdi-update" title="Update Lecture">
      <h1>Hello</h1>
      <v-text-field
          v-model="current_menu.name"
          hide-details="auto"
          label="Main input"
      ></v-text-field>
      <v-checkbox v-model="current_menu.availability" label="Checkbox"></v-checkbox>
      <template v-slot:actions>
        <v-btn class="ms-auto" text="Close" @click="dialog = false"></v-btn>
        <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline" text="Add" @click="dialog = false"></v-btn>
      </template>
    </v-card>
  </v-dialog>
</template>

<style scoped>

</style>