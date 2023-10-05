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
        >新增
        </el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="el-icon-delete"
            size="small"
            @click="ExAdd"
        >表格一键导入
        </el-button>
      </el-col>

      <!--      <el-col :span="1.5">
              <el-button
                  type="success"
                  plain
                  icon="el-icon-edit"
                  size="small"
                  :disabled="single"
                  @click="handleUpdate"
              >修改
              </el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                  type="danger"
                  plain
                  icon="el-icon-delete"
                  size="small"
                  :disabled="multiple"
                  @click="handleDelete"
              >删除
              </el-button>
            </el-col>-->

      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="el-icon-delete"
            size="small"
            @click="getList(1)"
        >刷新
        </el-button>
      </el-col>


    </el-row>

    <el-row
        v-if="!loading"
        :gutter="40">
      <el-col
          :xs="24" :sm="12" :lg="8" :xl="6"
          v-for="(item, index) in sshList"
          :key="item"
          :span="6"
      >
        <sshCard :sshData="item" @getList="getList"></sshCard>
      </el-col>
    </el-row>


<!--

    <div  v-for="(item, index) in sshList">
      <sshCard :sshData="item" @getList="getList" style="float: left"></sshCard>
    </div>-->

    <el-dialog title="表格一键导入" v-model="open2" width="600px" append-to-body>
      <exUpLoad></exUpLoad>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitExAdd">确 定</el-button>
        <el-button @click="cancelExAdd">取 消</el-button>
      </div>
    </el-dialog>

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
import {addSSh, listSSh} from "@/api/SSH_c";
import Terminal from "@/views/page/terminal/Terminal.vue";
import SshCard from "@/views/page/Main/SSh_config/sshCard.vue";
import ExUpLoad from "@/views/page/Main/SSh_config/ExUpLoad.vue";

export default {
  name: "Main",
  components: {ExUpLoad, SshCard, Terminal},
  data() {
    return {
      loading: true,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 服务器数据
      sshList: undefined,

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
      open2: false, // 是否显示弹出层

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

  created() {
    this.loading = true;
    this.getList(0)
  },

  methods: {
    /** 查询服务器列表 */
    getList(data) {
      this.loading = true;
      listSSh(data).then(response => {
            this.sshList = response.data
            this.loading = false;

          }
      );
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.ID = this.$store.state.user.id;
      this.open = true;
      this.title = "添加服务器";
    },
    /** 表格导入操作 */
    ExAdd() {
      this.open2 = true;
    },
    submitExAdd()  {
      this.open2 = false;
    },
    /** 表单取消按钮*/
    cancelExAdd() {
      this.open2 = false;

    },

    /** 提交表单按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // if (this.form.userId != undefined) {
          addSSh(this.form).then(response => {
            // this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList(1);
          });

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

</style>