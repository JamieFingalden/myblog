<script setup>
import { ref, onMounted } from 'vue';

import Cards from './Card.vue';

import bilibiliBlack from '../../assets/link/bilibili-black.png'
import bilibiliLine from '../../assets/link/bilibili-line.png'
import douyinImg from '../../assets/link/douyin.png'
import douyinClick from '../../assets/link/douyin-click.png'
import githubImg from '../../assets/link/github.png'
import githubClick from '../../assets/link/github-click.png'

const bilibiliPng = ref(bilibiliBlack)
const douyinPng = ref(douyinImg)
const githubPng = ref(githubImg)
const getFocusBilibili = () => {
    bilibiliPng.value = bilibiliLine
}
const leaveBilibili = () => {
    bilibiliPng.value = bilibiliBlack
}
const getFocusDouyin = () => {
    douyinPng.value = douyinClick
}
const leaveDouyin = () => {
    douyinPng.value = douyinImg
}
const getFocusgithub = () => {
    githubPng.value = githubClick
}
const leavegithub = () => {
    githubPng.value = githubImg
}

// 博客数据
const bolgDatas = [
    { title: 'first', name: 'fingalden', content: 'OK OK？', img: '' },
    { title: 'test1', name: 'fingalden', content: 'test1', img: '' },
    { title: 'test2', name: 'fingalden', content: 'test2', img: '' },
    { title: 'test3', name: 'fingalden', content: 'test3', img: '' },
]

// 博客详情状态
const showBlog = ref(false)
const selectedBlog = ref(null)

// 处理卡片点击事件
const handleCardClick = (blog) => {
    selectedBlog.value = blog
    showBlog.value = true
    document.body.style.overflow = 'hidden'
}

// 关闭博客详情
const closeBlog = () => {
    showBlog.value = false
    document.body.style.overflow = 'auto'
}

// 季节判断函数
const season = ref((() => {
    const month = new Date().getMonth() + 1
    if (month >= 3 && month <= 5) return 'spring'
    if (month >= 6 && month <= 8) return 'summer'
    if (month >= 9 && month <= 11) return 'autumn'
    return 'winter'
})());

// 卡片动画控制
const cardAnimationDelay = index => ({ 
  'animation-delay': `${0.2 + index * 0.15}s`,
  'animation-duration': '0.8s'
});

// 季节装饰映射
const seasonEmojis = {
  spring: '🌱 🌸',
  summer: '🌞 🍉',
  autumn: '🍁 🍂',
  winter: '❄️ ⛄'
};
</script>

<template>
    <div class="main">
        <div class="welcome">
            <div class="welcome-text animate-fade-in">
                <span style="font-size: 45px;">I 👋 am Fingalden<br>Welcome 🎉</span>
                <br>
                <span>
                    Hello, I am Jamie・Fingalden, a Software Technology scholar.
                    I am passionate about programming and have been involved in the development of several projects,
                    including a sales and inventory management system using the RuoYi framework.
                    I am committed to continuously improving my technical skills and leveraging my strengths in my future
                    career.
                </span>
            </div>
            <div class="link animate-slide-up">
                <div class="linkBox"><a href="https://space.bilibili.com/33594443"><img @mouseenter="getFocusBilibili"
                            @mouseleave="leaveBilibili" :src="bilibiliPng" alt="子非鱼的bilibili空间"></a></div>
                <div class="linkBox"><a
                        href="https://www.douyin.com/user/MS4wLjABAAAAiNEp_X84ebr084yDkpjSVed3NP3KtPLi2v1cS3Ent8k?from_tab_name=main"><img
                            @mouseenter="getFocusDouyin" @mouseleave="leaveDouyin" :src="douyinPng" alt="子非鱼douyin"></a>
                </div>
                <div class="linkBox"><a href="https://github.com/JamieFingalden"><img @mouseenter="getFocusgithub"
                            @mouseleave="leavegithub" :src="githubPng" alt=""></a></div>
            </div>
        </div>
        <div class="photo animate-fade-in-right">
            <img src="../../assets/photo/简历照.png" alt="">
        </div>
    </div>
    
    <div class="cards">
        <div class="card-container" 
             :style="cardAnimationDelay(index)"
             style="width: 350px; background-color: #ffffff;" 
             v-for="(item, index) in bolgDatas" 
             :key="index"
             @click="handleCardClick(item)">
            <Cards :title="item.title" :name="item.name" :content="item.content" :img="item.img" />
        </div>
    </div>

    <!-- 博客详情弹出层 -->
    <Transition name="blog-transition">
        <div v-if="showBlog" class="blog-overlay" @click.self="closeBlog">
            <div class="blog-content">
                <div class="season-decoration" :class="season">
                    {{ seasonEmojis[season] }}
                </div>
                <div class="blog-close" :class="season" @click="closeBlog">
                    <span>×</span>
                </div>
                <h2>{{ selectedBlog?.title }}</h2>
                <div class="blog-author">By {{ selectedBlog?.name }}</div>
                <div class="blog-body">
                    {{ selectedBlog?.content }}
                </div>
            </div>
        </div>
    </Transition>
