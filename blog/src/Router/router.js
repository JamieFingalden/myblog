import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    {
      path: '/home',
      component: () => import('../components/Home/main.vue')
    },
    {
      path: '/weather',
      name: 'weather',
      component: () => import('../components/Weather/Weather.vue')
    },
    // 添加AI聊天页面的路由
    {
      path: '/chat',
      name: 'chat',
      component: () => import('../View/AIChat/Chat.vue')
    }
  ]
})

export default router