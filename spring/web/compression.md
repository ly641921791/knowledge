开启Gzip
-

Spring Boot响应压缩有以下配置：

|配置|说明|默认值|
|---|---|---|
|server.compression.enabled|是否开启|false|
|server.compression.mime-types|需要压缩的内容的类型|"text/html", "text/xml", "text/plain", "text/css", "text/javascript", "application/javascript", "application/json", "application/xml"
|server.compression.min-response-size|需要压缩的内容的最小长度，默认单位：B|2048，等同于配置2KB|
|server.compression.excluded-user-agents|不压缩指定用户代理的请求|空|

> 参考文档 https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/#how-to-enable-http-response-compression