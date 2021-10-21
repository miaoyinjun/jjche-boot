## XXL-JOB
> 客户端
### 配置
> 前缀 boot.admin.xxl.job.

|  参数                        | 说明             | 默认值                    
|  :---:                      | :---:            | :---:                    
| admin.addresses             | 服务地址          | http://localhost:8888/xxl-job-admin  |
| accessToken                 | 服务令牌          | 无                                             |
| executor.appname            | 客户端名称        | ${spring.application.name}                     |
| executor.address            | 客户端地址        | 无                                             |
| executor.ip                 | 客户端IP          |  无                                             |
| executor.port               | 客户端端口        | 9999                                           |
| executor.logretentiondays   | 客户端日志保存时长 | 30                                             |
| executor.logpath            | 客户端日志地址    | ${log.path:./logs/}                             |   
