import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'admin/data_permission_fields',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'admin/data_permission_fields/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'admin/data_permission_fields',
    method: 'put',
    data
  })
}

export function get(id) {
  return request({
    url: 'admin/data_permission_fields/' + id,
    method: 'get'
  })
}
export default { add, edit, del, get }
