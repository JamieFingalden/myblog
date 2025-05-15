import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export const useEnglishWordsStore = defineStore('englishWords', {
  state: () => ({
    wordsList: []
  }),

  getters: {
    wordsCount: (state) => state.wordsList.length,
    hasWords: (state) => state.wordsList.length > 0,

    getWordsByDate: (state) => {
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