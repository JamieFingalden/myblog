<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { loginApi } from '../../api/Login' // 新增

const router = useRouter();
const username = ref('');
const password = ref('');
const errorMessage = ref('');
const isLoading = ref(false);
const showAnimation = ref(false);

const login = async () => {
  if (!username.value || !password.value) {
    errorMessage.value = '请输入用户名和密码';
    return;
  }

  try {
    isLoading.value = true;
    const response = await loginApi.login({
      username: username.value,
      password: password.value
    });
    // 假设后端返回 { code: 200, data: { token: 'xxx' }, message: 'xxx' }
    if (response.code === 200) {
      // 修改为 Authorization 以匹配后端校验
      localStorage.setItem('Authorization', response.data);
      const redirectPath = router.currentRoute.value.query.redirect || '/';
      router.push(redirectPath);
    } else {
      errorMessage.value = response.message || '登录失败';
    }
  } catch (error) {
    errorMessage.value = error.message || '登录失败，请稍后重试';
  } finally {
    isLoading.value = false;
  }
};

const goToRegister = () => {
  router.push('/register');
};

onMounted(() => {
  setTimeout(() => {
    showAnimation.value = true;
  }, 100);
});
</script>

<template>
  <div class="login-page">
    <div class="login-container" :class="{ 'show': showAnimation }">
      <div class="login-card">
        <!-- 左侧登录表单 -->
        <div class="login-form-side">
          <div class="login-tabs">
            <button class="tab active">登录</button>
            <button class="tab" @click="goToRegister">注册</button>
          </div>

          <div class="login-header">
            <h2 class="login-title">登录</h2>
          </div>

          <div class="form-group">
            <div class="input-container">
              <span class="input-icon">👤</span>
              <input type="text" id="username" v-model="username" placeholder="用户名" @keyup.enter="login"
                autocomplete="off">
            </div>
          </div>

          <div class="form-group">
            <div class="input-container">
              <span class="input-icon">🔒</span>
              <input type="password" id="password" v-model="password" placeholder="密码" @keyup.enter="login">
            </div>
          </div>

          <div class="forgot-password">
            <a href="#">忘记密码?</a>
          </div>

          <div class="error-message" v-if="errorMessage">
            <span class="error-icon">⚠️</span>
            {{ errorMessage }}
          </div>

          <button class="login-btn" @click="login" :disabled="isLoading">
            <span class="btn-text">{{ isLoading ? '登录中...' : '登录' }}</span>
            <span class="btn-icon" v-if="!isLoading">→</span>
            <span class="btn-loader" v-else></span>
          </button>

          <div class="social-login">
            <p>或使用以下方式登录</p>
            <div class="social-icons">
              <a href="#" class="social-icon">
                <div class="icon-circle">G</div>
              </a>
              <a href="#" class="social-icon">
                <div class="icon-circle">f</div>
              </a>
              <a href="#" class="social-icon">
                <div class="icon-circle">t</div>
              </a>
              <a href="#" class="social-icon">
                <div class="icon-circle">in</div>
              </a>
            </div>
          </div>
        </div>

        <!-- 右侧图片和注册提示 -->
        <div class="login-image-side">
          <div class="image-content">
            <div class="illustration">
              <!-- 这里可以放置一个SVG或图片 -->
              <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/javascript/javascript-original.svg"
                alt="Illustration">
            </div>
            <div class="register-prompt">
              <p>已有账号?</p>
              <p>登录以继续您的旅程</p>
              <button class="register-btn" @click="goToRegister">注册</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 95%;
  /* 修改为窗口宽度的80% */
  margin: 0 auto;
  /* 居中显示 */
  min-height: 80vh;
  background-color: #ebebebe8;
  position: relative;
  overflow: hidden;
  margin-top: 40px;
  border-radius: 12px;
  /* 减小圆角 */
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.333);
}

.login-container {
  position: relative;
  z-index: 1;
  width: 80%;
  max-width: 900px;
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.8s ease, transform 0.8s ease;
}

.login-container.show {
  opacity: 1;
  transform: translateY(0);
}

.login-card {
  display: flex;
  background-color: white;
  border-radius: 8px;
  /* 减小圆角 */
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  /* 更轻微的阴影 */
  overflow: hidden;
  height: 550px;
}

.login-form-side {
  flex: 1;
  padding: 2.5rem;
  display: flex;
  flex-direction: column;
}

.login-tabs {
  display: flex;
  margin-bottom: 1.5rem;
}

.tab {
  padding: 0.5rem 1rem;
  background: none;
  border: none;
  font-size: 0.9rem;
  color: #888;
  cursor: pointer;
  position: relative;
}

.tab.active {
  color: #333;
  font-weight: 600;
}

