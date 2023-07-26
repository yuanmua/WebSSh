import {getToken, removeToken, setToken} from "@/js/auth";
import {getInfo, login, logout} from "@/api/login";
import {listSSh} from "@/api/SSH_c";

const ssh = {
    state: {
        // 服务器数据
        sshList:  [
            {
                "sshId":"1",
                "sshName": "我的",
                "sshHost": "192.168.1.1",
                "sshClass": "123",
                "sshPort": "1234",
                "sshUserName": "yuanmua",
                "sshPassword": "123123",
                "remark": "没有"
            },
            {
                "sshId":"2",
                "sshName": "我的2",
                "sshHost": "192.168.10.1",
                "sshClass": "1231",
                "sshPort": "1234",
                "sshUserName": "yuanmu",
                "sshPassword": "123123zwy",
                "remark": "没有"
            }
        ],
    },
    mutations: {
        SET_SSH_LIST:(state, sshList) => {
            state.sshList = sshList
        },
    },

    actions: {
        // 登录
        getList({commit}) {
            listSSh(this.$store.state.user.id).then(response => {
                    this.userList = response.rows;
                commit(' SET_SSH_LIST', response.rows)
                }
            );
        },
        Login({commit}, userInfo) {
            const username = userInfo.username.trim()
            const password = userInfo.password
            const code = userInfo.code
            const uuid = userInfo.uuid
            return new Promise((resolve, reject) => {
                login(username, password, code, uuid).then(res => {
                    setToken(res.token)
                    commit('SET_TOKEN', res.token)
                    resolve()
                }).catch(error => {
                    reject(error)
                })
            })
        },

    }
}

export default ssh