import request from '@/utils/request'
import { encrypt } from '@/utils/rsaEncrypt'

export function add(data) {
  return request({
    url: 'admin/users',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'admin/users',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'admin/users',
    method: 'put',
    data
  })
}

export function editUser(data) {
  return request({
    url: 'admin/users/center',
    method: 'put',
    data
  })
}

export function updatePass(user) {
  const data = {
    oldPass: encrypt(user.oldPass),
    newPass: encrypt(user.newPass)
  }
  return request({
    url: 'admin/users/updatePass/',
    method: 'post',
    data
  })
}

export function resetPass(username, newPass) {
  const data = {
    username: username,
    newPass: encrypt(newPass)
  }
  return request({
    url: 'admin/users/resetPass/',
    method: 'put',
    data
  })
}

export function updateEmail(form) {
  const data = {
    password: encrypt(form.pass),
    email: form.email
  }
  return request({
    url: 'admin/users/updateEmail/' + form.code,
    method: 'post',
    data
  })
}

export default { add, edit, del }

