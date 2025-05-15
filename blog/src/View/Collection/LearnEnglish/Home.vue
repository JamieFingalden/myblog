<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useEnglishWordsStore } from '../../../stores/englishWords';
import { wordApi } from '../../../api/word';

const router = useRouter();
const englishWordsStore = useEnglishWordsStore();
const wordCount = computed(() => englishWordsStore.wordsCount);
const hasWords = computed(() => englishWordsStore.hasWords);
const wordsList = computed(() => englishWordsStore.wordsList);
const wordsByDate = computed(() => englishWordsStore.wordsByDate);
// 按日期从新到旧排序的单词组
const sortedWordsByDate = computed(() => {
  const dates = Object.keys(wordsByDate.value).sort((a, b) => new Date(b) - new Date(a));
  const sorted = {};
  dates.forEach(date => {
    sorted[date] = wordsByDate.value[date];
  });
  return sorted;
});
// 修改为数组，支持多选
const selectedDates = ref([]);
const isLoading = ref(false);
const errorMessage = ref('');

// 导航到添加单词页面
const goToAddWords = () => {
  router.push('/learnEnglish/add');
};

// 导航到练习页面
const goToPractice = () => {
  if (hasWords.value) {
    if (selectedDates.value.length > 0) {
      // 有选择的单词组
      if (selectedDates.value.length === 1) {
        // 单个日期
        router.push(`/learnEnglish/practice?date=${selectedDates.value[0]}`);
      } else {
        // 多个日期，将日期数组作为参数传递
        router.push(`/learnEnglish/practice?dates=${selectedDates.value.join(',')}`);
      }
    } else {
      // 未选择任何单词组，使用所有单词
      router.push('/learnEnglish/practice');
    }
  } else {
    alert('请先添加单词后再开始学习！');
  }
};

// 选择单词组（支持多选）
const selectWordGroup = (date) => {
  const index = selectedDates.value.indexOf(date);
  if (index === -1) {
    // 如果不在数组中，添加
    selectedDates.value.push(date);
  } else {
    // 如果已在数组中，移除
    selectedDates.value.splice(index, 1);
  }
};

// 获取单词列表
const fetchWordsList = async () => {
  isLoading.value = true;
  errorMessage.value = '';

  try {
    const response = await wordApi.getAllWord();
    // 更健壮的数据处理
    if (response && response.data) {
      let wordsData;

      // 处理不同的响应格式
      if (response.code === 200 && Array.isArray(response.data)) {
        // 标准格式 {code: 200, data: [...]}
        wordsData = response.data;
      } else if (Array.isArray(response.data)) {
        // 直接返回数组
        wordsData = response.data;
      } else if (typeof response.data === 'object' && response.data.code === 200 && Array.isArray(response.data.data)) {
        // 嵌套格式 {data: {code: 200, data: [...]}}
        wordsData = response.data.data;
      } else {
        throw new Error('未知的响应数据格式');
      }

      // 使用 Pinia store 的 $patch 方法更新状态
      englishWordsStore.$patch((state) => {
        state.wordsList = wordsData;
      });

      console.log('成功获取单词列表并更新store');
    } else {
      throw new Error('响应数据为空或格式不正确');
    }
  } catch (error) {
    errorMessage.value = `获取单词列表失败: ${error.message || '未知错误'}`;
    console.error('获取单词列表失败:', error);
  } finally {
    isLoading.value = false;
  }
};

// 组件挂载时获取单词列表
onMounted(() => {
  fetchWordsList();
});

// 计算当前选中的单词总数
const selectedWordsCount = computed(() => {
  if (selectedDates.value.length === 0) return wordCount.value;

  return selectedDates.value.reduce((total, date) => {
    return total + (wordsByDate.value[date]?.length || 0);
  }, 0);
});

