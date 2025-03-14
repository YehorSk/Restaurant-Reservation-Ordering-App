import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from "@/views/LoginView.vue";
import {UseAuthStore} from "@/stores/AuthStore.js";
import MenuView from "@/views/MenuView.vue";
import MenuItemsView from "@/views/MenuItemsView.vue";
import TablesView from "@/views/TablesView.vue";
import TimeSlotsView from "@/views/TimeSlotsView.vue";
import UsersView from "@/views/UsersView.vue";
import OrdersView from "@/views/OrdersView.vue";
import ReservationsView from "@/views/ReservationsView.vue";
import ResetPasswordView from "@/views/ResetPasswordView.vue";
import NotificationsView from "@/views/NotificationsView.vue";
import NotFoundView from "@/views/NotFoundView.vue";
import InfoView from "@/views/InfoView.vue";

async function checkAuth(roleCheck, next) {
  const authStore = UseAuthStore();
  await authStore.authenticate();
  if (roleCheck(authStore.user)) {
    next();
  } else {
    next({ name: 'login' });
  }
}

const adminGuard = (to, from, next) => checkAuth(user => user.role === 'admin', next);
const userGuard = (to, from, next) => {
  const authStore = UseAuthStore();
  if (authStore.user?.id) {
    next({ name: 'home' });
  } else {
    next();
  }
};

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
    },
    {
      path: '/users',
      name: 'users',
      component: UsersView,
      beforeEnter: adminGuard
    },
    {
      path: '/info',
      name: 'info',
      component: InfoView,
      beforeEnter: adminGuard
    },
    {
      path: '/orders',
      name: 'orders',
      component: OrdersView,
      beforeEnter: adminGuard
    },
    {
      path: '/reservations',
      name: 'reservations',
      component: ReservationsView,
      beforeEnter: adminGuard
    },
    {
      path: '/notifications',
      name: 'notifications',
      component: NotificationsView,
      beforeEnter: adminGuard
    },
    {
      path: '/reset-password/:token',
      name: 'reset-password',
      component: ResetPasswordView
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: NotFoundView
    },
  ]
})

export default router
