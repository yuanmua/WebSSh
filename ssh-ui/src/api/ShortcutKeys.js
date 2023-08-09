import request from '@/js/request'

// 查询快捷键
export function listCommand(data) {
    return request({
        url: '/system/listCommand',
        method: 'get',
        params: data
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
export function delCommand(CommandId) {
    return request({
        url: '/system/delCommand/' + CommandId,
        method: 'delete'
    })
}
