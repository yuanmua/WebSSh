import {createRouter, createWebHashHistory} from "vue-router";
import login from "../views/page/login/login.vue";
import MainView from "../views/page/terminal/TerView.vue";

import Layout from '@/layout/index.vue'
import Register from "@/views/page/login/register.vue";


// 2. 定义一些路由
// 每个路由都需要映射到一个组件。
// 我们后面再讨论嵌套路由。
const routes = [
  { path: '/',
    component: login
  },
  /*{ path: '/index',
    component: MainView
  },*/
  {
    path: '/register',
    component: Register,
    hidden: true
  },
  {
    path: '/index',
    component: Layout,
    children: [
      {
        path: '/index',
        component: () => import('@/views/page/Main/index.vue'),
        name: 'Index',
      },
      {
        path: '/addSSH',
        component: () => import('@/views/page/Main/SSh_config/addSSH.vue'),
        name: 'addSSH',
      }

    ]
  },
]

// 3. 创建路由实例并传递 `routes` 配置
// 你可以在这里输入更多的配置，但我们在这里
// 暂时保持简单
const router = createRouter({
  // 4. 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
  history: createWebHashHistory(),
  routes, // `routes: routes` 的缩写
  linkActiveClass:'active',
})

export default router
