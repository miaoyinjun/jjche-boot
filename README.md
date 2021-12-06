# 项目介绍

## boot-admin

企业级快速开发平台，基于SpringBoot2、MyBatis，Vue前后端分离，自动化配置，通过封装一系列Starter，快速生成脚手架项目、后台管理系统。

### 体验地址：http://149.28.233.161

- **登录**

用户名：demo，密码：123456

- **应用监控/接口文档**

用户名：admin，密码：123456

### 开发文档：https://miaoyinjun.gitee.io/boot-admin-book/

### 项目源码

| 源码   | 地址                                     |
| ------ | ---------------------------------------- |
| gitee  | https://gitee.com/miaoyinjun/boot-admin  |
| github | https://github.com/miaoyinjun/boot-admin |


![image-20210916171732597](https://miaoyinjun.gitee.io/boot-admin-book/assets/image-20210916171732597.png)

![image-20210916171752707](https://miaoyinjun.gitee.io/boot-admin-book/assets/image-20210916171752707.png)

![image-20210916171814326](https://miaoyinjun.gitee.io/boot-admin-book/assets/image-20210916171814326.png)

![image-20210918105646982](https://miaoyinjun.gitee.io/boot-admin-book/assets/image-20210918105646982.png)

![image-20210927154722486](https://miaoyinjun.gitee.io/boot-admin-book/assets/image-20210927154722486.png)

![image-20211202181126329](https://miaoyinjun.gitee.io/boot-admin-book/assets/image-20211202181126329.png)
## 特别鸣谢

1. 感谢[eladmin](https://github.com/elunez/eladmin) 允许在基础业务功能上进行扩展

>eladmin作为独立模块引入，并作了以下扩展
> 1. 后端全部重写，原业务逻辑保留，Mybatis替换JPA
> 2. 新的注释和代码规范
> 3. 引入了新的日志模块[mzt-biz-log](https://github.com/mouzt/mzt-biz-log)，并做了扩展
4. 同时支持账号密码与短信登录，可再扩展其它方式登录
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

2. 感谢[mouzt](https://github.com/mouzt/mzt-biz-log/) 提供的日志组件


## TODO
1. 首页-监控，pv，uv
2. 前端-清除缓存
3. 后端返回菜单优化
4. 集成springCloud
5. k8s
6. istio