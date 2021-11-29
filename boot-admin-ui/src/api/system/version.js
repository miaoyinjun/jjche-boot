import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'sys/versions',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'sys/versions/',
    method: 'delete',
    data: ids
  })
}

export function activated(id) {
  return request({
    url: 'sys/versions/' + id,
    method: 'put',
  })
}

export function edit(data) {
  return request({
    url: 'sys/versions',
    method: 'put',
    data
  })
}

export function get(id) {
  return request({
    url: 'sys/versions/' + id,
    method: 'get'
  })
}

export function latest() {
  return request({
    url: 'sys/versions/latest',
    method: 'get'
  })
}

export default { add, edit, del, get, latest}
