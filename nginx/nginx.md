[TOC]

# Nginx文档

官网文档：http://nginx.org/en/docs/

## 初学者指南

### 配置文件结构

```log
...				# 全局块
events {		# events块
    ...
}
http {			# http块
    ...
    server {	# server块
        ...	
        location [PATTERN] {
            ...
        }
    }
    server {
    }
}
```

###配置静态服务器

```log
server {
    location / {
        root /data/www;
    }
    location /images/ {
        root /data;
    }
}
```

http://localhost/images/example.png -> /data/images/example.png

http://localhost/directory/index.html -> /data/www/directory/index.html

### 配置代理服务器





## 使用

### 常用命令

- nginx -s stop ：快速关闭nginx
- nginx -s quit ：正常关闭nginx
- nginx -s reload ：重新加载配置文件
- nginx -s reopen ：重新打开日志文件
- nginx -c configName ：指定配置文件启动
- nginx -t ：测试配置文件是否可用
- nginx -v ：显示版本
- nginx -V ：显示版本、编译器版本、配置参数

### 配置文件

配置文件可能出现在三个地方

- /etc/nginx/nginx.conf
- /usr/local/etc/nginx/nginx.conf
- /usr/local/nginx/nginx.conf



默认配置文件 ：conf/nginx.conf



#### 配置文件结构

```log
|- 指令
	|- 普通指令
	|- 数组指令
	|- 行动指令
|- 上下文/块
```



### 功能配置

#### 配置反向代理

```property
#运行用户
#user somebody;

#启动进程,通常设置成和cpu的数量相等
worker_processes  1;

#全局错误日志
error_log  D:/Tools/nginx-1.10.1/logs/error.log;
error_log  D:/Tools/nginx-1.10.1/logs/notice.log  notice;
error_log  D:/Tools/nginx-1.10.1/logs/info.log  info;

#PID文件，记录当前启动的nginx的进程ID
pid        D:/Tools/nginx-1.10.1/logs/nginx.pid;

#工作模式及连接数上限
events {
   worker_connections 1024;    #单个后台worker process进程的最大并发链接数
}

#设定http服务器，利用它的反向代理功能提供负载均衡支持
http {
   #设定mime类型(邮件支持类型),类型由mime.types文件定义
   include       D:/Tools/nginx-1.10.1/conf/mime.types;
   default_type  application/octet-stream;

   #设定日志
   log_format  main  '[$remote_addr] - [$remote_user] [$time_local] "$request" '
                     '$status $body_bytes_sent "$http_referer" '
                     '"$http_user_agent" "$http_x_forwarded_for"';

   access_log    D:/Tools/nginx-1.10.1/logs/access.log main;
   rewrite_log     on;

   #sendfile 指令指定 nginx 是否调用 sendfile 函数（zero copy 方式）来输出文件，对于普通应用，
   #必须设为 on,如果用来进行下载等应用磁盘IO重负载应用，可设置为 off，以平衡磁盘与网络I/O处理速度，降低系统的uptime.
   sendfile        on;
   #tcp_nopush     on;

   #连接超时时间
   keepalive_timeout  120;
   tcp_nodelay        on;

   #gzip压缩开关
   #gzip  on;

   #设定实际的服务器列表
   upstream zp_server1{
       server 127.0.0.1:8089;
   }

   #HTTP服务器
   server {
       #监听80端口，80端口是知名端口号，用于HTTP协议
       listen       80;

       #定义使用www.xx.com访问
       server_name  www.helloworld.com;

       #首页
       index index.html

       #指向webapp的目录
       root D:_WorkspaceProjectgithubzpSpringNotesspring-securityspring-shirosrcmainwebapp;

       #编码格式
       charset utf-8;

       #代理配置参数
       proxy_connect_timeout 180;
       proxy_send_timeout 180;
       proxy_read_timeout 180;
       proxy_set_header Host $host;
       proxy_set_header X-Forwarder-For $remote_addr;

       #反向代理的路径（和upstream绑定），location 后面设置映射的路径
       location / {
           proxy_pass http://zp_server1;
       }

       #静态文件，nginx自己处理
       location ~ ^/(images|javascript|js|css|flash|media|static)/ {
           root D:_WorkspaceProjectgithubzpSpringNotesspring-securityspring-shirosrcmainwebappiews;
           #过期30天，静态文件不怎么更新，过期可以设大一点，如果频繁更新，则可以设置得小一点。
           expires 30d;
       }

       #设定查看Nginx状态的地址
       location /NginxStatus {
           stub_status           on;
           access_log            on;
           auth_basic            "NginxStatus";
           auth_basic_user_file  conf/htpasswd;
       }

       #禁止访问 .htxxx 文件
       location ~ /.ht {
           deny all;
       }

       #错误处理页面（可选择性配置）
       #error_page   404              /404.html;
       #error_page   500 502 503 504  /50x.html;
       #location = /50x.html {
       #    root   html;
       #}
   }
}
```

