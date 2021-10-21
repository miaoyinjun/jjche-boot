import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'admin/app',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'admin/app',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'admin/app',
    method: 'put',
    data
  })
}

export default { add, edit, del }
