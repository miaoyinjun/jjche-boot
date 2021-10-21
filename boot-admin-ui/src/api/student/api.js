import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'students',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'students/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'students',
    method: 'put',
    data
  })
}

export function get(id) {
  return request({
    url: 'students/' + id,
    method: 'get'
  })
}

export default { add, edit, del, get }
