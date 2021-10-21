import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'admin/jobs',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'admin/jobs',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'admin/jobs',
    method: 'put',
    data
  })
}

export function updateIsPause(id) {
  return request({
    url: 'admin/jobs/' + id,
    method: 'put'
  })
}

export function get(id) {
  return request({
    url: 'admin/jobs/' + id,
    method: 'get'
  })
}

export function execution(id) {
  return request({
    url: 'admin/jobs/exec/' + id,
    method: 'put'
  })
}

export default { del, updateIsPause, execution, add, edit, get }
