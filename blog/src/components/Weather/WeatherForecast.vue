<template>
  <div class="weather-container" :class="weatherBackground">
    <!-- Frosted glass container -->
    <div class="weather-card">
      <!-- Header section with location and search -->
      <div class="header-section">
        <div class="location">
          <h1>{{ weather.city }}</h1>
          <p>{{ currentDate }}</p>
        </div>
        <div class="search-box">
          <input v-model="searchQuery" @keyup.enter="searchLocation" type="text" placeholder="搜索城市..." />
        </div>
      </div>

      <!-- Main content grid -->
      <div class="main-content">
        <!-- Left column: Current weather -->
        <div class="current-weather-section">
          <div class="current-weather">
            <div class="weather-icon" v-html="getWeatherIcon(weather.current.condition)"></div>
            <div class="temp-container">
              <h2>{{ Math.round(weather.current.temp) }}°</h2>
              <p>{{ weather.current.condition }}</p>
            </div>
          </div>

          <div class="weather-details">
            <div class="detail-item">
              <div class="detail-icon"><thermometer-icon size="18" /></div>
              <span>体感温度 {{ Math.round(weather.current.feelsLike) }}°</span>
            </div>
            <div class="detail-item">
              <div class="detail-icon"><droplets-icon size="18" /></div>
              <span>湿度 {{ weather.current.humidity }}%</span>
            </div>
            <div class="detail-item">
              <div class="detail-icon"><wind-icon size="18" /></div>
              <span>风速 {{ weather.current.wind }} km/h</span>
            </div>
            <div class="detail-item">
              <div class="detail-icon"><sun-icon size="18" /></div>
              <span>紫外线 {{ weather.current.uv }}</span>
            </div>
            <div class="detail-item">
              <div class="detail-icon"><eye-icon size="18" /></div>
              <span>能见度 {{ weather.current.visibility }} km</span>
            </div>
            <div class="detail-item">
              <div class="detail-icon"><gauge-icon size="18" /></div>
              <span>气压 {{ weather.current.pressure }} hPa</span>
            </div>
          </div>
        </div>

        <!-- Right column: Forecasts -->
        <div class="forecast-section">
          <!-- Hourly forecast (6 hours) -->
          <div class="hourly-section">
            <h3>6小时预报</h3>
            <div class="hourly-forecast">
              <div v-for="(hour, index) in weather.hourly.slice(0, 6)" :key="index" class="hourly-item">
                <p>{{ hour.time }}</p>
                <div class="hourly-icon" v-html="getWeatherIcon(hour.condition, 'small')"></div>
                <p class="temp">{{ Math.round(hour.temp) }}°</p>
                <p class="precip">{{ hour.precipitation }}%</p>
              </div>
            </div>
          </div>

          <!-- Daily forecast -->
          <div class="daily-forecast">
            <h3>3天预报</h3>
            <div class="forecast-list">
              <div v-for="(day, index) in weather.forecast.slice(0, 3)" :key="index" class="forecast-day">
                <div class="day-info">
                  <p class="day-name">{{ day.day }}</p>
                  <p class="day-date">{{ day.date }}</p>
                </div>
                <div class="forecast-icon" v-html="getWeatherIcon(day.condition, 'small')"></div>
                <div class="condition">{{ day.condition }}</div>
                <div class="temp-range">
                  <span class="high">{{ Math.round(day.high) }}°</span>
                  <div class="temp-bar">
                    <div class="temp-fill" :style="{ width: getTempBarWidth(day.low, day.high) + '%' }"></div>
                  </div>
                  <span class="low">{{ Math.round(day.low) }}°</span>
                </div>
                <div class="precip-chance">
                  <div class="precip-icon"><droplets-icon size="14" /></div>
                  <span>{{ day.precipitation }}%</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Loading overlay -->
    <div v-if="loading" class="loading-overlay">
      <loader-icon size="50" class="animate-spin" />
    </div>

    <!-- Error message -->
    <div v-if="error" class="error-message">
      <alert-circle-icon size="24" />
      <p>{{ error }}</p>
      <button @click="error = null">关闭</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import {
  Search, Loader, Thermometer, Droplets, Wind,
  Sun, Eye, Gauge, AlertCircle
} from 'lucide-vue-next';

