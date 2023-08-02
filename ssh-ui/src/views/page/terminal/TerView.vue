
<template>
  <div>
    <div class="sidebar">
      <MenuSidebar></MenuSidebar>
    </div>
    <div class="terminal-main">
      <Terminal></Terminal>
    </div>
  </div>
</template>
<script>
import MenuSidebar from "../../element/MenuSidebar.vue";
import Terminal from "./Terminal.vue";

export default {
  name: "Main.view",
  components: {Terminal, MenuSidebar},
}
</script>

<style scoped>
.sidebar{
  float: left;
  width: 30%;
}
.terminal-main{

  float: left;
  width: 60%;
}
</style>