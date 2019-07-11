JWT
-

###### 什么是JWT

全称是Json Web Token，是一种特殊的Token，默认不加密，不应该存放私密信息，一般放在请求头Authorization

###### JWT的结构

JWT由三部分组成：头部（header）、载荷（payload）、签名（signature），格式如下：

```
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLnlKjmiLciLCJwYXNzd29yZCI6IjEyMzQ1NiIsImlzcyI6IuetvuWPkeiAhSIsImlkIjoic2xtMTIzIiwiZXhwIjoxNTU1MDQ5NzI1LCJ1c2VybmFtZSI6InNsbSJ9.x5ZoTW5NS4wBCIK61v4YCGi8bsveifBwnsMNBQz8s_s
```

头部

header用于声明类型和加密算法，内容如下：

``` json
{
    "typ":"JWT",    // 声明类型为JWT
    "alg":"HS256"   // 声明加密算法为HS256
}
```

将header通过Base64加密得到`eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9`

荷载

payload用于存放信息，包含标准声明和其他声明，不建议存放敏感信息

标准声明包含一些jwt的基本属性，如下

iss: jwt签发者
sub: jwt所面向的用户
aud: 接收jwt的一方
exp: jwt的过期时间，这个过期时间必须要大于签发时间
nbf: 定义在什么时间之前，该jwt都是不可用的.
iat: jwt的签发时间
jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。

其他声明包含自定义的信息，如：姓名

``` json
{
    "sub": "123456",
    "name": "admin",
    "admin": true
}
```

将payload通过Base64加密得到`eyJzdWIiOiLnlKjmiLciLCJwYXNzd29yZCI6IjEyMzQ1NiIsImlzcyI6IuetvuWPkeiAhSIsImlkIjoic2xtMTIzIiwiZXhwIjoxNTU1MDQ5NzI1LCJ1c2VybmFtZSI6InNsbSJ9`

签名

signature用于验证JWT是否被更改，使用header中加密算法将header和payload与密钥secret加密获得

也就是 signature = HS256( Base64(header) + "." + Base64(payload) , secret )

最后 JWT = header + "." + payload + "." + signature