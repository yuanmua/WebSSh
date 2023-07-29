import {getToken, removeToken, setToken} from "@/js/auth";
import {getInfo, login, logout} from "@/api/login";
import {listSSh} from "@/api/SSH_c";

const ssh = {
    state: {
        // 服务器数据
        sshList: [ ],
    },
    mutations: {
        SET_SSH_LIST:(state, sshList) => {
            state.sshList = sshList
        },
    },

    actions: {

        GetList({commit},userInfo) {
            listSSh(userInfo).then(response => {
                console.log(response.data)
                commit('SET_SSH_LIST', response.data)
                }
            );
        },



    }
}

export default ssh