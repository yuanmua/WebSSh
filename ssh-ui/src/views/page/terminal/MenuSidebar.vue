<template>
  <div class="Sidebar">
    <!--    <upLoad/>-->
    <div class="Sidebar-key">
      <h2>快捷键
      </h2>
      <el-tabs type="border-card">
        <el-tab-pane label="常用快捷键">
          <comks></comks>
        </el-tab-pane>
        <el-tab-pane label="自定义快捷键">
          <custom></custom>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script>
import {ref} from 'vue'
import {Timer} from '@element-plus/icons-vue'
import {addCommand, delCommand, listCommand, updateCommand} from "@/api/ShortcutKeys";
import {ElMessage, ElMessageBox} from 'element-plus'
import UpLoad from "@/views/page/terminal/upLoad.vue";
import Custom from "@/views/page/terminal/custom.vue";
import Comks from "@/views/page/terminal/comks.vue";

export default {
  name: "MenuSidebar",
  components: {
    Comks,
    Custom,
    UpLoad,
    Timer,
    ref
  },
  data() {
    return {}
  }
}
</script>

<style scoped>
.Sidebar-key {
  width: 100%;

}

.Sidebar {
}

.upload-demo {
  width: 100%;
}

.el-icon--upload {
  width: 100%;

}
</style>
