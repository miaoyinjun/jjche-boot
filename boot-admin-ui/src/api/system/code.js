import request from '@/utils/request'

export function resetEmail(data) {
  return request({
    url: 'sys/code/resetEmail?email=' + data,
    method: 'post'
  })
}

export function updatePass(pass) {
  return request({
    url: 'sys/users/updatePass/' + pass,
    method: 'get'
  })
}
