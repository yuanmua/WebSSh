import request from '@/js/request'

// 查询用户列表
export function listCommand(data) {
    return request({
        url: '/system/list',
        method: 'get',
        params: data
    })
}


// 新增用户
export function addCommand(data) {
    return request({
        url: '/system/addSSh',
        method: 'post',
        data: data
    })
}

// 修改用户
export function updateCommand(data) {
    return request({
        url: '/system/updateSSh',
        method: 'put',
        data: data
    })
}

// 删除用户
export function delCommand(sshId) {
    return request({
        url: '/system/ssh/' + sshId,
        method: 'delete'
    })
}
