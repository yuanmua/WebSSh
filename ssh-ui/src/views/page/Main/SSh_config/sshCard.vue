<template>
  <div>
  <el-card class="box-card">
    <template #header>

      <div class="card-header">
        <span>{{ sshData.sshName }}</span>
        <router-link :to="`index/ssh/${sshData.sshId}`">

          进入服务器

        </router-link>


      </div>
    </template>
    <div class="info-item">
      <span class="info-label">ID:</span>
      <span>{{ sshData.ID }}</span>
    </div>
    <div class="info-item">
      <span class="info-label">Host:</span>
      <span>{{ sshData.sshHost }}</span>
    </div>
    <div class="info-item">
      <span class="info-label">Class:</span>
      <span>{{ sshData.sshClass }}</span>
    </div>
    <div class="info-item">
      <span class="info-label">Port:</span>
      <span>{{ sshData.sshPort }}</span>
    </div>
    <div class="info-item">
      <span class="info-label">Username:</span>
      <span>{{ sshData.sshUserName }}</span>
    </div>
    <div class="info-item">
      <span class="info-label">Password:</span>
      <span>{{ sshData.sshPassword }}</span>
    </div>
    <div class="info-item">
      <span class="info-label">Remark:</span>
      <span>{{ sshData.remark }}</span>
    </div>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="small"
            @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="small"
            @click="handleDelete"
        >删除</el-button>
      </el-col>
    </el-row>

    <div class="status-bar" :class="{ 'active-bar': isActive, 'inactive-bar': !isActive }"></div>
  </el-card>
  </div>
</template>

<script>
import {delSSh} from "@/api/SSH_c";

export default {
  name: "sshCard",
  props:["sshData"],
  methods: {
    handleUpdate(row) {
      const userId = row.userId || this.ids;
      getUser(userId).then(response => {
        this.form = response.data;
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.$set(this.form, "postIds", response.postIds);
        this.$set(this.form, "roleIds", response.roleIds);
        this.open = true;
        this.title = "修改用户";
        this.form.password = "";
      });
    },

    /** 删除按钮操作 */
    handleDelete() {
      delSSh(this.sshData.id)
    },

  }

}
</script>

<style scoped>
.status-bar {
  height: 10px;
  background-color: #f0f0f0;
}

.active-bar {
  background-color: #66bb6a; /* Green color for active server */
}

.inactive-bar {
  background-color: #bdbdbd; /* Gray color for inactive server */
}
.box-card {
  width: 400px;
  margin: 20px auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #f0f0f0;
  font-weight: bold;
}

.button {
  padding: 0;
}

.info-item {
  margin: 10px;
  display: flex;
  align-items: center;
}

.info-label {
  min-width: 100px;
  font-weight: bold;
}

.text {
  margin: 10px;
}
</style>