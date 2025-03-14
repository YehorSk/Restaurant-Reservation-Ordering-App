<script>
  import {UseAuthStore} from "@/stores/AuthStore.js";
  import { watch,reactive } from "vue";
  import { useToast } from "vue-toastification";
  import router from "@/router/index.js";

  export default {
    data(){
      return{
        email:  '',
        password: '',
        authStore:  UseAuthStore(),
        success_dialog:false,
        toast: useToast()
      }
    },
    mounted() {
      initFlowbite();
    },
    methods:{
      submitLogInForm(){
        this.authStore.login(this.email,this.password);
      },
      clearErrors(){
        this.authStore.credentials= '';
        this.authStore.errors = [];
      },
      clearForm(){
        this.email = '';
        this.password = '';
      }
    },
    watch:{
      "authStore.success": {
        handler(newValue) {
          if (newValue) {
            this.clearForm();
            const toast = useToast();
            toast.success(newValue);
            this.authStore.success = "";
          }
        },
        immediate: true,
      },
        'authStore.credentials': {
          handler(newValue) {
            if (newValue) {
              const toast = useToast();
              toast.error(newValue);
              this.authStore.credentials = "";
            }
          },
          immediate: true,
        },
      'authStore.loggedOut':{
        handler(newValue){
          if (newValue) {
            const toast = useToast();
            toast.success(newValue);
            this.authStore.loggedOut = "";
          }
        },
        immediate: true,
      }
    }
  }
</script>

<template>
  <div class="min-h-screen flex items-center justify-center w-full h-screen dark:bg-gray-950">
    <div class="bg-white dark:bg-gray-900 shadow-md rounded-lg px-8 py-6 max-w-md w-full">
      <h1 class="text-2xl font-bold text-center mb-4 dark:text-gray-200">PLATEA ADMIN</h1>
        <div class="mb-4">
          <label for="email" class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Email Address</label>
          <input
              type="email"
              id="email"
              v-model="email"
              :class="{'is-invalid':authStore.errors['email']}"
              class="shadow-sm rounded-md w-full px-3 py-2 border border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="your@email.com"
              required
          >
        </div>
        <div class="mb-4">
          <label for="password" class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Password</label>
          <input
              type="password"
              id="password"
              v-model="password"
              :class="{ 'is-invalid': authStore.errors['password'] || authStore.credentials }"
              class="shadow-sm rounded-md w-full px-3 py-2 border border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="Enter your password"
              required
          >
          <i style="color: #FF6600" class="input-icon uil uil-lock-alt"></i>
          <div v-if="authStore.credentials" class="invalid-feedback">
            <p class="mt-2 text-sm text-red-600 dark:text-red-500"><span class="font-medium">{{ authStore.credentials }}</span></p>
          </div>
        </div>
        <button
            type="submit"
            @click="submitLogInForm"
            class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        >
          Login
        </button>
    </div>
  </div>
</template>

<style scoped>

</style>