<style scoped>
@import '/src/assets/main.css';
</style>
<template>
  <section class="bg-gray-50 dark:bg-gray-900">
    <div class="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
      <div class="w-full p-6 bg-white rounded-lg shadow dark:border md:mt-0 sm:max-w-md dark:bg-gray-800 dark:border-gray-700 sm:p-8">
        <h2 class="mb-1 text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
          Change Password
        </h2>
        <form class="mt-4 space-y-4 lg:mt-5 md:space-y-5" @submit.prevent>
          <div>
            <label for="password" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">New Password</label>
            <input type="password" v-model="new_pwd" name="password" id="password" placeholder="••••••••" class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" required="">
          </div>
          <div>
            <label for="confirm-password" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Confirm password</label>
            <input type="password" v-model="confirm_new_pwd" name="confirm-password" id="confirm-password" placeholder="••••••••" class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" required="">
          </div>
          <button type="submit" @click="submitForm" class="w-full bg-blue-500 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50 text-white font-semibold py-2 px-4 rounded">
            Reset password
          </button>
        </form>
      </div>
    </div>
  </section>
</template>

<script>
import {UseAuthStore} from "@/stores/AuthStore.js";
import { initFlowbite } from 'flowbite'
import { useToast } from "vue-toastification";
import {useRoute} from "vue-router";

export default {
  name: "ForgotPassword",
  data(){
    return {
      authStore:  UseAuthStore(),
      success_dialog:false,
      toast: useToast(),
      new_pwd:'',
      confirm_new_pwd:'',
      route: useRoute(),
    }
  },
  mounted() {
    initFlowbite();
  },
  methods:{
    submitForm(){
      this.authStore.reset_password(this.new_pwd,this.confirm_new_pwd,this.route.query.email,this.route.params.token);
    }
  },
  watch: {
    "authStore.success": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.success(newValue);
          this.authStore.success = "";
        }
      },
      immediate: true,
    },
  },
}
</script>