<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const round1Correct = ref(0);
const round1Total = ref(0);
const round2Correct = ref(0);
const round2Total = ref(0);
const totalCorrect = computed(() => round1Correct.value + round2Correct.value);
const totalQuestions = computed(() => round1Total.value + round2Total.value);
const totalAccuracy = computed(() => {
  if (totalQuestions.value === 0) return 0;
  return Math.round((totalCorrect.value / totalQuestions.value) * 100);
});

// 从路由参数获取结果数据
onMounted(() => {
  const route = router.currentRoute.value;
  round1Correct.value = parseInt(route.query.round1Correct || 0);
  round1Total.value = parseInt(route.query.round1Total || 0);
  round2Correct.value = parseInt(route.query.round2Correct || 0);
  round2Total.value = parseInt(route.query.round2Total || 0);
});

// 返回首页
const goHome = () => {
  router.push('/learnEnglish');
};

// 再学一次
const practiceAgain = () => {
  router.push('/learnEnglish/practice');
};

// 获取评价
const getEvaluation = (accuracy) => {
  if (accuracy >= 90) return { text: '太棒了！', emoji: '🎉', color: '#34a853' };
  if (accuracy >= 70) return { text: '做得不错！', emoji: '👍', color: '#1a73e8' };
  if (accuracy >= 50) return { text: '继续努力！', emoji: '💪', color: '#fbbc05' };
  return { text: '需要更多练习', emoji: '📚', color: '#ea4335' };
};
</script>

<template>
  <div class="result-container">
    <div class="result-header">
      <h2>练习结果</h2>
    </div>

    <div class="result-card">
      <div class="result-summary">
        <div class="evaluation-emoji">{{ getEvaluation(totalAccuracy).emoji }}</div>
        <div class="evaluation-text" :style="{ color: getEvaluation(totalAccuracy).color }">
          {{ getEvaluation(totalAccuracy).text }}
        </div>
        <div class="accuracy-display">
          <div class="accuracy-number">{{ totalAccuracy }}%</div>
          <div class="accuracy-label">正确率</div>
        </div>
      </div>

      <div class="result-details">
        <div class="round-result">
          <h3>第一轮 (英→中)</h3>
          <div class="score">
            <span class="correct">{{ round1Correct }}</span> / {{ round1Total }}
            <span class="percentage">({{ round1Total ? Math.round((round1Correct / round1Total) * 100) : 0 }}%)</span>
          </div>
        </div>

        <div class="round-result">
          <h3>第二轮 (中→英)</h3>
          <div class="score">
            <span class="correct">{{ round2Correct }}</span> / {{ round2Total }}
            <span class="percentage">({{ round2Total ? Math.round((round2Correct / round2Total) * 100) : 0 }}%)</span>
          </div>
        </div>
      </div>

      <div class="result-message">
        <p v-if="totalAccuracy >= 90">
          恭喜你！你已经掌握了这组单词，可以尝试学习新的单词了。
        </p>
        <p v-else-if="totalAccuracy >= 70">
          不错的成绩！再多练习几次，你就能完全掌握这些单词了。
        </p>
        <p v-else>
          这些单词还需要更多练习。别担心，坚持就是胜利！
        </p>
      </div>
    </div>

    <div class="action-buttons">
      <button class="action-btn practice-again-btn" @click="practiceAgain">
        <span class="btn-icon">🔄</span>
        <span>再学一次</span>
      </button>
      <button class="action-btn home-btn" @click="goHome">
        <span class="btn-icon">🏠</span>
        <span>返回首页</span>
      </button>
    </div>
  </div>
</template>

<style scoped>
.result-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 1rem;
}

.result-header {
  text-align: center;
  margin-bottom: 2rem;
}

.result-header h2 {
  font-size: 2rem;
  color: #333;
}

.result-card {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding: 2rem;
  margin-bottom: 2rem;
}

.result-summary {
  text-align: center;
  margin-bottom: 2rem;
  padding-bottom: 2rem;
  border-bottom: 1px solid #eee;
}

.evaluation-emoji {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.evaluation-text {
  font-size: 1.8rem;
  font-weight: 600;
  margin-bottom: 1.5rem;
}

.accuracy-display {
  display: inline-block;
  background-color: #f8f9fa;
  padding: 1rem 2rem;
  border-radius: 50px;
}

.accuracy-number {
  font-size: 2.5rem;
  font-weight: 700;
  color: #1a73e8;
}

.accuracy-label {
  font-size: 1rem;
  color: #666;
}

.result-details {
  display: flex;
  justify-content: space-around;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  gap: 1.5rem;
}

.round-result {
  text-align: center;
  flex: 1;
  min-width: 200px;
}

.round-result h3 {
  font-size: 1.2rem;
  color: #555;
  margin-bottom: 0.5rem;
}

.score {
  font-size: 1.5rem;
  color: #333;
}

.correct {
  color: #34a853;
  font-weight: 600;
}

.percentage {
  font-size: 1rem;
  color: #666;
  margin-left: 0.5rem;
}

.result-message {
  background-color: #f0f7ff;
  padding: 1.5rem;
  border-radius: 8px;
  border-left: 4px solid #1a73e8;
}

.result-message p {
  margin: 0;
  color: #333;
  font-size: 1.1rem;
  line-height: 1.5;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 1.5rem;
  margin-top: 2rem;
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

.practice-again-btn {
  background-color: #34a853;
  color: white;
}

.practice-again-btn:hover {
  background-color: #2d8e47;
}

.home-btn {
  background-color: #1a73e8;
  color: white;
}

.home-btn:hover {
  background-color: #1557b0;
}

@media (max-width: 600px) {
  .result-card {
    padding: 1.5rem;
  }

  .evaluation-emoji {
    font-size: 3rem;
  }

  .evaluation-text {
    font-size: 1.5rem;
  }

  .accuracy-number {
    font-size: 2rem;
  }

  .action-buttons {
    flex-direction: column;
  }

  .action-btn {
    width: 100%;
    justify-content: center;
  }
}
</style>