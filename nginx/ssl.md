配置SSL
-


```conf
server{
	listen 443;
	server_name xxx.com;
	
	ssl on;
	ssl_certificate     xxx.crt;
	ssl_certificate_key xxx.key;
	
	location / {
		proxy_pass http://xxx.com;
	}
}
```



> https://cloud.tencent.com/developer/article/1333913