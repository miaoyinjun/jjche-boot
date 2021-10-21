# 服务器环境配置

- ## nginx

  > 离线安装

  1. 上传nginx-1.18.0-2.el7.ngx.x86_64.rpm到/opt目录下

  2. 安装

     ```shell
     cd /opt
     rpm -ivh nginx-1.18.0-2.el7.ngx.x86_64.rpm
     systemctl start nginx
     systemctl enable  nginx
     setsebool -P httpd_can_network_connect 1
     
     curl http://localhost:80
     ```

  3. 配置

     1. 上传[nginx.conf](../server/nginx.conf.md)到/etc/nginx目录下
     2. 上传[ui.conf](../server/nginx-ui.conf.md)到/etc/nginx/conf.d目录下
     3. 上传cert 到/etc/nginx下
     4. 配置

     ```shell
     $ cd /etc/nginx/conf.d && rm -rf default.conf && nginx -s reload
     ```

- ## redis

  >  离线安装

  1. 上传jemalloc-3.6.0-1.el7.x86_64.rpm、rpm -ivh redis-3.2.12-2.el7.x86_64.rpm到/opt目录

  2. 安装

     ```shell
     $ rpm -ivh jemalloc-3.6.0-1.el7.x86_64.rpm && rpm -ivh redis-3.2.12-2.el7.x86_64.rpm
     ```

  3. 启动

     ```shell
     $ systemctl enable  redis && systemctl start  redis
     ```

  4. 配置

     ```shell
     $ vim /etc/redis.conf
     ```

     设置如下

     ```sh
     requirepass "xxxxx"
     bind 0.0.0.0
     port 16379
     ```

  5. 重启

     ```shell
     $ systemctl restart  redis
     ```

- ## mysql

  1. root账号连接

     ```shell
     mysql -uroot -p
     ```

  2. 创建数据库boot-admin

     ```sql
     CREATE DATABASE IF NOT EXISTS `boot-admin` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
     ```

  3. 创建用户boot-admin，读写和只读账号

     ```sql
     CREATE USER 'boot-admin_rw'@'%' IDENTIFIED BY '11111';
     GRANT ALL ON ta.* TO 'ta_rw'@'%’;
     
     CREATE USER 'boot-admin_ro'@'%' IDENTIFIED BY '22222';
     GRANT SElECT ON ta.* TO 'ta_ro'@'%';
     ```