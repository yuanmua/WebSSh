import {getToken, removeToken, setToken} from "@/js/auth";
import {getInfo, login, logout} from "@/api/login";
import {listSSh} from "@/api/SSH_c";

const ssh = {
    state: {
        // 服务器数据
        sshList: [],
    },
    mutations: {
        SET_SSH_LIST:(state, sshList) => {
            state.sshList = sshList
        },
    },

    actions: {

        GetList({commit}) {
            listSSh().then(response => {
                commit('SET_SSH_LIST', response.data)
                return true
                }
            );
        },



    }
}

export default ssh