// State
const loading = ref(false);
const error = ref(null);
const searchQuery = ref('');
const apiKey = ref('8d273afa95d9c5b453ec54421f43072e'); // 高德地图 API key
const weatherCondition = ref('clear');
const timeOfDay = ref('day');
const season = ref('summer');
const adcode = ref('110000'); // 默认北京

// Weather data with default values
const weather = ref({
  city: '北京市',
  current: {
    temp: 0,
    feelsLike: 0,
    humidity: 0,
    wind: 0,
    condition: '晴',
    uv: '中等',
    visibility: 0,
    pressure: 0,
    aqi: 0,
    sunrise: '00:00',
    sunset: '00:00'
  },
  forecast: [],
  hourly: []
});

// Computed properties
const currentDate = computed(() => {
  const options = { weekday: 'long', month: 'long', day: 'numeric' };
  return new Date().toLocaleDateString('zh-CN', options);
});

const weatherBackground = computed(() => {
  // Dynamic background based on weather condition, time of day and season
  return `bg-${weatherCondition.value}-${timeOfDay.value}-${season.value}`;
});

// Methods
const getWeatherIcon = (condition, size = 'large') => {
  if (!condition) return getSunnyIcon(size);

  const conditionLower = condition.toLowerCase();

  if (conditionLower.includes('晴') && !conditionLower.includes('多云')) {
    return getSunnyIcon(size);
  } else if (conditionLower.includes('多云') && !conditionLower.includes('雨')) {
    return getPartlyCloudyIcon(size);
  } else if (conditionLower.includes('阴')) {
    return getCloudyIcon(size);
  } else if (conditionLower.includes('小雨')) {
    return getLightRainIcon(size);
  } else if (conditionLower.includes('中雨') || conditionLower.includes('大雨')) {
    return getHeavyRainIcon(size);
  } else if (conditionLower.includes('雷')) {
    return getThunderstormIcon(size);
  } else if (conditionLower.includes('雪')) {
    return getSnowIcon(size);
  } else if (conditionLower.includes('雾') || conditionLower.includes('霾')) {
    return getFogIcon(size);
  } else if (conditionLower.includes('雨')) {
    return getRainIcon(size);
  } else {
    return getCloudyIcon(size);
  }
};

// Apple-style weather icons
const getSunnyIcon = (size) => {
  const scale = size === 'small' ? 0.6 : 1;
  const width = 100 * scale;
  const height = 100 * scale;

  return `
      <svg width="${width}" height="${height}" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
        <circle cx="50" cy="50" r="25" fill="#FFD600" />
        <g filter="url(#filter0_f)">
          <circle cx="50" cy="50" r="20" fill="#FF9D00" />
        </g>
        <g filter="url(#filter1_f)">
          <circle cx="50" cy="50" r="30" fill="#FFEB3B" fill-opacity="0.3" />
        </g>
        <path d="M50 15V10" stroke="#FFD600" stroke-width="3" stroke-linecap="round"/>
        <path d="M50 90V85" stroke="#FFD600" stroke-width="3" stroke-linecap="round"/>
        <path d="M85 50H90" stroke="#FFD600" stroke-width="3" stroke-linecap="round"/>
        <path d="M10 50H15" stroke="#FFD600" stroke-width="3" stroke-linecap="round"/>
        <path d="M73.5355 26.4645L77.0711 22.9289" stroke="#FFD600" stroke-width="3" stroke-linecap="round"/>
        <path d="M22.9289 77.0711L26.4645 73.5355" stroke="#FFD600" stroke-width="3" stroke-linecap="round"/>
        <path d="M73.5355 73.5355L77.0711 77.0711" stroke="#FFD600" stroke-width="3" stroke-linecap="round"/>
        <path d="M22.9289 22.9289L26.4645 26.4645" stroke="#FFD600" stroke-width="3" stroke-linecap="round"/>
        <defs>
          <filter id="filter0_f" x="20" y="20" width="60" height="60" filterUnits="userSpaceOnUse" color-interpolation-filters="sRGB">
            <feFlood flood-opacity="0" result="BackgroundImageFix"/>
            <feBlend mode="normal" in="SourceGraphic" in2="BackgroundImageFix" result="shape"/>
            <feGaussianBlur stdDeviation="5" result="effect1_foregroundBlur"/>
          </filter>
          <filter id="filter1_f" x="0" y="0" width="100" height="100" filterUnits="userSpaceOnUse" color-interpolation-filters="sRGB">
            <feFlood flood-opacity="0" result="BackgroundImageFix"/>
            <feBlend mode="normal" in="SourceGraphic" in2="BackgroundImageFix" result="shape"/>
            <feGaussianBlur stdDeviation="10" result="effect1_foregroundBlur"/>
          </filter>
        </defs>
      </svg>
    `;
};

