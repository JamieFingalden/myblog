<script setup>
import { ref, onMounted } from 'vue';

import Cards from './Card.vue';

import bilibiliBlack from '../../assets/link/bilibili-black.png'
import bilibiliLine from '../../assets/link/bilibili-line.png'
import douyinImg from '../../assets/link/douyin.png'
import douyinClick from '../../assets/link/douyin-click.png'
import githubImg from '../../assets/link/github.png'
import githubClick from '../../assets/link/github-click.png'

import { blogApi } from '../../api/blog'
import { loginApi } from '../../api/Login'

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
// 博客数据改为响应式数据
const blogDatas = ref([]);

// 获取博客列表数据
const fetchBlogList = async () => {
    try {
        const response = await blogApi.getList();
        console.log('获取到的博客数据:', response.code); // 添加日志

        if (response.code === 200) {
            console.log('获取到的博客数据:', response.data); // 添加日志
            blogDatas.value = response.data.map(item => ({
                title: item.title,
                name: 'fingalden',
                content: item.content,
                img: '',
                id: item.id,
                excerpt: item.excerpt,
                status: item.status
            }));
            console.log('处理后的数据:', blogDatas.value); // 添加日志
        }
    } catch (error) {
        console.error('获取博客列表失败:', error);
        blogDatas.value = [];
    }
};

const validationLoadToken = async () => {
    console.log('验证Token...');
    try {
        const response = await loginApi.validationToken();
        console.log('令牌验证:', response.data); // 添加日志
        if (response.data == "right") {
            console.log('令牌验证成功');
        } else {
            console.log('令牌验证失败');
            localStorage.removeItem('Authorization');
        }
    } catch (error) {
        console.error('验证Token失败:', error);
    }
}

// 在组件挂载时获取数据
onMounted(() => {
    fetchBlogList();
    validationLoadToken();
});

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

// 新增博客表单相关状态
const showBlogForm = ref(false);
const isEditing = ref(false);
const formBlog = ref({
    title: '',
    content: '',
    excerpt: '',
    status: '草稿'
});

// 打开新增博客表单
const openAddBlogForm = () => {
    isEditing.value = false;
    formBlog.value = {
        title: '',
        content: '',
        excerpt: '',
        status: '草稿'
    };
    showBlogForm.value = true;
    document.body.style.overflow = 'hidden';
};

// 打开编辑博客表单
const openEditBlogForm = (blog) => {
    isEditing.value = true;
    formBlog.value = { ...blog };
    showBlogForm.value = true;
    document.body.style.overflow = 'hidden';
    // 如果是从博客详情页打开编辑，则关闭详情页
    if (showBlog.value) {
        showBlog.value = false;
    }
};

// 关闭博客表单
const closeBlogForm = () => {
    showBlogForm.value = false;
    document.body.style.overflow = 'auto';
};

// 保存博客（新增或更新）
const saveBlog = async () => {
    try {
        if (!formBlog.value.title || !formBlog.value.content) {
            alert('标题和内容不能为空');
            return;
        }

        if (isEditing.value) {
            // 更新博客
            const response = await blogApi.update(formBlog.value);
            if (response.code === 200) {
                alert('博客更新成功');
                fetchBlogList(); // 重新获取列表
                closeBlogForm();
            } else {
                alert('博客更新失败: ' + response.msg);
            }
        } else {
            // 新增博客
            const response = await blogApi.create(formBlog.value);
            if (response.code === 200) {
                alert('博客创建成功');
                fetchBlogList(); // 重新获取列表
                closeBlogForm();
            } else {
                alert('博客创建失败: ' + response.msg);
            }
        }
    } catch (error) {
        console.error('保存博客失败:', error);
        alert('操作失败，请稍后重试');
    }
};

