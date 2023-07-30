<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <div class="right-menu">
<!--      <template v-if="device!=='mobile'">-->
<!--&lt;!&ndash;        <search id="header-search" class="right-menu-item" />&ndash;&gt;-->

<!--&lt;!&ndash;        <el-tooltip content="源码地址" effect="dark" placement="bottom">&ndash;&gt;-->
<!--&lt;!&ndash;          <ruo-yi-git id="" class="right-menu-item hover-effect" />&ndash;&gt;-->
<!--&lt;!&ndash;        </el-tooltip>&ndash;&gt;-->

<!--&lt;!&ndash;        <screenfull id="screenfull" class="right-menu-item hover-effect" />&ndash;&gt;-->


<!--      </template>-->

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img src="/public/images/用户.png" class="user-avatar" alt="">
          <i class="el-icon-caret-bottom" />
        </div>
        <template #dropdown>
        <el-dropdown-menu slot="dropdown">
<!--          <router-link to="/user/profile">-->
<!--            <el-dropdown-item>个人中心</el-dropdown-item>-->
<!--          </router-link>-->
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

  </div>
</template>

<script>
// import Breadcrumb from '@/components/Breadcrumb/index.vue'
import Hamburger from '@/components/Hamburger/index.vue'
import {mapGetters} from "vuex";
// import RuoYiGit from '@/components/RuoYi/Git/index.vue'

export default {
  components: {
    Hamburger,
    // RuoYiGit,
  },
  computed: {
    ...mapGetters([
      'sidebar',
    ]),
  },
  methods: {
    //这个是右边的开关
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          window.location.href = '/'
        })
      }).catch(() => {});
    }

  }
}
</script>

<style >

.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
