<template>
  <div class="Sidebar">

        <el-button size="default" type="primary" @click="shortcutAdd()" style="float:right">
          新增
        </el-button>


      <el-table :data="tableData" style="width: 100%; height: 400px">
        <el-table-column label="信息" width="80">
          <template #default="scope">
            <el-popover trigger="hover" placement="top" width="auto">
              <template #default>
                <div>指令名: {{ scope.row.name }}</div>
                <div>介绍: {{ scope.row.remarks }}</div>
                <div>时间: {{ scope.row.createTime }}</div>
              </template>
              <template #reference>
                <el-tag>{{ scope.row.name }}</el-tag>
              </template>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="命令" width="auto">
          <template #default="scope">
            {{ scope.row.cmds }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">

            <el-button size="small" type="success" @click="send(scope.row.cmds)"
                       style="width: 90%; margin: 10%">
              发送
            </el-button>
            <el-button size="small" type="warning" @click="handleEdit(scope.$index, scope.row)">
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


    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="指令名称" prop="name">
              <el-input v-model="form.name" placeholder="指令名称" maxlength="30"/>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="备注">
              <el-input v-model="form.remarks" type="textarea" placeholder="请输入介绍"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="指令内容" prop="cmds">
              <el-input v-model="form.cmds" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

</template>
<script>
import {ref} from 'vue'
import {Timer} from '@element-plus/icons-vue'
import {addCommand, delCommand, listCommand, updateCommand} from "@/api/ShortcutKeys";
import {ElMessage, ElMessageBox} from 'element-plus'
import UpLoad from "@/views/page/terminal/upLoad.vue";

export default {
  name: "custom",
  components: {
    UpLoad,
    Timer,
    ref
  },
  data() {
    return {
      loading: true,
      isCollapse: true,
      tableData: [],

      isActive: false,
      // 弹出层标题
      title: "",
      // 表单参数
      form: {
        id: undefined,
        userId: this.$store.state.user.id,
        serverId: undefined,
        name: undefined,
        cmds: undefined,
        remarks: undefined,
      },
      // 是否显示弹出层
      open: false, // 是否显示弹出层
      // 表单校验
      rules: {
        name: [
          {required: true, message: "关键字名称不能为空", trigger: "blur"},
          {min: 2, max: 20, message: '关键字名称长度必须在 2 和 20 之间', trigger: 'blur'}
        ],
        cmds: {required: true, message: "内容不能为空", trigger: "blur"},
      }

    };
  },
  created() {
    this.loading = true;
    this.getListCommand()

  },

  methods: {
    send(command) {
      this.$emit('send2', command);

    },
    getListCommand() {
      listCommand(this.$route.params.id).then(response => {
            this.tableData = response.data
            this.loading = false;
          }
      );
    },
    /** 新增按钮操作 */
    shortcutAdd() {
      this.reset();
      this.open = true;
      this.title = "添加指令";
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            this.form.serverId = this.$route.params.id;
            updateCommand(this.form).then(response => {
              ElMessage({
                message: '修改成功',
                type: 'success',
              })
              this.open = false;
              this.getListCommand();
            });
          } else {
            this.form.serverId = this.$route.params.id;
            addCommand(this.form).then(response => {
              ElMessage({
                message: '新增成功',
                type: 'success',
              })
              this.open = false;
              this.getListCommand();
            });
          }
        }
      });
    },
    /** 表单取消按钮*/
    cancel() {
      this.open = false;
      this.reset();
    },

    /** 修改按钮操作 */
    handleEdit(index, row) {
      this.reset();
      this.form.id = row.id;
      this.form.name = row.name;
      this.form.remarks = row.remarks;
      this.form.cmds = row.cmds;
      this.title = "修改快捷键";
      this.open = true;
      console.log(row);
      console.log(index, row);
      console.log(this.form);

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
      const id = row.id
      ElMessageBox.confirm('此操作将永久删除"' + row.name + '"的快捷键, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delCommand(id).then(res => {
          this.getListCommand();
          ElMessage({
            type: 'success',
            message: '删除成功!'
          });
        }).catch(res => {
          ElMessage({
            type: 'error',
            message: '删除失败!'
          });
        });

      }).catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消删除'
        });
      });

    },

    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        userId: this.$store.state.user.id,
        serverId: undefined,
        name: undefined,
        cmds: undefined,
        remarks: undefined,
      };
      this.title = "form";
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
