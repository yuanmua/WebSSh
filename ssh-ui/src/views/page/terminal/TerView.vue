
<template>
  <div>
    <div class="sidebar">
      <MenuSidebar @send="send"></MenuSidebar>
    </div>
    <div class="terminal-main">
      <Terminal ref="Terminal"></Terminal>
    </div>
  </div>
</template>
<script>
import MenuSidebar from "./MenuSidebar.vue";
import Terminal from "./Terminal.vue";

export default {
  name: "TerView",
  components: {Terminal, MenuSidebar},
  methods: {
    send(data) {
      this.$refs.Terminal.TerminalSend(data);
    },
  }

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