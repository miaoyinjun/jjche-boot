import request from '@/utils/request'

export function get() {
  return request({
    url: 'admin/aliPay',
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: 'admin/aliPay',
    data,
    method: 'put'
  })
}

// 支付
export function toAliPay(url, data) {
  return request({
    url: 'admin/' + url,
    data,
    method: 'post'
  })
}
