<template>
  <div class="sidebar-logo-container">
    <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/index">
      <img v-if="logo" :src="logo" class="sidebar-logo-c" alt=""/>
    </router-link>

    <h1 class="font_kun">鲲</h1>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/index">
      <img v-if="logo" :src="logo" class="sidebar-logo"  alt=""/>
    </router-link>

    <div class="sidebar-title">
        <h1 class="font_kunKun">鲲鲲ssh</h1>
      </div>
  </div>

</template>

<script>
import logoImg from './logo.png'

export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      logo: logoImg
    }
  }
}
</script>

<style>
.sidebar-logo-container {
  position: relative;
  width: 100%;
  background: #2b2f3a;
  text-align: center;


  height: 100px;

  overflow: hidden;

}
.font_kun{
  color: #fff;

}
.font_kunKun{
  overflow: hidden;
  height: 50px;

}

.sidebar-logo-link {
  height: 100%;
  width: 100%;
}

.sidebar-logo {
  height: 80px;
  width: 40%;
  float: left;
  margin-top: 10px;

}

.sidebar-title {

  padding-top: 25px;

  height: 100px;
  width: 50%;
  display: inline-block;
  margin: 0;
  color: #fff;
  font-weight: 600;
  line-height: 50px;
  font-size: 14px;
  vertical-align: middle;
}

.sidebar-logo-c {
  width: 100%;
}
</style>
