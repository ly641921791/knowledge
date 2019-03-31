MySQL
-

[MySQL日志](log.md)





MySQL Master服务器搭建（Docker）

基于镜像5.7

1. 在用户目录创建`mysql_master`目录

2. 在mysql_master目录下创建`conf.d`和`data`目录

conf.d目录用于映射到容器的conf.d目录，容器中默认的conf.d目录有三个配置文件，内容合并后如下

```conf
[mysqld]
skip-host-cache
skip-name-resolve

[mysql]

[mysqldump]
quick
quote-names
max_allowed_packet      = 16M
```

data目录用于映射容器的data目录

3. 准备配置文件

在conf.d目录下新建`mysql.cnf`，内容如下：

```conf
[mysqld]
log-bin=bin_log_file
expire_logs_days=0
max_binlog_size=100M
binlog-format=ROW
server_id=1
skip-host-cache
skip-name-resolve

[mysql]

[mysqldump]
quick
quote-names
max_allowed_packet      = 16M
```

开启二进制日志的配置在mysqld下，我经常写错位置导致开启失败

4. 启动容器

```shell
docker run \
        --name mysql_master \
        -p 3306:3306 \
        -e MYSQL_ROOT_PASSWORD=123456 \
        -v /Users/ly/mysql_master/conf.d:/etc/mysql/conf.d \
        -v /Users/ly/mysql_master/data:/var/lib/mysql \
        -d mysql:5.7
```

5. 连接服务器，执行命令`SHOW VARIABLES LIKE 'log%'`，检查结果