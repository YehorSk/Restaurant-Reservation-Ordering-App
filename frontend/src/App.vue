<script setup>
import { RouterLink, RouterView } from 'vue-router'
import NavComponent from "@/components/SideBarComponent.vue";
import { useRoute } from "vue-router";

const route = useRoute();

</script>

<template>
  <NavComponent v-if="route.name !== 'login'"/>
  <RouterView />
</template>
