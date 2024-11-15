<script>
import NavComponent from "@/components/SideBarComponent.vue";
import { UseAuthStore } from "@/stores/AuthStore.js";
import { useToast } from 'vue-toastification';

export default {
  data(){
      return{
        authStore: UseAuthStore(),
        toast: useToast(),
      }
  },
  watch: {
    // Watch for changes in the success message
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

<template>
  <div class="p-4 sm:ml-64">
    <h1>Hello World</h1>
  </div>
</template>
