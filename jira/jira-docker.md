Docker安装、破解、配置JIRA-7.13

1 准备镜像

docker pull cptactionhank/atlassian-jira-software
docker pull mysql:5.7

2 安装mysql

8720端口是为了可以用过外部访问，若不需要外部访问，可以不暴露端口。

docker run \
	--name mysql-jira \
	--restart always \
	-p 8720:3306 \
	-e MYSQL_ROOT_PASSWORD=Zcj123456 \
	-e MYSQL_DATABASE=jira \
	-e MYSQL_USER=jira \
	-e MYSQL_PASSWORD=jira \
	-d \
	mysql:5.7

3 安装jira

docker run --detach --restart always --link mysql-jira:mysql --publish 8721:8080 cptactionhank/atlassian-jira-software

4 破解jira

一般用户替换破解包可能会权限不足，这里使用root用户操作。假如容器id为7509371edd48

root用户进入容器

    sudo docker exec -ti -u root 7509371edd48 bash

删除原jar

    rm -rf /opt/atlassian/jira/atlassian-jira/WEB-INF/lib/atlassian-extras-3.1.2.jar

退出容器

    exit

将破解包移到容器里

    sudo docker cp atlassian-extras-3.1.2.jar 7509371edd48:/opt/atlassian/jira/atlassian-jira/WEB-INF/lib/

重启容器

    docker restart 7509371edd48

5 配置jira

注意mysql指定库的字符集必须是utf8，否则创建项目会报错


配置
https://www.cnblogs.com/lion.net/p/6281668.html#%E6%89%93%E5%BC%80%E6%B5%8F%E8%A7%88%E5%99%A8httplocalhost20012

安装  https://blog.csdn.net/x6582026/article/details/81671468


破解地址  https://www.ilanni.com/?p=12119

创建项目报错 解决方案
	进入容器查看日志，发现数据库字符集的问题，修改即可。

移动破解jar到容器
	https://baijiahao.baidu.com/s?id=1574094506379030&wfr=spider&for=pc

破解文件替换 权限不足 解决方案
	获取root权限进入	https://blog.csdn.net/u012763794/article/details/80943472





