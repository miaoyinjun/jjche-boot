import request from '@/utils/request'

export function getAllJob() {
  const params = {
    pageIndex: 0,
    pageSize: 9999,
    enabled: true
  }
  return request({
    url: 'sys/job',
    method: 'get',
    params
  })
}

export function add(data) {
  return request({
    url: 'sys/job',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'sys/job',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'sys/job',
    method: 'put',
    data
  })
}

export default { add, edit, del }
