<template>
  <div class="login" id="login-app">
    <div class="login-box">
      <img src="../../../../public/images/login/login-l.png" alt="">
      <div class="login-form">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" >
          <div class="login-form-title">
            <img src="../../../assets/logo_b.png" style="width:150px;height:150px;" alt="" />
          </div>
          <el-tabs type="border-card">
            <el-tab-pane label="登录">
              <el-form-item prop="username">
                <el-input v-model="loginForm.username" type="text" placeholder="账号"/>
              </el-form-item>
              <el-form-item prop="password">
                <el-input v-model="loginForm.password" type="password" placeholder="密码"/>
              </el-form-item>
              <el-form-item style="width:100%;margin-bottom: 10px;">
                <el-button :loading="loading" class="login-btn" size="medium" type="primary" style="width:100%;"
                           @click.native.prevent="handleLogin">
                  <span v-if="!loading">登录</span>
                  <span v-else>登录中...</span>
                </el-button>
              </el-form-item>

            </el-tab-pane>

            <el-tab-pane label="手机号登录">
              <el-form-item prop="phoneNumber">
                <el-input v-model="loginForm2.phone" type="text" placeholder="手机号"/>
              </el-form-item>
              <el-form-item prop="code">
                <el-input v-model="loginForm2.code" type="password" style="width: 63%" placeholder="验证码"/>
                <el-button type="primary" class="login-code" @click=getCode() >发送</el-button>
              </el-form-item>
              <el-form-item style="width:100%;margin-bottom: 10px;">
                <el-button :loading="loading" class="login-btn" size="medium" type="primary" style="width:100%;"
                           @click.native.prevent="handleLogin2">
                  <span v-if="!loading">登录</span>
                  <span v-else>登录中...</span>
                </el-button>
              </el-form-item>

            </el-tab-pane>
          </el-tabs>


          <el-form-item style="width:90%; margin-left: 5%">
            <el-button  class="login-btn" size="default" type="primary" style="width:100%;"
                        onclick='window.open("/#/register")'>
              <span>注册</span>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import Cookies from "js-cookie";
import Custom from "@/views/page/terminal/custom.vue";
import Comks from "@/views/page/terminal/comks.vue";
import {getCode, login, login2} from "@/api/login";
import {setToken} from "@/js/auth";

export default {
  name: "loginVive",
  components: {Comks, Custom},
  data() {
    return {
      loginForm:{
        username: '',
        password: '',
        rememberMe: false,
      },
      loginForm2:{
        code: "",
        phone:null,
        rememberMe: false,
      },
      loading: false
    }
  },
  computed: {
    loginRules() {
      const validateUsername = (rule, value, callback) => {
        if (value.length < 1 ) {
          callback(new Error('请输入用户名'))
        } else {
          callback()
        }
      }
      const validatePassword = (rule, value, callback) => {
        if (value.length < 6) {
          callback(new Error('密码必须在6位以上'))
        } else {
          callback()
        }
      }
      return {
        'username': [{ 'validator': validateUsername, 'trigger': 'blur' }],
        'password': [{ 'validator': validatePassword, 'trigger': 'blur' }]
      }
    }
  },
  created() {

      console.log(this.$store)
  },
  methods: {
    getCode() {
      this.loading_c = true
      getCode( "phone="+this.loginForm2.phone).then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (this.captchaEnabled) {
          this.loginForm2.uuid = res.uuid;
        }
      });
      setTimeout(() =>  this.loading_c = false, 10000)


    },
    handleLogin2() {
      login2(this.loginForm2).then(res => {
        setToken(res.data)
        window.location.href = '/#/index'
      })
    },

    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", this.loginForm.password, { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            // this.router.push({ path: this.redirect || "/" }).catch(()=>{});
            window.location.href = '/#/index'
          }).catch(() => {
            this.loading = false;
          });
        }
      });
    }


  }
}
</script>

<style scoped>

.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-color: #333;
}
.login-code {
  width: 30%;
  float: right;
  margin: 5px;
}
.login-box {
  width: 1000px;
  height: 474.38px;
  border-radius: 8px;
  display: flex;
}

.login-box img {
  width: 60%;
  height: auto;
}

.login-form {
  background: #ffffff;
  width: 40%;
  border-radius: 0px 8px 8px 0px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-form .el-form {
  width: 214px;
  height: 307px;
}
.login-form .el-form-item {
  margin-bottom: 30px;
}
.login-form .el-form-item.is-error .el-input__inner {
  border: 0 !important;
  border-bottom: 1px solid #fd7065 !important;
  background: #fff !important;
}
.login-form .input-icon {
  height: 32px;
  width: 18px;
  margin-left: -2px;
}
.login-form .el-input__inner {
  border: 0;
  border-bottom: 1px solid #e9e9e8;
  border-radius: 0;
  font-size: 14px;
  font-weight: 400;
  color: #333333;
  height: 32px;
  line-height: 32px;
}
.login-form .el-input__prefix {
  left: 0;
}
.login-form .el-input--prefix .el-input__inner {
  padding-left: 26px;
}
.login-form .el-input__inner::placeholder {
  color: #aeb5c4;
}
.login-form .el-form-item--medium .el-form-item__content {
  line-height: 32px;
}
.login-form .el-input--medium .el-input__icon {
  line-height: 32px;
}
.login-btn {
  border-radius: 17px;
  padding: 11px 20px !important;
  margin-top: 10px;
  margin-right: 0;
  font-weight: 500;
  font-size: 14px;
  border: 0;
  background-color: #ffc200;
}
.login-btn:hover,

.login-btn:focus {
  /* background: #FFC200; */
  /* color: #ffffff; */
}
.login-form-title {
  height: 36px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 40px;
}
.login-form-title .title-label {
  font-weight: 500;
  font-size: 20px;
  color: #333333;
  margin-left: 10px;
}

</style>