<template>
  <div class="main">
    <div class="sidebar_in">
      <div class="sidebar_li">
        <h2 class="header-mu">目录</h2>
        <ul>
          <li><a href="javascript:void(0)" @click="goAnchor('#install-ssh')">安装SSH</a></li>
          <li><a href="javascript:void(0)" @click="goAnchor('#configure-ssh')">配置SSH</a></li>
          <li><a href="javascript:void(0)" @click="goAnchor('#configure-ssh')">使用webSSH</a></li>


        </ul>
      </div>
    </div>
    <div class="content">
      <h1 class="ins-h1">基于OpenEuler SSH安装和使用指南</h1>


      <h2 class="ins-h2" id="install-ssh">一、安装SSH</h2>
      <p>1.查看SSH是否安装：一般是默认安装好了的 。你可以运行以下命令来检查：</p>
      <code>$ rpm -qa | grep ssh</code>
      <p>2.如果未安装SSH，可以使用以下命令进行安装：</p>
      <code>$ sudo yum install openssh-server-8.2p1-13.oe1.aarch64</code>
      <p>3.启动ssh的服务:</p>
      <code>systemctl start sshd.service</code>
      <p>4.设置开机自动启动ssh服务</p>
      <code>systemctl enable sshd.service</code>

      <h2 class="ins-h2" id="configure-ssh">二、配置SSH</h2>
      <p>1.可以安装缺失的包并配置SSH：</p>
      <code># yum install openssh*</code>
      <p>2.SSH服务器配置文件位于<code>/etc/ssh/sshd_config</code>目录下。</p>
      <p>3.可以打开并修改SSH配置文件以满足你的需求。如端口等（默认端口是22）</p>
      <p>获取IP地址</p>
      <p>在终端用<code>$ ifconfig</code>或<code>$ address</code>来查找ip</p>
      <p>或者在华为控制台复制ip</p>


      <h2 class="ins-h2" id="configure-ssh">三、使用webSSH</h2>
      <p>1.登录我们的网页，来到主页面点击左上角新增按钮</p>
      <p>2.填写下表</p>
      <p>服务器名称自己设定，ip与端口是刚才查找与设置的，用户名和密码是登录主机的用户名和密码，填入之后点击确定。</p>
      <img class="ins-img" src="../../../../public/images/c1.png" alt=""/>
      <p> </p>
      <p>3.出现卡片后直接点击进入服务器连接就可以连接上我们的服务器啦！</p>

    </div>
  </div>

</template>

<script>

export default {
  name: "instructions",

  methods: {
    goAnchor: function (type) {
      var anchor = this.$el.querySelector(type)
      // chrome
      document.body.scrollTop = anchor.offsetTop;
      // firefox
      document.documentElement.scrollTop = anchor.offsetTop;
    }
  },

}
</script>

<style>
.ins-img {
  width: 50%;
}

p {
  margin: 10px;
}

.header-mu {
  color: #bfcbd9;
}

.main {
  width: 100%;
  font-family: Arial, sans-serif;
}
.sidebar_li{
  margin: 20px;


}
.sidebar_in {
  position: fixed;
  top: 200px;
  right: 10px;
  border-radius: 10px;
  width: 250px;
  float: left;
  background-color: rgba(229, 229, 229, 0.5);
  padding-top: 20px;
  color: #4b4b4b;
}

.content {
  margin: auto;
  padding: 20px;
  width: 60%;
}

.ibs-h1 {
  color: #333;
  border-bottom: 2px solid #333;
}

.ins-h2 {
  color: #444;
  font-weight: bold;
}

code {
  background-color: #f4f4f4;
  padding: 2px 6px;
  border: 1px solid #ddd;
}
</style>