const getPartlyCloudyIcon = (size) => {
  const scale = size === 'small' ? 0.6 : 1;
  const width = 100 * scale;
  const height = 100 * scale;

  return `
      <svg width="${width}" height="${height}" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
        <circle cx="35" cy="40" r="20" fill="#FFD600" />
        <g filter="url(#filter0_f)">
          <circle cx="35" cy="40" r="15" fill="#FF9D00" />
        </g>
        <path fill-rule="evenodd" clip-rule="evenodd" d="M70 75C77.732 75 84 68.732 84 61C84 53.268 77.732 47 70 47C69.2958 47 68.6056 47.0598 67.9333 47.1757C65.8295 39.4636 58.7259 34 50.5 34C40.835 34 33 41.835 33 51.5C33 52.0468 33.0233 52.5883 33.0688 53.1235C31.7308 52.3989 30.1638 52 28.5 52C22.1487 52 17 57.1487 17 63.5C17 69.8513 22.1487 75 28.5 75H70Z" fill="white"/>
        <defs>
          <filter id="filter0_f" x="10" y="15" width="50" height="50" filterUnits="userSpaceOnUse" color-interpolation-filters="sRGB">
            <feFlood flood-opacity="0" result="BackgroundImageFix"/>
            <feBlend mode="normal" in="SourceGraphic" in2="BackgroundImageFix" result="shape"/>
            <feGaussianBlur stdDeviation="5" result="effect1_foregroundBlur"/>
          </filter>
        </defs>
      </svg>
    `;
};

const getCloudyIcon = (size) => {
  const scale = size === 'small' ? 0.6 : 1;
  const width = 100 * scale;
  const height = 100 * scale;

  return `
      <svg width="${width}" height="${height}" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path fill-rule="evenodd" clip-rule="evenodd" d="M75 70C84.3888 70 92 62.3888 92 53C92 43.6112 84.3888 36 75 36C74.1613 36 73.3369 36.0744 72.5296 36.2185C70.0334 27.1314 61.6667 20.5 51.75 20.5C40.1503 20.5 30.75 29.9003 30.75 41.5C30.75 42.1462 30.7791 42.7854 30.836 43.4158C29.2635 42.5448 27.4775 42.0625 25.5938 42.0625C18.1856 42.0625 12.1875 48.0606 12.1875 55.4688C12.1875 62.8769 18.1856 68.875 25.5938 68.875H75V70Z" fill="#E2E8F0"/>
        <path fill-rule="evenodd" clip-rule="evenodd" d="M70 80C77.732 80 84 73.732 84 66C84 58.268 77.732 52 70 52C69.2958 52 68.6056 52.0598 67.9333 52.1757C65.8295 44.4636 58.7259 39 50.5 39C40.835 39 33 46.835 33 56.5C33 57.0468 33.0233 57.5883 33.0688 58.1235C31.7308 57.3989 30.1638 57 28.5 57C22.1487 57 17 62.1487 17 68.5C17 74.8513 22.1487 80 28.5 80H70Z" fill="white"/>
      </svg>
    `;
};

const getRainIcon = (size) => {
  const scale = size === 'small' ? 0.6 : 1;
  const width = 100 * scale;
  const height = 100 * scale;

  return `
      <svg width="${width}" height="${height}" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path fill-rule="evenodd" clip-rule="evenodd" d="M70 55C77.732 55 84 48.732 84 41C84 33.268 77.732 27 70 27C69.2958 27 68.6056 27.0598 67.9333 27.1757C65.8295 19.4636 58.7259 14 50.5 14C40.835 14 33 21.835 33 31.5C33 32.0468 33.0233 32.5883 33.0688 33.1235C31.7308 32.3989 30.1638 32 28.5 32C22.1487 32 17 37.1487 17 43.5C17 49.8513 22.1487 55 28.5 55H70Z" fill="#E2E8F0"/>
        <path d="M30 65L25 75" stroke="#60A5FA" stroke-width="3" stroke-linecap="round"/>
        <path d="M45 65L40 75" stroke="#60A5FA" stroke-width="3" stroke-linecap="round"/>
        <path d="M60 65L55 75" stroke="#60A5FA" stroke-width="3" stroke-linecap="round"/>
        <path d="M75 65L70 75" stroke="#60A5FA" stroke-width="3" stroke-linecap="round"/>
        <path d="M37.5 75L32.5 85" stroke="#60A5FA" stroke-width="3" stroke-linecap="round"/>
        <path d="M52.5 75L47.5 85" stroke="#60A5FA" stroke-width="3" stroke-linecap="round"/>
        <path d="M67.5 75L62.5 85" stroke="#60A5FA" stroke-width="3" stroke-linecap="round"/>
      </svg>
    `;
};

