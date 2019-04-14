

Nginx
	轻量级的Web/反向代理/电子邮件代理服务器
	特定：占用内存少，并发强
	服务器
	默认端口：80
	
	进入sbin目录
	启动 ./nginx
	其他
		./nginx -s stop		快速关闭
		./nginx -s quit		等worker线程处理完毕关闭
		./nginx -s reload	重新加载配置文件
		./nginx -s reopen	重新打开日志文件


	用于解决C10K问题：一个服务器同时连接10k客户端
	
	基本的nginx由master进程和worker进程组成。master读取配置文件，维护worker，worker对请求处理

	
	nginx无法访问
		1：可能是端口未开放：/sbin/iptables -I INPUT -p tcp --dport 80 -j ACCEPT
	
正向代理和反向代理的区别
	正向代理：客户端通过代理服务器向服务端发出请求，服务端并不知道请求的源头
	反向代理：客户端向服务器发出请求，服务器只是反向代理服务器，会将请求发送至真正的服务器，客户端并不知道请求最终的流向
	用途：
		正向代理典型用途就是跳过防火墙访问外网，如通过VPN翻墙
		反向代理典型用途就是负载均衡
	安全性：
		正向代理客户端可以通过它访问任意网站且隐藏自身，不安全，需要采取安全措施仅对授权的客户端提供服务
		反向代理对外透明，客户端并不知道自己访问的是代理服务器

CentOS7安装nginx服务器
1：编译环境安装
	安装 nginx 需要先将官网下载的源码进行编译，编译依赖 gcc 环境，如果没有 gcc 环境，则需要安装：
	yum install gcc-c++
2：pcre安装
	nginx 的 http 模块使用 pcre 来解析正则表达式，所以需要安装
	yum install -y pcre pcre-devel
3：zlib 安装
	zlib 库提供了很多种压缩和解压缩的方式， nginx 使用 zlib 对 http 包的内容进行 gzip ，所以需要在 Centos 上安装 zlib 库。
	yum install -y zlib zlib-devel
4：OpenSSL 安装
	OpenSSL 是一个强大的安全套接字层密码库，囊括主要的密码算法、常用的密钥和证书封装管理功能及 SSL 协议，并提供丰富的应用程序供测试或其它目的使用。
	nginx 不仅支持 http 协议，还支持 https（即在ssl协议上传输http），所以需要在 Centos 安装 OpenSSL 库。
	yum install -y openssl openssl-devel

5：下载nginx源码
	推荐使用命令下载
	wget -c https://nginx.org/download/nginx-1.10.1.tar.gz
6：解压并进入目录
	tar -zxvf nginx-1.10.1.tar.gz
	cd nginx-1.10.1
7：配置
	使用默认配置
	./configure
8：编译安装
	make
	make install
	







































