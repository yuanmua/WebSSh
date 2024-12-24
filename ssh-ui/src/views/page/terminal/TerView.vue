<template>
  <div>
    <div class="sidebar">
      <MenuSidebar @send="send"></MenuSidebar>
    </div>
    <div class="terminal-main">
      <Terminal ref="Terminal"></Terminal>
    </div>

  </div>
  <div class="Echarts">
    <Echarts @send="send"></Echarts>
  </div>

  <div>
    <!--    <fileGuan></fileGuan>-->
  </div>
</template>
<script>
import MenuSidebar from "./MenuSidebar.vue";
import Terminal from "./Terminal.vue";
import Echarts from "@/views/page/terminal/tool/Echarts.vue";
// import FileGuan from "@/views/page/terminal/tool/fileGuan.vue";

export default {
  name: "TerView",
  components: {Echarts, Terminal, MenuSidebar},
  methods: {
    send(data) {
      this.$refs.Terminal.TerminalSend(data);
    },
  }

}
</script>

<style scoped>
.sidebar {
  float: left;
  width: 30%;
  height: 60%;
}

.terminal-main {

  float: left;
  width: 50%;
  height: 60px;

}

.Echarts {

  float: right;
  width: 20%;
}
</style>