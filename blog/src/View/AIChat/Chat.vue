<script setup>
import { ref, computed } from 'vue';
import ChatHistory from '../../components/AIChat/ChatHistory.vue';
import ChatWindow from '../../components/AIChat/ChatWindow.vue';

// 响应式数据
const conversations = ref([
  {
    id: '1',
    title: '关于Vue3的问题',
    date: '2023-05-15',
    messages: [
      {
        content: '你好！我是AI助手，有什么可以帮助你的吗？',
        sender: 'ai',
        time: getCurrentTime()
      }
    ]
  },
  {
    id: '2',
    title: '如何学习前端开发',
    date: '2023-05-14',
    messages: [
      {
        content: '你好！我是AI助手，有什么可以帮助你的吗？',
        sender: 'ai',
        time: getCurrentTime()
      }
    ]
  }
]);

const activeConversationId = ref('1');
const loading = ref(false);
const showSidebar = ref(true);

// 计算属性
const activeConversation = computed(() => {
  return conversations.value.find(conv => conv.id === activeConversationId.value) || {
    id: '',
    title: '',
    messages: []
  };
});

// 方法
function getCurrentTime() {
  const now = new Date();
  return `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}`;
}

function selectConversation(id) {
  activeConversationId.value = id;
}

function createNewConversation() {
  const newId = Date.now().toString();
  const newConversation = {
    id: newId,
    title: '新对话',
    date: new Date().toLocaleDateString(),
    messages: [
      {
        content: '你好！我是AI助手，有什么可以帮助你的吗？',
        sender: 'ai',
        time: getCurrentTime()
      }
    ]
  };
  
  conversations.value.unshift(newConversation);
  activeConversationId.value = newId;
}

function sendMessage(data) {
  if (!data.content.trim()) return;
  
  const conversationIndex = conversations.value.findIndex(conv => conv.id === data.conversationId);
  if (conversationIndex === -1) return;
  
  // 添加用户消息
  conversations.value[conversationIndex].messages.push({
    content: data.content,
    sender: 'user',
    time: getCurrentTime()
  });
  
  // 更新对话标题（如果是第一条消息）
  if (conversations.value[conversationIndex].messages.length === 2) {
    conversations.value[conversationIndex].title = data.content.substring(0, 20) + (data.content.length > 20 ? '...' : '');
  }
  
  const userQuestion = data.content;
  
  // 模拟AI响应
  loading.value = true;
  setTimeout(() => {
    loading.value = false;
    conversations.value[conversationIndex].messages.push({
      content: `这是对"${userQuestion}"的AI回复`,
      sender: 'ai',
      time: getCurrentTime()
    });
  }, 1000);
}

function toggleSidebar() {
  showSidebar.value = !showSidebar.value;
}
</script>

<template>
  <div class="chat-container card">
    <div class="mobile-header" v-if="!showSidebar">
      <button class="toggle-sidebar-btn" @click="toggleSidebar">
        <svg viewBox="0 0 24 24" width="24" height="24" fill="currentColor">
          <path d="M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z"/>
        </svg>
      </button>
    </div>
    
    <div class="chat-layout">
      <!-- 历史会话侧边栏 -->
      <ChatHistory 
        v-if="showSidebar"
        :conversations="conversations"
        :activeConversationId="activeConversationId"
        @select-conversation="selectConversation"
        @new-conversation="createNewConversation"
      />
      
      <!-- 对话窗口 -->
      <ChatWindow 
        :conversation="activeConversation"
        @send-message="sendMessage"
      />
    </div>
  </div>
</template>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  width: 98%;
  max-width: 1200px;
  height: 80vh;
  margin: 2rem auto;
  background-color: white;
  font-family: 'Arial', sans-serif;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  position: relative;
}

.chat-layout {
  display: flex;
  height: 100%;
}

.mobile-header {
  display: none;
  padding: 10px;
  background-color: white;
  border-bottom: 1px solid #eee;
}

.toggle-sidebar-btn {
  background: none;
  border: none;
  color: #333;
  cursor: pointer;
  padding: 5px;
}

@media (max-width: 768px) {
  .chat-container {
    width: 95%;
    height: 85vh;
    margin: 1rem auto;
  }
  
  .mobile-header {
    display: block;
  }
  
  .chat-layout {
    flex-direction: column;
  }
}
</style>