import axios from 'axios'

const request = axios.create({
    baseURL: 'http://localhost:8080',  // 确保使用完整的URL（包含http://）
    timeout: 5000,
    withCredentials: true  // 允许跨域携带cookie
})

// 添加响应拦截器，统一处理错误
request.interceptors.response.use(
    response => response,
    error => {
        console.error('请求错误:', error.message)
        return Promise.reject(error)
    }
)

export default request