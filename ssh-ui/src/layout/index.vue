<template>

  <div :class="classObj" class="app-wrapper">


    <sidebar class="sidebar-container"/>
    <div class="main-container">
      <div>
        <navbar/>
      </div>
      <app-main/>

    </div>
  </div>
</template>

<script>
import {AppMain, Navbar, Sidebar} from './components'
import {mapState} from "vuex";

export default {
  name: 'Layout',
  components: {
    AppMain,
    Navbar,
    Sidebar,
  },
  computed: {
    ...mapState({
      sidebar: state => state.app.sidebar,

    }),
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
      }
    },
  }
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
  width: 200px !important;
  background-color: rgb(52, 55, 68);
  height: 100%;
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 1001;
  overflow: hidden;
  -webkit-box-shadow: 2px 0 6px rgba(0, 21, 41, .35);
  box-shadow: 2px 0 6px rgba(0, 21, 41, .35);
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

.hideSidebar {
  .sidebar-container {
    width: 54px !important;
  }

  .main-container {
    margin-left: 54px;
  }

  .submenu-title-noDropdown {
    padding: 0 !important;
    position: relative;

    .el-tooltip {
      padding: 0 !important;

      .svg-icon {
        margin-left: 20px;
      }
    }
  }

  .el-submenu {
    overflow: hidden;

    & > .el-submenu__title {
      padding: 0 !important;

      .svg-icon {
        margin-left: 20px;
      }

    }
  }

  .el-menu--collapse {
    .el-submenu {
      & > .el-submenu__title {
        & > span {
          height: 0;
          width: 0;
          overflow: hidden;
          visibility: hidden;
          display: inline-block;
        }
      }
    }
  }
}

</style>
