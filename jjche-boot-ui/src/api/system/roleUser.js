import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'sys/roles/users',
    method: 'post',
    data
  })
}
export function del(data) {
  return request({
    url: 'sys/roles/users',
    method: 'delete',
    data: data
  })
}

export function notUsers(params) {
  return request({
    url: 'sys/roles/not/users',
    method: 'get',
    params
  })
}

export default { add, del, notUsers }
