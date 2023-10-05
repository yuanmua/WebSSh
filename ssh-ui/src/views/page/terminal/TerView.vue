
<template>
  <div>
    <div class="sidebar">
      <MenuSidebar @send="send"></MenuSidebar>
    </div>
    <div class="terminal-main">
      <Terminal ref="Terminal"></Terminal>
    </div>
  </div>
  <div>
<!--    <fileGuan></fileGuan>-->
  </div>
</template>
<script>
import MenuSidebar from "./MenuSidebar.vue";
import Terminal from "./Terminal.vue";
// import FileGuan from "@/views/page/terminal/tool/fileGuan.vue";

export default {
  name: "TerView",
  components: { Terminal, MenuSidebar},
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
  height: 60%;
}
.terminal-main{

  float: left;
  width: 60%;
  height: 60px;

}
</style>