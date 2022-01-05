import request from '@/utils/request'

export function get(dictName) {
  const params = {
    dictName,
    pageIndex: 1,
    pageSize: 9999
  }
  return request({
    url: 'sys/dictDetail',
    method: 'get',
    params
  })
}

export function getDictMap(dictName) {
  const params = {
    dictName,
    pageIndex: 1,
    pageSize: 9999
  }
  return request({
    url: 'sys/dictDetail/map',
    method: 'get',
    params
  })
}

export function add(data) {
  return request({
    url: 'sys/dictDetail',
    method: 'post',
    data
  })
}

export function del(id) {
  return request({
    url: 'sys/dictDetail/' + id,
    method: 'delete'
  })
}

export function edit(data) {
  return request({
    url: 'sys/dictDetail',
    method: 'put',
    data
  })
}

export default { add, edit, del }
