<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <v-sheet class="max-w-full">
      <div class="flex flex-wrap">
        <div class="w-full md:w-1/2 p-2 max-w-md">
          <v-form fast-fail @submit.prevent>
            <v-text-field
                v-model="name"
                label="Name"
                color="orange"
            ></v-text-field>
            <v-text-field
                v-model="short_description"
                label="Short Description"
                color="orange"
            ></v-text-field>
            <v-text-field
                v-model="long_description"
                label="Long Description"
                color="orange"
            ></v-text-field>
            <v-text-field
                v-model="recipe"
                label="Recipe"
                color="orange"
            ></v-text-field>
            <v-text-field
                v-model="picture"
                label="Picture"
                color="orange"
            ></v-text-field>
            <v-text-field
                v-model="price"
                label="Price"
                color="orange"
                type="number"
            ></v-text-field>
            <v-btn class="mt-2 mx-2" type="submit" @click="submitForm()" block>Save</v-btn>
          </v-form>
        </div>
      </div>
    </v-sheet>
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
            Edit
          </th>
          <th scope="col" class="px-6 py-3">
            Delete
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
          <td>
            <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline inline-block" @click="dialog = true, setMenuItem(item)">
              Update/Show
            </v-btn>
          </td>
          <td>
            <form @submit.prevent class="inline-block">
              <v-btn @click="menuStore.destroyMenuItem(this.route.params.id, item)"
                     color="red-lighten-2"
                     text="Delete"
              ></v-btn>
            </form>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
  <v-dialog v-model="dialog" width="auto" persistent>
    <v-card min-width="600" prepend-icon="mdi-update" title="Update Menu Item">
      <v-text-field
          v-model="editMenuItem.name"
          hide-details="auto"
          label="Name"
      ></v-text-field>
      <v-textarea
          v-model="editMenuItem.short_description"
          hide-details="auto"
          label="Short Description"
      ></v-textarea>
      <v-textarea
          v-model="editMenuItem.long_description"
          hide-details="auto"
          label="Long Description"
      ></v-textarea>
      <v-textarea
          v-model="editMenuItem.recipe"
          hide-details="auto"
          label="Recipe"
      ></v-textarea>
      <v-text-field
          v-model="editMenuItem.picture"
          hide-details="auto"
          label="Picture"
      ></v-text-field>
      <v-text-field
          v-model="editMenuItem.price"
          hide-details="auto"
          label="Price"
          type="number"
      ></v-text-field>
      <template v-slot:actions>
        <v-btn class="ms-auto" text="Close" @click="dialog = false"></v-btn>
        <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline" text="Update" @click="dialog = false, this.menuStore.updateMenuItem(this.route.params.id,editMenuItem)"></v-btn>
      </template>
    </v-card>
  </v-dialog>
</template>

<script>
import {RouterLink, useRoute} from "vue-router";
import {UseMenuStore} from "@/stores/MenuStore.js";
import {useToast} from "vue-toastification";
import NavComponent from "@/components/SideBarComponent.vue";

export default {
  name: "MenuItemsView",
  components: {NavComponent},
  data(){
    return {
      route: useRoute(),
      menuStore: UseMenuStore(),
      name: "",
      short_description: "",
      long_description: "",
      recipe: "",
      picture: "",
      price: 0,
      dialog: false,
      editMenuItem: {}
    }
  },
  watch: {
    // Watch for changes in the success message
    "menuStore.success": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.success(newValue);
          this.menuStore.success = "";
        }
      },
      immediate: true,
    },
  },
  created() {
    this.menuStore.success = "";
  },
  beforeMount(){
    this.menuStore.fetchMenuItems(this.route.params.id)
  },
  methods:{
    submitForm(){
      this.menuStore.insertMenuItem(this.route.params.id, this.name, this.short_description, this.long_description, this.recipe, this.picture, this.price);
      this.name = "";
      this.short_description = "";
      this.long_description = "";
      this.recipe = "";
      this.picture = "";
      this.price = 0;
    },
    setMenuItem(item){
      this.editMenuItem = item;
    }
  }
}
</script>

<style scoped>

</style>