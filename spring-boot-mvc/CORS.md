[TOC]

# CORS

CORS是一个W3C标准，全称是"跨域资源共享"（Cross-origin resource sharing）。

为了解决跨域AJAX请求不能发送而出现的。

当浏览器发现AJAX请求跨域，会进行一些特殊处理，若目标服务器按照CORS标准进行了配置，就可以处理跨域请求。

## 简单请求处理流程

简单请求的定义在文末

简单请求处理流程如下：

1. 浏览器判断请求跨域，在请求头中添加`Origin`字段，说明本次请求的来源，包括协议、域名、端口

2. 服务器收到请求，根据请求头中的`Origin`字段，判断是否处理这个请求

2.1 若该请求的来源在服务器允许的范围，则处理请求，并在响应头加入`Access-Control-`相关的字段
2.2 若该请求的来源不在服务器允许范围，则返回响应，浏览器在响应头未发现`Access-Control-`相关的字段，报错跨域请求

## 非简单请求处理流程



## 如何携带Cookie

```log

1. 拦截请求，设置请求头

// 解决跨越问题 
response.setHeader("Access-Control-Allow-Origin", "*");
 response.setHeader("Access-Control-Allow-Methods", "*");
 response.setHeader("Access-Control-Max-Age", "3600"); 
response.setHeader("Access-Control-Allow-Headers", "DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization,SessionToken"); 
// 允许跨域请求中携带cookie
 response.setHeader("Access-Control-Allow-Credentials", "true");


2. 此时跨域携带cookie会报错

Response to preflight request doesn't pass access control check: 
A wildcard '*' cannot be used in the 'Access-Control-Allow-Origin' header when the credentials flag is true. 
Origin 'null' is therefore not allowed access. 
The credentials mode of an XMLHttpRequest is controlled by the withCredentials attribute.

大概意思是，当Credentials是true时，Access-Control-Allow-Origin不允许为*


    3. 解决该问题

response.setHeader("Access-Control-Allow-Origin", “null”);

```


## 简单请求

满足下面三个条件

1. 下面三种请求方式

- HEAD
- POST
- GET

2. 下面五种请求头

- Accept
- Accept-Language
- Content-Type
- Content-Language
- Last-Event-ID

3. Content-Type仅支持下面三个

- application/x-www-form-urlencoded
- multipart/form-data
- text/plain



## 参考文档

> w3c ：https://www.w3.org/TR/cors/

> http://www.ruanyifeng.com/blog/2016/04/cors.html