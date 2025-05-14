<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// 作品集数据
const projects = ref([
  {
    id: 1,
    title: '个人博客系统',
    category: '全栈开发',
    description: '使用Vue.js和springboot开发的个人博客系统，支持文章管理、评论和用户认证。',
    link: 'https://github.com/yourusername/blog-project'
  },
  {
    id: 2,
    title: '天气预报应用',
    category: 'Web开发',
    description: '基于Vue.js的天气预报应用，使用第三方API获取实时天气数据。',
    router: '/weather',
    link: 'https://github.com/yourusername/weather-app'
  },
  {
    id: 5,
    title: '社交媒体分析工具',
    category: '数据分析',
    image: '/images/projects/analytics.jpg',
    description: '分析社交媒体数据的工具，提供可视化报表和趋势分析。',
    link: 'https://github.com/yourusername/social-analytics'
  },
  {
    id: 6,
    title: '英语学习平台',
    category: '教育科技',
    image: '/images/projects/elearning.jpg',
    description: '为学生和教师提供的在线学习平台，包括课程管理和进度跟踪。',
    router: '/learnEnglish',
    link: 'https://github.com/yourusername/elearning-platform'
  }
]);

// 所有分类
const categories = ref(['全部', '全栈开发', 'Web开发', '教育科技', '数据分析']);
// 当前选中的分类
const selectedCategory = ref('全部');
// 过滤后的项目
const filteredProjects = ref([]);

// 初始化过滤项目
onMounted(() => {
  // 初始显示所有项目
  filterProjects('全部');
});

// 根据分类过滤项目
const filterProjects = (category) => {
  selectedCategory.value = category;

  if (category === '全部') {
    filteredProjects.value = [...projects.value];
  } else {
    filteredProjects.value = projects.value.filter(project => project.category === category);
  }
};

// 添加路由跳转方法
const navigateTo = (path) => {
  router.push(path);
};
</script>

<template>
  <div class="collection-container">
    <div class="collection-header">
      <h1>我的作品集</h1>
      <p>这里展示了我的一些项目和作品，涵盖了不同领域的技术实践</p>
    </div>

    <div class="categories">
      <button v-for="category in categories" :key="category"
        :class="['category-btn', { active: selectedCategory === category }]" @click="filterProjects(category)">
        {{ category }}
      </button>
    </div>

    <div class="projects-grid">
      <div v-for="project in filteredProjects" :key="project.id" class="project-card">
        <div class="project-info">
          <h3>{{ project.title }}</h3>
          <span class="project-category">{{ project.category }}</span>
          <p>{{ project.description }}</p>
          <div class="project-actions">
            <a :href="project.link" target="_blank" class="project-link">查看项目</a>
            <button v-if="project.router" @click="navigateTo(project.router)" class="project-route-btn">
              在线体验
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.collection-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.collection-header {
  text-align: center;
  margin-bottom: 3rem;
}

.collection-header h1 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  color: #333;
}

.collection-header p {
  font-size: 1.1rem;
  color: #666;
  max-width: 700px;
  margin: 0 auto;
}

.categories {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 3rem;
}

.category-btn {
  padding: 0.6rem 1.2rem;
  background-color: #f5f5f5;
  border: none;
  border-radius: 30px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.category-btn:hover {
  background-color: #e0e0e0;
}

.category-btn.active {
  background-color: #333;
  color: white;
}

.projects-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 2rem;
}

.project-card {
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  background-color: white;
  padding: 1.5rem;
}

.project-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
}

.project-info {
  padding: 0;
}

.project-info h3 {
  margin: 0 0 0.5rem 0;
  font-size: 1.3rem;
  color: #333;
}

.project-category {
  display: inline-block;
  padding: 0.3rem 0.8rem;
  background-color: #f0f0f0;
  border-radius: 20px;
  font-size: 0.8rem;
  color: #555;
  margin-bottom: 1rem;
}

.project-info p {
  color: #666;
  margin-bottom: 1.5rem;
  line-height: 1.5;
}

.project-actions {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
  justify-content: center;
}

.project-link {
  display: inline-block;
  padding: 0.6rem 1.2rem;
  background-color: #333;
  color: white;
  text-decoration: none;
  border-radius: 5px;
  font-weight: 500;
  transition: background-color 0.3s ease;
}

.project-link:hover {
  background-color: #555;
}

.project-route-btn {
  display: inline-block;
  padding: 0.6rem 1.2rem;
  background-color: #f5f5f5;
  color: #333;
  text-decoration: none;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.project-route-btn:hover {
  background-color: #e0e0e0;
}

@media (max-width: 768px) {
  .projects-grid {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }

  .collection-header h1 {
    font-size: 2rem;
  }
}

@media (max-width: 480px) {
  .collection-container {
    padding: 1rem;
  }

  .projects-grid {
    grid-template-columns: 1fr;
  }

  .category-btn {
    padding: 0.5rem 1rem;
    font-size: 0.8rem;
  }
}
</style>