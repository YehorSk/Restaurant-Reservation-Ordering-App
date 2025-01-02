<template>
  <div class="p-4 sm:ml-64">
    <v-sheet class="max-w-full">
      <div class="flex flex-wrap">
        <div class="w-full md:w-1/2 p-2 max-w-md">
          <v-form fast-fail @submit.prevent>
            <v-text-field
                v-model="capacity"
                label="Capacity"
                color="orange"
                type="number"
            ></v-text-field>
            <v-text-field
                v-model="number"
                label="Number"
                color="orange"
            ></v-text-field>
            <v-btn class="mt-2 mx-2" type="submit" @click="submitForm()" block>Save</v-btn>
          </v-form>
        </div>
      </div>
    </v-sheet>
    <br>
    <h2 class="text-4xl font-extrabold dark:text-white">All Table's</h2>
    <div v-if="tableStore.isLoading" class="text-center text-gray-500 py-6">
      <RingLoader/>
    </div>
    <table v-else class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400 my-6">
      <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
      <tr>
        <th scope="col" class="px-6 py-3">
          Number
        </th>
        <th scope="col" class="px-6 py-3">
          Capacity
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
      <tr v-for="table in tableStore.getTables" :key="table.id" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
        <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
          {{ table.number }}
        </th>
        <td class="px-6 py-4">
          {{ table.capacity }}
        </td>
        <td>
          <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline inline-block" @click="dialog = true, setTable(table)">
            Update
          </v-btn>
        </td>
        <td>
          <form @submit.prevent class="inline-block">
            <v-btn @click="tableStore.deleteTable(table.id)"
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
    <v-card min-width="600" prepend-icon="mdi-update" title="Update Time Slot">
      <v-text-field
          v-model="editTable.number"
          hide-details="auto"
          label="Number"
      ></v-text-field>
      <v-text-field
          v-model="editTable.capacity"
          hide-details="auto"
          label="Capacity"
          type="number"
      ></v-text-field>
      <template v-slot:actions>
        <v-btn class="ms-auto" text="Close" @click="dialog = false"></v-btn>
        <v-btn class="font-medium text-green-600 dark:text-green-500 hover:underline" text="Update" @click="dialog = false, tableStore.updateTable(editTable)"></v-btn>
      </template>
    </v-card>
  </v-dialog>
</template>

<script>

import {UseTableStore} from "@/stores/TablesStore.js";
import {useToast} from "vue-toastification";

export default {
  data(){
    return {
      tableStore: UseTableStore(),
      number: "",
      capacity: 0,
      dialog: false,
      editTable: []
    }
  },
  watch: {
    "tableStore.success": {
      handler(newValue) {
        if (newValue) {
          const toast = useToast();
          toast.success(newValue);
          this.tableStore.success = "";
        }
      },
      immediate: true,
    },
  },
  beforeMount(){
    this.tableStore.fetchTables();
  },
  created() {
    this.tableStore.success = "";
  },
  methods: {
    setTable(table) {
      this.editTable = table;
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