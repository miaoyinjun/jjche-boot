import request from '@/utils/request'

export function get(dictName) {
  const params = {
    dictName,
    pageIndex: 0,
    pageSize: 9999
  }
  return request({
    url: 'admin/dataPermission',
    method: 'get',
    params
  })
}

export function add(data) {
  return request({
    url: 'admin/dataPermission',
    method: 'post',
    data
  })
}

export function del(id) {
  return request({
    url: 'admin/dataPermission/' + id,
    method: 'delete'
  })
}

export function edit(data) {
  return request({
    url: 'admin/dataPermission',
    method: 'put',
    data
  })
}

export default { add, edit, del }
