<script>

  import {UseAuthStore} from "@/stores/AuthStore.js";

  export default {
    data(){
      return{
        authStore: UseAuthStore()
      };
    }
  }
</script>

<template>

  <button data-drawer-target="logo-sidebar" data-drawer-toggle="logo-sidebar" aria-controls="logo-sidebar" type="button" class="inline-flex items-center p-2 mt-2 ms-3 text-sm text-gray-500 rounded-lg sm:hidden hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-200 dark:text-gray-400 dark:hover:bg-gray-700 dark:focus:ring-gray-600">
    <span class="sr-only">Open sidebar</span>
    <svg class="w-6 h-6" aria-hidden="true" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
      <path clip-rule="evenodd" fill-rule="evenodd" d="M2 4.75A.75.75 0 012.75 4h14.5a.75.75 0 010 1.5H2.75A.75.75 0 012 4.75zm0 10.5a.75.75 0 01.75-.75h7.5a.75.75 0 010 1.5h-7.5a.75.75 0 01-.75-.75zM2 10a.75.75 0 01.75-.75h14.5a.75.75 0 010 1.5H2.75A.75.75 0 012 10z"></path>
    </svg>
  </button>

  <aside id="logo-sidebar" class="fixed top-0 left-0 z-40 w-64 h-screen transition-transform -translate-x-full sm:translate-x-0" aria-label="Sidebar">
    <div class="h-full px-3 py-4 overflow-y-auto bg-gray-50 dark:bg-gray-800">
      <ul class="space-y-2 font-medium">
        <li>
          <RouterLink to="/" class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
            <svg class="w-5 h-5 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" x="0px" y="0px" width="100" height="100" viewBox="0 0 50 50">
              <path d="M 25 1.0507812 C 24.7825 1.0507812 24.565859 1.1197656 24.380859 1.2597656 L 1.3808594 19.210938 C 0.95085938 19.550938 0.8709375 20.179141 1.2109375 20.619141 C 1.5509375 21.049141 2.1791406 21.129062 2.6191406 20.789062 L 4 19.710938 L 4 46 C 4 46.55 4.45 47 5 47 L 19 47 L 19 29 L 31 29 L 31 47 L 45 47 C 45.55 47 46 46.55 46 46 L 46 19.710938 L 47.380859 20.789062 C 47.570859 20.929063 47.78 21 48 21 C 48.3 21 48.589063 20.869141 48.789062 20.619141 C 49.129063 20.179141 49.049141 19.550938 48.619141 19.210938 L 25.619141 1.2597656 C 25.434141 1.1197656 25.2175 1.0507812 25 1.0507812 z M 35 5 L 35 6.0507812 L 41 10.730469 L 41 5 L 35 5 z"></path>
            </svg>
            <span class="ms-3">Home</span>
          </RouterLink>
        </li>
        <li>
          <RouterLink to="/menu" class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
            <svg class="flex-shrink-0 w-5 h-5 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 18 18">
              <path d="M3 2h12a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V3a1 1 0 0 1 1-1Zm11 2H4v10h10V4ZM5 5.5h5a.5.5 0 0 1 0 1H5a.5.5 0 1 1 0-1Zm0 3h8a.5.5 0 0 1 0 1H5a.5.5 0 1 1 0-1Zm0 3h8a.5.5 0 0 1 0 1H5a.5.5 0 1 1 0-1Z"/>
            </svg>
            <span class="flex-1 ms-3 whitespace-nowrap">Menu</span>
          </RouterLink>
        </li>
        <li>
          <RouterLink to="/tables" class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
            <svg class="flex-shrink-0 w-5 h-5 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 18 18">
              <path d="M3 2h12a1 1 0 0 1 0 2H3a1 1 0 1 1 0-2Zm6 3a1 1 0 0 1 1 1v6h3a1 1 0 1 1 0 2H5a1 1 0 1 1 0-2h3V6a1 1 0 0 1 1-1Z"/>
            </svg>
            <span class="flex-1 ms-3 whitespace-nowrap">Tables</span>
          </RouterLink>
        </li>
        <li>
          <RouterLink to="/timeslots" class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
            <svg class="flex-shrink-0 w-5 h-5 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 18 18">
              <path d="M9 1a8 8 0 1 0 8 8A8 8 0 0 0 9 1Zm0 14a6 6 0 1 1 6-6 6 6 0 0 1-6 6Zm.5-11a.5.5 0 0 0-1 0v4.25l2.67 1.6a.5.5 0 0 0 .53-.85L9.5 8.14Z"/>
            </svg>
            <span class="flex-1 ms-3 whitespace-nowrap">Time Slots</span>
          </RouterLink>
        </li>
        <li>
          <RouterLink to="/users" class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
            <svg class="flex-shrink-0 w-5 h-5 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 18 18">
              <path d="M9 0a4.5 4.5 0 1 1 0 9A4.5 4.5 0 0 1 9 0Zm0 10.5c-3.03 0-6 1.49-6 4.5v1a1 1 0 0 0 1 1h10a1 1 0 0 0 1-1v-1c0-3.01-2.97-4.5-6-4.5Z"/>
            </svg>
            <span class="flex-1 ms-3 whitespace-nowrap">Users</span>
          </RouterLink>
        </li>
        <li>
          <RouterLink to="/orders" class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
            <svg class="flex-shrink-0 w-5 h-5 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 18 18">
              <path d="M5 1h8a1 1 0 0 1 1 1v14a1 1 0 0 1-1.5.87L9 15.2l-3.5 1.67A1 1 0 0 1 4 16V2a1 1 0 0 1 1-1Zm1 3a.5.5 0 0 0 0 1h6a.5.5 0 1 0 0-1H6Zm0 3a.5.5 0 0 0 0 1h6a.5.5 0 1 0 0-1H6Zm0 3a.5.5 0 0 0 0 1h4a.5.5 0 1 0 0-1H6Z"/>
            </svg>
            <span class="flex-1 ms-3 whitespace-nowrap">Orders</span>
          </RouterLink>
        </li>
        <li>
          <RouterLink to="/reservations" class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
            <svg class="flex-shrink-0 w-5 h-5 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 18 18">
              <path d="M6 1a1 1 0 1 1 2 0v1h2V1a1 1 0 1 1 2 0v1h2a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h2V1Zm8 4H4v9a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1V5ZM6 7a1 1 0 0 1 1 1v2h3a1 1 0 1 1 0 2H6a1 1 0 0 1-1-1V8a1 1 0 0 1 1-1Z"/>
            </svg>
            <span class="flex-1 ms-3 whitespace-nowrap">Reservation</span>
          </RouterLink>
        </li>
        <li>
          <RouterLink to="/notifications" class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
            <svg class="flex-shrink-0 w-5 h-5 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white"
                 aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor"
                 viewBox="0 0 20 20">
              <path fill-rule="evenodd" clip-rule="evenodd" d="M10 2a6 6 0 0 0-6 6v4.5l-1.5 1.5a1 1 0 0 0 .7 1.7h13.6a1 1 0 0 0 .7-1.7L16 12.5V8a6 6 0 0 0-6-6Zm-4 6a4 4 0 1 1 8 0v4.5a1 1 0 0 0 .3.7l1 1H4.7l1-1a1 1 0 0 0 .3-.7V8Zm2 8a2 2 0 1 0 4 0H8Z" />
            </svg>
            <span class="flex-1 ms-3 whitespace-nowrap">Notifications</span>
          </RouterLink>
        </li>
        <li>
          <RouterLink @click="authStore.logout()" to="/login" class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group">
            <svg class="flex-shrink-0 w-5 h-5 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 18 18">
              <path d="M7 2a1 1 0 0 0-1 1v2a1 1 0 0 0 2 0V4h6v10H8v-1a1 1 0 0 0-2 0v2a1 1 0 0 0 1 1h8a2 2 0 0 0 2-2V4a2 2 0 0 0-2-2H7ZM4.707 11.707a1 1 0 0 0 1.414-1.414L5.414 10H10a1 1 0 1 0 0-2H5.414l.707-.707a1 1 0 1 0-1.414-1.414l-2.5 2.5a1 1 0 0 0 0 1.414l2.5 2.5Z"/>
            </svg>
            <span class="ms-3">Logout</span>
          </RouterLink>
        </li>
      </ul>
    </div>
  </aside>

</template>

<style scoped>
@import '/src/assets/main.css';
</style>