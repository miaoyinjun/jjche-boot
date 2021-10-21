import request from '@/utils/request'

export function generator(tableName, type) {
  return request({
    url: 'admin/generator/' + tableName + '/' + type,
    method: 'post',
    responseType: type === 2 ? 'blob' : ''
  })
}

export function save(data) {
  return request({
    url: 'admin/generator',
    data,
    method: 'put'
  })
}

export function sync(tables) {
  return request({
    url: 'admin/generator/sync',
    method: 'post',
    data: tables
  })
}

