import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'sys/app',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'sys/app',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'sys/app',
    method: 'put',
    data
  })
}

export default { add, edit, del }
