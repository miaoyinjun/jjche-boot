## jjche-boot

企业级快速开发平台，前后端分离设计，基于SpringBoot2.x、SpringCloud、Spring Security，MyBatis-plus，Vue，在线代码生成器快速生成前后端CRUD代码，支持单体服务与微服务之间灵活切换、帮助开发者节省70%的重复工作，更专注业务，节省开发成本，100%开源。

### 体验地址：http://149.28.233.161

​			**登录**：账号：demo，密码：123456

​			**应用监控/接口文档**：账号：admin，密码：123456

### 技术文档：https://miaoyinjun.gitee.io/jjche-boot-book/

### 项目介绍

1. **基础功能**：用户、角色、菜单、部门、岗位、数据字典、任务调度、版本

2. **微服务（spring cloud）**：支持单体服务与微服务之间灵活切换

3. **在线代码生成器**

   后端代码：controller、service、mapper、DO、DTO、VO、类、方法注释、字段验证、日志、接口文档，菜单-权限控制SQL

   前端代码：api.js，index.vue

   手动MERGE后，不需要任何修改直接使用

4. **操作日志**：基于注解，可在方法执行前/后记录日志(用户、请求参数、类型、描述、结果、耗时、浏览器、操作系统、IP来源等)

   ​	新增场景：入参包含学生姓名：张三，日志记录：新增的学生姓名是[张三]

   ​    删除场景：入参只有学生的id，可在注解中调用自定义函数查出学生姓名，日志记录：被删除的学生姓名是[张三]

   ​	修改场景：入参包含修改后的学生姓名：李四，日志记录：修改内容：「[(姓名)，旧值：'张三'，新值：'李四']」

5. **查询过滤器**：SQL自动动态拼装，条件（等于、不等于、大于、小于、全模糊、左模糊、右模糊、区间、包含、不为空、为空、自定义SQL等）

6. **数据权限**：菜单级、按钮级、数据行级、数据列级、列表和表单字段级

7. **字段验证**：借助强大的注解验证和异常捕捉实现（非空、数字、日期、手机号、邮件等）

8. **文件服务**：集成本地存储、七牛云，可自动扩展

9. **数据库**：支持读写分离、分库分表

10. **接口定义**：统一restful风格，完整的出入参格式定义、集成knife4j在线接口文档，JWT token安全验证

11. **系统监控**：在线用户、操作日志、服务器JVM监控、SQL监控、spring-boot-admin应用监控，Redis，最后一次GIT提交信息

12. **权限控制**：RBAC基于角色控制访问

13. **对外接口安全**：通过应用标识、签名、出入参密文、限流、白名单传输方式，保证接口数据安全

14. **账号/密码策略**：账号锁定、账号过期、密码过期、密码复杂度定义

15. **工具类**：集成hutool、短信发送、邮件发送、Excel导出

16. **前端crud组件**，实现页面的分页查询、新增、修改、删除、导出

17. **数据库版本控制**：Liquibase跟sql版本变化，回滚

18. **开发规范**：阿里代码规范文档、注释规范

19. **最佳实践**：提供本地开发、测试、生产、Docker部署文档



### 技术栈

|         技术          |             名称             |                   说明                    |
| :-------------------: | :--------------------------: | :---------------------------------------: |
|      springBoot       |        springBoot框架        |                                           |
| spring-cloud-alibaba  |            微服务            |                                           |
|    spring Security    |           安全框架           |                 权限认证                  |
|     mybatis plus      |         mybatis增强          |           增强对数据库操作工具            |
|         Druid         |         数据库连接池         |                 提供监控                  |
|        knife4j        |     swagger接口文档增强      |                                           |
|       MapStruct       |         Bean映射工具         |                                           |
|       jetCache        |         通用缓存框架         |                                           |
|       liquibase       |      管理数据库变化工具      | 跟踪,管理和应用数据库变化的数据库重构工具 |
|         p6spy         |       SQL日志打印工具        |                                           |
|   spring-boot-admin   | 管理和监控SpringBoot应用程序 |                                           |
|        jasypt         |         配置文件加密         |                                           |
|        lombok         |   生成POJO的getter/setter    |                                           |
|        hutool         |           工具类库           |                                           |
|        logback        |           日志框架           |                                           |
|        xxl-job        |        分布式定时框架        |                                           |
| jgitflow-maven-plugin | 简化实现git flow工作流程插件 |                                           |
| git-commit-id-plugin  |    git commit信息收集插件    |   maven打jar包时带上 git commit相关信息   |
| maven-javadoc-plugin  |         javadoc插件          |          检查，填充部分注释信息           |
|  screw-maven-plugin   |      数据库文档生成插件      |                                           |
|     springloaded      |          热部署插件          |                                           |

### 模块说明



