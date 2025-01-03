import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from "@/views/LoginView.vue";
import {UseAuthStore} from "@/stores/AuthStore.js";
import MenuView from "@/views/MenuView.vue";
import MenuItemsView from "@/views/MenuItemsView.vue";
import TablesView from "@/views/TablesView.vue";
import TimeSlotsView from "@/views/TimeSlotsView.vue";

async function checkAuth(roleCheck, next) {
  const authStore = UseAuthStore();
  // await authStore.fetchUser();
  if (roleCheck(authStore.user)) {
    next();
  } else {
    next({ name: 'login' });
  }
}

const adminGuard = (to, from, next) => checkAuth(user => user.role === 'admin', next);
const userGuard = (to, from, next) => checkAuth(user => !user.id, next);


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      beforeEnter: adminGuard
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      beforeEnter: userGuard
    },
    {
      path: '/menu',
      name: 'menu',
      component: MenuView,
      beforeEnter: adminGuard
    },
    {
      path: '/menu/:id',
      name: 'menuItems',
      component: MenuItemsView,
      beforeEnter: adminGuard
    },
    {
      path: '/tables',
      name: 'tables',
      component: TablesView,
      beforeEnter: adminGuard
    },
    {
      path: '/timeslots',
      name: 'timeslots',
      component: TimeSlotsView,
      beforeEnter: adminGuard
    }
  ]
})

export default router
