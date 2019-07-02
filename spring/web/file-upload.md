# Spring Boot 文件上传

本文介绍Spring Boot搭建的Web项目如何上传文件

#### 配置

Spring Boot默认提供了文件上传相关的组件，相关配置类`MultipartAutoConfiguration`，配置属性类`MultipartProperties`

常用配置

|属性名|默认值|说明|
|---|---|---|
|spring.servlet.multipart.location|无|存储文件的临时目录，CentOS系统建议修改，原因见常见问题1|
|spring.servlet.multipart.max-file-size|1MB|文件最大长度限制|
|spring.servlet.multipart.max-request-size|10MB|请求最大长度限制|

注：也可以通过配置类修改配置，配置方式略，当配置类和配置文件同时存在时，配置文件失效

#### 使用

通过`@RequestParam`接收`MultipartFile`类型参数获得上传的文件，代码如下

```java
public class DemoController{ 
	public void upload(@RequestParam MultipartFile file){
	}
}
```

#### 常见问题

1. Failed to parse multipart servlet request; nested exception is java.io.IOException: The temporary upload location 
[/tmp/tomcat.xxx.xxx/work/Tomcat/localhost/ROOT] is not valid。

项目启动后，会在/tmp目录下创建文件夹存储临时文件，CentOS系统会定时清理指定目录文件，最终导致找不到目录而报错

解决方案：创建并通过`spring.servlet.multipart.location`指定其他目录

> 相关参考
>https://blog.51cto.com/breaklinux/2149624?source=dra
>http://blog.51cto.com/kusorz/2051877?utm_source=oschina-app

2. Could not parse multipart servlet request; nested exception is java.io.IOException: The temporary upload location 
[/tmp/tomcat.xxx.xxx/work/Tomcat/localhost/ROOT] is not valid。

同问题1


