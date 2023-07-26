<template>
<!--  <sidebar  class="sidebar-container"/>
  <navbar/>

  <app-main/>-->

  <div  class="app-wrapper" >
<!--    <div  class="drawer-bg" />-->
    <sidebar  class="sidebar-container"/>
    <div  class="main-container">
      <div >
        <navbar/>
      </div>
      <app-main/>

    </div>
  </div>
</template>

<script>
import { AppMain , Navbar ,Sidebar} from './components'
export default {
  name: 'Layout',
  components: {
    AppMain,
    Navbar,
    Sidebar,

  },
}

</script>

<style>
.main-container {
  height: 100%;
  -webkit-transition: margin-left .28s;
  transition: margin-left .28s;
  margin-left: 200px;
  position: relative;
}
.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
}
.sidebar-container {
  -webkit-transition: width .28s;
  transition: width .28s;
  width: 200px!important;
  background-color: #c19465;
  height: 100%;
  position: fixed;
  font-size: 0;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 1001;
  overflow: hidden;
  -webkit-box-shadow: 2px 0 6px rgba(0,21,41,.35);
  box-shadow: 2px 0 6px rgba(0,21,41,.35);
}

  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;

    &.mobile.openSidebar {
      position: fixed;
      top: 0;
    }
  }

  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }

  .fixed-header {
    position: fixed;
    top: 0;
    right: 0;
    z-index: 9;
    width: calc(100% - #{$base-sidebar-width});
    transition: width 0.28s;
  }

  .hideSidebar .fixed-header {
    width: calc(100% - 54px);
  }

  .sidebarHide .fixed-header {
    width: 100%;
  }

  .mobile .fixed-header {
    width: 100%;
  }
</style>
