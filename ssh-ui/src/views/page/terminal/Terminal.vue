<template>
    <div>
        <button @click="OnClick" type="submit">连接到SSH</button>
        <button @click="close" type="button" id="close">关闭连接</button>
        <el-button type="primary" @click="back">返回</el-button>

      <div id="terminal"></div>
    </div>
</template>

<script>
import { Terminal } from "xterm";
import "xterm/css/xterm.css";
import "xterm/lib/xterm.js";
import WSSHClient from '@/js/webssh.js'
let term = new Terminal({
    cols: 97,
        rows: 37,
        cursorBlink: true, // 光标闪烁
        cursorStyle: "block", // 光标样式  null | 'block' | 'underline' | 'bar'
        // scrollback: 800, // 回滚
        tabStopWidth: 8, // 制表宽度
        screenKeys: true
});

let client = new WSSHClient();
function connect(client, options) {
    //执行连接操作
    client.connect({
        onError: function (error) {
            //连接失败回调
            term.write('错误: ' + error + '\r\n');
        },
        onConnect: function () {
            //连接成功回调
            client.sendInitData(options);
            //在页面上显示连接中...
            term.write('连接中...\r\n');
        },
        onClose: function () {
            //连接关闭回调
            term.write("\r\n连接已关闭\r\n");
        },
        onData: function (data) {
            //收到数据时回调
            term.write(data);
        }
    });
}
function init(client) {
    term.open(document.getElementById('terminal'));
    term.onData( function (data) {
        console.log(data);
        //键盘输入时的回调函数
        client.sendClientData(data);
    });
}

export default {
    name: "Terminal",
    data (){
        return {
            options:{
                operate: 'connect',
                host: '192.168.130.128',//IP
                port: 22,//端口号
                username: 'yuanmua',//用户名
                password: '123s123s'//密码
            }
        }

    },
    mounted() {
      init(client)
      console.log("连接开启");

      if (this.sshIdList===undefined){
        window.location.href = '/#/index'
      }
      console.log(this.sshIdList);

      this.options.host = this.sshIdList.sshHost;
      this.options.port = this.sshIdList.sshPort;
      this.options.username = this.sshIdList.sshUserName;
      this.options.password = this.sshIdList.sshPassword
      ;

      console.log(this.options);

    },
    methods:{
        OnClick(){
            connect(client, this.options);
        },
        close() {
            if (client!==undefined){
                console.log(client);
                console.log("连接开213启");
                client.close()
            }
            else console.log("没有开连接");
        },
      back(){
        window.location.href = '/#/index'
      }

    } ,

    computed:{

      id(){
        return this.$route.params.id
      },

      sshIdList(){
      return this.$store.state.ssh.sshList.find(
          sshIdList => sshIdList.id==this.id
      )
    }

  }



}/*
var term = new Terminal({
    cursorBlink: true, // 光标闪烁
    cursorStyle: "block", // 光标样式  null | 'block' | 'underline' | 'bar'
    scrollback: 800, //回滚
    tabStopWidth: 8, //制表宽度
    screenKeys: true
});*/


</script>

<style scoped>
@import "../../../assets/webssh.css";
</style>
