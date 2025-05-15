<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { loginApi } from '../../api/Login' // 新增

const router = useRouter();
const username = ref('');
const password = ref('');
const confirmPassword = ref('');
const email = ref('');
const errorMessage = ref('');
const isLoading = ref(false);
const showAnimation = ref(false);

const register = async () => {
  if (!username.value || !password.value || !confirmPassword.value) {
    errorMessage.value = '请填写所有必填字段';
    return;
  }
  if (password.value !== confirmPassword.value) {
    errorMessage.value = '两次输入的密码不一致';
    return;
  }
  if (password.value.length < 6) {
    errorMessage.value = '密码长度不能少于6个字符';
    return;
  }

  try {
    isLoading.value = true;
    const response = await loginApi.register({
      username: username.value,
      password: password.value,
      email: email.value
    });
    // 假设后端返回 { code: 200, message: '注册成功' }
    if (response.code === 200) {
      router.push('/login');
    } else {
      errorMessage.value = response.message || '注册失败';
    }
  } catch (error) {
    errorMessage.value = error.message || '注册失败，请稍后重试';
  } finally {
    isLoading.value = false;
  }
};

const goToLogin = () => {
  router.push('/login');
};

onMounted(() => {
  setTimeout(() => {
    showAnimation.value = true;
  }, 100);
});
</script>

<template>
  <div class="register-page">
    <div class="register-container" :class="{ 'show': showAnimation }">
      <div class="register-card">
        <!-- 左侧注册表单 -->
        <div class="register-form-side">
          <div class="register-tabs">
            <button class="tab" @click="goToLogin">登录</button>
            <button class="tab active">注册</button>
          </div>

          <div class="register-header">
            <h2 class="register-title">创建账号</h2>
          </div>

          <div class="form-group">
            <div class="input-container">
              <span class="input-icon">👤</span>
              <input type="text" id="username" v-model="username" placeholder="用户名" autocomplete="off">
            </div>
          </div>

          <div class="form-group">
            <div class="input-container">
              <span class="input-icon">✉️</span>
              <input type="email" id="email" v-model="email" placeholder="邮箱" autocomplete="off">
            </div>
          </div>

          <div class="form-group">
            <div class="input-container">
              <span class="input-icon">🔒</span>
              <input type="password" id="password" v-model="password" placeholder="密码">
            </div>
          </div>

          <div class="form-group">
            <div class="input-container">
              <span class="input-icon">🔒</span>
              <input type="password" id="confirmPassword" v-model="confirmPassword" placeholder="确认密码"
                @keyup.enter="register">
            </div>
          </div>

          <div class="error-message" v-if="errorMessage">
            <span class="error-icon">⚠️</span>
            {{ errorMessage }}
          </div>

          <button class="register-btn" @click="register" :disabled="isLoading">
            <span class="btn-text">{{ isLoading ? '注册中...' : '注册' }}</span>
            <span class="btn-icon" v-if="!isLoading">→</span>
            <span class="btn-loader" v-else></span>
          </button>

          <div class="terms">
            <p>注册即表示您同意我们的<a href="#">服务条款</a>和<a href="#">隐私政策</a></p>
          </div>
        </div>

        <!-- 右侧图片和登录提示 -->
        <div class="register-image-side">
          <div class="image-content">
            <div class="illustration">
              <!-- 这里可以放置一个SVG或图片 -->
              <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/vuejs/vuejs-original.svg" alt="Illustration">
            </div>
            <div class="login-prompt">
              <p>已有账号?</p>
              <p>登录以继续您的旅程</p>
              <button class="login-btn" @click="goToLogin">登录</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 95%;
  margin: 0 auto;
  min-height: 80vh;
  background-color: #ebebebe8;
  /* 修改为纯白色背景 */
  position: relative;
  overflow: hidden;
  margin-top: 40px;
  border-radius: 12px;
  /* 减小圆角 */
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.333);
  /* 更轻微的阴影 */
}

.register-container {
  position: relative;
  z-index: 1;
  width: 80%;
  max-width: 900px;
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.8s ease, transform 0.8s ease;
}

.register-container.show {
  opacity: 1;
  transform: translateY(0);
}

.register-card {
  display: flex;
  background-color: white;
  border-radius: 8px;
  /* 减小圆角 */
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  /* 更轻微的阴影 */
  overflow: hidden;
  height: 550px;
}

.register-form-side {
  flex: 1;
  padding: 2.5rem;
  display: flex;
  flex-direction: column;
}

.register-tabs {
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

.register-header {
  margin-bottom: 1rem;
}

.register-title {
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

.register-btn {
  width: 100%;
  padding: 25px 1rem;
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
  height: 60px;
  /* 添加固定高度 */
}

.register-btn:hover {
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

.terms {
  margin-top: auto;
  text-align: center;
  font-size: 0.85rem;
  color: #888;
}

.terms a {
  color: #000000;
  /* 修改为黑色 */
  text-decoration: none;
}

/* 右侧样式 */
.register-image-side {
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

.login-prompt {
  margin-bottom: 2rem;
}

.login-prompt p {
  margin: 0.5rem 0;
}

.login-prompt p:first-child {
  font-size: 1.2rem;
  font-weight: 600;
}

.login-btn {
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

.login-btn:hover {
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
  .register-card {
    flex-direction: column;
    height: auto;
  }

  .register-image-side {
    display: none;
  }

  .register-container {
    width: 90%;
  }
}
</style>