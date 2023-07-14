<script setup>
import Terminal from "./views/Terminal.vue";
import FormView from "@/views/Formview.vue";
import ElFormAbc from "@/views/ElFormabc.vue";
import MenuSidebar from "@/views/element/MenuSidebar.vue";
</script>

<template><!--
 <ElFormAbc></ElFormAbc>
 <FormView></FormView>-->
 <div class="sidebar">
  <MenuSidebar></MenuSidebar>
 </div>
 <div class="terminal-main">
  <Terminal></Terminal>
 </div>
</template>

<style scoped>
.sidebar{
 float: left;
 width: 20%;
}
.terminal-main{

 float: left;
 width: 60%;
}
</style>
