<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useEnglishWordsStore } from '../../../stores/englishWords';

const router = useRouter();
const englishWordsStore = useEnglishWordsStore();
const wordCount = computed(() => englishWordsStore.wordsCount);
const hasWords = computed(() => englishWordsStore.hasWords);
const wordsList = computed(() => englishWordsStore.wordsList);
const wordsByDate = computed(() => englishWordsStore.getWordsByDate());
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

// 获取已保存的单词数量
onMounted(() => {
  englishWordsStore.initWords();
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
  <div class="home-container">
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

    <!-- 现有的欢迎和统计部分保持不变 -->

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

      <!-- 不分组显示所有单词 -->
      <div v-else class="words-table">
        <table>
          <thead>
            <tr>
              <th>英文</th>
              <th>中文</th>
              <th>例句</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(word, index) in wordsList" :key="index">
              <td>{{ word.english }}</td>
              <td>{{ word.chinese }}</td>
              <td class="example-cell">{{ word.example || '-' }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  align-items: center;
  width: 100%;
}

.welcome-section {
  text-align: center;
  max-width: 800px;
}

.welcome-section h2 {
  font-size: 2rem;
  margin-bottom: 1rem;
  color: #333;
}

.welcome-section p {
  font-size: 1.1rem;
  color: #666;
  line-height: 1.6;
}

.stats-section {
  width: 100%;
  display: flex;
  justify-content: center;
  margin: 1rem 0;
}

.stat-card {
  display: flex;
  align-items: center;
  background-color: #f8f9fa;
  padding: 1.5rem;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  min-width: 250px;
}

.stat-icon {
  font-size: 2.5rem;
  margin-right: 1.5rem;
}

.stat-info h3 {
  margin: 0;
  font-size: 1.2rem;
  color: #555;
}

.stat-value {
  font-size: 1.8rem;
  font-weight: bold;
  color: #1a73e8;
  margin: 0.5rem 0 0 0;
}

.action-section {
  display: flex;
  gap: 1.5rem;
  margin-top: 1rem;
}

.action-btn {
  display: flex;
  align-items: center;
  padding: 0.8rem 1.5rem;
  border: none;
  border-radius: 8px;
  font-size: 1.1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-icon {
  margin-right: 0.8rem;
  font-size: 1.2rem;
}

.add-btn {
  background-color: #1a73e8;
  color: white;
}

.add-btn:hover {
  background-color: #1557b0;
}

.practice-btn {
  background-color: #34a853;
  color: white;
}

.practice-btn:hover {
  background-color: #2d8e47;
}

.practice-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.tip-section {
  margin-top: 1rem;
  padding: 1rem;
  background-color: #fff8e1;
  border-left: 4px solid #ffca28;
  border-radius: 4px;
}

.tip-section p {
  margin: 0;
  color: #5f4339;
}

@media (max-width: 600px) {
  .action-section {
    flex-direction: column;
    width: 100%;
    max-width: 300px;
  }

  .action-btn {
    width: 100%;
    justify-content: center;
  }
}

/* 单词列表样式 */
.words-list-section {
  width: 100%;
}

.section-title {
  text-align: center;
  font-size: 1.5rem;
  margin-bottom: 1.5rem;
  color: #333;
}

.date-group {
  margin-bottom: 2rem;
}

.date-title {
  font-size: 1.2rem;
  color: #555;
  margin-bottom: 0.8rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #eee;
}

.words-table {
  width: 100%;
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  overflow: hidden;
  table-layout: fixed;
}

th,
td {
  padding: 0.8rem 1rem;
  text-align: left;
}

/* 设置列宽比例为1:1:3 */
th:nth-child(1),
td:nth-child(1) {
  width: 25%;
  /* 英文列占20% */
}

th:nth-child(2),
td:nth-child(2) {
  width: 25%;
  /* 中文列占20% */
}

th:nth-child(3),
td:nth-child(3) {
  width: 50%;
  /* 例句列占60% */
}

th {
  background-color: #f5f5f5;
  font-weight: 600;
  color: #333;
}

tr:nth-child(even) {
  background-color: #f9f9f9;
}

tr:hover {
  background-color: #f0f7ff;
}

.example-cell {
  max-width: 300px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

@media (max-width: 768px) {
  .words-table {
    font-size: 0.9rem;
  }

  th,
  td {
    padding: 0.6rem 0.8rem;
  }

  .example-cell {
    max-width: 150px;
  }
}

/* 添加单词组选择样式 */
.word-groups-section {
  width: 100%;
  /* margin-bottom: 2rem; */
}

.word-groups {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  justify-content: center;
  margin-bottom: 2rem;
}

.word-group-card {
  background-color: #f8f9fa;
  border: 2px solid #f0f0f0;
  border-radius: 8px;
  padding: 1rem;
  min-width: 150px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.word-group-card:hover {
  background-color: #f0f7ff;
  border-color: #d0e3ff;
}

.word-group-card.selected {
  background-color: #e8f0fe;
  border-color: #1a73e8;
}

.word-group-date {
  font-weight: 600;
  color: #333;
  margin-bottom: 0.5rem;
}

.word-group-count {
  font-size: 0.9rem;
  color: #666;
}

.selection-info {
  text-align: center;
  margin-top: 1rem;
  padding: 0.5rem;
  background-color: #f0f7ff;
  border-radius: 4px;
  color: #1a73e8;
}

@media (max-width: 768px) {
  .word-group-card {
    min-width: 120px;
  }
}
</style>