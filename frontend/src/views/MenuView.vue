<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
        <v-sheet class="max-w-full" v-if="showAddMenu">
          <div class="flex flex-wrap">
            <div class="w-full md:w-1/2 p-2 max-w-md">
              <v-form fast-fail @submit.prevent>
                <v-text-field
                    v-model="name"
                    :label="$t('Forms.Name')"
                    color="orange"
                ></v-text-field>
                <v-text-field
                    v-model="description"
                    :label="$t('Forms.Description')"
                    color="orange"
                ></v-text-field>
                <v-btn class="mt-2 mx-2" type="submit" @click="submitForm()" :disabled="!isFormValid" block>{{ $t('default.Save') }}</v-btn>
              </v-form>
            </div>
          </div>
        </v-sheet>
    <br>
      <h2 class="text-4xl font-extrabold dark:text-white">{{ $t('Menu.All_Menus')  }}</h2>
    <br>
    <form class="flex items-center max-w-sm mx-auto" @submit.prevent="onSearch">
      <div class="relative w-full">
        <input type="text" v-model="search" id="simple-search" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" :placeholder=" $t('default.Search') " />
      </div>
      <button type="submit" class="p-2.5 ms-2 text-sm font-medium text-white bg-blue-700 rounded-lg border border-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
        <svg class="w-4 h-4" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
          <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
        </svg>
        <span class="sr-only">{{ $t('default.Search')  }}</span>
      </button>
    </form>
    <br>
    <button type="submit" v-if="!showAddMenu" @click="showAddMenu = true" class="my-6 text-white inline-flex items-center justify-center bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
      <svg class="me-1 -ms-1 w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" clip-rule="evenodd"></path></svg>
      {{ $t("Menu.Add_new_Menu") }}
    </button>
      <div v-if="menuStore.isLoading" class="text-center text-gray-500 py-6">
        <PulseLoader/>
      </div>
    <div v-else class="overflow-x-auto">
      <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400 my-6">
        <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
        <tr>
          <th scope="col" class="px-6 py-3">{{ $t("Menu.Name") }}</th>
          <th scope="col" class="px-6 py-3">{{ $t("Menu.Description") }}</th>
          <th scope="col" class="px-6 py-3">{{ $t("Menu.Availability") }}</th>
          <th scope="col" class="px-6 py-3">{{ $t("default.Edit") }}</th>
          <th scope="col" class="px-6 py-3">{{ $t("default.Delete") }}</th>
          <th scope="col" class="px-6 py-3">{{ $t("Menu.Items") }}</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="menu in menuStore.getMenus.data" :key="menu.id" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
          <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
            {{ menu.name }}
          </th>
          <td class="px-6 py-4">
            {{ menu.description }}
          </td>
          <td class="px-6 py-4">
            {{ (menu.availability === 1) ? $t("Menu.Available") : $t("Menu.Not_Available") }}
          </td>
          <td>
            <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline inline-block" @click="dialog = true, setMenu(menu)">
              {{ $t('default.Update') }}
            </v-btn>
          </td>
          <td>
            <form @submit.prevent class="inline-block">
              <v-btn @click="menuStore.destroyMenu(menu.id)"
                     color="red-lighten-2"
                     :text="$t('default.Delete')"
              ></v-btn>
            </form>
          </td>
          <td>
            <RouterLink :to="'/menu/'+menu.id" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800">
              {{ $t("Menu.Items") }}
            </RouterLink>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="text-center">
      <v-pagination
          v-model="menuStore.current_page"
          :length="menuStore.total_pages"
          rounded="circle"
      ></v-pagination>
    </div>
  </div>
  <v-dialog v-model="dialog" max-width="900" persistent>
    <v-card prepend-icon="mdi-update" :title="$t('Menu.Update_Menu')">
      <v-text-field
          v-model="edit_menu.name"
          hide-details="auto"
          :label="$t('Menu.Name')"
      ></v-text-field>
      <v-text-field
          v-model="edit_menu.description"
          hide-details="auto"
          :label="$t('Menu.Description')"
      ></v-text-field>
      <v-checkbox v-model="edit_menu.availability" :true-value="1" :false-value="0" :label="$t('Menu.Is_available')"></v-checkbox>
      <template v-slot:actions>
        <v-btn class="ms-auto" :text="$t('default.Close')" @click="dialog = false"></v-btn>
        <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline" :text="$t('default.Update')" @click="dialog = false, updateMenu(edit_menu)"></v-btn>
      </template>
    </v-card>
  </v-dialog>
</template>

<script>
import {UseMenuStore} from "@/stores/MenuStore.js";
import {useToast} from "vue-toastification";
import NavComponent from "@/components/SideBarComponent.vue";
import PulseLoader from "vue-spinner/src/PulseLoader.vue";
import {watch} from "vue";
import { initFlowbite } from 'flowbite'

export default{
  components: {NavComponent, PulseLoader},
  data(){
    return{
      menuStore: UseMenuStore(),
      name: "",
      description: "",
      dialog: false,
      edit_menu: [],
      search: '',
      showAddMenu: false
    }
  },
  computed: {
    isFormValid() {
      return this.name.length >= 3 && this.description.length >= 5;
    }
  },
  watch: {
    "menuStore.success": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          this.clearForm();
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
    watch(() => this.menuStore.current_page, (newValue, oldValue) => {
      if (newValue) {
        this.menuStore.fetchMenus(this.search)
      }
    });
  },
  beforeMount(){
    this.menuStore.fetchMenus()
  },
  created() {
    this.menuStore.success = "";
  },
  methods:{
    setMenu(menu){
      this.edit_menu = JSON.parse(JSON.stringify(menu));
    },
    submitForm(){
      this.menuStore.insertMenu(this.name, this.description);
    },
    updateMenu(menu){
      this.menuStore.updateMenu(menu);
    },
    destroyMenu(id){
      this.menuStore.destroyMenu(id);
    },
    onSearch() {
      this.menuStore.current_page = 1;
      this.menuStore.fetchMenus(this.search);
    },
    clearForm(){
      this.name = "";
      this.description = "";
    }
  }
}
</script>

<style scoped>

</style>