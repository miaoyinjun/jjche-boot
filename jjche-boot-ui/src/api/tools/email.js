import request from '@/utils/request'

export function get() {
  return request({
    url: 'sys/email',
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: 'sys/email',
    data,
    method: 'put'
  })
}

export function send(data) {
  return request({
    url: 'sys/email',
    data,
    method: 'post'
  })
}