const getLightRainIcon = (size) => {
  const scale = size === 'small' ? 0.6 : 1;
  const width = 100 * scale;
  const height = 100 * scale;

  return `
      <svg width="${width}" height="${height}" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path fill-rule="evenodd" clip-rule="evenodd" d="M70 55C77.732 55 84 48.732 84 41C84 33.268 77.732 27 70 27C69.2958 27 68.6056 27.0598 67.9333 27.1757C65.8295 19.4636 58.7259 14 50.5 14C40.835 14 33 21.835 33 31.5C33 32.0468 33.0233 32.5883 33.0688 33.1235C31.7308 32.3989 30.1638 32 28.5 32C22.1487 32 17 37.1487 17 43.5C17 49.8513 22.1487 55 28.5 55H70Z" fill="white"/>
        <path d="M37 65L35 70" stroke="#60A5FA" stroke-width="2" stroke-linecap="round"/>
        <path d="M47 65L45 70" stroke="#60A5FA" stroke-width="2" stroke-linecap="round"/>
        <path d="M57 65L55 70" stroke="#60A5FA" stroke-width="2" stroke-linecap="round"/>
        <path d="M67 65L65 70" stroke="#60A5FA" stroke-width="2" stroke-linecap="round"/>
        <path d="M42 75L40 80" stroke="#60A5FA" stroke-width="2" stroke-linecap="round"/>
        <path d="M52 75L50 80" stroke="#60A5FA" stroke-width="2" stroke-linecap="round"/>
        <path d="M62 75L60 80" stroke="#60A5FA" stroke-width="2" stroke-linecap="round"/>
      </svg>
    `;
};

const getHeavyRainIcon = (size) => {
  const scale = size === 'small' ? 0.6 : 1;
  const width = 100 * scale;
  const height = 100 * scale;

  return `
      <svg width="${width}" height="${height}" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path fill-rule="evenodd" clip-rule="evenodd" d="M70 50C77.732 50 84 43.732 84 36C84 28.268 77.732 22 70 22C69.2958 22 68.6056 22.0598 67.9333 22.1757C65.8295 14.4636 58.7259 9 50.5 9C40.835 9 33 16.835 33 26.5C33 27.0468 33.0233 27.5883 33.0688 28.1235C31.7308 27.3989 30.1638 27 28.5 27C22.1487 27 17 32.1487 17 38.5C17 44.8513 22.1487 50 28.5 50H70Z" fill="#94A3B8"/>
        <path d="M30 55L20 75" stroke="#3B82F6" stroke-width="3" stroke-linecap="round"/>
        <path d="M45 55L35 75" stroke="#3B82F6" stroke-width="3" stroke-linecap="round"/>
        <path d="M60 55L50 75" stroke="#3B82F6" stroke-width="3" stroke-linecap="round"/>
        <path d="M75 55L65 75" stroke="#3B82F6" stroke-width="3" stroke-linecap="round"/>
        <path d="M37.5 65L27.5 85" stroke="#3B82F6" stroke-width="3" stroke-linecap="round"/>
        <path d="M52.5 65L42.5 85" stroke="#3B82F6" stroke-width="3" stroke-linecap="round"/>
        <path d="M67.5 65L57.5 85" stroke="#3B82F6" stroke-width="3" stroke-linecap="round"/>
      </svg>
    `;
};