> jjche-boot
>
> > jjche-boot-ui -- 前端
> >
> > jjche-boot --后端
> >
> > > jjche-boot-demo -- 单体入口
> > >
> > > jjche-boot-parent -- 父模块
> > >
> > > > jjche-boot-base-api -- 单体/微服务交互定义
> > > >
> > > > jjche-boot-common -- 通用定义
> >
> > > > jjche-boot-eladmin -- 基础业务
> >
> > > > > jjche-boot-eladmin-generator -- 代码生成
> >
> > > > > jjche-boot-eladmin-logging -- 日志
> >
> > > > > jjche-boot-eladmin-tools -- 工具
> >
> > > > jjche-boot-starters -- 组件增强
> > >
> > > > > jjche-boot-cache-starter -- 缓存
> > > > > jjche-boot-cloud-gray-starter -- 灰度
> > > > >
> > > > > jjche-boot-cloud-starter 微服务定义
> >
> > > > > jjche-boot-core-starter -- 核心
> >
> > > > > jjche-boot-filter-starter -- 安全过滤器
> >
> > > > > jjche-boot-shardingsphere-starter 分库分表
> >
> > > > > jjche-boot-jackson-starter -- jackson定义
> >
> > > > > jjche-boot-log-starter -- 日志
> >
> > > > > jjche-boot-mybatis-starter -- mybatis定义
> >
> > > > > jjche-boot-sba-starter -- spring-boot-admin增强
> >
> > > > > jjche-boot-security-starter -- 安全
> > > > >
> > > > > jjche-boot-starter -- 系统定义
> >
> > > > > jjche-boot-swagger-starter -- swagger增强
> > > > >
> > > > > jjche-boot-system-starter -- 系统核心
> > > > >
> > > > > jjche-boot-userdetail-starter -- 默认安全认证定义
> > >
> > > jjche-cloud -- 微服务入口
> > >
> > > > jjche-cloud-demo-provider -- 演示基础服务
> > > >
> > > > jjche-cloud-gateway -- 网关
> > > >
> > > > jjche-cloud-monitor -- 监控
> > > >
> > > > jjche-cloud-nacos -- 注册中心
> > > >
> > > > jjche-cloud-system -- 系统服务



![image-20210916171732597](https://miaoyinjun.gitee.io/jjche-boot-book/assets/image-20210916171732597.png)

![image-20210916171752707](https://miaoyinjun.gitee.io/jjche-boot-book/assets/image-20210916171752707.png)

![image-20211208135706365](https://miaoyinjun.gitee.io/jjche-boot-book/assets/image-20211208135706365.png)

![image-20211208134446909](https://miaoyinjun.gitee.io/jjche-boot-book/assets/image-20211208134446909.png)

![image-20211208135631192](https://miaoyinjun.gitee.io/jjche-boot-book/assets/image-20211208135631192.png)

![image-20211208135759032](https://miaoyinjun.gitee.io/jjche-boot-book/assets/image-20211208135759032.png)

![image-20211111145248092](https://miaoyinjun.gitee.io/jjche-boot-book/assets/image-20211111145248092.png)

![image-20211111152519726](https://miaoyinjun.gitee.io/jjche-boot-book/assets/image-20211111152519726.png)

![image-20211122161617315](https://miaoyinjun.gitee.io/jjche-boot-book/assets/image-20211122161617315.png)

![image-20210926170407498](https://miaoyinjun.gitee.io/jjche-boot-book/assets/image-20210926170407498.png)

![image-20211202181126329](https://miaoyinjun.gitee.io/jjche-boot-book/assets/image-20211202181126329.png)

![image-20210927154722486](https://miaoyinjun.gitee.io/jjche-boot-book/assets/image-20210927154722486.png)

![image-20210918105646982](https://miaoyinjun.gitee.io/jjche-boot-book/assets/image-20210918105646982.png)

## 特别鸣谢

1. 感谢[eladmin](https://github.com/elunez/eladmin) 允许在基础业务功能上进行扩展

>eladmin作为独立模块引入，并作了以下扩展
>
1. 后端全部重写，原业务逻辑保留，Mybatis替换JPA
2. 新的注释和代码规范
3. 引入了新的日志模块[mzt-biz-log](https://github.com/mouzt/mzt-biz-log)，并做了扩展
4. 同时支持账号密码与短信登录，可再扩展其它方式登录
5. 引入liquibase控制数据库结构变化，支持回滚
6. 行列数据级别权限控制
7. 增加注解，入参解密，出参加密，举例：返回id主键加密，保证数据安全
8. 引入SpringBootAdmin，服务端与客户端都为当前服务，非开发模式必须登录才能使用，可查看git最后一次的提交信息，用于验证项目版本是否正确
9. 引入knife4j替换原swagger，并增加了枚举支持，可在api文档界面下拉选择枚举
10. 增加账号过期、密码过期、隔N天强制修改密码等机制
11. 支持参数验证@RequestParam、@requestbody
12. 增加版本控制菜单
13. 使用[vue-admin-beautiful-pro](https://github.com/chuzhixin/vue-admin-beautiful-pro) 重写了登录页面
14. 定时器增加分布式锁，避免启动多个服务场景任务重复执行
15. crud.js支持打开编辑界面时可请求获取数据接口
16. 支持 spring cloud
17. 其它自行体验

2. 感谢[mouzt](https://github.com/mouzt/mzt-biz-log/) 提供的日志组件


## TODO

1. springCloud完善微服务周边组件
2. k8s
3. istio

## 支持

如果您喜欢该项目，请给项目**点亮⭐️**，让更多的开发者看到

## 交流

QQ交流群：604384111


如果您还想支持一下，请作者喝杯 **咖啡**吧🤓

|                       支付宝                       |                             微信                             |
| :------------------------------------------------: | :----------------------------------------------------------: |
| ![1640670237773__01](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1640670237773__01.jpg) | ![mm_facetoface_collect_qrcode_1640667560388__01__01](https://miaoyinjun.gitee.io/jjche-boot-book/assets/mm_facetoface_collect_qrcode_1640667560388__01__01.png) |
