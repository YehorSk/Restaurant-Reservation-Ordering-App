<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <h2 class="text-4xl font-extrabold dark:text-white">Restaurant Information</h2>
    <v-sheet class="max-w-full">
      <div class="flex flex-wrap">
        <!-- First Column (General Info) -->
        <div class="w-full md:w-1/2 p-2 max-w-md">
          <v-form fast-fail @submit.prevent>
            <v-text-field v-model="name" label="Name" color="orange"></v-text-field>
            <v-text-field v-model="description" label="Description" color="orange"></v-text-field>
            <v-text-field v-model="address" label="Address" color="orange"></v-text-field>
            <v-text-field v-model="phone" label="Phone" color="orange"></v-text-field>
            <v-text-field v-model="email" label="Email" color="orange"></v-text-field>
            <v-text-field v-model="website" label="Website" color="orange"></v-text-field>
          </v-form>
        </div>

        <!-- Second Column (Opening Hours) -->
        <div class="w-full md:w-1/2 p-2 max-w-md">
          <v-form fast-fail @submit.prevent>
            <v-text-field v-model="openingHours.Monday" label="Monday" color="orange"></v-text-field>
            <v-text-field v-model="openingHours.Tuesday" label="Tuesday" color="orange"></v-text-field>
            <v-text-field v-model="openingHours.Wednesday" label="Wednesday" color="orange"></v-text-field>
            <v-text-field v-model="openingHours.Thursday" label="Thursday" color="orange"></v-text-field>
            <v-text-field v-model="openingHours.Friday" label="Friday" color="orange"></v-text-field>
            <v-text-field v-model="openingHours.Saturday" label="Saturday" color="orange"></v-text-field>
            <v-text-field v-model="openingHours.Sunday" label="Sunday" color="orange"></v-text-field>
          </v-form>
        </div>

        <!-- Submit Button spanning both columns -->
        <div class="w-1/2 p-2 flex justify-center">
          <v-btn class="mt-2 mx-2" type="submit" @click="submitForm()" block>Save</v-btn>
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
        Monday: "",
        Tuesday: "",
        Wednesday: "",
        Thursday: "",
        Friday: "",
        Saturday: "",
        Sunday: "",
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
    submitForm() {
      this.infoStore.updateInfo(this.name, this.description, this.address, this.phone, this.email, this.website, this.openingHours);
    }
  }
}
</script>
