<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const navigateToChat = () => {
    router.push('/chat');
};
</script>

<template>
    <div class="Navbox">
        <div class="logo"><img src="../assets/logo/white.png" alt="logo"></div>

        <div class="nav-links">
            <router-link to="/" active-class="active">
                <div class="logo nav-item">HOME</div>
            </router-link>
            <router-link to="/weather" active-class="active">
                <div class="logo nav-item">WEATHER</div>
            </router-link>
            <router-link to="/collection" active-class="active">
                <div class="logo nav-item">COLLECTION</div>
            </router-link>
            <div class="logo nav-item">RESUME</div>
        </div>

        <div class="talk" @click="navigateToChat"><span>Let's Talk 💬</span></div>
    </div>
</template>

<style scoped>
.Navbox {
    max-width: 75rem;
    min-width: 36.25rem;
    width: 100%;
    height: 5rem;
    border-radius: 0.9375rem;
    background: #ffffff;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: bold;
    padding: 0 1.25rem;
    box-sizing: border-box;
    position: relative;
    box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.05);
    transition: box-shadow 0.3s ease, transform 0.3s ease;
}

.Navbox:hover {
    box-shadow: 0 0.25rem 0.5rem rgba(0, 0, 0, 0.1);
    transform: translateY(-0.125rem);
}

.nav-links {
    display: flex;
    align-items: center;
    gap: 1.875rem;
    flex-grow: 1;
    justify-content: space-evenly;
    margin: 0 2rem;
}

.logo {
    cursor: pointer;
}

.nav-item {
    position: relative;
    padding: 0.5rem 0;
    transition: color 0.3s ease;
}

.nav-item:hover {
    color: #555;
}

.nav-item::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 50%;
    width: 0;
    height: 0.125rem;
    background-color: #000;
    transition: width 0.3s ease, left 0.3s ease;
}

.nav-item:hover::after {
    width: 100%;
    left: 0;
}

.active .nav-item {
    color: #000;
}

.active .nav-item::after {
    width: 100%;
    left: 0;
}

.logo img {
    padding-left: 0;
    padding-top: 0.3125rem;
    width: 7.5rem;
    transition: transform 0.3s ease;
}

.logo img:hover {
    transform: scale(1.05);
}

.talk {
    width: 9.375rem;
    height: 2.5rem;
    margin-right: 0;
    background-color: black;
    border-radius: 1.25rem;
    color: #ffffff;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    transition: all 0.3s ease;
    overflow: hidden;
    position: relative;
}

.talk::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
    transition: left 0.7s ease;
}

.talk:hover {
    background-color: #333;
    transform: translateY(-0.125rem);
    box-shadow: 0 0.25rem 0.5rem rgba(0, 0, 0, 0.2);
}

.talk:hover::before {
    left: 100%;
}

@media (max-width: 75rem) {
    .Navbox {
        width: 95%;
    }
}

@media (max-width: 62.5rem) {
    .nav-links {
        gap: 1.25rem;
    }

    .logo img {
        width: 6.25rem;
    }
}

@media (max-width: 50rem) {
    .nav-links {
        gap: 0.9375rem;
    }

    .talk {
        width: 7.5rem;
    }
}
</style>
