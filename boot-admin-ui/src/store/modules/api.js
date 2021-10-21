import Settings from '../../settings'

const baseApiUrl = Settings.apiPrefix
const baseSbaUrl = Settings.sbaPrefix
const api = {
  state: {
    // 部署包上传
    deployUploadApi: baseApiUrl + '/admin/deploy/upload',
    // SQL脚本上传
    databaseUploadApi: baseApiUrl + '/admin/database/upload',
    // 实时控制台
    socketApi: baseApiUrl + '/admin/websocket?token=kl',
    // 图片上传
    imagesUploadApi: baseApiUrl + '/admin/localStorage/pictures',
    // 修改头像
    updateAvatarApi: baseApiUrl + '/admin/users/updateAvatar',
    // 上传文件到七牛云
    qiNiuUploadApi: baseApiUrl + '/admin/qiNiuContent',
    // Sql 监控
    sqlApi: baseSbaUrl + '/druid/index.html',
    // sba 监控
    sbaApi: baseSbaUrl,
    // swagger
    swaggerApi: baseSbaUrl + '/api/doc.html',
    // 文件上传
    fileUploadApi: baseApiUrl + '/admin/localStorage'
  }
}

export default api
