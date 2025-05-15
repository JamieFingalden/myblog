import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    isLoggedIn: false,
    token: null,
  }),

  getters: {
    username: (state) => state.user?.username,
    userId: (state) => state.user?.id,
  },

  actions: {
    setUser(userData) {
      this.user = userData;
      this.isLoggedIn = true;
    },

    setToken(token) {
      this.token = token;
      localStorage.setItem('Authorization', token);
    },

    logout() {
      this.user = null;
      this.isLoggedIn = false;
      this.token = null;
      localStorage.removeItem('Authorization');
    },

    async checkAuth() {
      const token = localStorage.getItem('Authorization');
      if (!token) return false;

      try {
        this.token = token;
        // 这里后续会调用后端API验证token
        // const response = await fetch('/api/user/checkLogin', {
        //   headers: { 'Authorization': `Bearer ${token}` }
        // });
        // const data = await response.json();

        // if (data.code === 200) {
        //   this.setUser(data.data);
        //   return true;
        // } else {
        //   this.logout();
        //   return false;
        // }

        // 模拟验证成功
        return true;
      } catch (error) {
        this.logout();
        return false;
      }
    }
  }
});