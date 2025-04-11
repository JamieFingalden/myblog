<script setup lang="ts">

import Card from 'primevue/card';
import { Avatar } from 'primevue';

// Define props
const props = defineProps<{
  title: string;
  name: string;
  content: string;
  img: string;
}>();

</script>

<template>
<Card style="width: 25rem; overflow: hidden; background-color: white !important;" class="white-card">
    <template #header>
        <div style="padding-top: 30px;">
            <Avatar :image="props.img || 'http://localhost:5173/src/assets/photo/%E7%AE%80%E5%8E%86%E7%85%A7.png'" size="large" shape="circle" ></Avatar>
        </div>
    </template>
    <template #title>{{ props.title }}</template>
    <template #subtitle>{{ props.name }}</template>
    <template #content>
        <p class="m-0">
            {{ props.content }}
        </p>
    </template>
</Card>
</template>

<style scoped>
img {
    margin-top: 30px;
}
Avatar{
    margin-top: 30px;
}

/* 强制使用白色背景 */
:deep(.white-card) {
    background-color: white !important;
    color: #333 !important;
}

:deep(.white-card .p-card-body) {
    background-color: white !important;
}

:deep(.white-card .p-card-content) {
    background-color: white !important;
    color: #333 !important;
}

:deep(.white-card .p-card-header) {
    background-color: white !important;
}

:deep(.white-card .p-card-title) {
    color: #333 !important;
}

:deep(.white-card .p-card-subtitle) {
    color: #666 !important;
}

/* 覆盖任何可能的暗色主题样式 */
:deep(.p-component), :deep(.p-card) {
    background-color: white !important;
    color: #333 !important;
}
</style>