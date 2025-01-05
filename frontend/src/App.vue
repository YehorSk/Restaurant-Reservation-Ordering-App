<script setup>
import { RouterLink, RouterView } from 'vue-router'
import { useRoute } from "vue-router";

const route = useRoute();

</script>

<template>
  <RouterView />
</template>
