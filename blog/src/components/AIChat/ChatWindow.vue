<script setup>
import { ref, onMounted, onUpdated, nextTick, watch, onUnmounted } from 'vue';

// 用于跟踪哪些思考内容被展开
const expandedThinks = ref({});
// 用于跟踪思考内容是否已完成
const thinkCompleted = ref({});
// 用于存储滚动定时器
const scrollIntervalRef = ref(null);

// 切换思考内容的展开/折叠状态
function toggleThink(index) {
  expandedThinks.value[index] = !expandedThinks.value[index];
}

// 格式化消息内容，移除<think>标签
function formatMessage(content) {
  if (!content) return '';
  
  // 移除<think>...</think>标签及其内容
  let formattedContent = content.replace(/<think>[\s\S]*?<\/think>/g, '');
  
  // 添加流式输出的光标（如果需要）
  if (props.conversation.messages.some(msg => msg.isStreaming)) {
    formattedContent += '<span class="streaming-cursor"></span>';
  }
  
  return formattedContent;
}

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

// 专门监听流式输出状态，确保在流式输出过程中持续滚动
watch(() => props.conversation.messages.some(msg => msg.isStreaming), (isStreaming) => {
  // 清除之前的定时器
  if (scrollIntervalRef.value) {
    clearInterval(scrollIntervalRef.value);
    scrollIntervalRef.value = null;
  }
  
  if (isStreaming) {
    // 立即滚动一次
    scrollToBottom(true);
    
    // 如果有消息正在流式输出，设置一个定时器定期滚动到底部
    scrollIntervalRef.value = setInterval(() => {
      if (props.conversation.messages.some(msg => msg.isStreaming)) {
        scrollToBottom(true);
      } else {
        clearInterval(scrollIntervalRef.value);
        scrollIntervalRef.value = null;
      }
    }, 100); // 每100毫秒滚动一次
  }
}, { immediate: true });

// 监听消息变化，处理思考部分的展开和收起
watch(() => props.conversation.messages, (newMessages, oldMessages) => {
  // 处理新增的消息
  if (newMessages.length > (oldMessages?.length || 0)) {
    const newIndex = newMessages.length - 1;
    const newMessage = newMessages[newIndex];
    
    // 如果是AI消息且正在流式传输，则默认展开思考部分
    if (newMessage.sender === 'ai' && newMessage.isStreaming) {
      expandedThinks.value[newIndex] = true;
      thinkCompleted.value[newIndex] = false;
    }
    
    // 新消息添加后滚动到底部
    scrollToBottom(true);
  }
  
  // 检查所有消息的思考内容变化
  newMessages.forEach((message, index) => {
    if (message.sender === 'ai' && message.isStreaming) {
      const oldMessage = oldMessages?.[index];
      
      // 如果思考内容存在且与上一次相同，但主内容增加了，说明思考已完成，开始输出主内容
      if (message.thinkContent && 
          oldMessage && 
          message.thinkContent === oldMessage.thinkContent && 
          message.content.length > oldMessage.content.length &&
          !thinkCompleted.value[index]) {
        // 标记思考已完成
        thinkCompleted.value[index] = true;
        // 收起思考部分
        nextTick(() => {
          expandedThinks.value[index] = false;
          scrollToBottom(true); // 收起思考部分后滚动到底部
        });
      }
      
      // 消息内容有更新时滚动到底部
      if (oldMessage && 
          (message.content.length > oldMessage.content.length || 
           message.thinkContent?.length > (oldMessage.thinkContent?.length || 0))) {
        scrollToBottom(true); // 直接调用，强制滚动
      }
    }
  });
}, { deep: true });

const emit = defineEmits(['send-message']);

// 响应式数据
const userInput = ref('');
const loading = ref(false);
const messagesContainer = ref(null);

