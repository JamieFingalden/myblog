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
const apiError = ref(null);

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
  const newId = Date.now().toString(); // 使用时间戳作为唯一ID
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

async function sendMessage(data) {
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
  const chatId = data.conversationId; // 获取当前对话的ID
  
  // 调用AI对话API
  loading.value = true;
  apiError.value = null;
  
  try {
    // 使用POST请求替代GET请求，并添加chatId
    const response = await fetch('http://localhost:8080/AIChat/chat', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      body: JSON.stringify({ 
        prompt: userQuestion,
        chatId: chatId  // 添加chatId到请求体
      })
    });
    
    if (!response.ok) {
      throw new Error(`API请求失败: ${response.status} ${response.statusText}`);
    }
    
    // 创建一个新的AI消息对象，标记为正在流式传输
    const aiMessageIndex = conversations.value[conversationIndex].messages.length;
    conversations.value[conversationIndex].messages.push({
      content: '',
      thinkContent: '',  // 初始化为空字符串而不是null，便于累积
      sender: 'ai',
      time: getCurrentTime(),
      isStreaming: true
    });
    
    // 使用流式处理响应
    const reader = response.body.getReader();
    const decoder = new TextDecoder('utf-8');
    let buffer = '';  // 用于累积接收到的数据
    let inThinkBlock = false;  // 标记是否在思考块内
    let mainContent = '';  // 主要内容
    let thinkContent = '';  // 思考内容
    
    // 处理流式响应
    while (true) {
      const { done, value } = await reader.read();
      if (done) break;
      
      // 解码接收到的数据块
      const chunk = decoder.decode(value, { stream: true });
      buffer += chunk;
      
      // 处理缓冲区中的数据
      let startThinkPos, endThinkPos;
      
      // 检查是否有新的思考块开始
      while ((startThinkPos = buffer.indexOf('<think>')) !== -1) {
        // 将思考块前的内容添加到主内容
        mainContent += buffer.substring(0, startThinkPos);
        buffer = buffer.substring(startThinkPos + 7);  // 移除 '<think>' 标签
        inThinkBlock = true;
      }
      
      // 检查是否有思考块结束
      if (inThinkBlock && (endThinkPos = buffer.indexOf('</think>')) !== -1) {
        // 将思考块内的内容添加到思考内容
        thinkContent += buffer.substring(0, endThinkPos);
        buffer = buffer.substring(endThinkPos + 8);  // 移除 '</think>' 标签
        inThinkBlock = false;
      } else if (inThinkBlock) {
        // 仍在思考块内，将所有内容添加到思考内容
        thinkContent += buffer;
        buffer = '';
      } else {
        // 不在思考块内，将所有内容添加到主内容
        mainContent += buffer;
        buffer = '';
      }
      
      // 更新AI回复，包括主内容和思考内容
      conversations.value[conversationIndex].messages[aiMessageIndex] = {
        content: mainContent,
        thinkContent: thinkContent,
        sender: 'ai',
        time: getCurrentTime(),
        isStreaming: true
      };
    }
    
    // 处理缓冲区中剩余的内容
    if (buffer) {
      if (inThinkBlock) {
        thinkContent += buffer;
      } else {
        mainContent += buffer;
      }
    }
    
    // 完成流式传输，更新最终内容并移除流式标记
    conversations.value[conversationIndex].messages[aiMessageIndex] = {
      content: mainContent,
      thinkContent: thinkContent,
      sender: 'ai',
      time: getCurrentTime(),
      isStreaming: false
    };
    
  } catch (error) {
    console.error('调用AI接口出错:', error);
    apiError.value = error.message;
    
    // 添加错误消息
    conversations.value[conversationIndex].messages.push({
      content: `抱歉，发生了一个错误: ${error.message}`,
      sender: 'ai',
      time: getCurrentTime(),
      isError: true
    });
  } finally {
    loading.value = false;
  }
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
        :loading="loading"
        :apiError="apiError"
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