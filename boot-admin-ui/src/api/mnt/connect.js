import request from '@/utils/request'

export function testDbConnect(data) {
  return request({
    url: 'admin/database/testConnect',
    method: 'post',
    data
  })
}

export function testServerConnect(data) {
  return request({
    url: 'admin/serverDeploy/testConnect',
    method: 'post',
    data
  })
}
