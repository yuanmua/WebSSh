import request from "@/js/request.js";

//普通上传，带类型
export function uploadFile(formData, fileType) {
    return request({
        url: `/files/upload?fileType=${fileType}`,
        method: 'post',
        data: formData,
        headers: {
            isToken: true,
            'Content-Type': 'multipart/form-data',
        },
    });
}

//上传
export function uploadFile(formData) {
    return request({
        url: '/files/upload',
        method: 'post',
        data: formData,
        headers: {
            isToken: true,
            'Content-Type': 'multipart/form-data',
        },
    });
}



export function downloadImg(uml) {
    return '/api/files/download2?filePath='+ uml;
}

export function download(uml) {
    return '/api/files/download2?filePath='+ uml;
}

//hdfs的下载
export function downloadImg3(uml) {
    return uml;
}