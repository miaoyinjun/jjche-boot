import request from '@/utils/request'

export function get() {
  return request({
    url: 'admin/email',
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: 'admin/email',
    data,
    method: 'put'
  })
}

export function send(data) {
  return request({
    url: 'admin/email',
    data,
    method: 'post'
  })
}
