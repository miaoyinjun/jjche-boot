import request from '@/utils/request'

export function getDicts() {
  return request({
    url: 'sys/dict/all',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: 'sys/dict',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'sys/dict/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'sys/dict',
    method: 'put',
    data
  })
}

export default { add, edit, del }
