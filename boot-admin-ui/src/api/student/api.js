import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'students',
    method: 'post',
    data
  })
}

export function del(ids) {
  const params = {
    ids: ids
  }
  return request({
    url: 'students/',
    method: 'delete',
    data: params
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
  const params = {
    id: id
  }
  return request({
    url: 'students/get',
    method: 'get',
    params
  })
}

export default { add, edit, del, get }