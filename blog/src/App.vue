<script setup>
import Navigation from './components/Navigation.vue';
import Buttom from './components/Universal/Bottom.vue'
</script>

<template>
  <div class="Appbox">
    <Navigation class="Nav" />
    <router-view />
    <Buttom />
  </div>
</template>

<style scoped>
.Appbox{
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
