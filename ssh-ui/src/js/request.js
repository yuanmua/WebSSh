import axios from "axios";
import { ElMessageBox, ElMessage } from 'element-plus'
import {getToken} from "@/js/auth";

// 是否显示重新登录
export let isRelogin = { show: false };
  axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
  // 创建axios实例
  const service = axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: '/api',
    // 超时
    timeout: 1000000
  })
  // request拦截器
  service.interceptors.request.use(config => {
    // 从cookie中获取token
    const token = document.cookie.split(';').find(item => item.trim().startsWith('Admin-Token='));
    if (token) {
        config.headers['Authorization'] = 'Bearer ' + token.split('=')[1].trim();
    }
    return config;
  }, error => {
      console.log(error)
      return Promise.reject(error)
  })

  // 响应拦截器
  service.interceptors.response.use(res => {
      // console.log('---响应拦截器---',res)
      // 未设置状态码则默认成功状态
      const code = res.data.code ;
      // 获取错误信息
      const msg = res.data.msg
      // console.log('---code---',code)
      if (code === 0 && msg === 'NOT_LOGIN') {// 返回登录页面

        ElMessageBox.confirm('登录状态已过期，请重新登录', '系统提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 清除cookie
          document.cookie = 'Admin-Token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
          // 跳转到登录页
          location.href = '/';
        });
        return Promise.reject('未登录');
      }  else if (code === 0) {
        ElMessage({ message: msg, type: 'error' })
        return Promise.reject(new Error(msg))
      } else {
        return res.data
      }
    },
    error => {
      console.log('err' + error)
      let { message } = error;
      if (message == "Network Error") {
        message = "后端接口连接异常";
      }
      else if (message.includes("timeout")) {
        message = "系统接口请求超时";
      }
      else if (message.includes("Request failed with status code")) {
        message = "系统接口" + message.substr(message.length - 3) + "异常";
      }
      window.ELEMENT.Message({
        message: message,
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(error)
    })
export default service
