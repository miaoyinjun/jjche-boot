# 项目介绍
## boot-admin
> 基于SpringBoot2、Vue的前后端分离的后台管理系统

## 开发环境
- 语言：Java 8

- IDE(JAVA)： IDEA 安装lombok插件

- 依赖管理：Maven

- 数据库：MySQL5.7+

- 缓存：Redis

## 后端
|  技术                        | 名称                         | 说明
|  :---:                      | :---:                       | :---:                            
| springBoot                  | springBoot框架               |
| spring Security             | 安全框架                     |权限认证
| mybatis plus                | mybatis增强                 | 增强对数据库操作工具
| Druid                       | 数据库连接池                 | 提供监控
| knife4j                     | swagger接口文档增强          |
| MapStruct                    | Bean映射工具                |
| jetCache                    | 通用缓存框架                 |
| liquibase                   | 管理数据库变化工具            | 跟踪,管理和应用数据库变化的数据库重构工具
| p6spy                       | SQL日志打印工具               |
| spring-boot-admin           | 管理和监控SpringBoot应用程序   |
| jasypt                      | 配置文件加密                  |
| lombok                      | 生成POJO的getter/setter      |
| hutool                      | 工具类库                     |
| logback                     | 日志框架                     |
| xxl-job                     | 分布式定时框架                |
| jgitflow-maven-plugin       | 简化实现git flow工作流程插件   |
| git-commit-id-plugin        | git commit信息收集插件        |  maven打jar包时带上 git commit相关信息
| maven-javadoc-plugin        | javadoc插件                  |  检查，填充部分注释信息
| screw-maven-plugin          | 数据库文档生成插件             |
| springloaded                | 热部署插件                    |

## 前端
基于https://github.com/elunez/eladmin-web

## 运行
### 环境变量配置
1. `$ vim ~/.bash_profile`，并在底部写入以下配置
```
#mysql
export BOOT_ADMIN_DB_HOST="" #默认localhost
export BOOT_ADMIN_DB_DATABASE="" #默认boot-admin
export BOOT_ADMIN_DB_UNAME="" #默认root
export BOOT_ADMIN_DB_PASSWORD="" #默认为空
export BOOT_ADMIN_DB_PORT="" #默认3306

#redis
export BOOT_ADMIN_REDIS_DB="" #默认1
export BOOT_ADMIN_REDIS_HOST="" #默认localhost
export BOOT_ADMIN_REDIS_PORT="" #默认redis
export BOOT_ADMIN_REDIS_PASSWORD="16379" #默认6379
```
2. `$ source ~/.bash_profile`，使其生效

###首次运行-后端
1.`$ cd boot-admin-parent && mvn clean install`，安装父组件
2.`$ cd boot-admin-web-demo && mvn clean install` 安装demo父组件

###DEV-后端
1.`$ cd boot-admin-web-demo/api && mvn clean install` 安装demo 定义
2.`$ cd boot-admin-web-demo/service && mvn clean compile spring-boot:run` 运行demo
3. 访问接口文档地址：http://localhost:8801/sba/api/doc.html

###DEV-前端
1. `$ cd boot-admin-ui`
2. `$ npm i`
3. `$ npm run dev`
4. 访问https://localhost:8013/，用户名：admin，密码：123456

## DOCKER
> 前台运行，方便查看日志
### 后端
1. `$ cd boot-admin-web-demo`
2. `$ mvn clean compile install && mvn -f boot-admin-web-demo-service/pom.xml dockerfile:build && docker-compose -f ./boot-admin-web-demo-service/docker/docker-compose.yml --env-file=./boot-admin-web-demo-service/docker/.env up`
3. 访问接口文档地址：http://localhost:8801/sba/api/doc.html
### 前端
1. `$ cd boot-admin-ui`
2. `$ npm i && npm run build:docker && docker-compose -f ./docker/docker-compose.yml --env-file=./docker/.env up`
3. 访问https://localhost，用户名：admin，密码：123456

