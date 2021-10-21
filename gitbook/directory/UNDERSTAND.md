# 快速了解

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

## 模块说明
> boot-admin
> > boot-admin-parent  --  父模块
> >
> > > boot-admin-common  --  通用定义
>
> >> boot-admin-eladmin  --  基础业务
>
> >>> boot-admin-eladmin-generator  --  代码生成
>
> >>> boot-admin-eladmin-logging  --  日志
>
> >>> boot-admin-eladmin-system  --  系统
>
> >>> boot-admin-eladmin-tools  --  工具
>
> >> boot-admin-starters  --  组件增强
> >>
> >> > boot-admin-cache-starter  --  缓存
>
> >>> boot-admin-core-starter  --  核心
>
> >>> boot-admin-filter-starter  --  安全过滤器
>
> >>> boot-admin-jackson-starter  --  jackson定义
>
> >>> boot-admin-log-starter  --  日志
>
> >>> boot-admin-mybatis-starter  --  mybatis定义
>
> >>> boot-admin-sba-starter  --  spring-boot-admin增强
>
> >>> boot-admin-security-starter  --  安全
>
> >>> boot-admin-swagger-starter  --  swagger增强
>
> > boot-admin-ui  --  前端
>
> > boot-admin-web-demo  --  演示
> >
> 