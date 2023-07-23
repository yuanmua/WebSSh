import request from "../../src/js/request";

export function loginApi(data) {
  return request({
    url: '/employee/login',
    method: 'post',
    data: data
  })
}

export function logoutApi(){
  return $axios({
    'url': '/employee/logout',
    'method': 'post',
  })
}
