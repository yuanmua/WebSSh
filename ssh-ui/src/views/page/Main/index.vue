<template>
  <div>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="small"
            @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="small"
            :disabled="single"
            @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="small"
            :disabled="multiple"
            @click="handleDelete"
        >删除</el-button>
      </el-col>
    </el-row>
    <el-row>
      <el-col
          v-for="(item, index) in userList"
          :key="item"
          :span="8"
          :offset="index > 0 ? 2 : 0"
      >
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>{{ item.sshName }}</span>
              <router-link :to="`index/ssh/${item.sshId}`">

                进入服务器

              </router-link>


            </div>
          </template>
          <div class="info-item">
            <span class="info-label">ID:</span>
            <span>{{ item.ID }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">Host:</span>
            <span>{{ item.sshHost }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">Class:</span>
            <span>{{ item.sshClass }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">Port:</span>
            <span>{{ item.sshPort }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">Username:</span>
            <span>{{ item.sshUserName }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">Password:</span>
            <span>{{ item.sshPassword }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">Remark:</span>
            <span>{{ item.remark }}</span>
          </div>
          <div class="status-bar" :class="{ 'active-bar': isActive, 'inactive-bar': !isActive }"></div>
        </el-card>
      </el-col>
    </el-row>


    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title"     v-model="open"  width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="服务器名称" prop="sshName">
              <el-input v-model="form.sshName" placeholder="服务器名称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="服务器IP" prop="sshHost">
              <el-input v-model="form.sshHost" placeholder="服务器IP" maxlength="30" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="服务器分类" prop="sshClass">
              <el-input v-model="form.sshClass" placeholder="服务器分类" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="服务器端口" prop="sshPort">
              <el-input v-model="form.sshPort" placeholder="服务器端口" maxlength="30" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="服务器名" prop="sshUserName">
              <el-input v-model="form.sshUserName" placeholder="请输入服务器用户名称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务器密码" prop="sshPassword">
              <el-input v-model="form.sshPassword" placeholder="请输入用户密码" type="password" maxlength="20" show-password/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
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
import {addSSh, listSSh, updateUser} from "@/api/SSH_c";
import Terminal from "@/views/page/terminal/Terminal.vue";

export default {
  name: "Main",
  components: {Terminal},
  data() {
    return {
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 弹出层标题
      title: "",
      // 服务器数据
      userList:  [
        {
          "sshId":"1",
          "sshName": "我的",
          "sshHost": "192.168.1.1",
          "sshClass": "123",
          "sshPort": "1234",
          "sshUserName": "yuanmua",
          "sshPassword": "123123",
          "remark": "没有"
        },
        {
          "sshId":"2",
          "sshName": "我的2",
          "sshHost": "192.168.10.1",
          "sshClass": "1231",
          "sshPort": "1234",
          "sshUserName": "yuanmu",
          "sshPassword": "123123zwy",
          "remark": "没有"
        }
      ],
      // 表单参数
      form: {
        ID: this.$store.state.user.id,
        sshName:'',
        sshHost:'',
        sshClass:'',
        sshPort:'',
        sshUserName:'',
        sshPassword:'',
        remark: undefined,
      },
      // 是否显示弹出层
      open: false, // 是否显示弹出层
      // 表单校验
      rules: {
        sshName: [
          { required: true, message: "服务器称不能为空", trigger: "blur" },
          { min: 2, max: 20, message: '服务器称长度必须在 2 和 20 之间', trigger: 'blur' }
        ],
        sshHost: [
          { required: true, message: "服务器IP不能为空", trigger: "blur" },
          {
            pattern: /^(?!0)(?!.*\.$)((25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)\.){3}(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)$/,
            message: "请输入正确的IP地址",
            trigger: "blur"
          }
        ],
        sshPort: [
          { required: true, message: "端口不能为空", trigger: "blur" },
          {
            pattern: /^(\d{1,5})$/,
            message: "请输入正确的端口号",
            trigger: ["blur", "change"]
          }
        ],
        sshUserName:{ required: true, message: "服务器IP不能为空", trigger: "blur" },
        sshPassword:{ required: true, message: "服务器IP不能为空", trigger: "blur" },


      }
    }
  },
  methods: {
    /** 查询服务器列表 */
    getList() {
      listSSh(this.$store.state.user.id).then(response => {
            this.userList = response.rows;
          }
      );
    },


    /** 新增按钮操作 */
    handleAdd() {
      this.ID = this.$store.state.user.id;
      this.open = true;
      this.title = "添加服务器";
    },
    /** 修改按钮操作 */
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
    handleDelete(row) {
      const userIds = row.userId || this.ids;
      this.$modal.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？').then(function() {
        return delUser(userIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 提交表单按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // if (this.form.userId != undefined) {
          addSSh(this.form).then(response => {
              // this.$modal.msgSuccess("修改成功");
              this.open = false;
              // this.getList(); 记得以后加上
            });
          /*   } else {
           addUser(this.form).then(response => {
             this.$modal.msgSuccess("新增成功");
             this.open = false;
             this.getList();
           });
          }*/
        }
      });
    },
    /** 表单取消按钮*/
    cancel() {
      this.open = false;
      // this.reset();
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