// 方法
function scrollToBottom(force = false) {
  if (!messagesContainer.value) return;
  
  const container = messagesContainer.value;
  const isNearBottom = container.scrollHeight - container.scrollTop - container.clientHeight < 100;
  
  // 如果用户已经滚动到接近底部，或者强制滚动
  if (isNearBottom || force) {
    // 使用 requestAnimationFrame 确保在下一帧渲染前执行滚动
    requestAnimationFrame(() => {
      container.scrollTop = container.scrollHeight;
      
      // 双重保险：再次使用 setTimeout 确保滚动生效
      setTimeout(() => {
        container.scrollTop = container.scrollHeight;
      }, 10);
    });
  }
}

function sendMessage() {
  if (!userInput.value.trim()) return;
  
  emit('send-message', {
    content: userInput.value,
    conversationId: props.conversation.id
  });
  
  userInput.value = '';
  // 发送消息后立即滚动到底部
  scrollToBottom(true);
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
  scrollToBottom(true);
  autoResize();
});

onUpdated(() => {
  // 如果有消息正在流式输出，则滚动到底部
  if (props.conversation.messages.some(msg => msg.isStreaming)) {
    scrollToBottom(true);
  }
  autoResize();
});

// 组件卸载时清除定时器
onUnmounted(() => {
  if (scrollIntervalRef.value) {
    clearInterval(scrollIntervalRef.value);
    scrollIntervalRef.value = null;
  }
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
             :class="['message', message.sender === 'user' ? 'user-message' : 'ai-message', {'streaming': message.isStreaming}]">
          
          <!-- 显示AI思考过程 - 调整位置和样式 -->
          <div v-if="message.thinkContent && message.sender === 'ai'" class="think-container">
            <div class="think-header" @click="toggleThink(index)">
              <svg :class="['think-icon', {'rotated': expandedThinks[index]}]" viewBox="0 0 24 24" width="16" height="16">
                <path d="M7 10l5 5 5-5z" fill="currentColor"/>
              </svg>
              <span>{{ expandedThinks[index] ? '收起思考过程' : '查看思考过程' }}</span>
            </div>
            <div v-show="expandedThinks[index] || message.isStreaming" class="think-content">
              <p><span class="think-label">思考：</span>{{ message.thinkContent }}</p>
            </div>
          </div>
          
          <div class="message-content" v-html="formatMessage(message.content)">
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
  scroll-behavior: smooth; /* 添加平滑滚动效果 */
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

/* 流式输出相关样式 */
.streaming-cursor {
  display: inline-block;
  width: 2px;
  height: 16px;
  background-color: #333;
  margin-left: 2px;
  vertical-align: middle;
  animation: blink 0.8s infinite;
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0; }
}

.streaming {
  border-color: #ddd;
  box-shadow: 0 0 0 2px rgba(0, 0, 0, 0.05);
}

/* AI思考部分的样式 */
.think-container {
  margin-bottom: 10px;
  border-bottom: 1px dashed #e0e0e0;
  padding-bottom: 8px;
  text-align: left;
}

.think-header {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #666;
  font-size: 0.85rem;
  user-select: none;
}

.think-icon {
  margin-right: 5px;
  transition: transform 0.3s ease;
}

.think-icon.rotated {
  transform: rotate(180deg);
}

.think-content {
  margin-top: 8px;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 6px;
  font-size: 0.9rem;
  color: #555;
  line-height: 1.5;
  white-space: pre-wrap;
}
</style>

// 专门监听流式输出状态，确保在流式输出过程中持续滚动
watch(() => props.conversation.messages.some(msg => msg.isStreaming), (isStreaming) => {
  if (isStreaming) {
    // 如果有消息正在流式输出，设置一个定时器定期滚动到底部
    const scrollInterval = setInterval(() => {
      if (props.conversation.messages.some(msg => msg.isStreaming)) {
        scrollToBottom();
      } else {
        clearInterval(scrollInterval);
      }
    }, 100); // 每100毫秒滚动一次
    
    // 确保组件销毁时清除定时器
    onUnmounted(() => {
      clearInterval(scrollInterval);
    });
  }
}, { immediate: true });