# liquibase
## 简介
Liquibase是一个用于跟踪、管理和应用数据库变化的开源数据库重构工具。它将所有数据库的变化保存在XML文件中，便于版本控制和项目部署升级。使用该工具可以稳定、高效率地实现代码迁移与回滚。

## 数据库
> + 版本： mysql5.7.8+
> + 默认字符集: utf8mb4
> + 默认排序规则: utf8mb4_general_ci

>创建数据库SQL    

```sql
CREATE DATABASE IF NOT EXISTS `boot-admin` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

### 建表规约 --阿里代码规范
1.**【强制】**表必备三字段: id,gmt_create,gmt_modified

2.**【强制】**逻辑删除字段: is_deleted, created_by, updated_by

3.**【强制】**表字段注释

4.**【强制】**表达是与否概念的字段

> - 必须使用is_xxx 的方式命名，数据类型是 unsigned tinyint(1 表示是，0 表示否)。
> - 说明:任何字段如果为非负数，必须是unsigned。
> - 注意:POJO类中的任何布尔类型的变量，都不要加is前缀，需要在resultMap设置从is_xxx到Xxx的映射关系。
> - 数据库表示是与否的值，使用tinyint类型，坚持is_xxx的命名方式是为了明确其取值含义与取值范围。
> - 正例:表达逻辑删除的字段名is_deleted，1 表示删除，0 表示未删除。

5.**【强制】**索引命名。

> - 主键索引名为pk_字段名;唯一索引名为uk_字段名;普通索引名则为idx_字段名。
> - 说明:pk_ 即 primary key;uk_ 即 unique key;idx_ 即 index 的简称。

6.**【强制】**小数类型为decimal，禁止使用float和double。

> - 说明:在存储的时候，float 和 double 都存在精度损失的问题，很可能在比较值的时候，得到不正确的 结果。如果存储的数据范围超过 decimal 的范围，建议将数据拆成整数和小数并分开存储。
> - 运算：https://hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E6%95%B0%E5%AD%97%E5%B7%A5%E5%85%B7-NumberUtil

7.**【强制】**varchar

> - 是可变长字符串不预先分配存储空间，长度不要超过5000，如果存储长度 大于此值，定义字段类型为 text，独立出来一张表，用主键来对应，避免影响其它字段索引效率。

8.**【推荐】**表的命名最好是遵循“业务名称_表的作用”。

> - 正例:alipay_task / force_project / trade_config

### 表操作
1. *实际操作可参考**resources/liquibase/1.0.0**内的定义*

2. *多人开发时不推荐使用共享数据库，sql版本不好控制*，推荐在开发人员本机安装数据库

3. *推荐使用**纯sql**做表操作，以后迁移比较方便*

4. *一**定要写回滚的语句**，默认配置开启了test-rollback-on-update属性*，这样做是为了保证sql更新可控，可回滚

5. *dev、qa、demo、prod环境，update操作默认通过spring-liquibase方式执行，想手动控制sql，可修改spring.liquibase.enabled属性禁用*
   XML文件所在目录：resources/liquibase/changelog/[maven.project.version]

   

## 添加顺序

1. resources/liquibase/changelog下添加版本目录，如1.0.0
2. 在版本目录下增加xml目录与master.xml
3. 在xml目录增加todoxxxx.xml和sql目录
4. master.xml引入新添加的todoxxxx.xml

示例

> resources
> > liquibase
> > > changelog
> > >
> > > > 1.0.0

>>>>> xml
>>>>>> sql
>>>>>> 20200702_001_add_tb_student.xml -- 添加学生表
>>>>
>>>>master.xml --引入20200702_001_add_tb_student.xml，并添加tag


>>>> 1.1.0

>>>>> xml
>>>>>> sql
>>>>>> 20200703_001_init_data_student.xml -- 初始化学生表数据
>>>>
>>>>master.xml --引入20200703_001_init_data_student.xml，并添加tag

### 新增表
> XML命名规范：[日期]_[序号]_add_tb_[表名].xml
> 示例：20200702_001_add_tb_student.xml

### 初始化数据
> XML命名规范：[日期]_[序号]_init_data_[表名].xml
> 示例：20200703_001_init_data_student.xml

### 增加字段
> XML命名规范：[日期]_[序号]_add_col_[表名].xml
> 示例：20200703_002_add_col_student.xml

### 删除字段
> XML命名规范：[日期]_[序号]_del_col_[表名].xml
> 示例：20200703_003_del_col_student.xml

### 修改字段
> XML命名规范：[日期]_[序号]_modify_col_[表名].xml
> 示例：20200703_004_modify_col_student.xml

### 添加索引
> uk:唯一索引, idx:普通索引
> XML命名规范：[日期]_[序号]_add_[uk/idx]_[表名].xml
> 示例：20200703_005_add_idx_student.xml

### 初始化数据CSV，不推荐
> XML命名规范：[日期]_[序号]_init_data_[表名].xml
> 示例：20200702_003_init_data_student.xml

> CSV命名规范：[日期]_[序号]_init_data_[表名].csv
> 示例：20200703_001_init_data_student.csv
```CSV
  name,age,good_id
  王,1,1
  大,2,3
  力,3,4
```

### 数据库文档生成
1. screw
> 文档位置：target/db/doc/*.html
``` shell
    $ mvn clean package screw:run
