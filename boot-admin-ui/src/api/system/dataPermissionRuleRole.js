import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'admin/data_permission_rule_roles',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'admin/data_permission_rule_roles/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'admin/data_permission_rule_roles',
    method: 'put',
    data
  })
}

export function get(id) {
  return request({
    url: 'admin/data_permission_rule_roles/' + id,
    method: 'get'
  })
}

export function getSelectedId(params) {
  return request({
    url: 'admin/data_permission_rule_roles/selectedIds',
    method: 'get',
    params
  })
}

export default { add, edit, del, get, getSelectedId }
