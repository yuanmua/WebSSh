<script setup>
import Terminal from "./views/page/terminal/Terminal.vue";
import FormView from "@/views/Formview.vue";
import MenuSidebar from "@/views/element/MenuSidebar.vue";
import LoginVive from "./views/page/login/login.vue";
</script>

<template>
<!-- <div class="sidebar">-->
<!--  <MenuSidebar></MenuSidebar>-->
<!-- </div>-->
<!-- <div class="terminal-main">-->
<!--  <Terminal></Terminal>-->
<!-- </div>-->
  <router-view></router-view>
<!--
<loginVive></loginVive>
-->
</template>

<style scoped>

</style>