// 删除博客
const deleteBlog = async (id) => {
    if (!confirm('确定要删除这篇博客吗？此操作不可恢复。')) {
        return;
    }

    try {
        const response = await blogApi.delete(id);
        if (response.code === 200) {
            alert('博客删除成功');
            fetchBlogList(); // 重新获取列表
            if (showBlog.value) {
                closeBlog(); // 如果正在查看详情，关闭详情
            }
        } else {
            alert('博客删除失败: ' + response.msg);
        }
    } catch (error) {
        console.error('删除博客失败:', error);
        alert('删除失败，请稍后重试');
    }
};

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
                    I am committed to continuously improving my technical skills and leveraging my strengths in my
                    future
                    career.
                </span>
            </div>
            <!-- <div class="link animate-slide-up">
                <div class="linkBox"><a href="https://space.bilibili.com/33594443"><img @mouseenter="getFocusBilibili"
                            @mouseleave="leaveBilibili" :src="bilibiliPng" alt="子非鱼的bilibili空间"></a></div>
                <div class="linkBox"><a
                        href="https://www.douyin.com/user/MS4wLjABAAAAiNEp_X84ebr084yDkpjSVed3NP3KtPLi2v1cS3Ent8k?from_tab_name=main"><img
                            @mouseenter="getFocusDouyin" @mouseleave="leaveDouyin" :src="douyinPng" alt="子非鱼douyin"></a>
                </div>
                <div class="linkBox"><a href="https://github.com/JamieFingalden"><img @mouseenter="getFocusgithub"
                            @mouseleave="leavegithub" :src="githubPng" alt=""></a></div>
            </div> -->
        </div>
        <div class="photo animate-fade-in-right">
            <img src="../../assets/photo/简历照.png" alt="">
        </div>
    </div>

    <div class="cards">
        <!-- 添加博客按钮 -->
        <div class="add-blog-btn" :class="{ 'centered-btn': blogDatas.length === 0 }" @click="openAddBlogForm">
            <span>+</span>
            <p>添加新博客</p>
        </div>

        <div class="card-container" v-for="(item, index) in blogDatas" :key="item.id" :style="cardAnimationDelay(index)"
            style="width: 350px; background-color: #ffffff;">
            <Cards :title="item.title" :name="item.name" :content="item.content" :img="item.img"
                @click="handleCardClick(item)" />
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
                <!-- 添加博客操作按钮 -->
                <div class="blog-actions">
                    <button class="edit-btn" @click="openEditBlogForm(selectedBlog)">编辑</button>
                    <button class="delete-btn" @click="deleteBlog(selectedBlog.id)">删除</button>
                </div>
            </div>
        </div>
    </Transition>

    <!-- 博客表单弹出层 -->
    <Transition name="blog-transition">
        <div v-if="showBlogForm" class="blog-overlay" @click.self="closeBlogForm">
            <div class="blog-form">
                <div class="blog-close" :class="season" @click="closeBlogForm">
                    <span>×</span>
                </div>
                <h2>{{ isEditing ? '编辑博客' : '创建新博客' }}</h2>

                <div class="form-group">
                    <label for="title">标题</label>
                    <input type="text" id="title" v-model="formBlog.title" placeholder="请输入博客标题">
                </div>

                <div class="form-group">
                    <label for="content">内容</label>
                    <textarea id="content" v-model="formBlog.content" placeholder="请输入博客内容" rows="8"></textarea>
                </div>

                <div class="form-group">
                    <label for="excerpt">摘要</label>
                    <textarea id="excerpt" v-model="formBlog.excerpt" placeholder="请输入博客摘要（可选）" rows="3"></textarea>
                </div>

                <div class="form-group">
                    <label for="status">状态</label>
                    <select id="status" v-model="formBlog.status">
                        <option value="草稿">草稿</option>
                        <option value="完成">完成</option>
                    </select>
                </div>

                <button class="save-btn" @click="saveBlog">保存</button>
            </div>
        </div>
    </Transition>
</template>

