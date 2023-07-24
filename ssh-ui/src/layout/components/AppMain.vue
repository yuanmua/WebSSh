<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
        <router-view />
    </transition>z
  </section>
</template>

<script>
export default {
  name: 'AppMain',

}
</script>

<style>
.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}

.fixed-header + .app-main {
  padding-top: 50px;
}


  .fixed-header + .app-main {
    padding-top: 84px;
  }
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 6px;
  }
}

::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background-color: #c0c0c0;
  border-radius: 3px;
}
</style>