## 模块说明
> boot-admin
>> boot-admin-parent  --  父模块
>>> boot-admin-common  --  通用定义
>>>
>>> boot-admin-eladmin  --  基础业务
>>>>
>>>> boot-admin-eladmin-generator  --  代码生成
>>>>
>>>> boot-admin-eladmin-logging  --  日志
>>>>
>>>> boot-admin-eladmin-system  --  系统
>>>>
>>>> boot-admin-eladmin-tools  --  工具
>>>
>>> boot-admin-starters  --  组件增强
>>>> boot-admin-cache-starter  --  缓存
>>>
>>>> boot-admin-core-starter  --  核心
>>>
>>>> boot-admin-filter-starter  --  安全过滤器
>>>
>>>> boot-admin-jackson-starter  --  jackson定义
>>>
>>>> boot-admin-log-starter  --  日志
>>>
>>>> boot-admin-mybatis-starter  --  mybatis定义
>>>
>>>> boot-admin-sba-starter  --  spring-boot-admin增强
>>>
>>>> boot-admin-security-starter  --  安全
>>>
>>>> boot-admin-swagger-starter  --  swagger增强
>> 
>> boot-admin-ui  --  前端
>>
>> boot-admin-web-demo  --  演示
>>> boot-admin-web-demo-api 定义
>>> 
>>> boot-admin-web-demo-service 业务入口

## 最佳实践
1. 发布boot-admin-parent到maven私仓
> 分支*develop 发布的快照版本，master发布的是release版本*
- `$ cd boot-admin/boot-admin-parent`
- `$ mvn verify install:install deploy:deploy`
2. 业务开发
- 将boot-admin-web-demo复制出来进行独立开发，如有需要MAVEN私有仓库请配置BOOT_ADMIN_MAVEN_URL环境变量
3. 版本发布参考【版本发布】
4. 系统管理/版本管理，新建版本，激活
5.完成

## linux运行
配置完成环境变量后，使用boot-admin-web-demo-service/linux/restart.sh控制服务的重启

## 引入
> 没有使用import方式，是因import会导致properties与plugins里的配置失效。
```
<parent>
    <groupId>com.boot.admin</groupId>
    <artifactId>boot-admin-parent</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</parent>
```


## 特别鸣谢
感谢[eladmin](https://github.com/elunez/eladmin)
提供的基础业务功能源码

eladmin作为独立模块引入，并作了以下增强
> 1. 后端全部重写，原业务逻辑保留
> 2. 新的注释和代码规范
> 3. 引入了新的日志模块[mzt-biz-log](https://github.com/mouzt/mzt-biz-log)
     ，并做了二次优化
> 4. 同时支持账号密码与短信登录，可再扩展其它方式登录
> 5. 引入liquibase控制数据库结构变化，支持回滚
> 6. 行列数据级别权限控制
> 7. 增加注解，入参解密，出参加密，举例：返回id主键加密，保证数据安全
> 8. 引入SpringBootAdmin，服务端与客户端都为当前服务，非开发模式必须登录才能使用，可查看git最后一次的提交信息，用于验证项目版本是否正确
> 9. 引入knife4j替换原swagger，并增加了枚举支持，可在api文档界面下拉选择枚举
> 10. 增加账号过期、密码过期、隔N天强制修改密码等机制
> 11. 支持参数验证@RequestParam、@requestbody
> 12. 增加版本控制菜单
> 13. 使用[vue-admin-beautiful-pro](https://github.com/chuzhixin/vue-admin-beautiful-pro) 重写了登录页面
> 14. 定时器增加分布式锁，避免启动多个服务场景任务重复执行
> 15. crud.js支持打开编辑界面时可请求获取数据接口
> 17. 其它自行体验

## TODO
1. 首页-监控，pv，uv
2. 前端-清除缓存 
3. 后端返回菜单优化
4. 集成springCloud
5. k8s
6. istio