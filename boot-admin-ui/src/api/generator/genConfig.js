import request from '@/utils/request'

export function get(tableName) {
  return request({
    url: 'sys/genConfig/' + tableName,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: 'sys/genConfig',
    data,
    method: 'put'
  })
}
