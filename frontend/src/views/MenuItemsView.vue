<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <v-sheet class="max-w-full" v-if="showAddMenu">
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
                v-model="price"
                label="Price"
                color="orange"
                type="number"
            ></v-text-field>
            <v-file-input
                v-model="addFile"
                accept="image/png, image/jpeg, image/bmp"
                :prepend-icon="null"
                color="orange"
                @change="onFileChange($event, 'add')"
                label="Choose Image"
            ></v-file-input>
            <v-img v-if="addImageUrl" :src="addImageUrl"></v-img>
            <v-btn class="mt-2 mx-2" type="submit" @click="submitForm()" block>Save</v-btn>
          </v-form>
        </div>
      </div>
    </v-sheet>
    <div class="relative overflow-x-auto">
      <h2 class="text-4xl font-extrabold dark:text-white">All Menu Items</h2>
      <br>
      <form class="flex items-center max-w-sm mx-auto" @submit.prevent="onSearch">
        <div class="relative w-full">
          <input type="text" v-model="search" id="simple-search" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="Search..." />
        </div>
        <button type="submit" class="p-2.5 ms-2 text-sm font-medium text-white bg-blue-700 rounded-lg border border-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
          <svg class="w-4 h-4" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
          </svg>
          <span class="sr-only">Search</span>
        </button>
      </form>
      <br>
      <button type="submit" v-if="!showAddMenu" @click="showAddMenu = true" class="my-6 text-white inline-flex items-center justify-center bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
        <svg class="me-1 -ms-1 w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" clip-rule="evenodd"></path></svg>
        Add new Item Menu
      </button>
      <div v-if="menuStore.isLoading" class="text-center text-gray-500 py-6">
        <PulseLoader/>
      </div>
      <table v-else class="my-6 w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
        <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
        <tr>
          <th scope="col" class="px-6 py-3">
            Image
          </th>
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
        <tr v-for="item in menuStore.menuItems.data" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
          <td class="px-6 py-4">
            <img :src="'https://api.platea.site/backend/public/storage/' + item.picture" class="w-16 md:w-32 max-w-full max-h-full" alt="Menu Item Picture">
          </td>
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
      <div class="text-center">
        <v-pagination
            v-model="menuStore.current_page_items"
            :length="menuStore.total_pages_items"
            rounded="circle"
        ></v-pagination>
      </div>
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
          v-model="editMenuItem.price"
          hide-details="auto"
          label="Price"
          type="number"
      ></v-text-field>
      <input type="file" accept="image/*" @change="onFileChange($event, 'update')" class="mb-4">
      <template v-slot:actions>
        <v-btn class="ms-auto" text="Close" @click="dialog = false"></v-btn>
        <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline" text="Update" @click="updateMenuItem"></v-btn>
      </template>
    </v-card>
  </v-dialog>
</template>

<script>
import {RouterLink, useRoute} from "vue-router";
import {UseMenuStore} from "@/stores/MenuStore.js";
import {useToast} from "vue-toastification";
import NavComponent from "@/components/SideBarComponent.vue";
import PulseLoader from "vue-spinner/src/PulseLoader.vue";
import {watch} from "vue";

export default {
  name: "MenuItemsView",
  components: {NavComponent, PulseLoader},
  data(){
    return {
      route: useRoute(),
      menuStore: UseMenuStore(),
      name: "",
      short_description: "",
      long_description: "",
      recipe: "",
      addFile: null,
      updateFile: null,
      addImageUrl: "",
      updateImageUrl: "",
      price: 0,
      dialog: false,
      editMenuItem: {},
      search: '',
      showAddMenu: false
    }
  },
  watch: {
    // Watch for changes in the success message
    "menuStore.success": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          this.showAddMenu = false;
          toast.success(newValue);
          this.menuStore.success = "";
        }
      },
      immediate: true,
    },
    "menuStore.failure": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.error(newValue);
          this.menuStore.failure = "";
        }
      },
      immediate: true,
    },
  },
  mounted() {
    initFlowbite();
    watch(() => this.menuStore.current_page_items, (newValue, oldValue) => {
      if (newValue) {
        this.menuStore.fetchMenuItems(this.search, this.route.params.id)
      }
    });
  },
  created() {
    this.menuStore.success = "";
  },
  beforeMount(){
    this.menuStore.fetchMenuItems(this.search, this.route.params.id)
  },
  methods:{
    submitForm(){
      this.menuStore.insertMenuItem(this.route.params.id, this.name, this.short_description, this.long_description, this.recipe, this.addFile, this.price);
      this.name = "";
      this.short_description = "";
      this.long_description = "";
      this.recipe = "";
      this.addFile = null;
      this.addImageUrl = '';
      this.addFileInput = '';
      this.price = 0;
    },
    setMenuItem(item){
      this.editMenuItem = JSON.parse(JSON.stringify(item));
    },
    updateMenuItem(){
      this.dialog = false;
      this.menuStore.updateMenuItem(this.route.params.id,this.editMenuItem, this.updateFile);
    },
    onSearch() {
      this.menuStore.current_page_items = 1;
      this.menuStore.fetchMenuItems(this.search, this.route.params.id);
    },
    createImage(file, form) {
      const reader = new FileReader();
      reader.onload = e => {
        if (form === 'update') {
        } else {
          this.addImageUrl = e.target.result;
        }
      };
      reader.readAsDataURL(file);
    },
    onFileChange(event, form) {
      const files = event.target.files;
      if (!files || files.length === 0) {
        console.log("No files selected.");
        return;
      }
      const file = files[0];
      if (form === 'update') {
        this.updateFile = file;
      } else {
        this.addFile = file;
      }
      this.createImage(file, form);
    },
  }
}
</script>

<style scoped>

</style>