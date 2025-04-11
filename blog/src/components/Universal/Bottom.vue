<template>
  <div class="footer-container">
    <div class="footer-content">
      <div class="copyright-info">
        © 2025 Fingalden's Blog
      </div>
      <div class="links-info">
        <a href="https://github.com/JamieFingalden" target="_blank" class="link">
          <span class="link-icon">
            <svg viewBox="0 0 24 24" width="18" height="18" fill="currentColor">
              <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/>
            </svg>
          </span>
          <span class="link-text">GitHub</span>
        </a>
        <span class="divider">•</span>
        <a href="https://space.bilibili.com/33594443" target="_blank" class="link">
          <span class="link-icon">
            <svg viewBox="0 0 24 24" width="18" height="18" fill="currentColor">
              <path d="M17.813 4.653h.854c1.51.054 2.769.578 3.773 1.574 1.004.995 1.524 2.249 1.56 3.76v7.36c-.036 1.51-.556 2.769-1.56 3.773s-2.262 1.524-3.773 1.56H5.333c-1.51-.036-2.769-.556-3.773-1.56S.036 18.858 0 17.347v-7.36c.036-1.511.556-2.765 1.56-3.76 1.004-.996 2.262-1.52 3.773-1.574h.774l-1.174-1.12a1.234 1.234 0 0 1-.373-.906c0-.356.124-.658.373-.907l.027-.027c.267-.249.573-.373.92-.373.347 0 .653.124.92.373L9.653 4.44c.071.071.134.142.187.213h4.267a.836.836 0 0 1 .16-.213l2.853-2.747c.267-.249.573-.373.92-.373.347 0 .662.151.929.4.267.249.391.551.391.907 0 .355-.124.657-.373.906L17.813 4.653zM5.333 7.24c-.746.018-1.373.276-1.88.773-.506.498-.769 1.13-.786 1.894v7.52c.017.764.28 1.395.786 1.893.507.498 1.134.756 1.88.773h13.334c.746-.017 1.373-.275 1.88-.773.506-.498.769-1.129.786-1.893v-7.52c-.017-.765-.28-1.396-.786-1.894-.507-.497-1.134-.755-1.88-.773H5.333zM8 11.107c.373 0 .684.124.933.373.25.249.383.569.4.96v1.173c-.017.391-.15.711-.4.96-.249.25-.56.374-.933.374s-.684-.125-.933-.374c-.25-.249-.383-.569-.4-.96V12.44c.017-.391.15-.711.4-.96.249-.249.56-.373.933-.373zm8 0c.373 0 .684.124.933.373.25.249.383.569.4.96v1.173c-.017.391-.15.711-.4.96-.249.25-.56.374-.933.374s-.684-.125-.933-.374c-.25-.249-.383-.569-.4-.96V12.44c.017-.391.15-.711.4-.96.249-.249.56-.373.933-.373z"/>
            </svg>
          </span>
          <span class="link-text">Bilibili</span>
        </a>
      </div>
    </div>
  </div>
</template>
  
<script>
export default {
  name: 'FooterComponent'
}
</script>
  
<style scoped>
.footer-container {
  width: 100%;
  position: relative;
  overflow: hidden;
  color: #666;
  font-size: 14px;
  line-height: 1.5;
  margin-top: 40px;
}

.footer-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 25px 0 20px;
  background-color: #f8f9fa;
  position: relative;
  z-index: 2;
  border-top: 1px solid #e9ecef;
}

.copyright-info {
  margin-bottom: 15px;
  font-weight: 500;
  letter-spacing: 0.5px;
  position: relative;
  padding: 0 10px;
  transition: all 0.3s ease;
}

.copyright-info::before,
.copyright-info::after {
  content: '';
  position: absolute;
  width: 0;
  height: 2px;
  bottom: -5px;
  background: linear-gradient(90deg, #6a9eff, #a875ff);
  transition: width 0.5s ease;
}

.copyright-info::before {
  left: 50%;
}

.copyright-info::after {
  right: 50%;
}

.copyright-info:hover::before,
.copyright-info:hover::after {
  width: 50%;
}

.links-info {
  display: flex;
  align-items: center;
}

.link {
  color: #666;
  text-decoration: none;
  margin: 0 12px;
  position: relative;
  transition: all 0.3s ease;
  padding: 5px 8px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  overflow: hidden;
}

.link::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, rgba(106, 158, 255, 0.1), rgba(168, 117, 255, 0.1));
  transform: translateX(-100%);
  transition: transform 0.3s ease;
  z-index: 0;
  border-radius: 20px;
}

.link:hover {
  color: #333;
  transform: translateY(-3px);
}

.link:hover::before {
  transform: translateX(0);
}

.link-icon {
  margin-right: 6px;
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  transform: scale(1);
  transition: transform 0.3s ease;
}

.link:hover .link-icon {
  transform: scale(1.2) rotate(5deg);
}

.link-text {
  position: relative;
  z-index: 1;
}

.divider {
  margin: 0 5px;
  color: #999;
  font-size: 18px;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 0.5;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.2);
  }
}

@media (max-width: 768px) {
  .link {
    margin: 0 8px;
    padding: 4px 6px;
  }
}
</style>