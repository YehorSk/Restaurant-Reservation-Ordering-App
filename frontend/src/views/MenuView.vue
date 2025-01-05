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
                    v-model="description"
                    label="Description"
                    color="orange"
                ></v-text-field>
                <v-btn class="mt-2 mx-2" type="submit" @click="submitForm()" block>Save</v-btn>
              </v-form>
            </div>
          </div>
        </v-sheet>
    <br>
      <h2 class="text-4xl font-extrabold dark:text-white">All Menu's</h2>
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
            Delete
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
            <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline inline-block" @click="dialog = true, setMenu(menu)">
              Update
            </v-btn>
          </td>
          <td>
            <form @submit.prevent class="inline-block">
              <v-btn @click="menuStore.destroyMenu(menu.id)"
                     color="red-lighten-2"
                     text="Delete"
              ></v-btn>
            </form>
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
  <v-dialog v-model="dialog" width="auto" persistent>
    <v-card min-width="600" prepend-icon="mdi-update" title="Update Menu">
      <v-text-field
          v-model="edit_menu.name"
          hide-details="auto"
          label="Name"
      ></v-text-field>
      <v-text-field
          v-model="edit_menu.description"
          hide-details="auto"
          label="Description"
      ></v-text-field>
      <template v-slot:actions>
        <v-btn class="ms-auto" text="Close" @click="dialog = false"></v-btn>
        <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline" text="Update" @click="dialog = false, updateMenu(edit_menu)"></v-btn>
      </template>
    </v-card>
  </v-dialog>
</template>

<script>
import {UseMenuStore} from "@/stores/MenuStore.js";
import {useToast} from "vue-toastification";
import NavComponent from "@/components/SideBarComponent.vue";

export default{
  components: {NavComponent},
  data(){
    return{
      menuStore: UseMenuStore(),
      name: "",
      description: "",
      dialog: false,
      edit_menu: []
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
  beforeMount(){
    this.menuStore.fetchMenus()
  },
  created() {
    this.menuStore.success = "";
  },
  methods:{
    setMenu(menu){
      this.edit_menu = menu;
    },
    submitForm(){
      this.menuStore.insertMenu(this.name, this.description);
      this.name = "";
      this.description = "";
    },
    updateMenu(menu){
      this.menuStore.updateMenu(menu);
    },
    destroyMenu(id){
      this.menuStore.destroyMenu(id);
    }
  }
}
</script>

<style scoped>

</style>