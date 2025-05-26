import axios from 'axios'

// 创建axios实例
const request = axios.create({
    // 上线时使用
    baseURL: 'http://47.97.2.38:8080/',

    // 本地测试使用
    // baseURL: 'http://localhost:8080',
    timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
    config => {
        // 从localStorage获取token
        const token = localStorage.getItem('Authorization')
        // 如果token存在，则添加到请求头
        if (token) {
            // 确保headers对象存在
            if (!config.headers) {
                config.headers = {}
            }
            config.headers['Authorization'] = token
        } else {
            console.log('未找到token')
        }

        return config
    },
    error => {
        console.log('请求拦截器错误:', error)
        return Promise.reject(error)
    }
)

// 响应拦截器
request.interceptors.response.use(
    response => {
        return response.data
    },
    error => {
        return Promise.reject(error)
    }
)

export default request