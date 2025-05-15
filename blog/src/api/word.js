import request from './config'

export const wordApi = {
  getAllWord() {
    return request.get('/word/getAllWords')
  },

  addWord(data) {
    return request.post('/word/addWord', data)
  }
}