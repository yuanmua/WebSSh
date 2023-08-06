<template>
  <div class="Sidebar">
    <el-upload
        class="upload-demo"
        drag
        action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
        multiple
    >
      <el-icon class="el-icon--upload">
        <upload-filled/>
      </el-icon>
      <div class="el-upload__text">
        Drop file here or <em>click to upload</em>
      </div>
      <template #tip>
        <div class="el-upload__tip">
          jpg/png files with a size less than 500kb
        </div>
      </template>
    </el-upload>

    <div>
      <h2>快捷键</h2>
      <el-table :data="tableData" style="width: 100%; height: 500px">
        <el-table-column label="信息" width="80">
          <template #default="scope">
            <el-popover effect="light" trigger="hover" placement="top" width="auto">
              <template #default>
                <div>指令名: {{ scope.row.name }}</div>
                <div>介绍: {{ scope.row.introduce }}</div>
                <div>时间: {{ scope.row.date }}</div>
              </template>
              <template #reference>
                <el-tag>{{ scope.row.name }}</el-tag>
              </template>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="命令" width="180">
          <template #default="scope">
            {{ scope.row.command }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">

          <el-button size="small" @click="handleEdit(scope.$index, scope.row)" style="width: 90%; margin: 10%">
            发送
          </el-button>
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">
            编辑
          </el-button>
          <el-button
              size="small"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)">
            删除
          </el-button>
          </template>

        </el-table-column>
      </el-table>
    </div>

  </div>
</template>
<script>
import {ref} from 'vue'
import {UploadFilled} from '@element-plus/icons-vue'
import {Timer} from '@element-plus/icons-vue'
import {delCommand, listCommand} from "@/api/ShortcutKeys";
import {listSSh} from "@/api/SSH_c";
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: "MenuSidebar",
  components: {
    UploadFilled,
    Timer,
    ref
  },
  data() {
    return {
      loading: true,
      isCollapse: true,
      tableData: [
        {
          id:'1',
          name: '我的',
          date: '2016-05-02',
          command: 'ls',
          introduce: 'No. 189, Grove St, Los Angeles',
        },
        {
          id:'2',
          name: '我的1',
          date: '2016-05-04',
          command: 'Tom',
          introduce: 'No. 189, Grove St, Los Angeles',
        },
      ]
    };
  },
  created() {
    this.loading = true;
    this.getListCommand()

  },

  methods: {
    getListCommand() {
      listCommand().then(response => {
            this.tableData = response.data
            this.loading = false;
          }
      );
    },
    handleAdd() {
      this.ID = this.$store.state.user.id;
      this.open = true;
      this.title = "添加服务器";
    },
    /** 修改按钮操作 */
    handleEdit(index, row) {
      console.log(index, row);

      // this.reset();
      // const userId = row.userId || this.ids;
      // getUser(userId).then(response => {
      //   this.form = response.data;
      //   this.postOptions = response.posts;
      //   this.roleOptions = response.roles;
      //   this.$set(this.form, "postIds", response.postIds);
      //   this.$set(this.form, "roleIds", response.roleIds);
      //   this.open = true;
      //   this.title = "修改用户";
      //   this.form.password = "";
      // });
    },
    /** 删除按钮操作 */
    handleDelete(index, row) {
      const userIds = row.id

      this.$confirm('此操作将永久删除"'+ row.name+'"的快捷键, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let mes=delCommand(userIds);
        this.getListCommand();
        this.$message({
          type: 'success',
          message: '删除成功!'
        });

      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });

    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    }
  }
}
</script>

<style scoped>
.Sidebar {
}

.upload-demo .el-icon--upload {
  width: 100%;
}

.el-icon--upload .el-upload__text {
  width: 100%;

}
</style>
