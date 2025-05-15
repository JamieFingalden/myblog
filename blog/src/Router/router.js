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
      component: () => import('../components/Home/main.vue'),
      meta: { requiresAuth: false } // 首页不需要登录
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../components/Auth/Login.vue'),
      meta: { requiresAuth: false } // 登录页不需要登录
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../components/Auth/Register.vue'),
      meta: { requiresAuth: false } // 注册页不需要登录
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

// 添加全局前置守卫
router.beforeEach((to, from, next) => {
  console.log('路由守卫触发', to.path);

  // 检查路由是否需要认证
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth !== false);
  console.log('是否需要认证:', requiresAuth);

  if (requiresAuth) {
    // 从 localStorage 直接检查是否有 token
    const token = localStorage.getItem('Authorization');
    console.log('当前token状态:', token ? '已登录' : '未登录');

    if (!token) {
      // 未登录，重定向到登录页
      console.log('未登录，重定向到登录页');
      next({ path: '/login', query: { redirect: to.fullPath } });
    } else {
      // 有 token，允许访问
      console.log('已登录，允许访问');
      next();
    }
  } else {
    // 不需要认证的路由，直接访问
    console.log('不需要认证的路由，直接访问');
    next();
  }
});

export default router