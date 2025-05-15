<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useEnglishWordsStore } from '../../../stores/englishWords';
import { wordApi } from '../../../api/word'; // 添加API导入

const router = useRouter();
const englishWordsStore = useEnglishWordsStore();
const wordPairs = ref([
  { english: '', chinese: '', example: '' },
  { english: '', chinese: '', example: '' },
  { english: '', chinese: '', example: '' },
  { english: '', chinese: '', example: '' },
  { english: '', chinese: '', example: '' },
]);
const errorMessage = ref('');
const isSubmitting = ref(false);

// 添加新的词对输入框
const addWordPair = () => {
  wordPairs.value.push({ english: '', chinese: '', example: '' });
};

// 删除词对
const removeWordPair = (index) => {
  if (wordPairs.value.length > 1) {
    wordPairs.value.splice(index, 1);
  }
};

// 保存单词
const saveWords = async () => {
  // 验证是否有空项
  const hasEmptyField = wordPairs.value.some(pair =>
    !pair.english.trim() || !pair.chinese.trim()
  );

  if (hasEmptyField) {
    errorMessage.value = '请填写所有的英文和中文单词！';
    return;
  }

  // 过滤掉空项
  const validPairs = wordPairs.value.filter(pair =>
    pair.english.trim() && pair.chinese.trim()
  );

  try {
    isSubmitting.value = true;
    
    // 为每个单词对添加日期
    const wordsWithDate = validPairs.map(pair => ({
      ...pair,
      date: new Date().toISOString()
    }));
    
    // 直接使用API添加单词
    const response = await wordApi.addWord(wordsWithDate);
    
    if (response && response.code === 200 && response.data) {
      // 直接使用API返回的完整单词列表更新store
      englishWordsStore.$patch((state) => {
        state.wordsList = response.data;
      });
      
      // 跳转回首页
      router.push('/learnEnglish');
    } else {
      errorMessage.value = '保存单词失败，请稍后重试';
    }
  } catch (error) {
    console.error('保存单词出错:', error);
    errorMessage.value = '网络错误，请检查后端服务是否正常运行';
  } finally {
    isSubmitting.value = false;
  }
};

// 取消添加
const cancel = () => {
  router.push('/learnEnglish');
};
</script>

<template>
  <div class="add-words-container">
    <h2>添加新单词</h2>
    <p class="subtitle">请输入英文单词和对应的中文翻译</p>

    <div class="word-form">
      <div class="form-header">
        <div class="header-cell">英文</div>
        <div class="header-cell">中文</div>
        <div class="header-cell">例句(可选)</div>
        <div class="header-cell action-cell"></div>
      </div>

      <div v-for="(pair, index) in wordPairs" :key="index" class="word-pair">
        <input type="text" v-model="pair.english" placeholder="英文单词" class="word-input">
        <input type="text" v-model="pair.chinese" placeholder="中文翻译" class="word-input">
        <input type="text" v-model="pair.example" placeholder="例句(可选)" class="word-input">
        <button @click="removeWordPair(index)" class="remove-btn" :disabled="wordPairs.length <= 1">
          ✕
        </button>
      </div>

      <div class="add-more">
        <button @click="addWordPair" class="add-btn">
          <span>+ 添加更多单词</span>
        </button>
      </div>
    </div>

    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>

    <div class="form-actions">
      <button @click="cancel" class="cancel-btn" :disabled="isSubmitting">取消</button>
      <button @click="saveWords" class="save-btn" :disabled="isSubmitting">
        <span v-if="isSubmitting" class="loading-spinner"></span>
        <span>{{ isSubmitting ? '保存中...' : '保存单词' }}</span>
      </button>
    </div>
  </div>
</template>

<style scoped>
.add-words-container {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  text-align: center;
  margin-bottom: 0.5rem;
  color: #333;
}

.subtitle {
  text-align: center;
  color: #666;
  margin-bottom: 2rem;
}

.word-form {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  padding: 1.5rem;
  margin-bottom: 1.5rem;
}

.form-header {
  display: grid;
  grid-template-columns: 1fr 1fr 2fr 50px;
  gap: 1rem;
  margin-bottom: 1rem;
  font-weight: bold;
  color: #555;
}

.header-cell {
  padding: 0.5rem;
}

.action-cell {
  width: 50px;
}

.word-pair {
  display: grid;
  grid-template-columns: 1fr 1fr 2fr 50px;
  gap: 1rem;
  margin-bottom: 1rem;
  align-items: center;
}

.word-input {
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.word-input:focus {
  border-color: #1a73e8;
  outline: none;
}

.remove-btn {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  border: none;
  background-color: #f5f5f5;
  color: #666;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.remove-btn:hover {
  background-color: #ff4d4f;
  color: white;
}

.remove-btn:disabled {
  background-color: #f5f5f5;
  color: #ccc;
  cursor: not-allowed;
}

.add-more {
  margin-top: 1rem;
  text-align: center;
}

.add-btn {
  background-color: transparent;
  border: 1px dashed #1a73e8;
  color: #1a73e8;
  padding: 0.8rem 1.5rem;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.add-btn:hover {
  background-color: #f0f7ff;
}

.error-message {
  color: #ff4d4f;
  margin-bottom: 1rem;
  padding: 0.8rem;
  background-color: #fff2f0;
  border-radius: 4px;
  border-left: 4px solid #ff4d4f;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}

.cancel-btn {
  padding: 0.8rem 1.5rem;
  border: 1px solid #ddd;
  background-color: white;
  color: #666;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.cancel-btn:hover {
  background-color: #f5f5f5;
}

.save-btn {
  padding: 0.8rem 1.5rem;
  border: none;
  background-color: #1a73e8;
  color: white;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.save-btn:hover {
  background-color: #1557b0;
}

@media (max-width: 600px) {
  .word-pair {
    grid-template-columns: 1fr 1fr 40px;
    gap: 0.5rem;
  }

  .word-input {
    padding: 0.6rem;
    font-size: 0.9rem;
  }

  .form-actions {
    flex-direction: column-reverse;
  }

  .cancel-btn,
  .save-btn {
    width: 100%;
  }
}

@media (max-width: 768px) {

  .form-header,
  .word-pair {
    grid-template-columns: 1fr 1fr 50px;
  }

  .form-header div:nth-child(3) {
    display: none;
  }

  .word-pair input:nth-child(3) {
    display: none;
  }
}

/* 添加加载状态样式 */
.loading-spinner {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: #fff;
  animation: spin 1s ease-in-out infinite;
  margin-right: 8px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.save-btn:disabled {
  background-color: #a0c4f1;
  cursor: not-allowed;
}

.cancel-btn:disabled {
  background-color: #f0f0f0;
  color: #aaa;
  cursor: not-allowed;
}
</style>