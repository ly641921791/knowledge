[toc]

# JWT介绍

全称：Json Web Token，是一种特殊的Token

## 组成

由三部分组成：头部（header）、载荷（payload）、签证（signature），格式如下：

xxxx.yyyy.zzzz

### 头部

用于声明类型和加密算法，内容如下：

{
	"typ":"JWT",
	"alg":"HS256"
}

typ声明类型为JWT
alg声明加密算法为HS256，可以使用其他加密算法

将头部通过Base64加密得到xxxx

### 荷载

用于存放信息，包含标准声明和其他声明，不建议存放敏感信息

标准声明包含一些jwt的基本属性，如下

iss: jwt签发者
sub: jwt所面向的用户
aud: 接收jwt的一方
exp: jwt的过期时间，这个过期时间必须要大于签发时间
nbf: 定义在什么时间之前，该jwt都是不可用的.
iat: jwt的签发时间
jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。

其他声明包含自定义的信息

荷载内容也是JSON字符串，存放着声明信息的k-v对。声明信息不强制使用，因此最简单的荷载是{}

将荷载通过Base64加密得到yyyy

### 签证

用于jwt的验证

将xxxx.yyyy通过头部声明的加密算法进行盐加密得到zzzz，盐存放在服务端，保证jwt的安全性

