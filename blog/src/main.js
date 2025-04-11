import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import Aura from '@primevue/themes/aura'
import PrimeVue from 'primevue/config';
import router from './Router/router.js'

const app = createApp(App);
app.use(PrimeVue, {
    theme: {
        preset: Aura,
    },
})
app.use(router)
app.mount('#app')
