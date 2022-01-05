import request from '@/utils/request'

export function del(ids) {
  return request({
    url: 'sys/deployHistory',
    method: 'delete',
    data: ids
  })
}

/**
 * 版本回退
 * @param data 选中行
 */
export function reducte(data) {
  return request({
    url: 'sys/deploy/serverReduction',
    method: 'post',
    data
  })
}
