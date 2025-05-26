import request from './config'

// 博客相关接口
export const blogApi = {
    // 获取博客列表
    getList() {
        return request.get('/login/list')
    },
    // 获取博客详情
    getDetail(id) {
        return request.get(`/posts/${id}`)
    },
    // 创建博客
    create(data) {
        return request.post('/posts', data)
    },
    // 更新博客
    update(data) {
        return request.put('/posts', data)
    },
    // 删除博客
    delete(id) {
        return request.delete('/posts', { params: { id } })
    }
}