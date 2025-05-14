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
    },
    // 添加作品集页面的路由
    {
      path: '/collection',
      name: 'collection',
      component: () => import('../components/Collection/Collection.vue')
    },
    {
      path: '/learnEnglish',
      name: 'learnEnglish',
      component: () => import('../components/Collection/LearnEnglish/LearnEnglish.vue'),
      children: [
        {
          path: '',
          name: 'englishHome',
          component: () => import('../View/Collection/LearnEnglish/Home.vue')
        },
        {
          path: 'add',
          name: 'addWords',
          component: () => import('../View/Collection/LearnEnglish/AddWords.vue')
        },
        {
          path: 'practice',
          name: 'practice',
          component: () => import('../View/Collection/LearnEnglish/Practice.vue')
        },
        {
          path: 'result',
          name: 'result',
          component: () => import('../View/Collection/LearnEnglish/Result.vue')
        }
      ]
    }
  ]
})

export default router