import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export const useEnglishWordsStore = defineStore('englishWords', {
  state: () => ({
    wordsList: []
  }),

  getters: {
    wordsCount: (state) => state.wordsList.length,
    hasWords: (state) => state.wordsList.length > 0,

    // 修改为返回函数的getter，这样可以通过()调用
    wordsByDate: (state) => {
      // 直接返回分组结果，不需要调用
      const grouped = {};

      state.wordsList.forEach(word => {
        // 提取日期部分（不含时间）
        const dateOnly = word.date ? word.date.split('T')[0] : '未知日期';

        if (!grouped[dateOnly]) {
          grouped[dateOnly] = [];
        }

        grouped[dateOnly].push(word);
      });

      return grouped;
    }
  },

  actions: {
    getRandomWords(count) {
      if (this.wordsList.length <= count) {
        return [...this.wordsList];
      }

      const shuffled = [...this.wordsList].sort(() => 0.5 - Math.random());
      return shuffled.slice(0, count);
    },

    resetWords() {
      this.wordsList = [];
    }
  }
});