</template>

<style scoped>
/* 主要布局 */
.main {
    margin-top: 50px;
    padding: 50px;
    width: 1000px;
    height: 450px;
    display: flex;
    justify-self: start;
    margin-left: auto;
    margin-right: auto;
}

.welcome {
    width: 400px;
    height: 100%;
    margin-right: 100px;
    position: relative;
}

.welcome-text {
    opacity: 0;
    animation: fadeIn 0.8s ease-out forwards;
}

.photo {
    width: 400px;
    height: 100%;
    margin-left: 100px;
    opacity: 0;
    animation: fadeInRight 1s ease-out 0.3s forwards;
}

.photo img {
    width: 350px;
    transition: transform 0.5s ease;
}

.photo:hover img {
    transform: scale(1.03) rotate(1deg);
}

/* 社交链接 */
.link {
    width: 120px;
    position: absolute;
    bottom: 3%;
    right: 0;
    display: flex;
    justify-self: start;
    opacity: 0;
    animation: slideUp 0.6s ease-out 0.8s forwards;
}

.linkBox {
    width: 35px;
    height: 35px;
    margin-left: 5px;
    border-radius: 50%;
    background: #ffffff;
    display: flex;
    justify-content: center;
    align-items: center;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.linkBox:hover {
    transform: translateY(-5px);
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.linkBox a {
    display: flex;
    justify-content: center;
    align-items: center;
}

/* 卡片区域 */
.cards {
    display: flex;
    width: 100%;
    max-width: 1200px;
    margin: 0 auto;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
    gap: 25px;
    padding-bottom: 50px;
    perspective: 1200px;
}

.card-container {
    cursor: pointer;
    opacity: 0;
    transform: translateY(30px) scale(0.95);
    animation: cardAppear 0.8s cubic-bezier(0.34, 1.56, 0.64, 1) forwards;
    transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    overflow: hidden;
    will-change: transform, box-shadow;
    transform-style: preserve-3d;
    backface-visibility: hidden;
    position: relative;
}

.card-container:hover {
    transform: translateY(-15px) scale(1.05) rotateX(5deg) rotateY(5deg);
    box-shadow: 0 20px 30px rgba(0, 0, 0, 0.12), 0 8px 20px rgba(0, 0, 0, 0.08);
    z-index: 10;
}

.card-container::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, 
                rgba(255, 255, 255, 0.3) 0%, 
                rgba(255, 255, 255, 0) 60%);
    opacity: 0;
    transition: opacity 0.5s ease;
    pointer-events: none;
    z-index: 2;
}

.card-container:hover::after {
    opacity: 1;
}

.card-container:active {
    transform: translateY(-5px) scale(0.98) rotateX(2deg) rotateY(2deg);
    box-shadow: 10px 10px 15px rgba(0, 0, 0, 0.1);
    transition: all 0.2s ease;
}

/* 博客详情 */
.blog-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100vh;
    background-color: rgba(247, 248, 250, 0.75);
    backdrop-filter: blur(8px);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.blog-content {
    background: linear-gradient(to bottom right, #ffffff, #f8fafc);
    width: 45%;
    max-width: 600px;
    height: 75vh;
    min-height: 500px;
    padding: 3rem;
    border-radius: 24px;
    position: relative;
    box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08), 0 1px 2px rgba(0, 0, 0, 0.04);
    overflow: visible;
    display: flex;
    flex-direction: column;
}