const getThunderstormIcon = (size) => {
  const scale = size === 'small' ? 0.6 : 1;
  const width = 100 * scale;
  const height = 100 * scale;

  return `
      <svg width="${width}" height="${height}" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path fill-rule="evenodd" clip-rule="evenodd" d="M70 45C77.732 45 84 38.732 84 31C84 23.268 77.732 17 70 17C69.2958 17 68.6056 17.0598 67.9333 17.1757C65.8295 9.46356 58.7259 4 50.5 4C40.835 4 33 11.835 33 21.5C33 22.0468 33.0233 22.5883 33.0688 23.1235C31.7308 22.3989 30.1638 22 28.5 22C22.1487 22 17 27.1487 17 33.5C17 39.8513 22.1487 45 28.5 45H70Z" fill="#94A3B8"/>
        <path d="M45 50L35 65H50L40 85" stroke="#FACC15" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/>
        <path d="M65 50L55 65H70L60 85" stroke="#FACC15" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/>
        <path d="M30 55L25 65" stroke="#60A5FA" stroke-width="2" stroke-linecap="round"/>
        <path d="M75 55L70 65" stroke="#60A5FA" stroke-width="2" stroke-linecap="round"/>
      </svg>
    `;
};

const getSnowIcon = (size) => {
  const scale = size === 'small' ? 0.6 : 1;
  const width = 100 * scale;
  const height = 100 * scale;

  return `
      <svg width="${width}" height="${height}" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path fill-rule="evenodd" clip-rule="evenodd" d="M70 50C77.732 50 84 43.732 84 36C84 28.268 77.732 22 70 22C69.2958 22 68.6056 22.0598 67.9333 22.1757C65.8295 14.4636 58.7259 9 50.5 9C40.835 9 33 16.835 33 26.5C33 27.0468 33.0233 27.5883 33.0688 28.1235C31.7308 27.3989 30.1638 27 28.5 27C22.1487 27 17 32.1487 17 38.5C17 44.8513 22.1487 50 28.5 50H70Z" fill="#E2E8F0"/>
        <circle cx="30" cy="65" r="3" fill="white"/>
        <circle cx="45" cy="65" r="3" fill="white"/>
        <circle cx="60" cy="65" r="3" fill="white"/>
        <circle cx="75" cy="65" r="3" fill="white"/>
        <circle cx="37.5" cy="75" r="3" fill="white"/>
        <circle cx="52.5" cy="75" r="3" fill="white"/>
        <circle cx="67.5" cy="75" r="3" fill="white"/>
        <circle cx="30" cy="85" r="3" fill="white"/>
        <circle cx="45" cy="85" r="3" fill="white"/>
        <circle cx="60" cy="85" r="3" fill="white"/>
        <circle cx="75" cy="85" r="3" fill="white"/>
      </svg>
    `;
};

const getFogIcon = (size) => {
  const scale = size === 'small' ? 0.6 : 1;
  const width = 100 * scale;
  const height = 100 * scale;

  return `
      <svg width="${width}" height="${height}" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path fill-rule="evenodd" clip-rule="evenodd" d="M70 40C77.732 40 84 33.732 84 26C84 18.268 77.732 12 70 12C69.2958 12 68.6056 12.0598 67.9333 12.1757C65.8295 4.46356 58.7259 -1 50.5 -1C40.835 -1 33 6.83502 33 16.5C33 17.0468 33.0233 17.5883 33.0688 18.1235C31.7308 17.3989 30.1638 17 28.5 17C22.1487 17 17 22.1487 17 28.5C17 34.8513 22.1487 40 28.5 40H70Z" fill="#E2E8F0"/>
        <path d="M20 50H80" stroke="#CBD5E1" stroke-width="3" stroke-linecap="round"/>
        <path d="M25 60H75" stroke="#CBD5E1" stroke-width="3" stroke-linecap="round"/>
        <path d="M30 70H70" stroke="#CBD5E1" stroke-width="3" stroke-linecap="round"/>
        <path d="M35 80H65" stroke="#CBD5E1" stroke-width="3" stroke-linecap="round"/>
      </svg>
    `;
};

const getTempBarWidth = (low, high) => {
  // Calculate width percentage for temperature range bar
  const range = high - low;
  return range > 0 ? (range / 30) * 100 : 50; // 30 degrees as max range
};

const getAqiLevel = (aqi) => {
  if (aqi <= 50) return '优';
  if (aqi <= 100) return '良';
  if (aqi <= 150) return '中';
  if (aqi <= 200) return '差';
  return '很差';
};

const getAqiPercentage = (aqi) => {
  return Math.min(aqi / 300 * 100, 100);
};

