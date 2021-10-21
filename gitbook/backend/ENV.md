# 环境变量
|  变量名                                              | 默认值             | 分类    | 说明|
|  :----                                              | :---:             | :---:   | :----|
| BOOT_ADMIN_WEB_PORT                         | 8801              |  SERVER | 服务端口|
| BOOT_ADMIN_WEB_PROFILES_ACTIVE              | dev               |  SERVER | 环境，可选（dev, qa, demo, prod）|
| BOOT_ADMIN_DB_HOST                               | localhost         |  DB  | 数据库地址|
| BOOT_ADMIN_DB_DATABASE                           | boot-admin        |  DB  | 数据库名|
| BOOT_ADMIN_DB_UNAME                              | root              |  DB  | 数据库用户|
| BOOT_ADMIN_DB_PASSWORD                           |                   |  DB  | 数据库密码|
| BOOT_ADMIN_DB_PORT                               | 3306              |  DB  | 数据库端口|
| BOOT_ADMIN_DB_IS-PRINT-SQL | false | DB | 是否打印sql |
| BOOT_ADMIN_REDIS_DB                                 | 1                 |  REDIS  | redis数据库名|
| BOOT_ADMIN_REDIS_HOST                               | localhost         |  REDIS  | redis地址|
| BOOT_ADMIN_REDIS_PORT                               | redis             |  REDIS  | redis端口|
| BOOT_ADMIN_REDIS_PASSWORD                           | 6379              |  REDIS  | redis密码|
| BOOT_ADMIN_LOG_PATH                                 | ./logs            |  日志   | 日志路径|
| BOOT_ADMIN_FILTER_ENCRYPTION_FIELD_SECRET-KEY       | YgnE7hAjiifYFibD  |  过滤器  | 字段加密，密钥|
| BOOT_ADMIN_SBA_PASSWORD                             | 3nxnn4mtLyzn      |  SBA    | spring-boot-admin密码|
| BOOT_ADMIN_SBA_SERVER_URL                           | http://localhost:${server.port}      |  SBA    | spring-boot-admin注册的服务端地址|
| BOOT_ADMIN_USER_PASSWORD_DEFAULT-VAL                | 123456            |  USER   | 新用户默认密码|
| BOOT_ADMIN_SECURITY_JWT_BASE64-SECRET               | ***       |  USER   | jwt_security |
| BOOT_ADMIN_MAVEN_URL                         |                   |  MAVEN   | maven仓库地址如：http://x.x.x.x:1111/repository|

