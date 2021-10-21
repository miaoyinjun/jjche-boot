import request from '@/utils/request'

export function getErrDetail(id) {
  return request({
    url: 'admin/logs/error/' + id,
    method: 'get'
  })
}

export function delAllInfo() {
  return request({
    url: 'admin/logs/del',
    method: 'delete'
  })
}

export function getModules() {
  return request({
    url: 'admin/logs/modules',
    method: 'get'
  })
}
