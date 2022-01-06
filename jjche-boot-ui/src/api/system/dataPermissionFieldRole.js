import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'sys/data_permission_field_roles',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'sys/data_permission_field_roles/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'sys/data_permission_field_roles',
    method: 'put',
    data
  })
}

export function get(id) {
  return request({
    url: 'sys/data_permission_field_roles/' + id,
    method: 'get'
  })
}

export default { add, edit, del, get }
