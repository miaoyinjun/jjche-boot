import request from '@/utils/request'

export function count() {
  return request({
    url: 'sys/dashboard/count',
    method: 'get'
  })
}

export function chart() {
  return request({
    url: 'sys/dashboard/chart',
    method: 'get'
  })
}

export function pvIncr() {
  return request({
    url: 'sys/dashboard/pvIncr',
    method: 'post'
  })
}

export default { count, chart, pvIncr }
