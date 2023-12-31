import request from "../../src/js/request";

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
/*    code,
    uuid*/
  }
  return request({
    url: '/employee/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}
export function login2(data) {
  return request({
    url: '/employee/loginWithCode',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}
/*

export function loginApi(data) {
  return request({
    url: '/employee/login',
    method: 'post',
    data: data
  })
}
*/

// 注册方法
export function register(data) {
  return request({
    url: '/employee/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}
// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/employee/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCode(data) {
  return request({
    url: '/employee/code?' + data,
    headers: {
      isToken: false
    },
    method: 'post',

  })
}