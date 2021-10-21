#  最佳实践

## maven私仓

> 已搭建好maven私有仓库
>
> 本地settings.xml文件配置好发布maven的用户

1. 切换到boot-admin目录

   ```shell
   $ cd boot-admin
   ```

2. 发布SNAPSHOT版本，可**重复**发布

   ```shell
   $ git checkout develop && $ mvn verify install:install deploy:deploy
   ```

3. 发布RELEASE版本，**不可重复**发布

   1. 合并develop到master分支上，并删除pom.xml里的-SNAPSHOT标记

      ```shell
      $ git checkout develop && mvn jgitflow:release-start && mvn jgitflow:release-finish
      ```

   2. 发布

      ```shell
      $ git checkout master && $ mvn verify install:install deploy:deploy
      ```

4. 完成

   

## 开发

1. 复制boot-admin-web-demo并上传到git私库上

2. 从git私库上克隆出boot-admin-web-demo，并切换到develop分支上

   ```shell
   $ git clone https://gitlab.xxx.com/boot-admin-web-demo.git && git checkout develop
   ```

3. 配置BOOT_ADMIN_MAVEN_URL环境变量

4. IDEA导入boot-admin-web-demo

5. 参考[liquibase](../backend/LIQUIBASE.md)的添加顺序，建表

6. 运行Application.main，参考[代码生成](../backend/CODE-GENERATOR.md)，生成CRUD代码

7. 分支版本控制参考[分支管理](directory/GIT.md)

### 后端部署

> **jenkins 配置BOOT_ADMIN_MAVEN_URL环境变量**
>
> 参考[服务器环境配置](SERVER.md)，**确保redis，mysql环境已经准备**

1. 复制重启脚本boot-admin-web-demo/linux/restart.sh到Linux服务器相应目录下，根据需要进行修改

2. 增加运行权限

   ```shell
   $ chmod +x restart.sh
   ```

3. 配置环境变量，可配置的有[系统环境变量](backend/ENV.md)

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

4. 使用jenkins将boot-admin-web-demo.jar复制到Linux服务器，调用restart.sh启动

   ```shell
   $ clean package
   ```

   > **Send build artifacts over SSH**
   >
   > Source files：target/*.jar
   >
   > Remove prefix：target
   >
   > Remote directory：/home/user1/boot-admin-web/tmp/
   >
   > Exec command
   >
   > ```shell
   > cd /home/user1/boot-admin-web/
   > mv -f boot-admin-web.jar  boot-admin-web.jar.bak
   > cp -rf ./tmp/*.jar ./
   > ./restart.sh
   > ```

5. **管理员登录系统，系统管理/版本管理，新建版本，激活**

### 前端部署

1. 准备nginx环境参考[服务器环境配置](SERVER.md)

2. jenkins打包

   ```shell
   $ npm install && npm run build:prod
   ```

3. jenkins部署**Send build artifacts over SSH**

   > Source files：dist/
   >
   > Remove prefix：dist/
   >
   > Remote directory：/etc/nginx/boot-admin-ui-tmp/
   >
   > Exec command
   >
   > ```shell
   > $ cd /etc/nginx/
   > $ rm -rf boot-admin-ui-bak
   > $ mv -f boot-admin-ui boot-admin-ui-bak
   > $ mv -f boot-admin-ui-tmp boot-admin-ui
   > ```