#### 配置负载均衡

```property
http {
    #设定mime类型,类型由mime.type文件定义
   include       /etc/nginx/mime.types;
   default_type  application/octet-stream;
   #设定日志格式
   access_log    /var/log/nginx/access.log;

   #设定负载均衡的服务器列表
   upstream load_balance_server {
       #weigth参数表示权值，权值越高被分配到的几率越大
       server 192.168.1.11:80   weight=5;
       server 192.168.1.12:80   weight=1;
       server 192.168.1.13:80   weight=6;
   }

  #HTTP服务器
  server {
       #侦听80端口
       listen       80;

       #定义使用www.xx.com访问
       server_name  www.helloworld.com;

       #对所有请求进行负载均衡请求
       location / {
           root        /root;                 #定义服务器的默认网站根目录位置
           index       index.html index.htm;  #定义首页索引文件的名称
           proxy_pass  http://load_balance_server ;#请求转向load_balance_server 定义的服务器列表

           #以下是一些反向代理的配置(可选择性配置)
           #proxy_redirect off;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
           #后端的Web服务器可以通过X-Forwarded-For获取用户真实IP
           proxy_set_header X-Forwarded-For $remote_addr;
           proxy_connect_timeout 90;          #nginx跟后端服务器连接超时时间(代理连接超时)
           proxy_send_timeout 90;             #后端服务器数据回传时间(代理发送超时)
           proxy_read_timeout 90;             #连接成功后，后端服务器响应时间(代理接收超时)
           proxy_buffer_size 4k;              #设置代理服务器（nginx）保存用户头信息的缓冲区大小
           proxy_buffers 4 32k;               #proxy_buffers缓冲区，网页平均在32k以下的话，这样设置
           proxy_busy_buffers_size 64k;       #高负荷下缓冲大小（proxy_buffers*2）
           proxy_temp_file_write_size 64k;    #设定缓存文件夹大小，大于这个值，将从upstream服务器传

           client_max_body_size 10m;          #允许客户端请求的最大单文件字节数
           client_body_buffer_size 128k;      #缓冲区代理缓冲用户端请求的最大字节数
       }
   }
}
```

#### 配置多个地址转发

```property
http {
   #此处省略一些基本配置

   upstream product_server{
       server www.helloworld.com:8081;
   }

   upstream admin_server{
       server www.helloworld.com:8082;
   }

   upstream finance_server{
       server www.helloworld.com:8083;
   }

   server {
       #此处省略一些基本配置
       #默认指向product的server
       location / {
           proxy_pass http://product_server;
       }

       location /product/{
           proxy_pass http://product_server;
       }

       location /admin/ {
           proxy_pass http://admin_server;
       }

       location /finance/ {
           proxy_pass http://finance_server;
       }
   }
}
```

#### 配置HTTPS反向代理

- HTTPS 的固定端口号是 443，不同于 HTTP 的 80 端口
- SSL 标准需要引入安全证书，所以在 nginx.conf 中你需要指定证书和它对应的 key