.blog-content h2 {
    font-size: 2rem;
    font-weight: 600;
    color: #1e293b;
    margin: 0 0 0.5rem;
}

.blog-author {
    font-size: 0.95rem;
    color: #64748b;
    margin-bottom: 2rem;
    padding-bottom: 1rem;
    border-bottom: 1px solid #e2e8f0;
}

.blog-body {
    color: #334155;
    font-size: 1.05rem;
    line-height: 1.75;
    flex: 1;
    overflow-y: auto;
}

/* 季节装饰 */
.season-decoration {
    position: absolute;
    top: -18px;
    left: 24px;
    font-size: 26px;
    transform: rotate(-10deg);
    filter: drop-shadow(0 2px 6px rgba(0,0,0,0.1));
    animation: float 4s ease-in-out infinite;
}

.season-decoration.spring { color: #88c773; }
.season-decoration.summer { color: #ff7e67; }
.season-decoration.autumn { color: #e6a147; }
.season-decoration.winter { color: #77b5d9; }

/* 关闭按钮 */
.blog-close {
    position: absolute;
    top: 1.25rem;
    right: 1.25rem;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    transition: all 0.2s ease;
}

.blog-close span {
    font-size: 24px;
    line-height: 24px;
    height: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: -2px;
}

.blog-close.spring { background: rgba(136, 199, 115, 0.15); }
.blog-close.summer { background: rgba(255, 126, 103, 0.15); }
.blog-close.autumn { background: rgba(230, 161, 71, 0.15); }
.blog-close.winter { background: rgba(119, 181, 217, 0.15); }

.blog-close:hover { transform: scale(1.1); }
.blog-close.spring:hover { background: rgba(136, 199, 115, 0.25); }
.blog-close.summer:hover { background: rgba(255, 126, 103, 0.25); }
.blog-close.autumn:hover { background: rgba(230, 161, 71, 0.25); }
.blog-close.winter:hover { background: rgba(119, 181, 217, 0.25); }

/* 动画 */
@keyframes cardAppear {
    0% { opacity: 0; transform: translateY(50px) scale(0.9); }
    60% { opacity: 1; }
    100% { opacity: 1; transform: translateY(0) scale(1); }
}

@keyframes fadeIn {
    from { opacity: 0; transform: translateY(20px); }
    to { opacity: 1; transform: translateY(0); }
}

@keyframes fadeInRight {
    from { opacity: 0; transform: translateX(30px); }
    to { opacity: 1; transform: translateX(0); }
}

@keyframes slideUp {
    from { opacity: 0; transform: translateY(20px); }
    to { opacity: 1; transform: translateY(0); }
}

@keyframes float {
    0%, 100% { transform: rotate(-10deg) translateY(0); }
    50% { transform: rotate(-5deg) translateY(-5px); }
}

/* 过渡动画 */
.blog-transition-enter-active,
.blog-transition-leave-active {
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.blog-transition-enter-from,
.blog-transition-leave-to {
    opacity: 0;
    transform: scale(0.95) translateY(10px);
}

/* 滚动条样式 */
.blog-body::-webkit-scrollbar { width: 5px; }
.blog-body::-webkit-scrollbar-track { background: #f1f5f9; border-radius: 3px; }
.blog-body::-webkit-scrollbar-thumb { background: #cbd5e1; border-radius: 3px; }
.blog-body::-webkit-scrollbar-thumb:hover { background: #94a3b8; }

/* 响应式设计 */
@media (max-width: 1100px) {
    .main {
        width: 90%;
        padding: 30px;
    }
    .welcome, .photo { width: 45%; margin: 0 2.5%; }
    .photo img { width: 100%; }
}

@media (max-width: 768px) {
    .main {
        flex-direction: column;
        height: auto;
        align-items: center;
    }
    .welcome, .photo { width: 90%; margin: 20px 0; }
    .link {
        position: relative;
        margin-top: 30px;
        right: auto;
        bottom: auto;
    }
}

/* 强制卡片使用白色背景 */
:deep(.p-card) {
    background-color: white !important;
    color: #333 !important;
}

:deep(.p-card-body),
:deep(.p-card-content),
:deep(.p-card-header) {
    background-color: white !important;
}

:deep(.p-card-title) {
    color: #333 !important;
}

:deep(.p-card-subtitle) {
    color: #666 !important;
}
</style>