// 计算要显示的单词列表
const filteredWordsByDate = computed(() => {
  if (selectedDates.value.length === 0) {
    // 未选择任何单词组时，显示所有单词组（保持排序）
    return sortedWordsByDate.value;
  }

  // 只显示选中的单词组（保持排序）
  const filtered = {};
  // 按照原始排序顺序添加选中的日期
  Object.keys(sortedWordsByDate.value).forEach(date => {
    if (selectedDates.value.includes(date)) {
      filtered[date] = wordsByDate.value[date];
    }
  });
  return filtered;
});
</script>

<template>
  <!-- 添加加载状态和错误信息显示 -->
  <div class="home-container">
    <div v-if="isLoading" class="loading-indicator">
      <p>加载中...</p>
    </div>

    <div v-if="errorMessage" class="error-message">
      <p>{{ errorMessage }}</p>
      <button @click="fetchWordsList" class="retry-btn">重试</button>
    </div>

    <div class="welcome-section">
      <h2>欢迎使用英语单词学习工具</h2>
      <p>这是一个帮助你记忆英语单词的简单工具，通过添加单词和反复练习来提高你的词汇量。</p>
    </div>

    <div class="stats-section">
      <div class="stat-card">
        <div class="stat-icon">📚</div>
        <div class="stat-info">
          <h3>已保存单词</h3>
          <p class="stat-value">{{ wordCount }} 个</p>
        </div>
      </div>
    </div>

    <!-- 添加单词组选择部分 -->
    <div class="word-groups-section" v-if="hasWords">
      <h3 class="section-title">选择单词组</h3>
      <div class="word-groups">
        <div v-for="(words, date) in sortedWordsByDate" :key="date" class="word-group-card"
          :class="{ 'selected': selectedDates.includes(date) }" @click="selectWordGroup(date)">
          <div class="word-group-date">{{ date }}</div>
          <div class="word-group-count">{{ words.length }} 个单词</div>
        </div>
      </div>
      <div class="selection-info" v-if="selectedDates.length > 0">
        <p>已选择 {{ selectedDates.length }} 个单词组 (共 {{ selectedWordsCount }} 个单词)</p>
      </div>
      <div class="selection-info" v-else>
        <p>未选择特定单词组，将显示所有单词</p>
      </div>
    </div>

    <div class="action-section">
      <button class="action-btn add-btn" @click="goToAddWords">
        <span class="btn-icon">➕</span>
        <span>添加新单词</span>
      </button>

      <button class="action-btn practice-btn" @click="goToPractice" :disabled="!hasWords">
        <span class="btn-icon">🚀</span>
        <span>开始学习</span>
      </button>
    </div>

    <div class="tip-section" v-if="!hasWords">
      <p>提示：请先添加一些单词，然后开始学习。</p>
    </div>

    <!-- 单词列表区域 -->
    <div class="words-list-section" v-if="hasWords">
      <h3 class="section-title">单词列表</h3>

      <!-- 按日期分组显示 -->
      <div v-if="Object.keys(filteredWordsByDate).length > 0" class="words-by-date">
        <div v-for="(words, date) in filteredWordsByDate" :key="date" class="date-group">
          <h4 class="date-title">{{ date }}</h4>
          <div class="words-table">
            <table>
              <thead>
                <tr>
                  <th>英文</th>
                  <th>中文</th>
                  <th>例句</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(word, index) in words" :key="index">
                  <td>{{ word.english }}</td>
                  <td>{{ word.chinese }}</td>
                  <td class="example-cell">{{ word.example || '-' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 添加新样式 */
.loading-indicator {
  text-align: center;
  padding: 1rem;
  margin: 1rem 0;
}

.error-message {
  background-color: #ffebee;
  color: #d32f2f;
  padding: 1rem;
  border-radius: 8px;
  margin: 1rem 0;
  text-align: center;
}

.retry-btn {
  background-color: #d32f2f;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  margin-top: 0.5rem;
  cursor: pointer;
}

.retry-btn:hover {
  background-color: #b71c1c;
}

.home-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 1rem;
}

.welcome-section {
  text-align: center;
  margin-bottom: 2rem;
}

.stats-section {
  display: flex;
  justify-content: center;
  margin-bottom: 2rem;
}

.stat-card {
  background-color: #f5f5f5;
  border-radius: 8px;
  padding: 1rem;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  font-size: 2rem;
  margin-right: 1rem;
}

.stat-info h3 {
  margin: 0;
  font-size: 1rem;
  color: #666;
}

.stat-value {
  margin: 0;
  font-size: 1.5rem;
  font-weight: bold;
  color: #333;
}

.section-title {
  margin-bottom: 1rem;
  color: #333;
  border-bottom: 1px solid #eee;
  padding-bottom: 0.5rem;
}

.word-groups-section {
  margin-bottom: 2rem;
}

.word-groups {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 1rem;
  justify-content: center;
  /* 添加居中对齐 */
}

.word-group-card {
  background-color: #f5f5f5;
  border-radius: 8px;
  padding: 1rem;
  cursor: pointer;
  transition: all 0.3s;
  border: 2px solid transparent;
}

.word-group-card:hover {
  background-color: #e0e0e0;
}

.word-group-card.selected {
  border-color: #1a73e8;
  background-color: #e8f0fe;
}

.word-group-date {
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.word-group-count {
  font-size: 0.9rem;
  color: #666;
}

.selection-info {
  margin-top: 1rem;
  color: #666;
  font-style: italic;
}

.action-section {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 2rem;
}

.action-btn {
  display: flex;
  align-items: center;
  padding: 0.8rem 1.5rem;
  border-radius: 8px;
  border: none;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-icon {
  margin-right: 0.5rem;
  font-size: 1.2rem;
}

.add-btn {
  background-color: #4caf50;
  color: white;
}

.add-btn:hover {
  background-color: #388e3c;
}

.practice-btn {
  background-color: #1a73e8;
  color: white;
}

.practice-btn:hover {
  background-color: #1557b0;
}

.practice-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.tip-section {
  text-align: center;
  color: #666;
  font-style: italic;
  margin-bottom: 2rem;
}

.words-list-section {
  margin-bottom: 2rem;
}

.date-group {
  margin-bottom: 2rem;
}

.date-title {
  margin-bottom: 0.5rem;
  color: #333;
}

.words-table {
  overflow-x: auto;
  border-radius: 8px;
  /* 添加圆角 */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  /* 添加轻微阴影效果 */
}

table {
  width: 100%;
  border-collapse: separate;
  /* 修改为separate以支持圆角 */
  border-spacing: 0;
  overflow: hidden;
  /* 确保内容不会溢出圆角 */
}

th,
td {
  padding: 0.8rem;
  text-align: left;
  border-bottom: 1px solid #eee;
}

/* 设置列宽比例为2:2:3 */
th:nth-child(1),
td:nth-child(1) {
  width: 28.5%;
  /* 2/7的比例 */
}

th:nth-child(2),
td:nth-child(2) {
  width: 28.5%;
  /* 2/7的比例 */
}

th:nth-child(3),
td:nth-child(3) {
  width: 43%;
  /* 3/7的比例 */
}

/* 添加表格圆角 */
th:first-child {
  border-top-left-radius: 8px;
}

th:last-child {
  border-top-right-radius: 8px;
}

tr:last-child td:first-child {
  border-bottom-left-radius: 8px;
}

tr:last-child td:last-child {
  border-bottom-right-radius: 8px;
}

th {
  background-color: #f5f5f5;
  font-weight: bold;
}

.example-cell {
  max-width: 300px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

@media (max-width: 768px) {
  .action-section {
    flex-direction: column;
  }

  .action-btn {
    width: 100%;
  }

  .word-groups {
    justify-content: center;
  }
}
</style>