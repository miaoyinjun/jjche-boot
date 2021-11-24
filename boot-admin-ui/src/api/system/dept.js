import request from '@/utils/request'

export function getDepts(params) {
  return request({
    url: 'sys/dept',
    method: 'get',
    params
  })
}

export function getDeptSuperior(ids) {
  const data = ids
  return request({
    url: 'sys/dept/superior',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: 'sys/dept',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'sys/dept',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'sys/dept',
    method: 'put',
    data
  })
}

export default { add, edit, del, getDepts, getDeptSuperior }
