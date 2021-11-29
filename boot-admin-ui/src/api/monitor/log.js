import request from '@/utils/request'

export function getErrDetail(id) {
  return request({
    url: 'sys/logs/error/' + id,
    method: 'get'
  })
}

export function delAllInfo() {
  return request({
    url: 'sys/logs/del',
    method: 'delete'
  })
}

export function getModules() {
  return request({
    url: 'sys/logs/modules',
    method: 'get'
  })
}
