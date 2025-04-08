<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <h2 class="text-4xl font-extrabold dark:text-white">Restaurant Information</h2>
    <v-sheet class="max-w-full">
      <div class="flex flex-wrap">
        <!-- First Column (General Info) -->
        <div class="w-full md:w-1/2 p-2 max-w-md">
          <v-form fast-fail @submit.prevent>
            <v-text-field v-model="name" :label="$t('Forms.Name')" color="orange"></v-text-field>
            <v-text-field v-model="description" :label="$t('Forms.Description')" color="orange"></v-text-field>
            <v-text-field v-model="address" :label="$t('Forms.Address')" color="orange"></v-text-field>
            <v-text-field v-model="phone" :label="$t('Forms.Phone')" color="orange"></v-text-field>
            <v-text-field v-model="email" :label="$t('Forms.Email')" color="orange"></v-text-field>
            <v-text-field v-model="website" :label="$t('Forms.Website')" color="orange"></v-text-field>
          </v-form>
        </div>

        <!-- Second Column (Opening Hours) -->
        <div class="w-full md:w-1/2 p-2 max-w-md">
          <v-form fast-fail @submit.prevent>
            <v-row>
              <v-col cols="6">
                <v-text-field v-model="openingHours.Monday.hours" :label="$t('Forms.Monday')" :disabled="!openingHours.Monday.isOpen" color="orange"></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-checkbox v-model="openingHours.Monday.isOpen" :label="$t('Forms.Open')"></v-checkbox>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="6">
                <v-text-field v-model="openingHours.Tuesday.hours" :label="$t('Forms.Thursday')" :disabled="!openingHours.Tuesday.isOpen" color="orange"></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-checkbox v-model="openingHours.Tuesday.isOpen" :label="$t('Forms.Open')"></v-checkbox>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="6">
                <v-text-field v-model="openingHours.Wednesday.hours" :label="$t('Forms.Wednesday')" :disabled="!openingHours.Wednesday.isOpen" color="orange"></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-checkbox v-model="openingHours.Wednesday.isOpen" :label="$t('Forms.Open')"></v-checkbox>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="6">
                <v-text-field v-model="openingHours.Thursday.hours" :label="$t('Forms.Thursday')" :disabled="!openingHours.Thursday.isOpen" color="orange"></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-checkbox v-model="openingHours.Thursday.isOpen" :label="$t('Forms.Open')"></v-checkbox>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="6">
                <v-text-field v-model="openingHours.Friday.hours" :label="$t('Forms.Friday')" :disabled="!openingHours.Friday.isOpen" color="orange"></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-checkbox v-model="openingHours.Friday.isOpen" :label="$t('Forms.Open')"></v-checkbox>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="6">
                <v-text-field v-model="openingHours.Saturday.hours" :label="$t('Forms.Saturday')" :disabled="!openingHours.Saturday.isOpen" color="orange"></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-checkbox v-model="openingHours.Saturday.isOpen" :label="$t('Forms.Open')"></v-checkbox>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="6">
                <v-text-field v-model="openingHours.Sunday.hours" :label="$t('Forms.Sunday')" :disabled="!openingHours.Sunday.isOpen" color="orange"></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-checkbox v-model="openingHours.Sunday.isOpen" :label="$t('Forms.Open')"></v-checkbox>
              </v-col>
            </v-row>
          </v-form>
        </div>

        <!-- Submit Button spanning both columns -->
        <div class="w-1/2 p-2 flex justify-center">
          <v-btn class="mt-2 mx-2" type="submit" @click="submitForm()" block>{{$t('Forms.Save')}}</v-btn>
        </div>
      </div>
    </v-sheet>

  </div>
</template>

<script>
import NavComponent from "@/components/SideBarComponent.vue";
import { UseInfoStore } from "@/stores/InfoStore.js";
import {useToast} from "vue-toastification";
import { initFlowbite } from 'flowbite'

export default {
  name: "InfoView",
  components: {NavComponent},
  data(){
    return{
      infoStore: UseInfoStore(),
      name: "",
      description: "",
      address: "",
      phone: "",
      email: "",
      website: "",
      openingHours: {
        Monday: { hours: "", isOpen: false },
        Tuesday: { hours: "", isOpen: false },
        Wednesday: { hours: "", isOpen: false },
        Thursday: { hours: "", isOpen: false },
        Friday: { hours: "", isOpen: false },
        Saturday: { hours: "", isOpen: false },
        Sunday: { hours: "", isOpen: false },
      },
      toast: useToast(),
    }
  },
  mounted() {
    initFlowbite();
  },
  beforeMount(){
    this.infoStore.fetchInfo()
  },
  created() {
    this.infoStore.success = "";
  },
  watch: {
    "infoStore.success": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.success(newValue);
          this.infoStore.success = "";
        }
      },
      immediate: true,
    },
    "infoStore.getInfo":{
      handler(newInfo) {
        if (newInfo.length > 0) {
          this.name = newInfo[0].name || "";
          this.description = newInfo[0].description || "";
          this.address = newInfo[0].address || "";
          this.phone = newInfo[0].phone || "";
          this.email = newInfo[0].email || "";
          this.website = newInfo[0].website || "";
          let new_hours = {};
          try {
            new_hours = JSON.parse(newInfo[0].opening_hours) || {};
          } catch (error) {
            console.error("Invalid JSON format for opening_hours:", error);
          }
          this.openingHours.Monday = new_hours.Monday || "";
          this.openingHours.Tuesday = new_hours.Tuesday || "";
          this.openingHours.Wednesday = new_hours.Wednesday || "";
          this.openingHours.Thursday = new_hours.Thursday || "";
          this.openingHours.Friday = new_hours.Friday || "";
          this.openingHours.Saturday = new_hours.Saturday || "";
          this.openingHours.Sunday = new_hours.Sunday || "";
        }
      },
      deep: true,
      immediate: true,
    }
  },
  methods: {
    validateHoursFormat(hours) {
      const regex = /^([01]\d|2[0-3]):[0-5]\d-([01]\d|2[0-3]):[0-5]\d$/;
      return regex.test(hours);
    },
    submitForm() {
      const days = Object.keys(this.openingHours);
      for (const day of days) {
        const dayData = this.openingHours[day];
        if (dayData.isOpen && !this.validateHoursFormat(dayData.hours)) {
          this.toast.error(`${this.$t(`Forms.${day}`)} ${this.$t('Forms.invalid_time')}`);
          return;
        }
      }

      this.infoStore.updateInfo(this.name, this.description, this.address, this.phone, this.email, this.website, this.openingHours);
    }
  }
}
</script>