// Determine current season
const getCurrentSeason = () => {
  const now = new Date();
  const month = now.getMonth() + 1; // getMonth() returns 0-11

  if (month >= 3 && month <= 5) return 'spring';
  if (month >= 6 && month <= 8) return 'summer';
  if (month >= 9 && month <= 11) return 'fall';
  return 'winter';
};

// 获取城市编码
const getCityCode = async (cityName) => {
  try {
    const response = await fetch(`https://restapi.amap.com/v3/geocode/geo?address=${encodeURIComponent(cityName)}&key=${apiKey.value}`);
    const data = await response.json();

    if (data.status === '1' && data.geocodes && data.geocodes.length > 0) {
      return data.geocodes[0].adcode;
    } else {
      throw new Error('未找到该城市');
    }
  } catch (error) {
    console.error('获取城市编码失败:', error);
    throw error;
  }
};

// 获取实时天气
const getLiveWeather = async (cityCode) => {
  try {
    const response = await fetch(`https://restapi.amap.com/v3/weather/weatherInfo?city=${cityCode}&key=${apiKey.value}&extensions=base`);
    const data = await response.json();

    if (data.status === '1' && data.lives && data.lives.length > 0) {
      return data.lives[0];
    } else {
      throw new Error('获取实时天气失败');
    }
  } catch (error) {
    console.error('获取实时天气失败:', error);
    throw error;
  }
};

// 获取天气预报
const getForecastWeather = async (cityCode) => {
  try {
    const response = await fetch(`https://restapi.amap.com/v3/weather/weatherInfo?city=${cityCode}&key=${apiKey.value}&extensions=all`);
    const data = await response.json();

    if (data.status === '1' && data.forecasts && data.forecasts.length > 0) {
      return data.forecasts[0];
    } else {
      throw new Error('获取天气预报失败');
    }
  } catch (error) {
    console.error('获取天气预报失败:', error);
    throw error;
  }
};

// 获取空气质量
const getAirQuality = async (cityCode) => {
  try {
    // 注意：此处使用的是环境云API，需要单独申请，这里仅作示例
    const response = await fetch(`https://restapi.amap.com/v3/air/quality?city=${cityCode}&key=${apiKey.value}`);
    const data = await response.json();

    if (data.status === '1' && data.lives && data.lives.length > 0) {
      return data.lives[0];
    } else {
      return { aqi: 75 }; // 默认值
    }
  } catch (error) {
    console.error('获取空气质量失败:', error);
    return { aqi: 75 }; // 默认值
  }
};

// 格式化日期
const formatDate = (dateStr) => {
  const date = new Date(dateStr);
  return `${date.getMonth() + 1}/${date.getDate()}`;
};

// 获取星期几
const getDayOfWeek = (dateStr) => {
  const date = new Date(dateStr);
  const days = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
  return days[date.getDay()];
};

// 生成小时预报数据（由于高德API没有小时预报，这里模拟生成）
const generateHourlyForecast = (currentTemp, dayForecast) => {
  const now = new Date();
  const hours = now.getHours();
  const hourlyData = [];

  // 当前小时
  hourlyData.push({
    time: '现在',
    temp: currentTemp,
    condition: dayForecast.dayweather,
    precipitation: Math.floor(Math.random() * 30)
  });

  // 生成接下来5小时的数据
  for (let i = 1; i <= 5; i++) {
    const forecastHour = (hours + i) % 24;
    const isNight = forecastHour >= 18 || forecastHour < 6;

    // 根据白天/夜间选择天气状况
    const condition = isNight ? dayForecast.nightweather : dayForecast.dayweather;

    // 根据时间生成温度变化
    let tempVariation;
    if (forecastHour >= 10 && forecastHour <= 14) {
      // 中午时分温度较高
      tempVariation = Math.random() * 3;
    } else if (forecastHour >= 0 && forecastHour <= 5) {
      // 凌晨温度较低
      tempVariation = -Math.random() * 5;
    } else {
      // 其他时间随机波动
      tempVariation = Math.random() * 2 - 1;
    }

    const temp = Math.round(parseFloat(currentTemp) + tempVariation);
    const precipitation = Math.floor(Math.random() * (condition.includes('雨') ? 70 : 30));

    hourlyData.push({
      time: `${forecastHour}时`,
      temp,
      condition,
      precipitation
    });
  }

  return hourlyData; // 返回6小时的预报
};

