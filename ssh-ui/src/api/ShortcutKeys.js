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







export function listCommandCommand(id) {
    return request({
        url: '/system/listCommandCommand/' + id,
        method: 'get',
    })
}


// 新增快捷键
export function addCommandCommand(data) {
    return request({
        url: '/system/addCommandCommand',
        method: 'post',
        data: data
    })
}

// 修改快捷键
export function updateCommandCommand(data) {
    return request({
        url: '/system/updateCommandCommand',
        method: 'put',
        data: data
    })
}

// 删除快捷键
export function delCommandCommand(data) {
    return request({
        url: '/system/delCommandCommand/'+data,
        method: 'delete',
    })
}