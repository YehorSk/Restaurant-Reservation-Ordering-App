<template>
  <NavComponent/>
  <div class="p-4 sm:ml-64">
    <v-sheet class="max-w-full" v-if="showAddMenu">
      <div class="flex flex-wrap">
        <div class="w-full md:w-1/2 p-2 max-w-md">
          <v-form fast-fail @submit.prevent>
            <v-text-field
                v-model="capacity"
                :label="$t('Forms.Capacity')"
                color="orange"
                type="number"
            ></v-text-field>
            <v-text-field
                v-model="number"
                :label="$t('Forms.Number')"
                color="orange"
            ></v-text-field>
            <v-btn class="mt-2 mx-2" type="submit" @click="submitForm()" block>{{ $t('Forms.Save') }}</v-btn>
          </v-form>
        </div>
      </div>
    </v-sheet>
    <br>
    <button type="submit" v-if="!showAddMenu" @click="showAddMenu = true" class="my-6 text-white inline-flex items-center justify-center bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
      <svg class="me-1 -ms-1 w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" clip-rule="evenodd"></path></svg>
      Add new Table
    </button>
    <h2 class="text-4xl font-extrabold dark:text-white">{{ $t('Tables.All_Tables') }}</h2>
    <div v-if="tableStore.isLoading" class="text-center text-gray-500 py-6">
      <PulseLoader/>
    </div>
    <div v-else class="overflow-x-auto">
      <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400 my-6">
        <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
        <tr>
          <th scope="col" class="px-6 py-3">
            {{ $t('Tables.Id') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Tables.Number') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Tables.Capacity') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Tables.Edit') }}
          </th>
          <th scope="col" class="px-6 py-3">
            {{ $t('Tables.Delete') }}
          </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="table in tableStore.getTables" :key="table.id" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
          <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
            {{ table.id }}
          </th>
          <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
            {{ table.number }}
          </th>
          <td class="px-6 py-4">
            {{ table.capacity }}
          </td>
          <td>
            <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline inline-block" @click="dialog = true, setTable(table)">
              {{ $t('Tables.Update') }}
            </v-btn>
          </td>
          <td>
            <form @submit.prevent class="inline-block">
              <v-btn @click="tableStore.deleteTable(table.id)"
                     color="red-lighten-2"
                     :text="$t('Tables.Delete')"
              ></v-btn>
            </form>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
  <v-dialog v-model="dialog" max-width="900" persistent>
    <v-card prepend-icon="mdi-update" :title="$t('Tables.Update_Table')">
      <v-text-field
          v-model="editTable.number"
          hide-details="auto"
          :label="$t('Tables.Number')"
      ></v-text-field>
      <v-text-field
          v-model="editTable.capacity"
          hide-details="auto"
          :label="$t('Tables.Capacity')"
          type="number"
      ></v-text-field>
      <template v-slot:actions>
        <v-btn class="ms-auto" :text="$t('Tables.Close')" @click="dialog = false"></v-btn>
        <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline" :text="$t('Tables.Update')" @click="dialog = false, tableStore.updateTable(editTable)"></v-btn>
      </template>
    </v-card>
  </v-dialog>
</template>

<script>

import {UseTableStore} from "@/stores/TablesStore.js";
import {useToast} from "vue-toastification";
import NavComponent from "@/components/SideBarComponent.vue";
import PulseLoader from "vue-spinner/src/PulseLoader.vue";

export default {
  components: {NavComponent, PulseLoader},
  data(){
    return {
      tableStore: UseTableStore(),
      number: "",
      capacity: 0,
      dialog: false,
      editTable: [],
      showAddMenu: false
    }
  },
  watch: {
    "tableStore.success": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          this.showAddMenu = false;
          toast.success(newValue);
          this.tableStore.success = "";
        }
      },
      immediate: true,
    },
    "tableStore.failure": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.error(newValue);
          this.tableStore.failure = "";
        }
      },
      immediate: true,
    },
  },
  mounted() {
    initFlowbite();
  },
  beforeMount(){
    this.tableStore.fetchTables();
  },
  created() {
    this.tableStore.success = "";
  },
  methods: {
    setTable(table) {
      this.editTable = JSON.parse(JSON.stringify(table));
    },
    submitForm() {
      this.tableStore.insertTable(this.number, this.capacity);
      this.number = "";
      this.capacity = 0;
    },
  }
}
</script>

<style scoped>

</style>