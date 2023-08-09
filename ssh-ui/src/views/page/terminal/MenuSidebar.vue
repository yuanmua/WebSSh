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

    <div class="Sidebar-key">
      <h2>快捷键
        <el-button size="default" type="primary" @click="shortcutAdd()" style="float:right">
          新增
        </el-button>
      </h2>
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
        <el-table-column label="命令" width="auto">
          <template #default="scope">
            {{ scope.row.command }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">

            <el-button size="small" type="success" @click="handleEdit(scope.$index, scope.row)"
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
            <el-form-item label="指令名称" prop="shortcutName">
              <el-input v-model="form.shortcutName" placeholder="指令名称" maxlength="30"/>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="备注">
              <el-input v-model="form.introduce" type="textarea" placeholder="请输入介绍"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="指令内容" prop="shortcutContent">
              <el-input v-model="form.shortcutContent" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {ref} from 'vue'
import {UploadFilled} from '@element-plus/icons-vue'
import {Timer} from '@element-plus/icons-vue'
import {addCommand, delCommand, listCommand, updateCommand} from "@/api/ShortcutKeys";
import {listSSh} from "@/api/SSH_c";
import {ElMessage, ElMessageBox} from 'element-plus'

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
          id: '1',
          name: '我的',
          date: '2016-05-02',
          command: 'ls',
          introduce: 'No. 189, Grove St, Los Angeles',
        },
        {
          id: '2',
          name: '我的1',
          date: '2016-05-04',
          command: 'cd',
          introduce: 'No. 189, Grove St, Los Angeles',
        },
      ],


      isActive: false,
      // 弹出层标题
      title: "",
      // 表单参数
      form: {
        shortcutId: undefined,
        shortcutName: undefined,
        introduce: undefined,
        shortcutContent: undefined,
      },
      // 是否显示弹出层
      open: false, // 是否显示弹出层
      // 表单校验
      rules: {
        shortcutName: [
          {required: true, message: "关键字名称不能为空", trigger: "blur"},
          {min: 2, max: 20, message: '关键字名称长度必须在 2 和 20 之间', trigger: 'blur'}
        ],
        shortcutContent: {required: true, message: "内容不能为空", trigger: "blur"},
      }

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
          if (this.form.shortcutId !== undefined) {
            updateCommand(this.form).then(response => {
              ElMessage({
                message: '修改成功',
                type: 'success',
              })
              this.open = false;
              this.getList();
            });
          } else {
            addCommand(this.form).then(response => {
              ElMessage({
                message: '新增成功',
                type: 'success',
              })
              this.open = false;
              this.getList();
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
      this.form.shortcutId = row.id;
      this.form.shortcutName = row.name;
      this.form.introduce = row.introduce;
      this.form.shortcutContent = row.command;
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
      const userIds = row.id
      ElMessageBox.confirm('此操作将永久删除"' + row.name + '"的快捷键, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let mes = delCommand(userIds);
        this.getListCommand();
        ElMessage({
          type: 'success',
          message: '删除成功!'
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
        shortcutId: undefined,
        shortcutName: undefined,
        introduce: undefined,
        shortcutContent: undefined,
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

.upload-demo .el-icon--upload {
  width: 100%;
}

.el-icon--upload .el-upload__text {
  width: 100%;

}
</style>
