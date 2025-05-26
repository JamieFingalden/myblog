import request from './config'

export const loginApi = {
  login(data) {
    return request.post('/login/login', data)
  },
  register(data) {
    return request.post('/login/register', data)
  },

  validationToken() {
    return request.get('/posts/validation')
  }
}