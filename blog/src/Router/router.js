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
  ]
})

export default router