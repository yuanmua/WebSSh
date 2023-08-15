<template>

  <el-menu
      default-active="/index"
      class="el-menu-vertical-demo"
      :collapse="isCollapse"
      :unique-opened="true"
      text-color="#bfcbd9"
      active-text-color="#fd7065"
      :router="true"
  >
    <el-menu-item index="/" style="padding: 0; height: 100px">

      <logo :collapse="isCollapse" />

    </el-menu-item>

    <el-menu-item index="/index" class="menu_index">
      <el-icon><i class="el-icon"><svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path fill="currentColor" d="M800 416a288 288 0 1 0-576 0c0 118.144 94.528 272.128 288 456.576C705.472 688.128 800 534.144 800 416zM512 960C277.312 746.688 160 565.312 160 416a352 352 0 0 1 704 0c0 149.312-117.312 330.688-352 544z"></path><path fill="currentColor" d="M512 512a96 96 0 1 0 0-192 96 96 0 0 0 0 192zm0 64a160 160 0 1 1 0-320 160 160 0 0 1 0 320z"></path></svg></i></el-icon>
      <template #title>
        <span >主页</span>
      </template>
    </el-menu-item>


  </el-menu>
</template>


<script>
import Logo from "./Logo.vue";
import { mapGetters } from "vuex";

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
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      return path;
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
.menu_index:hover{
  background-color: #707070;
}
</style>