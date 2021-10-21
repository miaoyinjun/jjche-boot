import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'admin/versions',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'admin/versions/',
    method: 'delete',
    data: ids
  })
}

export function activated(id) {
  return request({
    url: 'admin/versions/' + id,
    method: 'put',
  })
}

export function edit(data) {
  return request({
    url: 'admin/versions',
    method: 'put',
    data
  })
}

export function get(id) {
  return request({
    url: 'admin/versions/' + id,
    method: 'get'
  })
}

export function latest() {
  return request({
    url: 'admin/versions/latest',
    method: 'get'
  })
}

export default { add, edit, del, get, latest}
