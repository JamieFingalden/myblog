<script setup>
import { ref, onMounted, onUpdated, nextTick } from 'vue';

// 定义props和emit
const props = defineProps({
  conversation: {
    type: Object,
    default: () => ({
      id: '',
      title: '',
      messages: []
    })
  }
});

const emit = defineEmits(['send-message']);

// 响应式数据
const userInput = ref('');
const loading = ref(false);
const messagesContainer = ref(null);

// 方法
function scrollToBottom() {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
  }
}

function sendMessage() {
  if (!userInput.value.trim()) return;
  
  emit('send-message', {
    content: userInput.value,
    conversationId: props.conversation.id
  });
  
  userInput.value = '';
}

// 生命周期钩子
const inputArea = ref(null);

// 自动调整输入框高度
function autoResize() {
  const textarea = inputArea.value;
  if (!textarea) return;
  
  // 重置高度，以便正确计算
  textarea.style.height = 'auto';
  
  // 设置新高度
  const newHeight = Math.min(textarea.scrollHeight, 120);
  textarea.style.height = newHeight + 'px';
}

onMounted(() => {
  scrollToBottom();
  autoResize();
});

onUpdated(() => {
  scrollToBottom();
  autoResize();
});
</script>

<template>
  <div class="chat-window">
    <!-- 聊天头部 -->
    <div class="chat-header">
      <h2>{{ conversation.title || 'AI 助手' }}</h2>
      <div class="header-actions">
        <button class="action-button">
          <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
            <path d="M12 15.5c1.9 0 3.5-1.6 3.5-3.5S13.9 8.5 12 8.5 8.5 10.1 8.5 12s1.6 3.5 3.5 3.5zm0-5c.8 0 1.5.7 1.5 1.5s-.7 1.5-1.5 1.5-1.5-.7-1.5-1.5.7-1.5 1.5-1.5zM19.5 12c0-1.9-1.6-3.5-3.5-3.5-.2 0-.4 0-.6.1.4.6.6 1.4.6 2.2 0 2.5-2 4.5-4.5 4.5-.8 0-1.6-.2-2.2-.6-.1.2-.1.4-.1.6 0 1.9 1.6 3.5 3.5 3.5.8 0 1.6-.3 2.2-.8.6.5 1.4.8 2.2.8 1.9 0 3.5-1.6 3.5-3.5 0-.8-.3-1.6-.8-2.2.5-.6.7-1.3.7-2.1zM12 4c-4.4 0-8 3.6-8 8s3.6 8 8 8 8-3.6 8-8-3.6-8-8-8zm0 14c-3.3 0-6-2.7-6-6s2.7-6 6-6 6 2.7 6 6-2.7 6-6 6z"/>
          </svg>
        </button>
      </div>
    </div>
    
    <!-- 聊天消息区域 -->
    <div class="chat-messages" ref="messagesContainer">
      <transition-group name="message-fade" tag="div" class="messages-container">
        <div v-for="(message, index) in conversation.messages" :key="index" 
             :class="['message', message.sender === 'user' ? 'user-message' : 'ai-message']">
          <div class="message-content">
            {{ message.content }}
          </div>
          <div class="message-time">{{ message.time }}</div>
        </div>
      </transition-group>
      
      <div v-if="loading" class="message ai-message">
        <div class="message-content">
          <div class="typing-indicator">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 新的聊天输入区域 -->
    <div class="new-chat-input-container">
      <div class="new-input-wrapper">
        <textarea 
          v-model="userInput" 
          placeholder="请输入您的问题..." 
          @keydown.enter.prevent="sendMessage"
          @input="autoResize"
          ref="inputArea"
          class="new-chat-input"
          rows="1"
        ></textarea>
        <button @click="sendMessage" class="new-send-button" title="发送消息">
          <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
            <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.chat-window {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: white;
}

.chat-header {
  padding: 20px 25px;
  background-color: white;
  color: #333;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
}

.chat-header h2 {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 600;
}

.header-actions {
  display: flex;
  gap: 15px;
}

.action-button {
  background: none;
  border: none;
  color: #666;
  cursor: pointer;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.action-button:hover {
  background-color: #f5f5f5;
  color: #333;
  transform: translateY(-2px);
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 25px;
  display: flex;
  flex-direction: column;
  gap: 15px;
  background-color: #f9f9f9;
}

.message {
  max-width: 70%;
  padding: 12px 18px;
  border-radius: 18px;
  position: relative;
  margin-bottom: 10px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease;
}

.message:hover {
  transform: translateY(-2px);
}

.user-message {
  align-self: flex-end;
  background-color: #000;
  color: white;
  border-bottom-right-radius: 5px;
}

.ai-message {
  align-self: flex-start;
  background-color: white;
  border-bottom-left-radius: 5px;
  border: 1px solid #eee;
}

.message-content {
  word-break: break-word;
  line-height: 1.5;
}

.message-time {
  font-size: 0.7rem;
  color: rgba(150, 150, 150, 0.8);
  text-align: right;
  margin-top: 5px;
}

.typing-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  padding: 5px 0;
}

.typing-indicator span {
  width: 6px;
  height: 6px;
  background-color: #999;
  border-radius: 50%;
  display: inline-block;
  animation: typing 1.5s infinite ease-in-out;
}

.typing-indicator span:nth-child(1) {
  animation-delay: 0s;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-5px);
  }
}

/* 滚动条样式 */
.chat-messages::-webkit-scrollbar {
  width: 5px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #ddd;
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #ccc;
}

@media (max-width: 768px) {
  .message {
    max-width: 85%;
  }
}

.messages-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
  width: 100%;
}

/* 添加消息过渡动画 */
.message-fade-enter-active,
.message-fade-leave-active {
  transition: all 0.4s ease;
}

.message-fade-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

.message-fade-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}

/* 新的聊天输入区域样式 */
.new-chat-input-container {
  padding: 16px;
  background-color: white;
  border-top: 1px solid #f0f0f0;
}

.new-input-wrapper {
  position: relative;
  background-color: #f5f5f5;
  border-radius: 24px; /* 修改圆角程度，从8px增加到12px */
  padding: 4px;
  display: flex;
  align-items: center;
}

.new-chat-input {
  flex: 1;
  background: transparent;
  border: none;
  padding: 10px 12px;
  font-size: 14px;
  line-height: 1.5;
  resize: none;
  min-height: 20px;
  max-height: 120px;
  outline: none;
  color: #333;
}

.new-send-button {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #000;
  color: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: transform 0.2s ease, background-color 0.2s ease;
  flex-shrink: 0;
  margin-left: 8px;
}

.new-send-button:hover {
  background-color: #333;
  transform: scale(1.05);
}

.new-send-button:active {
  transform: scale(0.95);
}

/* 确保滚动条样式适用于新的输入框 */
.new-chat-input::-webkit-scrollbar {
  width: 4px;
}

.new-chat-input::-webkit-scrollbar-track {
  background: transparent;
}

.new-chat-input::-webkit-scrollbar-thumb {
  background: #ddd;
  border-radius: 4px;
}

.new-chat-input::-webkit-scrollbar-thumb:hover {
  background: #ccc;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .new-input-wrapper {
    padding: 3px;
  }
  
  .new-chat-input {
    padding: 8px 10px;
  }
  
  .new-send-button {
    width: 28px;
    height: 28px;
  }
}
</style>