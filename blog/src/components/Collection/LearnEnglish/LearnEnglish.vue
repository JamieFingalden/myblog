<script setup>
import { onMounted } from 'vue';
import { useEnglishWordsStore } from '../../../stores/englishWords';

// 初始化单词 store
const englishWordsStore = useEnglishWordsStore();

onMounted(() => {
  // 在组件挂载时加载单词数据
  englishWordsStore.initWords();
});
</script>

<template>
  <div class="learn-english-container">
    <header class="learn-header">
      <h1>英语单词学习</h1>
    </header>

    <main class="learn-content">
      <router-view></router-view>
    </main>
  </div>
</template>

<style scoped>
.learn-english-container {
  width: 100%;
  margin: 0 auto;
  padding: 2rem;
  min-height: 80vh;
  display: flex;
  flex-direction: column;
}

.learn-header {
  margin-bottom: 2rem;
  text-align: center;
}

.learn-header h1 {
  font-size: 2.5rem;
  margin-bottom: 1.5rem;
  color: #333;
}

.learn-content {
  flex: 1;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  padding: 2rem;
}

@media (max-width: 768px) {
  .learn-english-container {
    padding: 1rem;
  }

  .learn-header h1 {
    font-size: 2rem;
  }

  .learn-content {
    padding: 1rem;
  }
}
</style>