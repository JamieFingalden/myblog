import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import axios from 'axios';

// 后端API基础URL
const API_BASE_URL = 'http://localhost:8080';

export const useEnglishWordsStore = defineStore('englishWords', () => {
  // 单词列表
  const wordsList = ref([]);
  
  // 计算属性：单词数量
  const wordsCount = computed(() => wordsList.value.length);
  
  // 计算属性：是否有单词
  const hasWords = computed(() => wordsList.value.length > 0);
  
  // 初始化：从后端加载单词
  const initWords = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/word/getAllWords`);
      if (response.data && response.data.data) {
        wordsList.value = response.data.data;
      }
    } catch (error) {
      console.error('获取单词列表失败:', error);
      // 加载失败时使用空数组
      wordsList.value = [];
    }
  };
  
  // 添加单词
  const addWords = async (newWords) => {
    try {
      // 调用后端API添加单词
      const response = await axios.post(`${API_BASE_URL}/word/addWord`, newWords);
      
      // 如果后端返回了最新的单词列表，直接使用
      if (response.data && response.data.data) {
        wordsList.value = response.data.data;
      } else {
        // 否则手动更新本地列表
        const wordsWithDate = newWords.map(word => ({
          ...word,
          date: word.date || new Date().toISOString()
        }));
        wordsList.value = [...wordsList.value, ...wordsWithDate];
      }
      
      return true;
    } catch (error) {
      console.error('添加单词失败:', error);
      return false;
    }
  };
  
  // 重置单词列表
  const resetWords = () => {
    wordsList.value = [];
    // 后期可添加清空数据库的API调用
  };
  
  // 获取随机单词（用于练习）
  const getRandomWords = (count) => {
    if (wordsList.value.length <= count) {
      return [...wordsList.value];
    }
    
    const shuffled = [...wordsList.value].sort(() => 0.5 - Math.random());
    return shuffled.slice(0, count);
  };
  
  // 按日期分组获取单词
  const getWordsByDate = () => {
    const grouped = {};
    
    wordsList.value.forEach(word => {
      // 提取日期部分（不含时间）
      const dateOnly = word.date ? word.date.split('T')[0] : '未知日期';
      
      if (!grouped[dateOnly]) {
        grouped[dateOnly] = [];
      }
      
      grouped[dateOnly].push(word);
    });
    
    return grouped;
  };

  return {
    wordsList,
    wordsCount,
    hasWords,
    initWords,
    addWords,
    resetWords,
    getRandomWords,
    getWordsByDate
  };
});