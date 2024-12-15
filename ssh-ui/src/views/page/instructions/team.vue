<template>
    <div class="body-1">
        <header class="header-1">
            <h1 class="team.h1">小组详情</h1>
        </header>
        <div class="container">
            <div class="team-member" style="margin: auto;  text-align: center;">
                <div class="team-member-info">
                    <h2>感谢老师</h2>
                    <p style="font-size: large">xxx</p>
                </div>
            </div>

            <div class="team-member">
                <!--        <img src="member1.jpg" alt="成员1">-->
                <div class="team-member-info">
                    <h2>成员1</h2>
                    <p>赵文胤</p>
                    <p>职位：前端开发 后端开发</p>
                </div>
            </div>


            <div class="team-member">
                <div class="team-member-info">
                    <h2>成员2</h2>
                    <p>周子昂</p>
                    <p>职位：组长，大数据</p>
                </div>
            </div>

            <!-- 你们自己加 -->
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
.team.h1 {
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