.tab.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 1rem;
  right: 1rem;
  height: 2px;
  background-color: #000000;
  /* 修改为黑色 */
}

.login-header {
  margin-bottom: 1rem;
}

.login-title {
  font-size: 1.8rem;
  color: #333;
  font-weight: 600;
}

.form-group {
  margin-bottom: 1.5rem;
}

.input-container {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 12px;
  font-size: 1rem;
  color: #888;
}

input {
  width: 100%;
  padding: 1rem 1rem 1rem 2.5rem;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  /* 减小圆角 */
  font-size: 1rem;
  transition: all 0.3s;
  background-color: #f9f9f9;
}

input:focus {
  outline: none;
  border-color: #000000;
  /* 修改为黑色 */
  background-color: white;
  box-shadow: 0 0 0 1px rgba(0, 0, 0, 0.1);
  /* 修改阴影颜色 */
}

.forgot-password {
  text-align: right;
  margin-bottom: 1.5rem;
}

.forgot-password a {
  color: #000000;
  /* 修改为黑色 */
  font-size: 0.85rem;
  text-decoration: none;
}

.error-message {
  display: flex;
  align-items: center;
  color: #e11d48;
  font-size: 0.9rem;
  margin-bottom: 1.5rem;
  padding: 0.8rem;
  background-color: rgba(225, 29, 72, 0.1);
  border-radius: 6px;
  animation: shake 0.5s ease-in-out;
}

.error-icon {
  margin-right: 0.5rem;
}

.login-btn {
  width: 100%;
  padding: 1rem;
  background-color: #000000;
  /* 修改为黑色 */
  color: white;
  border: none;
  border-radius: 4px;
  /* 减小圆角 */
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
  margin-bottom: 1.5rem;
  height: 50px;
  /* 添加固定高度 */
}

.login-btn:hover {
  background-color: #333333;
  /* 修改为深灰色 */
  transform: translateY(-1px);
  /* 减小悬浮效果 */
}

.btn-text {
  position: relative;
  z-index: 1;
}

.btn-icon {
  margin-left: 0.5rem;
  font-size: 1.1rem;
  transition: transform 0.3s;
}

.btn-loader {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid white;
  border-radius: 50%;
  margin-left: 0.5rem;
  animation: spin 1s linear infinite;
}

.social-login {
  margin-top: auto;
  text-align: center;
}

.social-login p {
  color: #888;
  font-size: 0.9rem;
  margin-bottom: 1rem;
  position: relative;
}

.social-login p::before,
.social-login p::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 25%;
  height: 1px;
  background-color: #e0e0e0;
}

.social-login p::before {
  left: 0;
}

.social-login p::after {
  right: 0;
}

.social-icons {
  display: flex;
  justify-content: center;
  gap: 1rem;
}

.icon-circle {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #f5f5f7;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #555;
  transition: all 0.3s;
}

.icon-circle:hover {
  background-color: #e8e8e8;
  transform: translateY(-2px);
}

/* 右侧样式 */
.login-image-side {
  flex: 1;
  background-color: #f7f6f3;
  /* 修改为浅灰色 */
  display: flex;
  align-items: center;
  justify-content: center;
  color: #333333;
  /* 修改为深灰色 */
  position: relative;
  overflow: hidden;
}

.image-content {
  text-align: center;
  padding: 2rem;
  z-index: 1;
}

.illustration {
  margin-bottom: 2rem;
}

.illustration img {
  width: 150px;
  height: 150px;
  object-fit: contain;
}

.register-prompt {
  margin-bottom: 2rem;
}

.register-prompt p {
  margin: 0.5rem 0;
}

.register-prompt p:first-child {
  font-size: 1.2rem;
  font-weight: 600;
}

.register-btn {
  background-color: transparent;
  color: #333333;
  /* 修改为深灰色 */
  border: 1px solid #333333;
  /* 修改为深灰色 */
  padding: 0.8rem 2rem;
  border-radius: 4px;
  /* 减小圆角 */
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  height: 50px;
  /* 添加固定高度 */
}

.register-btn:hover {
  background-color: rgba(0, 0, 0, 0.05);
  /* 修改为浅灰色 */
  transform: translateY(-1px);
  /* 减小悬浮效果 */
}

@keyframes shake {

  0%,
  100% {
    transform: translateX(0);
  }

  10%,
  30%,
  50%,
  70%,
  90% {
    transform: translateX(-5px);
  }

  20%,
  40%,
  60%,
  80% {
    transform: translateX(5px);
  }
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .login-card {
    flex-direction: column;
    height: auto;
  }

  .login-image-side {
    display: none;
  }

  .login-container {
    width: 90%;
  }
}
</style>
