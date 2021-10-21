import request from '@/utils/request'

export function get(tableName) {
  return request({
    url: 'admin/genConfig/' + tableName,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: 'admin/genConfig',
    data,
    method: 'put'
  })
}
