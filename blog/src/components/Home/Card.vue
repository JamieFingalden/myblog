<script setup>
defineProps({
    title: {
        type: String,
        required: true
    },
    name: {
        type: String,
        required: true
    },
    content: {
        type: String,
        required: true
    },
    img: {
        type: String,
        default: ''
    }
});
</script>

<template>
    <div class="card">
        <h3>{{ title }}</h3>
        <p class="author">{{ name }}</p>
        <p class="content">{{ content }}</p>
        <!-- <img v-if="img" :src="img" alt="博客图片"> -->
    </div>
</template>

<style scoped>
.card {
    padding: 20px;
    margin: 0 auto;
    height: 100%;
    border-radius: 12px;
    transition: all 0.3s ease;
    border: none;
    outline: none;
    /* 添加弹性布局实现垂直水平居中 */
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    text-align: center;
}

h3 {
    margin: 0 0 10px 0;
    font-size: 1.5rem;
    color: #1e293b;
}

.author {
    color: #64748b;
    font-size: 0.9rem;
    margin-bottom: 15px;
}

.content {
    color: #334155;
    line-height: 1.6;
    margin: 0;
    border: none;
    /* 确保内容区域没有边框 */
}

img {
    margin-top: 15px;
    max-width: 100%;
    border-radius: 8px;
}
</style>