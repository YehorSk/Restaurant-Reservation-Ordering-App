<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <h2 class="text-4xl font-extrabold dark:text-white">{{ $t('Users.All_Users') }}</h2>
    <br>
    <form class="flex items-center max-w-sm mx-auto" @submit.prevent="onSearch">
      <div class="relative w-full">
        <input type="text" v-model="search" id="simple-search" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" :placeholder="$t('Users.Search')" />
      </div>
      <button type="submit" class="p-2.5 ms-2 text-sm font-medium text-white bg-blue-700 rounded-lg border border-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
        <svg class="w-4 h-4" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
          <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"/>
        </svg>
        <span class="sr-only">{{ $t('Users.Search') }}</span>
      </button>
    </form>
    <br>
    <div v-if="userStore.isLoading" class="text-center text-gray-500 py-6">
      <PulseLoader/>
    </div>
    <div v-else class="overflow-x-auto">
      <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400 my-6">
        <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
        <tr>
          <th scope="col" class="px-6 py-3">
            {{ $t('Users.Id') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Users.Name') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Users.Email') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Users.Language') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Users.Role') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Users.Edit') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Users.Delete') }}
          </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="user in userStore.getUsers.data" :key="user.id" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
          <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
            {{ user.id }}
          </th>
          <th class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
            {{ user.name }}
          </th>
          <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
            {{ user.email }}
          </td>
          <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
            {{ user.language }}
          </td>
          <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
            {{ user.role }}
          </td>
          <td>
            <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline inline-block" @click="dialog = true, setUser(user)">
              {{ $t('Users.Update') }}
            </v-btn>
          </td>
          <td>
            <form @submit.prevent class="inline-block">
              <v-btn @click="userStore.destroyUser(user.id)"
                     color="red-lighten-2"
                     :text="$t('Users.Delete')"
              ></v-btn>
            </form>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="text-center">
      <v-pagination
          v-model="userStore.current_page"
          :length="userStore.total_pages"
          rounded="circle"
      ></v-pagination>
    </div>
  </div>
  <v-dialog v-model="dialog" max-width="900" persistent>
    <v-card prepend-icon="mdi-update" :title="$t('Users.Update_User')">
      <v-text-field
          v-model="edit_user.name"
          hide-details="auto"
          :label="$t('Users.Name')"
      ></v-text-field>
      <v-text-field
          v-model="edit_user.email"
          hide-details="auto"
          :label="$t('Users.Email')"
      ></v-text-field>
      <v-select
        v-model="edit_user.role"
        :label="$t('Users.Role')"
        :items="roles"></v-select>
      <template v-slot:actions>
        <v-btn class="ms-auto" :text="$t('Users.Close')" @click="dialog = false"></v-btn>
        <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline" :text="$t('Users.Update')" @click="dialog = false, updateUser(edit_user)"></v-btn>
      </template>
    </v-card>
  </v-dialog>
</template>

<script>
import NavComponent from "@/components/SideBarComponent.vue";
import {UseUserStore} from "@/stores/UserStore.js";
import {useToast} from "vue-toastification";
import PulseLoader from "vue-spinner/src/PulseLoader.vue";
import {watch} from "vue";

export default {
  components: {NavComponent, PulseLoader},
  data(){
    return {
      userStore: UseUserStore(),
      dialog: false,
      edit_user: [],
      roles: [
          "user",
          "waiter",
          "chef",
          "admin"
      ],
      search: '',
    }
  },
  watch: {
    "userStore.success": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.success(newValue);
          this.userStore.success = "";
        }
      },
      immediate: true,
    },
    "userStore.failure": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.error(newValue);
          this.userStore.failure = "";
        }
      },
      immediate: true,
    },
  },
  mounted() {
    initFlowbite();
    watch(() => this.userStore.current_page, (newValue, oldValue) => {
      if (newValue) {
        this.userStore.fetchUsers(this.search)
      }
    });
  },
  beforeMount(){
    this.userStore.fetchUsers()
  },
  created() {
    this.userStore.success = "";
  },
  methods: {
    submitForm(){

    },
    setUser(user){
      this.edit_user = JSON.parse(JSON.stringify(user));
    },
    updateUser(user){
      this.userStore.updateUser(user);
    },
    onSearch() {
      this.userStore.current_page = 1;
      this.userStore.fetchUsers(this.search);
    },
  }
}
</script>


<style scoped>

</style>