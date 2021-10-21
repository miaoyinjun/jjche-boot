import request from '@/utils/request'

export function count() {
  return request({
    url: 'admin/dashboard/count',
    method: 'get'
  })
}

export function chart() {
  return request({
    url: 'admin/dashboard/chart',
    method: 'get'
  })
}

export function pvIncr() {
  return request({
    url: 'admin/dashboard/pvIncr',
    method: 'post'
  })
}

export default { count, chart, pvIncr }
