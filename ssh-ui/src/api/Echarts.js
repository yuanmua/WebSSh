import request from "../../src/js/request";
export function updateZ(data) {
    return request({
        url: '/monitor',
        method: 'post',
        data: data
    })
}
