import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'sys/security_app_keys',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'sys/security_app_keys/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'sys/security_app_keys',
    method: 'put',
    data
  })
}

export function get(id) {
  return request({
    url: 'sys/security_app_keys/' + id,
    method: 'get'
  })
}

export default { add, edit, del, get }
