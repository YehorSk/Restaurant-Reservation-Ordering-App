<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <h2 class="text-4xl font-extrabold dark:text-white">All User's</h2>
    <div v-if="userStore.isLoading" class="text-center text-gray-500 py-6">
      <RingLoader/>
    </div>
    <table v-else class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400 my-6">
      <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
      <tr>
        <th scope="col" class="px-6 py-3">
          Name
        </th>
        <th scope="col" class="px-6 py-3">
          Email
        </th>
        <th scope="col" class="px-6 py-3">
          Role
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
      <tr v-for="user in userStore.getUsers" :key="user.id" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
        <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
          {{ user.name }}
        </th>
        <td class="px-6 py-4">
          {{ user.email }}
        </td>
        <td class="px-6 py-4">
          {{ user.role }}
        </td>
        <td>
          <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline inline-block" @click="dialog = true, setUser(user)">
            Update
          </v-btn>
        </td>
        <td>
          <form @submit.prevent class="inline-block">
            <v-btn @click="userStore.destroyUser(user.id)"
                   color="red-lighten-2"
                   text="Delete"
            ></v-btn>
          </form>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
  <v-dialog v-model="dialog" width="auto" persistent>
    <v-card min-width="600" prepend-icon="mdi-update" title="Update User">
      <v-text-field
          v-model="edit_user.name"
          hide-details="auto"
          label="Name"
      ></v-text-field>
      <v-text-field
          v-model="edit_user.email"
          hide-details="auto"
          label="Email"
      ></v-text-field>
      <v-select
        v-model="edit_user.role"
        label="Role"
        :items="roles"></v-select>
      <template v-slot:actions>
        <v-btn class="ms-auto" text="Close" @click="dialog = false"></v-btn>
        <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline" text="Update" @click="dialog = false, updateUser(edit_user)"></v-btn>
      </template>
    </v-card>
  </v-dialog>
</template>

<script>
import NavComponent from "@/components/SideBarComponent.vue";
import {UseUserStore} from "@/stores/UserStore.js";
import {useToast} from "vue-toastification";

export default {
  components: {NavComponent},
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
      ]
    }
  },
  watch: {
    // Watch for changes in the success message
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
      this.edit_user = user;
    },
    updateUser(user){
      this.userStore.updateUser(user);
    }
  }
}
</script>


<style scoped>

</style>