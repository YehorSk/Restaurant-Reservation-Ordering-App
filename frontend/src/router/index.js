import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from "@/views/LoginView.vue";
import {UseAuthStore} from "@/stores/AuthStore.js";

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
    }
  ]
})

export default router
