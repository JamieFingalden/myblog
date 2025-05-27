<script setup>
import { ref, computed, onMounted, watch, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import { useEnglishWordsStore } from '../../../stores/englishWords';

const router = useRouter();
const englishWordsStore = useEnglishWordsStore();

// 练习状态
const currentRound = ref(1); // 1: 英文→中文, 2: 中文→英文
const currentWordIndex = ref(0);
const userAnswers = ref([]);
const userInput = ref('');
const showFeedback = ref(false);
const isCorrect = ref(false);
const selectedDate = ref('');
const practiceWords = ref([]);
const roundResults = ref({
  round1: { correct: 0, total: 0 },
  round2: { correct: 0, total: 0 }
});

// 获取按日期分组的单词
const wordsByDate = computed(() => {
  return englishWordsStore.wordsByDate;
});

// 获取日期列表
const dateList = computed(() => {
  return Object.keys(wordsByDate.value).sort((a, b) => new Date(b) - new Date(a));
});

// 当前练习的单词
const currentWord = computed(() => {
  if (practiceWords.value.length > currentWordIndex.value) {
    return practiceWords.value[currentWordIndex.value];
  }
  return null;
});

// 当前问题
const currentQuestion = computed(() => {
  if (!currentWord.value) return '';
  return currentRound.value === 1 ? currentWord.value.english : currentWord.value.chinese;
});

// 当前答案
const currentAnswer = computed(() => {
  if (!currentWord.value) return '';
  return currentRound.value === 1 ? currentWord.value.chinese : currentWord.value.english;
});

// 初始化练习
onMounted(() => {
  // 从路由参数获取选择的日期
  const routeDate = router.currentRoute.value.query.date;
  const routeDates = router.currentRoute.value.query.dates;

  if (routeDates) {
    // 处理多个日期的情况
    const dateArray = routeDates.split(',');
    selectedDate.value = dateArray.join(', '); // 用于显示

    // 合并多个日期的单词
    let allWords = [];
    dateArray.forEach(date => {
      if (wordsByDate.value[date]) {
        allWords = [...allWords, ...wordsByDate.value[date]];
      }
    });

    if (allWords.length > 0) {
      practiceWords.value = allWords;
    } else if (dateList.value.length > 0) {
      // 如果没有找到单词，使用最新的单词组
      selectedDate.value = dateList.value[0];
      practiceWords.value = wordsByDate.value[dateList.value[0]];
    }
  } else if (routeDate && wordsByDate.value[routeDate]) {
    // 处理单个日期的情况
    selectedDate.value = routeDate;
    practiceWords.value = wordsByDate.value[routeDate];
  } else if (dateList.value.length > 0) {
    // 默认使用最新的单词组
    selectedDate.value = dateList.value[0];
    practiceWords.value = wordsByDate.value[dateList.value[0]];
  }

  // 如果没有单词，返回首页
  if (practiceWords.value.length === 0) {
    alert('没有可练习的单词，请先添加单词！');
    router.push('/learnEnglish');
    return;
  }

  // 随机排序单词
  practiceWords.value = [...practiceWords.value].sort(() => 0.5 - Math.random());

  // 初始化答案数组
  userAnswers.value = Array(practiceWords.value.length).fill('');

  // 设置第一轮总数
  roundResults.value.round1.total = practiceWords.value.length;
  roundResults.value.round2.total = practiceWords.value.length;
});

// 检查答案
const checkAnswer = () => {
  const trimmedInput = userInput.value.trim();
  const trimmedAnswer = currentAnswer.value.trim();

  // 保存用户答案
  userAnswers.value[currentWordIndex.value] = trimmedInput;

  // 检查是否正确（不区分大小写）
  let isAnswerCorrect = false;

  if (currentRound.value === 1) {
    // 英文→中文：检查用户输入是否匹配任一中文翻译
    const possibleAnswers = trimmedAnswer.split('、');
    isAnswerCorrect = possibleAnswers.some(answer =>
      trimmedInput.toLowerCase() === answer.trim().toLowerCase()
    );
  } else {
    // 中文→英文：仍然是精确匹配
    isAnswerCorrect = trimmedInput.toLowerCase() === trimmedAnswer.toLowerCase();
  }

  isCorrect.value = isAnswerCorrect;

  // 更新正确数量
  if (isCorrect.value) {
    if (currentRound.value === 1) {
      roundResults.value.round1.correct++;
    } else {
      roundResults.value.round2.correct++;
    }
  }

  // 显示反馈
  showFeedback.value = true;

  // 3秒后自动进入下一题
  setTimeout(() => {
    nextWord();
  }, 500);
};

// 下一个单词
const nextWord = () => {
  // 重置状态
  showFeedback.value = false;
  userInput.value = '';

  // 移动到下一个单词
  if (currentWordIndex.value < practiceWords.value.length - 1) {
    currentWordIndex.value++;
  } else {
    // 如果是第一轮结束
    if (currentRound.value === 1) {
      // 开始第二轮
      currentRound.value = 2;
      currentWordIndex.value = 0;
      // 重新随机排序单词
      practiceWords.value = [...practiceWords.value].sort(() => 0.5 - Math.random());
    } else {
      // 练习完成，跳转到结果页
      router.push({
        path: '/learnEnglish/result',
        query: {
          round1Correct: roundResults.value.round1.correct,
          round1Total: roundResults.value.round1.total,
          round2Correct: roundResults.value.round2.correct,
          round2Total: roundResults.value.round2.total
        }
      });
    }
  }
};

// 提交答案
const submitAnswer = () => {
  if (!userInput.value.trim()) {
    alert('请输入答案');
    return;
  }
  checkAnswer();
};

// 跳过当前单词
const skipWord = () => {
  userInput.value = '';
  userAnswers.value[currentWordIndex.value] = '(已跳过)';
  nextWord();
};

// 返回首页
const goHome = () => {
  if (confirm('确定要退出练习吗？当前进度将丢失。')) {
    router.push('/learnEnglish');
  }
};

// 答案输入框引用
const answerInput = ref(null);

// 自动聚焦输入框的函数
const focusInput = async () => {
  await nextTick();
  if (answerInput.value && !showFeedback.value) {
    answerInput.value.focus();
  }
};

// 监听 showFeedback 变化，当反馈消失时聚焦输入框
watch(showFeedback, (newValue) => {
  if (!newValue) {
    focusInput();
  }
});

// 监听 currentWordIndex 变化，当切换单词时聚焦输入框
watch(currentWordIndex, () => {
  focusInput();
});

// 监听 currentRound 变化，当切换轮次时聚焦输入框
watch(currentRound, () => {
  focusInput();
});

// 在组件挂载后聚焦输入框
onMounted(() => {
  // ... 现有代码 ...

  // 初始化完成后聚焦输入框
  focusInput();
});
</script>

<template>
  <div class="practice-container">
    <div class="practice-header">
      <h2>单词练习</h2>
      <div class="practice-info">
        <span class="round-badge">第{{ currentRound }}轮</span>
        <span class="progress-text">{{ currentWordIndex + 1 }} / {{ practiceWords.length }}</span>
      </div>
    </div>

    <div class="practice-content">
      <div class="word-card"
        :class="{ 'feedback-correct': showFeedback && isCorrect, 'feedback-wrong': showFeedback && !isCorrect }">
        <div class="question">{{ currentQuestion }}</div>

        <div v-if="showFeedback" class="feedback">
          <div class="feedback-icon">{{ isCorrect ? '✓' : '✗' }}</div>
          <div class="correct-answer" v-if="!isCorrect">
            正确答案: <strong>{{ currentAnswer }}</strong>
          </div>
        </div>

        <div v-else class="answer-input">
          <input type="text" v-model="userInput" :placeholder="currentRound === 1 ? '请输入中文翻译' : '请输入英文单词'"
            @keyup.enter="submitAnswer" ref="answerInput" autocomplete="off">
        </div>

        <!-- 修改例句显示逻辑：仅在第一轮（英文→中文）时显示例句 -->
        <div v-if="currentWord && currentWord.example && !showFeedback && currentRound === 1" class="example">
          <div class="example-label">例句:</div>
          <div class="example-text">{{ currentWord.example }}</div>
        </div>
      </div>
    </div>

    <div class="practice-actions" v-if="!showFeedback">
      <button class="action-btn skip-btn" @click="skipWord">
        跳过
      </button>
      <button class="action-btn submit-btn" @click="submitAnswer">
        提交
      </button>
    </div>

    <div class="practice-footer">
      <button class="home-btn" @click="goHome">
        <span>返回首页</span>
      </button>
    </div>
  </div>
</template>

<style scoped>
.practice-container {
  display: flex;
  flex-direction: column;
  gap: 2rem;
  max-width: 800px;
  margin: 0 auto;
  height: 100%;
}

.practice-header {
  text-align: center;
  margin-bottom: 1rem;
}

.practice-header h2 {
  font-size: 1.8rem;
  margin-bottom: 0.5rem;
  color: #333;
}

.practice-info {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 0.5rem;
}

.round-badge {
  background-color: #1a73e8;
  color: white;
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
}

.progress-text {
  color: #666;
  font-size: 0.9rem;
  padding: 0.3rem 0;
}

.practice-content {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.word-card {
  width: 100%;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding: 2.5rem;
  text-align: center;
  transition: all 0.3s;
}

.feedback-correct {
  background-color: #e6f7e9;
  border: 1px solid #34a853;
}

.feedback-wrong {
  background-color: #fef2f2;
  border: 1px solid #ff4d4f;
}

.question {
  font-size: 2.2rem;
  font-weight: 600;
  margin-bottom: 2rem;
  color: #333;
}

.answer-input {
  margin-bottom: 1.5rem;
}

.answer-input input {
  width: 100%;
  padding: 1rem;
  font-size: 1.2rem;
  border: 2px solid #ddd;
  border-radius: 8px;
  text-align: center;
  transition: border-color 0.3s;
}

.answer-input input:focus {
  outline: none;
  border-color: #1a73e8;
}

.feedback {
  margin: 2rem 0;
}

.feedback-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.feedback-correct .feedback-icon {
  color: #34a853;
}

.feedback-wrong .feedback-icon {
  color: #ff4d4f;
}

.correct-answer {
  font-size: 1.2rem;
  color: #333;
}

.example {
  margin-top: 1.5rem;
  padding: 1rem;
  background-color: #f8f9fa;
  border-radius: 8px;
  text-align: left;
}

.example-label {
  font-weight: 500;
  color: #666;
  margin-bottom: 0.5rem;
}

.example-text {
  color: #333;
  font-style: italic;
}

.practice-actions {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 1rem;
}

.action-btn {
  padding: 0.8rem 2rem;
  border: none;
  border-radius: 8px;
  font-size: 1.1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.skip-btn {
  background-color: #f5f5f5;
  color: #666;
}

.skip-btn:hover {
  background-color: #e0e0e0;
}

.submit-btn {
  background-color: #1a73e8;
  color: white;
}

.submit-btn:hover {
  background-color: #1557b0;
}

.practice-footer {
  margin-top: 2rem;
  text-align: center;
}

.home-btn {
  background-color: transparent;
  border: 1px solid #ddd;
  color: #666;
  padding: 0.6rem 1.2rem;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.home-btn:hover {
  background-color: #f5f5f5;
}

@media (max-width: 768px) {
  .word-card {
    padding: 1.5rem;
  }

  .question {
    font-size: 1.8rem;
  }

  .answer-input input {
    font-size: 1rem;
    padding: 0.8rem;
  }

  .practice-actions {
    flex-direction: column;
  }

  .action-btn {
    width: 100%;
  }
}
</style>