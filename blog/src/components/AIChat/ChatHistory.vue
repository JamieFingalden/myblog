<script setup>
import { ref } from 'vue';

// 定义props和emit
const props = defineProps({
  conversations: {
    type: Array,
    default: () => []
  },
  activeConversationId: {
    type: String,
    default: ''
  }
});

const emit = defineEmits(['select-conversation', 'new-conversation']);

// 创建新对话
const createNewConversation = () => {
  emit('new-conversation');
};

// 选择对话
const selectConversation = (id) => {
  emit('select-conversation', id);
};
</script>

<template>
  <div class="chat-history">
    <div class="history-header">
      <h3>历史会话</h3>
      <button class="new-chat-btn" @click="createNewConversation">
        <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
          <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
        </svg>
        <span>新对话</span>
      </button>
    </div>
    
    <div class="conversations-list">
      <transition-group name="conversation-fade" tag="div">
        <div 
          v-for="conversation in conversations" 
          :key="conversation.id"
          :class="['conversation-item', { active: conversation.id === activeConversationId }]"
          @click="selectConversation(conversation.id)"
        >
          <div class="conversation-title">{{ conversation.title }}</div>
          <div class="conversation-date">{{ conversation.date }}</div>
        </div>
      </transition-group>
      
      <div v-if="conversations.length === 0" class="no-conversations">
        暂无历史会话
      </div>
    </div>
  </div>
</template>

<style scoped>
.chat-history {
  width: 250px;
  height: 100%;
  border-right: 1px solid #eee;
  display: flex;
  flex-direction: column;
  background-color: white;
}

.history-header {
  padding: 20px;
  border-bottom: 1px solid #eee;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.history-header h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #333;
  letter-spacing: 0.5px;
}

.new-chat-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 8px 12px;
  background-color: #f0f0f0;
  color: #333;
  border: 1px solid #ddd;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.2s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.new-chat-btn:hover {
  background-color: #e5e5e5;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.15);
}

.conversations-list {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
}

/* 添加过渡动画样式 */
.conversation-fade-enter-active,
.conversation-fade-leave-active {
  transition: all 0.3s ease;
}

.conversation-fade-enter-from,
.conversation-fade-leave-to {
  opacity: 0;
  transform: translateX(-10px);
}

.conversation-item {
  padding: 12px;
  border-radius: 6px;
  margin-bottom: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.conversation-item:hover {
  background-color: #f5f5f5;
}

.conversation-item.active {
  background-color: #f0f0f0;
  border-left: 3px solid #000;
}

.conversation-item::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 2px;
  background-color: #000;
  transition: width 0.3s ease;
}

.conversation-item:hover::after {
  width: 100%;
}

.conversation-item.active::after {
  width: 100%;
}

.conversation-title {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.conversation-date {
  font-size: 12px;
  color: #999;
}

.no-conversations {
  padding: 20px;
  text-align: center;
  color: #999;
  font-size: 14px;
}

/* 滚动条样式 */
.conversations-list::-webkit-scrollbar {
  width: 4px;
}

.conversations-list::-webkit-scrollbar-track {
  background: transparent;
}

.conversations-list::-webkit-scrollbar-thumb {
  background: #ddd;
  border-radius: 4px;
}

.conversations-list::-webkit-scrollbar-thumb:hover {
  background: #ccc;
}

@media (max-width: 768px) {
  .chat-history {
    width: 100%;
    height: auto;
    border-right: none;
    border-bottom: 1px solid #eee;
  }
}
</style>