const searchLocation = async () => {
  if (!searchQuery.value.trim()) return;

  loading.value = true;
  error.value = null;

  try {
    // 1. 获取城市编码
    const cityCode = await getCityCode(searchQuery.value);
    adcode.value = cityCode;

    // 2. 获取天气数据
    await fetchWeatherData(cityCode);

    searchQuery.value = '';
  } catch (err) {
    console.error('搜索城市失败:', err);
    error.value = err.message || '搜索城市失败，请重试';
  } finally {
    loading.value = false;
  }
};

const fetchWeatherData = async (cityCode) => {
  try {
    // 1. 获取实时天气
    const liveData = await getLiveWeather(cityCode);

    // 2. 获取天气预报
    const forecastData = await getForecastWeather(cityCode);

    // 3. 获取空气质量
    const airQualityData = await getAirQuality(cityCode);

    // 4. 更新天气数据
    const forecastList = forecastData.casts.map(cast => ({
      day: cast.date === forecastData.casts[0].date ? '今天' : getDayOfWeek(cast.date),
      date: formatDate(cast.date),
      high: parseFloat(cast.daytemp),
      low: parseFloat(cast.nighttemp),
      condition: cast.dayweather,
      precipitation: cast.dayweather.includes('雨') ?
        Math.floor(Math.random() * 70 + 30) :
        Math.floor(Math.random() * 30)
    }));

    // 5. 生成小时预报
    const hourlyList = generateHourlyForecast(liveData.temperature, forecastData.casts[0]);

    // 6. 更新天气状态
    weather.value = {
      city: forecastData.city,
      current: {
        temp: parseFloat(liveData.temperature),
        feelsLike: parseFloat(liveData.temperature) - 2 + Math.random() * 4, // 模拟体感温度
        humidity: parseFloat(liveData.humidity),
        wind: parseFloat(liveData.windpower) * 3, // 转换为km/h的近似值
        condition: liveData.weather,
        uv: getUVIndex(liveData.weather),
        visibility: Math.floor(Math.random() * 5) + 5, // 模拟能见度
        pressure: Math.floor(Math.random() * 50) + 1000, // 模拟气压
        aqi: airQualityData.aqi || 75,
        sunrise: forecastData.casts[0].sunrise,
        sunset: forecastData.casts[0].sunset
      },
      forecast: forecastList,
      hourly: hourlyList
    };

    // 7. 更新天气背景
    updateWeatherCondition(liveData.weather);

    // 8. 更新季节
    season.value = getCurrentSeason();

  } catch (error) {
    console.error('获取天气数据失败:', error);
    throw new Error('获取天气数据失败，请重试');
  }
};

// 根据天气状况估算紫外线指数
const getUVIndex = (weather) => {
  if (weather.includes('晴')) {
    return '强';
  } else if (weather.includes('多云')) {
    return '中等';
  } else {
    return '弱';
  }
};

const updateWeatherCondition = (condition) => {
  const conditionLower = condition.toLowerCase();
  if (conditionLower.includes('晴') && !conditionLower.includes('多云')) {
    weatherCondition.value = 'clear';
  } else if (conditionLower.includes('雨')) {
    weatherCondition.value = 'rain';
  } else if (conditionLower.includes('雪')) {
    weatherCondition.value = 'snow';
  } else {
    weatherCondition.value = 'cloudy';
  }

  // Check if it's day or night
  const hours = new Date().getHours();
  timeOfDay.value = (hours >= 6 && hours < 18) ? 'day' : 'night';
};

