<template>
    <div>
      <div>
        <logo :collapse="isCollapse" />

        <el-scrollbar  wrap-class="scrollbar-wrapper">
          <el-menu
              :default-active="activeMenu"
              :collapse="isCollapse"
              text-color="#bfcbd9"
              :unique-opened="true"
              :collapse-transition="false"
              class="el-menu-vertical-demo">

            <router-link to="/index">
            <el-menu-item index="/index">
                  <el-icon><i class="el-icon"><svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path fill="currentColor" d="M800 416a288 288 0 1 0-576 0c0 118.144 94.528 272.128 288 456.576C705.472 688.128 800 534.144 800 416zM512 960C277.312 746.688 160 565.312 160 416a352 352 0 0 1 704 0c0 149.312-117.312 330.688-352 544z"></path><path fill="currentColor" d="M512 512a96 96 0 1 0 0-192 96 96 0 0 0 0 192zm0 64a160 160 0 1 1 0-320 160 160 0 0 1 0 320z"></path></svg></i></el-icon>
                <span>主页</span>
            </el-menu-item>
            </router-link>

          </el-menu>
        </el-scrollbar>
      </div>
    </div>
</template>

<script>
import Logo from "./Logo.vue";
import { mapGetters, mapState } from "vuex";

export default {
  components: { Logo },
  name: "Sidebar",
  data() {
    return {
      isCollapse: true
    };
  },
  methods: {

  },
  computed: {
    ...mapGetters(["sidebar"]),
    activeMenu() {
      const route = this.$route;
      const { meta, path } = route;
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      return path;
    },
    variables() {
      return variables;
    },
    isCollapse() {
      return !this.sidebar.opened;
    }
  }
}

</script>
<style>
.el-menu-vertical-demo{
  background: rgb(52, 55, 68);
  color: #bfcbd9;
}
</style>