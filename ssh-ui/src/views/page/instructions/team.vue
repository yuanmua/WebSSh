<template>
  <div class="body-1">
    <header class="header-1">
      <h1 class="team-title">制作团队</h1>
    </header>
    <div class="container">
      <div class="team-member" v-for="(member, index) in teamMembers" :key="index">
        <img v-if="index === 0" src="../../../assets/teacher_view.jpg" alt="成员头像" class="member-image">
        <div class="team-member-info">
          <h2>{{ member.title }}</h2>
          <p>{{ member.name }}</p>
          <p class="member-position">{{ member.position }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "TeamIntroduction",
  data() {
    return {
      teamMembers: [
        {
          title: '指导老师',
          name: '赵晨',
          position: '负责项目指导和监督'
        },
        {
          title: '组长1',
          name: '邹子昂',
          position: '人工智能算法实现、资源预测功能、整合文档、答辩、PPT工作'
        },
        {
          title: '成员2',
          name: '赵文胤',
          position: '整体架构实现、前端开发与后端框架、部署'
        },
        {
          title: '成员3',
          name: '蔡诗语',
          position: '测试与维护、数据分析、用户认证、服务器使用、快捷指令功能'
        },
        {
          title: '成员4',
          name: '张爽',
          position: 'PPT制作、思维导图与流程图制作、文稿撰写、系统监控、分权管理功能'
        },
        {
          title: '成员5',
          name: '李小龙',
          position: '大数据Hadoop与Hive开发、数据库设计与连接、文件管理功能'
        }
      ]
    };
  }
}
</script>

<style>
.body-1 {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  margin: 0;
  padding: 0;
  background-color: #f4f4f4;
}

.header-1 {
  background-color: #005f73;
  color: #fff;
  text-align: center;
  padding: 2rem;
}

.container {
  max-width: 1200px;
  margin: 4rem auto;
  padding: 2rem;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.team-member {
  display: flex;
  align-items: center;
  margin-bottom: 2rem;
  transition: transform 0.3s ease-in-out;
}

.team-member:hover {
  transform: translateY(-5px);
}

.member-image {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-right: 1rem;
  border: 4px solid #005f73;
}

.team-member-info {
  flex: 1;
}

.team-title {
  color: #fff;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

h2 {
  color: #005f73;
}

.member-position {
  font-size: 16px;
  color: #666;
}
</style>