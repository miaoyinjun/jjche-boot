import request from '@/utils/request'

export function getMenusTree(pid, roleId) {
  return request({
    url: 'admin/menus/lazy?pid=' + pid + '&roleId=' + roleId,
    method: 'get'
  })
}

export function getMenus(params) {
  return request({
    url: 'admin/menus',
    method: 'get',
    params
  })
}

export function getMenuSuperior(ids) {
  const data = ids.length && ids.length === 0 ? ids : Array.of(ids)
  return request({
    url: 'admin/menus/superior',
    method: 'post',
    data
  })
}

export function getChild(id) {
  return request({
    url: 'admin/menus/child?id=' + id,
    method: 'get'
  })
}

export function buildMenus() {
  return request({
    url: 'admin/menus/build',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: 'admin/menus',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'admin/menus',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'admin/menus',
    method: 'put',
    data
  })
}

export default { add, edit, del, getMenusTree, getMenuSuperior, getMenus, getChild }
