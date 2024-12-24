<template>
  <div class="body-1">
    <header class="header-1">
      <h1 class="team.h1">制作团队</h1>
    </header>
    <div class="container">
      <div class="team-member" style="margin: auto;  text-align: center;">
        <div class="team-member-info">
          <h2>指导老师</h2>
          <p style="font-size: large">吴云</p>
        </div>
      </div>

      <div class="team-member">
<!--        <img src="member1.jpg" alt="成员1">-->
        <div class="team-member-info">
          <h2>组长1</h2>
          <p>邹子昂</p>
          <p>职位：人工智能算法实现 资源预测功能 数据分析 整合文档、答辩、PPT工作</p>
        </div>
      </div>
      <div class="team-member">
        <div class="team-member-info">
          <h2>成员2</h2>
          <p>赵文胤</p>
          <p>职位：整体架构实现 前端开发与后端框架</p>
        </div>
      </div>
      <div class="team-member">
        <div class="team-member-info">
          <h2>成员3</h2>
          <p>章伟平</p>
          <p>职位：后端开发 服务器使用功能 系统监控功能 快捷指令功能</p>
        </div>
      </div>
      <div class="team-member">
        <div class="team-member-info">
          <h2>成员4</h2>
          <p>曾嘉怡</p>
          <p>职位：后端开发 用户认证功能 分权管理功能</p>
        </div>
      </div>
      <div class="team-member">
        <div class="team-member-info">
          <h2>成员5</h2>
          <p>蔡诗语</p>
          <p>职位：运维部署 运维与部署 测试与维护 数据分析</p>
        </div>
      </div>
      <div class="team-member">
        <div class="team-member-info">
          <h2>成员6</h2>
          <p>张爽</p>
          <p>职位：PPT制作 思维导图与流程图制作</p>
        </div>
      </div>
      <div class="team-member">
        <div class="team-member-info">
          <h2>成员7</h2>
          <p>刘文宇</p>
          <p>职位：数据库连接、设计 文档撰写</p>
        </div>
      </div>

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
.team.h1{
  color: #bfcbd9;
}
.body-1 {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  background-color: #f4f4f4;
}
.header-1 {
  background-color: #333;
  color: #fff;
  text-align: center;
  padding: 1rem;
}
.container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 2rem;
  background-color: #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}
.team-member {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
}
.team-member img {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  margin-right: 1rem;
}
.team-member-info {
  flex: 1;
}
</style>