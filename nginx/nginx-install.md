[toc]

# Nginx 安装教程

## yum 安装 Nginx

CentOS 7系统可以通过yum命令安装软件，步骤如下：

- 配置nginx源

```log
rpm -ivh http://nginx.org/packages/centos/7/noarch/RPMS/nginx-release-centos-7-0.el7.ngx.noarch.rpm
```

在/etc/yum.repos.d/目录下出现nginx.repo文件说明配置成功

- 安装nginx

```log
yum install -y nginx
```

执行`nginx -v`，控制台打印出nginx版本说明安装成功

执行`whereis nginx`，控制台会打印出nginx相关的目录

> 参考 ：https://www.centos.bz/2018/01/centos-7%EF%BC%8C%E4%BD%BF%E7%94%A8yum%E5%AE%89%E8%A3%85nginx/