// 初始化
onMounted(async () => {
  loading.value = true;

  try {
    // 设置当前季节
    season.value = getCurrentSeason();

    // 默认加载北京天气
    await fetchWeatherData(adcode.value);
  } catch (err) {
    console.error('初始化天气数据失败:', err);
    error.value = '加载天气数据失败，请刷新页面重试';
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.weather-container {
  width: 100%;
  height: 925px;
  margin: 0 auto;
  position: relative;
  /* overflow: hidden; */
  display: flex;
  justify-content: center;
  align-items: center;
}

.weather-card {
  width: 95%;
  height: 100%;
  padding: 40px;
  background: #ffffff;
  backdrop-filter: blur(10px);
  display: flex;
  flex-direction: column;

  /* 减少阴影效果，使其更加轻微 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05), 0 1px 5px rgba(0, 0, 0, 0.03);
  border-radius: 24px;
}

/* 顶部搜索栏 */
.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
}

.location h1 {
  font-size: 32px;
  font-weight: 600;
  margin: 0;
  color: #000000;
}

.location p {
  font-size: 16px;
  color: #333333;
  margin: 4px 0 0;
}

.search-box {
  position: relative;
  width: 300px;
}

.search-box input {
  width: 100%;
  padding: 12px 20px;
  border: 1px solid #cccccc;
  border-radius: 30px;
  background: #eeeeee;
  font-size: 16px;
  color: #1a1a1a;
  transition: all 0.3s ease;
}

.search-box input:focus {
  outline: none;
  border-color: #999999;
  box-shadow: 0 0 0 3px rgba(153, 153, 153, 0.2);
}

/* 主要内容区 - 横向布局 */
.main-content {
  display: flex;
  flex: 1;
  gap: 15px;
  height: calc(100% - 100px);
}

/* 左侧当前天气 */
.current-weather-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 30px;
  background: #eeeeee;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border-radius: 20px;
}

.current-weather {
  text-align: center;
}

.weather-icon {
  width: 180px;
  height: 120px;
  margin: 0 auto;
}

.temp-container h2 {
  font-size: 72px;
  font-weight: 500;
  margin: 20px 0 10px;
  color: #000000;
}

.temp-container p {
  color: #333333;
  font-size: 18px;
}

.weather-details {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  padding: 20px;
  background: #e5e5e5;
  border-radius: 15px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #222222;
  font-weight: 500;
}

.detail-icon {
  color: #000000;
}

/* 右侧预报区域 */
.forecast-section {
  flex: 1.2;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 小时预报 */
.hourly-section {
  background: #eeeeee;
  border-radius: 20px;
  padding: 25px;
  padding-top: 10px;
  padding-bottom: 10px;
}

.hourly-forecast {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 15px;
}

.hourly-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px 10px;
  background: #e5e5e5;
  border-radius: 15px;
  transition: transform 0.2s;
}

.hourly-item p {
  color: #222222;
  font-weight: 500;
}

.hourly-item:hover {
  transform: translateY(-2px);
}

/* 日预报 */
.daily-forecast {
  flex: 1;
  background: #eeeeee;
  border-radius: 20px;
  padding: 25px;
  padding-top: 10px;
  padding-bottom: 10px;
  overflow: hidden;
  min-height: 200px;
  /* 确保有足够的高度 */
}

.forecast-list {
  overflow-y: auto;
  max-height: 350px;
  /* 调整最大高度 */
  padding-right: 10px;
  margin-right: -10px;
  /* 补偿padding-right造成的偏移 */
}

/* 自定义滚动条样式 */
.forecast-list::-webkit-scrollbar {
  width: 6px;
}

.forecast-list::-webkit-scrollbar-track {
  background: #e5e5e5;
  border-radius: 3px;
}

.forecast-list::-webkit-scrollbar-thumb {
  background: #999999;
  border-radius: 3px;
}

.forecast-list::-webkit-scrollbar-thumb:hover {
  background: #666666;
}

.forecast-day {
  display: grid;
  grid-template-columns: 0.8fr 0.5fr 1fr 1.5fr 0.7fr;
  align-items: center;
  padding: 15px;
  margin-bottom: 8px;
  /* 添加间距 */
  background: #e5e5e5;
  border-radius: 12px;
  transition: all 0.2s;
  color: #222222;
  font-weight: 500;
}

.forecast-day:last-child {
  margin-bottom: 0;
}

.forecast-day:hover {
  background: #dedede;
  transform: translateX(4px);
}

/* 日期信息样式优化 */
.day-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.day-name {
  font-weight: 600;
  margin: 0;
}

.day-date {
  font-size: 12px;
  color: #666666;
  margin: 0;
}

/* 温度条样式 */
.temp-bar {
  height: 4px;
  background: #cccccc;
  border-radius: 2px;
  overflow: hidden;
}

.temp-fill {
  height: 100%;
  background: linear-gradient(90deg, #666666, #333333);
  border-radius: 2px;
}

.bg-clear-night-spring,
.bg-clear-night-summer,
.bg-clear-night-fall,
.bg-clear-night-winter {
  background: linear-gradient(135deg, #e0e0e0 0%, #eeeeee 100%);
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .weather-container {
    width: 100%;
    height: auto;
    padding: 20px;
  }

  .main-content {
    flex-direction: column;
  }
}
</style>