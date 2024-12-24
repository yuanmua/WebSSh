<template>
  <div>
    <el-card class="box-card">
      <template #header>

        <div :class="{ 'active-bar': sshData.status===1, 'inactive-bar': sshData.status===0 }" class="card-header">
          <span>{{ sshData.sshName }}</span>
          <router-link :to="`index/ssh/${sshData.id}`">

            进入服务器

          </router-link>


        </div>
      </template>

      <div class="info-item">
        <span class="info-label">ID:</span>
        <span>{{ sshData.id }}</span>
      </div>

      <div class="info-item">
        <span class="info-label">IP:</span>
        <span class="info-span">{{ sshData.sshHost }}</span>

        <span class="info-label">端口:</span>
        <span>{{ sshData.sshPort }}</span>

      </div>


      <div class="info-item">
        <span class="info-label">分类:</span>
        <span class="info-span">{{ sshData.sshClass }}</span>

        <span class="info-label">用户名:</span>
        <span>{{ sshData.sshUserName }}</span>

      </div>


      <div class="info-item">
        <span class="info-label">备注:</span>
        <div class="remark">
          <span>{{ sshData.remark }}</span>
        </div>
      </div>

      <el-button
          type="success"
          plain
          size="default"
          @click="handleUpdate"
          style="margin-right: 20%"
      >修改
      </el-button>
      <el-button
          type="success"
          plain
          size="default"
          @click="toSsh"
          style="margin-right: 20%"
      >进入
      </el-button>
      <el-button
          type="danger"
          plain
          size="default"
          @click="handleDelete"
      >删除
      </el-button>

    </el-card>


    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="服务器名称" prop="sshName">
              <el-input v-model="form.sshName" placeholder="服务器名称" maxlength="30"/>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="服务器IP" prop="sshHost">
              <el-input v-model="form.sshHost" placeholder="服务器IP" maxlength="30"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="服务器分类" prop="sshClass">
              <el-input v-model="form.sshClass" placeholder="服务器分类" maxlength="30"/>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="服务器端口" prop="sshPort">
              <el-input v-model="form.sshPort" placeholder="服务器端口" maxlength="30"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名" prop="sshUserName">
              <el-input v-model="form.sshUserName" placeholder="请输入服务器用户名称" maxlength="30"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务器密码" prop="sshPassword">
              <el-input v-model="form.sshPassword" placeholder="请输入用户密码" type="password" maxlength="20"
                        show-password/>
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
import { delSSh, updateSSh} from "@/api/SSH_c";
import {ElMessage, ElMessageBox} from "element-plus";

export default {
  name: "sshCard",
  props: ["sshData"],
  data() {
    return {
      isActive: false,

      // 弹出层标题
      title: "",
      // 表单参数
      form: {
        ID: this.$store.state.user.id,
        sshName: '',
        sshHost: '',
        sshClass: '',
        sshPort: '',
        sshUserName: '',
        sshPassword: '',
        remark: undefined,
      },
      // 是否显示弹出层
      open: false, // 是否显示弹出层
      // 表单校验
      rules: {
        sshName: [
          {required: true, message: "服务器名称不能为空", trigger: "blur"},
          {min: 2, max: 20, message: '服务器名称长度必须在 2 和 20 之间', trigger: 'blur'}
        ],
        sshHost: [
          {required: true, message: "服务器IP不能为空", trigger: "blur"},
          {
            pattern: /^(?!0)(?!.*\.$)((25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)\.){3}(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)$/,
            message: "请输入正确的IP地址",
            trigger: "blur"
          }
        ],
        sshPort: [
          {required: true, message: "端口不能为空", trigger: "blur"},
          {
            pattern: /^(\d{1,5})$/,
            message: "请输入正确的端口号",
            trigger: ["blur", "change"]
          }
        ],
        sshUserName: {required: true, message: "服务器IP不能为空", trigger: "blur"},
        sshPassword: {required: true, message: "服务器IP不能为空", trigger: "blur"},


      }
    }
  },
  methods: {
    toSsh(){
      this.$router.push('index/ssh/'+this.sshData.id)

    },
    /** 修改按钮操作 */
    handleUpdate() {
      this.reset();
      this.form = this.sshData;
      this.open = true;
      this.title = "修改用服务器";
      // this.form.password = "";

    },

    /** 删除按钮操作 */
    handleDelete() {
      ElMessageBox.confirm('此操作将永久删除"' + this.sshData.sshName + '"的服务器, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delSSh(this.sshData.id).then(res => {
          this.$emit('getList', 0);
        });
        // this.getList(0);
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

    /** 提交表单按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateSSh(this.form).then(response => {
            // this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.$emit('getList', 1);

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
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        ID: undefined,
        sshName: '',
        sshHost: '',
        sshClass: '',
        sshPort: '',
        sshUserName: '',
        sshPassword: '',
        remark: undefined,
      };
      this.title = "form";
    },

  }

}
</script>

<style scoped>
.remark {
  height: 70px;
  overflow: auto;
}

.active-bar {
  background-color: #66bb6a; /* Green color for active server */
}

.inactive-bar {
  background-color: #f0f0f0; /* Gray color for inactive server */
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
  font-weight: bold;
}

.button {
  padding: 0;
}

.info-item {
  margin: 10px;
  display: flex;
  align-items: flex-start;
}

.info-label {
  min-width: 15%;
  font-weight: bold;

}

.info-span {
  min-width: 35%;

}

.text {
  margin: 10px;
}
</style>