<style scoped>
/* 主要布局 */
.main {
    margin-top: 15px;
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
    transform: scale(1.03) rotate(1deg);
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
    transform: scale(1.1);
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
    height: 250px;
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
    /* 添加以下样式实现垂直水平居中 */
    display: flex;
    justify-content: center;
    align-items: center;
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

/* 博客操作按钮 */
.blog-actions {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    margin-top: 20px;
    padding-top: 15px;
    border-top: 1px solid #e2e8f0;
}

.blog-actions .edit-btn,
.blog-actions .delete-btn {
    padding: 8px 16px;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    font-size: 0.9rem;
    transition: all 0.2s ease;
}

.blog-actions .edit-btn {
    background-color: #3b82f6;
    color: white;
}

.blog-actions .edit-btn:hover {
    background-color: #2563eb;
}

.blog-actions .delete-btn {
    background-color: #ef4444;
    color: white;
}

.blog-actions .delete-btn:hover {
    background-color: #dc2626;
}

/* 季节装饰 */
.season-decoration {
    position: absolute;
    top: -18px;
    left: 24px;
    font-size: 26px;
    transform: rotate(-10deg);
    filter: drop-shadow(0 2px 6px rgba(0, 0, 0, 0.1));
    animation: float 4s ease-in-out infinite;
}

.season-decoration.spring {
    color: #88c773;
}

.season-decoration.summer {
    color: #ff7e67;
}

.season-decoration.autumn {
    color: #e6a147;
}

.season-decoration.winter {
    color: #77b5d9;
}

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

.blog-close.spring {
    background: rgba(136, 199, 115, 0.15);
}

.blog-close.summer {
    background: rgba(255, 126, 103, 0.15);
}

.blog-close.autumn {
    background: rgba(230, 161, 71, 0.15);
}

.blog-close.winter {
    background: rgba(119, 181, 217, 0.15);
}

.blog-close:hover {
    transform: scale(1.1);
}

.blog-close.spring:hover {
    background: rgba(136, 199, 115, 0.25);
}

.blog-close.summer:hover {
    background: rgba(255, 126, 103, 0.25);
}

.blog-close.autumn:hover {
    background: rgba(230, 161, 71, 0.25);
}

.blog-close.winter:hover {
    background: rgba(119, 181, 217, 0.25);
}

/* 动画 */
@keyframes cardAppear {
    0% {
        opacity: 0;
        transform: translateY(50px) scale(0.9);
    }

    60% {
        opacity: 1;
    }

    100% {
        opacity: 1;
        transform: translateY(0) scale(1);
    }
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

@keyframes fadeInRight {
    from {
        opacity: 0;
        transform: translateX(30px);
    }

    to {
        opacity: 1;
        transform: translateX(0);
    }
}

@keyframes slideUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

@keyframes float {

    0%,
    100% {
        transform: rotate(-10deg) translateY(0);
    }

    50% {
        transform: rotate(-5deg) translateY(-5px);
    }
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
.blog-body::-webkit-scrollbar {
    width: 5px;
}

.blog-body::-webkit-scrollbar-track {
    background: #f1f5f9;
    border-radius: 3px;
}

.blog-body::-webkit-scrollbar-thumb {
    background: #cbd5e1;
    border-radius: 3px;
}

.blog-body::-webkit-scrollbar-thumb:hover {
    background: #94a3b8;
}

/* 响应式设计 */
@media (max-width: 1100px) {
    .main {
        width: 90%;
        padding: 30px;
    }

    .welcome,
    .photo {
        width: 45%;
        margin: 0 2.5%;
    }

    .photo img {
        width: 100%;
    }
}

@media (max-width: 768px) {
    .main {
        flex-direction: column;
        height: auto;
        align-items: center;
    }

    .welcome,
    .photo {
        width: 90%;
        margin: 20px 0;
    }

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

/* 添加博客按钮 */
.add-blog-btn {
    width: 350px;
    height: 250px;
    background-color: #f8fafc;
    border: 2px dashed #cbd5e1;
    border-radius: 12px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    transition: all 0.3s ease;
    margin: 12.5px 12.5px 12.5px;
}

.add-blog-btn:hover {
    background-color: #f1f5f9;
    border-color: #94a3b8;
    transform: translateY(-5px);
}

.add-blog-btn span {
    font-size: 3rem;
    color: #64748b;
    margin-bottom: 10px;
}

.add-blog-btn p {
    color: #64748b;
    font-size: 1.1rem;
}

/* 移除卡片操作按钮样式 */

/* 博客表单样式 */
.blog-form {
    background: linear-gradient(to bottom right, #ffffff, #f8fafc);
    width: 50%;
    max-width: 700px;
    height: auto;
    max-height: 85vh;
    padding: 2.5rem;
    border-radius: 24px;
    position: relative;
    box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08), 0 1px 2px rgba(0, 0, 0, 0.04);
    overflow-y: auto;
}

.blog-form h2 {
    font-size: 1.8rem;
    font-weight: 600;
    color: #1e293b;
    margin: 0 0 1.5rem;
}

.form-group {
    margin-bottom: 1.5rem;
}

.form-group label {
    display: block;
    margin-bottom: 0.5rem;
    font-size: 1rem;
    color: #334155;
    font-weight: 500;
}

.form-group input,
.form-group textarea,
.form-group select {
    width: 100%;
    padding: 0.75rem;
    border: 1px solid #e2e8f0;
    border-radius: 8px;
    font-size: 1rem;
    color: #1e293b;
    background-color: #ffffff;
    transition: border-color 0.2s ease;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
    outline: none;
    border-color: #3b82f6;
    box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.save-btn {
    background-color: #10b981;
    color: white;
    border: none;
    border-radius: 8px;
    padding: 0.75rem 1.5rem;
    font-size: 1rem;
    font-weight: 500;
    cursor: pointer;
    transition: background-color 0.2s ease;
}

.save-btn:hover {
    background-color: #059669;
}

/* 滚动条样式 */
.blog-form::-webkit-scrollbar {
    width: 5px;
}

.blog-form::-webkit-scrollbar-track {
    background: #f1f5f9;
    border-radius: 3px;
}

.blog-form::-webkit-scrollbar-thumb {
    background: #cbd5e1;
    border-radius: 3px;
}

.blog-form::-webkit-scrollbar-thumb:hover {
    background: #94a3b8;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .blog-form {
        width: 90%;
        padding: 1.5rem;
    }
}
</style>
