import request from '@/js/request'

// 查询快捷键
export function listCommand(id) {
    return request({
        url: '/system/listCommand/' + id,
        method: 'get',
    })
}


// 新增快捷键
export function addCommand(data) {
    return request({
        url: '/system/addCommand',
        method: 'post',
        data: data
    })
}

// 修改快捷键
export function updateCommand(data) {
    return request({
        url: '/system/updateCommand',
        method: 'put',
        data: data
    })
}

// 删除快捷键
export function delCommand(data) {
    return request({
        url: '/system/delCommand/'+data,
        method: 'delete',
    })
}
