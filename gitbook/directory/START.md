# 快速了解

## 开发环境
- 语言：Java 8
- IDE(JAVA)： IDEA 安装lombok插件
- 依赖管理：Maven
- 数据库：MySQL5.7+
- 缓存：Redis

## 获取源码
> 获取后端源码

```shell
$ git clone https://gitee.com/miaoyinjun/boot-admin
```

> 获取前端源码

```shell
$ git clone https://gitee.com/miaoyinjun/boot-admin-ui
```



## 本地

### 1. 创建数据库

```sql
CREATE DATABASE IF NOT EXISTS `boot-admin` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

### 2. 环境变量配置

> **配置文件**与**参数**2种，**任选一种配置方式**

- #### 配置文件方式（**推荐**）

> 编辑环境变量配置文件，并在底部写入以下配置，参考[系统环境变量](backend/ENV.md)

>  打开配置文件

```shell
$ vim ~/.bash_profile
```

```sh
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

> 使其生效

```shell
$ source ~/.bash_profile
```

- #### 参数方式

  > **maven、jar以-D后接变量名=变量值传递环境变量**

  - main：BOOT_ADMIN_DB_HOST="localhost";BOOT_ADMIN_DB_PASSWORD="123";
  - maven：mvn  spring-boot:run -DBOOT_ADMIN_DB_HOST=localhost -DBOOT_ADMIN_DB_PASSWORD=123
  - jar：java -jar -DBOOT_ADMIN_DB_HOST=localhost -DBOOT_ADMIN_DB_PASSWORD=123

  

### 3. 后端运行

- #### IDEA方式

  ![image-20210926140929555](../assets/image-20210926140929555.png)

  

- #### maven方式

> 切换到项目根目录

```shell
$ cd boot-admin
```

> 安装必要依赖

```shell
$ cd boot-admin-parent && mvn clean install
```

> 运行demo

```shell
$ cd ../ && cd boot-admin-web-demo && mvn clean compile spring-boot:run
```



- **访问接口文档**：http://localhost:8801/sba/api/doc.html



### 4. 前端运行

> 切换目录
`$ cd boot-admin-ui`

> 安装依赖
`$ npm i`

> 运行
`$ npm run dev`

> 访问https://localhost:8013/
   用户名：admin，密码：123456

## DOCKER
> **前台**运行，方便查看日志

### 1. 后端

> 切换到项目根目录

`$ cd boot-admin `

> 安装必要依赖

`$ cd boot-admin-parent && mvn clean install`

`$ cd ../ && cd boot-admin-web-demo`

`$ mvn clean install && mvn clean install && mvn dockerfile:build && docker-compose -f ./docker/docker-compose.yml --env-file=./docker/.env up`

> 访问接口文档地址：http://localhost:8801/sba/api/doc.html

### 2. 前端
`$ cd ../ && cd boot-admin-ui`
`$ npm i && npm run build:docker && docker-compose -f ./docker/docker-compose.yml --env-file=./docker/.env up`

> 访问http://localhost 用户名：admin，密码：123456