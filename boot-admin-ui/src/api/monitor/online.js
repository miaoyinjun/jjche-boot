import request from '@/utils/request'

export function del(keys) {
  return request({
    url: 'admin/auth/online',
    method: 'delete',
    data: keys
  })
}
