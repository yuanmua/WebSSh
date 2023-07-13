<template>

    <form action="javascript:" class="form-inline" id="ssh-service">
        <div>服务器SSH参数：</div>
        <label for="host">IP</label>
        <input type="text" id="host" name="host" value="127.0.0.1">
        <label for="port">端口</label>
        <input type="text" id="port" name="port" value="22">
        <label for="username">登录名</label>
        <input type="text" id="username" name="username" value="star">
        <label for="password">密码</label>
        <input type="text" id="password" name="password" value="star">
        <button type="submit">连接到SSH</button>
        <button type="button" id="close">关闭连接</button>
    </form>
</template>

<script>
export default {
    name: "FormView",
    mounted() {
      /*  let client = new WSSHClient();
        let term = undefined
        init(client)
        $('#ssh-service').submit(function () {
            console.log("连接开启");
            connect(client, {
                operate: 'connect',
                host: $('#ssh-service [name="host"]').val(),//IP
                port: $('#ssh-service [name="port"]').val(),//端口号
                username: $('#ssh-service [name="username"]').val(),//用户名
                password: $('#ssh-service [name="password"]').val()//密码
            });
            return false
        })
        $('#ssh-service #close').click(function () {
            console.log("连接关闭");
            close(client)
        })*/
    }
}
</script>

<style scoped>

</style>