```
2. liquibase
> 文档位置：target/liquibase/dbDoc/index.html
``` shell
    $ mvn clean package liquibase:dbDoc -Dprofiles.active=${profiles.active} -Dliquibase.password=${liquibase.password}
```

### 回滚
>回滚操作的前提必须有个标记位，这里使用tag

> - 打tag方法1，***推荐***
>
>
> > 需要在确认不会在改动数据库表后，在版本/master.xml里手动修改版本号
```xml
    <changeSet  author="miaoyj"  id="add tag">
        <tagDatabase  tag="1.2.0"/>
    </changeSet>
```


> - 打tag方法2
>
>
> ```shell
> $ liquibase tag ${version}
> ```

~~> maven 打tag方法3~~
~~> 需要注意的是，CI工具要得到上次的版本号，用以获取对应的jar与liqubase的tag~~
~~> 非dev环境，需要执行liquibase升级、增加标签命令：~~
~~> 参数profiles.active指定了数据库配置文件与context的环境。~~
~~$ mvn clean package liquibase:updateTestingRollback liquibase:update liquibase:tag liquibase:dbDoc -Dprofiles.active=${profiles.active} -Dliquibase.password=${liquibase.password}~~
~~>> 数据库变更的默认版本与pom.xml的version保持一致，无需指定版本号(mvn liquibase:tag ~~-Dliquibase.tag=1.0.0)~~

> 回滚命令：
>
> 参考 https://docsstage.liquibase.com/commands/home.html
>
> 准备工作
1. 安装liquibase
2. liquibase安装目录/lib/加入mysql.jar
3. liquibase.properties位置在项目位置/resources/liquibase/master.xml的上层目录
> 根据实际情况任选一种   
1. 回滚-指定tag，立即执行回滚操作
   ```$ liquibase rollback 1.0.0 --changeLogFile=liquibase/master.xml```
2. 生成回滚的sql-指定tag，不会执行回滚操作，***推荐***
   ```$ liquibase rollbackSQL 1.0.1 --changeLogFile=liquibase/master.xml --outputFile=rollback_1.0.1.sql```
3. 生成回滚的sql-指定xml或sql，不实用
   ``$ liquibase --changeLogFile=liquibase/master.xml --outputFile=future.txt futureRollbackSQL``

~~> maven方式 4.0版本已收费，废弃~~
~~$ mvn clean package liquibase:rollback -Dprofiles.active=${profiles.active} -Dliquibase.password=${liquibase.password} -Dliquibase.rollbackTag=${指定回滚到的标签版本号}~~

### 切换项目分支问题
> 在开发过程中切换分支可能会出现数据集不统一问题，推荐刷新或重新同步数据库，参考命令：
~~~shell
    1.$ liquibase dropAll
    2.$ liquibase update
~~~

### 上下文
> 在不同的环境中执行不同的SQL
> 如：在DEV环境中执行插入测试数据，在生产环境中不执行。
> 示例：context指定一个或多个环境下才执行，如命令不指定context参数，默认执行。
```
    <changeSet id="20200703_001_init_data_student" author="miaoyj" context="prod,demo">
```

### 最佳实践
> - **DEVOPS方式**
1. 更新：liquibase的update操作交给liquibase-spring，即项目启动时即自动执行update操作
2. 回滚：在jenkins服务器上安装liquibase命令，单独建立一个job，内容：获取的项目代码分支同要发布的分支保持一致，
   切换到resources/liquibase.properties目录下，使用方法1.回滚-指定tag，立即执行回滚操作
3. 项目里liquibase.properties里不要涉及密码等信息，在执行命令时通过参数指定密码 --password   

> **开发、DBA分离方式**
1. 更新：禁用liquibase-spring方式，使用命令生成update.sql交由DBA执行：``` $liquibase --changeLogFile=liquibase/master.xml updateSQL --outputFile=update.sql --contexts=prod ```
2. 回滚：使用回滚方式2. 生成回滚的sql-指定tag，不会执行回滚操作，*推荐*，生成回滚的sql交由DBA执行
3. 项目里liquibase.properties里不要涉及密码等信息，在执行命令时通过参数指定密码 --password

> - **Docker**
>   待更新

###问题
1. liquibase-maven-plugin
    + 3.10.1版本
        1. changeLogFile路径是src/main/resources/liquibase/master.xml
        2. 不支持<includeAll，解决方法：
            + liquibase/master.xml
            
            `<include file="changelog/1.0.0/master.xml" relativeToChangelogFile="true"/>`
            + changelog/1.0.0/master.xml
            
            `<include file="20200702_001_add_tb_student.xml" relativeToChangelogFile="true"/>`
            
        3. addColumn remarks属性会修改表的remarks属性，需要在之后增加修改表备注SQL，独立的changeSet https://github.com/liquibase/liquibase/issues/1210
    + 4.0.0
       1. 支持includeAll，但该标签会将会执行path下所有子目录文件（如指定场景下的SQL文件：执行完addColumn操作后的sql，会提前执行），按自排序(有潜在风险)进行执行，用includeFile指定xml与sql顺序解决。
       2. addColumn remarks属性会修改表的remarks属性，独立的changeSet，需要在之后增加修改表备注SQL https://github.com/liquibase/liquibase/issues/1210
       3. modifyDataType会删除字段备注
       4. spring.liquibase.tag，不会打tag，需要用command或maven plugin，暂时使用tagDatabase解决 https://github.com/liquibase/liquibase/issues/1627
