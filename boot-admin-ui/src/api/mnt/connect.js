import request from '@/utils/request'

export function testDbConnect(data) {
  return request({
    url: 'sys/database/testConnect',
    method: 'post',
    data
  })
}

export function testServerConnect(data) {
  return request({
    url: 'sys/serverDeploy/testConnect',
    method: 'post',
    data
  })
}
