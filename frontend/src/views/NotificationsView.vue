<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <v-sheet class="max-w-full">
      <div class="flex flex-wrap">
        <div class="w-full md:w-1/2 p-2 max-w-md">
          <v-form fast-fail @submit.prevent>
            <v-text-field
                v-model="title"
                :label="$t('Forms.Title')"
                color="orange"
            ></v-text-field>
            <v-text-field
                v-model="body"
                :label="$t('Forms.Body')"
                color="orange"
            ></v-text-field>
            <v-btn class="mt-2 mx-2" type="submit" @click="sendNotification()" :disabled="!isFormValid" block>{{ $t('Forms.Send') }}</v-btn>
          </v-form>
        </div>
      </div>
    </v-sheet>
  </div>
</template>

<script>
import {UseNotificationStore} from "@/stores/NotificationStore.js";
import {useToast} from "vue-toastification";
import NavComponent from "@/components/SideBarComponent.vue";
import PulseLoader from "vue-spinner/src/PulseLoader.vue";
import { initFlowbite } from 'flowbite'

export default {
  name: "NotificationsView",
  components: {NavComponent, PulseLoader},
  data(){
    return {
      notificationsStore: UseNotificationStore(),
      title: "",
      body: ""
    }
  },
  watch: {
    "notificationsStore.success": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.success(newValue);
          this.notificationsStore.success = "";
        }
      },
      immediate: true,
    },
    "notificationsStore.failure": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.error(newValue);
          this.notificationsStore.failure = "";
        }
      },
      immediate: true,
    },
  },
  computed: {
    isFormValid() {
      return this.title !== "" && this.body !== "";
    },
  },
  mounted() {
    initFlowbite();
  },
  methods:{
    sendNotification(){
        this.notificationsStore.sendNotificationToEveryone(this.title, this.body);
        this.title = "";
        this.body = "";
    }
  }
}
</script>