```property
#HTTP服务器
 server {
     #监听443端口。443为知名端口号，主要用于HTTPS协议
     listen       443 ssl;

     #定义使用www.xx.com访问
     server_name  www.helloworld.com;

     #ssl证书文件位置(常见证书文件格式为：crt/pem)
     ssl_certificate      cert.pem;
     #ssl证书key位置
     ssl_certificate_key  cert.key;

     #ssl配置参数（选择性配置）
     ssl_session_cache    shared:SSL:1m;
     ssl_session_timeout  5m;
     #数字签名，此处使用MD5
     ssl_ciphers  HIGH:!aNULL:!MD5;
     ssl_prefer_server_ciphers  on;

     location / {
         root   /root;
         index  index.html index.htm;
     }
 }
```

#### 静态页配置

```property
worker_processes  1;

events {
   worker_connections  1024;
}

http {
   include       mime.types;
   default_type  application/octet-stream;
   sendfile        on;
   keepalive_timeout  65;

   gzip on;
   gzip_types text/plain application/x-javascript text/css application/xml text/javascript application/javascript image/jpeg image/gif image/png;
   gzip_vary on;

   server {
       listen       80;
       server_name  static.zp.cn;

       location / {
           root /app/dist;
           index index.html;
           #转发任何请求到 index.html
       }
   }
}
```

#### 跨域配置

web 领域开发中，经常采用前后端分离模式。这种模式下，前端和后端分别是独立的 web 应用程序，例如：后端是 Java 程序，前端是 React 或 Vue 应用。

各自独立的 web app 在互相访问时，势必存在跨域问题。解决跨域问题一般有两种思路：

CORS

在后端服务器设置 HTTP 响应头，把你需要运行访问的域名加入加入 Access-Control-Allow-Origin 中。

jsonp

把后端根据请求，构造json数据，并返回，前端用 jsonp 跨域。

这两种思路，本文不展开讨论。

需要说明的是，nginx 根据第一种思路，也提供了一种解决跨域的解决方案。

举例：www.helloworld.com 网站是由一个前端 app ，一个后端 app 组成的。前端端口号为 9000， 后端端口号为 8080。

前端和后端如果使用 http 进行交互时，请求会被拒绝，因为存在跨域问题。来看看，nginx 是怎么解决的吧：

首先，在 enable-cors.conf 文件中设置 cors ：

```property
# allow origin list
set $ACAO '*';

# set single origin
if ($http_origin ~* (www.helloworld.com)$) {
 set $ACAO $http_origin;
}

if ($cors = "trueget") {
   add_header 'Access-Control-Allow-Origin' "$http_origin";
   add_header 'Access-Control-Allow-Credentials' 'true';
   add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS';
   add_header 'Access-Control-Allow-Headers' 'DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type';
}

if ($request_method = 'OPTIONS') {
 set $cors "${cors}options";
}

if ($request_method = 'GET') {
 set $cors "${cors}get";
}

if ($request_method = 'POST') {
 set $cors "${cors}post";
}
```

接下来，在你的服务器中 include enable-cors.conf 来引入跨域配置：

```property
# ----------------------------------------------------
# 此文件为项目 nginx 配置片段
# 可以直接在 nginx config 中 include（推荐）
# 或者 copy 到现有 nginx 中，自行配置
# www.helloworld.com 域名需配合 dns hosts 进行配置
# 其中，api 开启了 cors，需配合本目录下另一份配置文件
# ----------------------------------------------------
upstream front_server{
 server www.helloworld.com:9000;
}
upstream api_server{
 server www.helloworld.com:8080;
}

server {
 listen       80;
 server_name  www.helloworld.com;

 location ~ ^/api/ {
   include enable-cors.conf;
   proxy_pass http://api_server;
   rewrite "^/api/(.*)$" /$1 break;
 }

 location ~ ^/ {
   proxy_pass http://front_server;
 }
}
```