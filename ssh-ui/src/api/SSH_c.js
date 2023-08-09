import request from '@/js/request'

// 查询ssh列表
export function listSSh(data) {
    return request({
        url: '/system/list',
        method: 'get',
        params: data
    })
}


// 新增ssh
export function addSSh(data) {
    return request({
        url: '/system/addSSh',
        method: 'post',
        data: data
    })
}

// 修改ssh
export function updateSSh(data) {
    return request({
        url: '/system/updateSSh',
        method: 'put',
        data: data
    })
}

// 删除ssh
export function delSSh(sshId) {
    return request({
        url: '/system/ssh/' + sshId,
        method: 'delete'
    })
}

// 查询ssh
export function getSsh(id) {
    return request({
        url: '/system/getSsh' + id,
        method: 'get'
    })
}
