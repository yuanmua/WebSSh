<template>
  <div class="body-1">
    <header class="header-1">
      <h1 class="team.h1">制作团队</h1>
    </header>
    <div class="container">
      <div class="team-member" style="margin: auto;  text-align: center;">
        <div class="team-member-info">
          <h2>指导老师</h2>
          <p style="font-size: large">张明星</p>
        </div>
      </div>

      <div class="team-member">
<!--        <img src="member1.jpg" alt="成员1">-->
        <div class="team-member-info">
          <h2>成员1</h2>
          <p>赵文胤</p>
          <p>职位：前端开发 组长</p>
        </div>
      </div>
      <div class="team-member">
        <div class="team-member-info">
          <h2>成员2</h2>
          <p>王东胜</p>
          <p>职位：后端开发 文档工作</p>
        </div>
      </div>
      <div class="team-member">
        <div class="team-member-info">
          <h2>成员3</h2>
          <p>曾伟明</p>
          <p>职位：后端开发 云技术工作</p>
        </div>
      </div>
      <div class="team-member">
        <div class="team-member-info">
          <h2>成员4</h2>
          <p>肖童</p>
          <p>职位：ppt 云技术工作</p>
        </div>
      </div>
      <div class="team-member">
        <div class="team-member-info">
          <h2>成员5 6 7</h2>
          <p>陈铭柯 刘安波 肖宇卓</p>
          <p>职位：云技术工作</p>
        </div>
      </div>
      <div class="team-member">
      <div class="team-member-info">
        <h2>成员8 9</h2>
        <p>肇冠博 胡思雨</p>
      </div>
    </div>
      <!-- 可继续添加更多成员信息 -->
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