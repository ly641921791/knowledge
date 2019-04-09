git搭建maven仓库
-

使用Git搭建maven仓库，无需额外的服务器，流程简单，推荐个人使用。以Github为例

### 创建仓库

创建Git仓库，无需任何配置，maven仓库就创建完成，
地址为：https://raw.githubusercontent.com/${username}/${repository_name}/master

例如：Git仓库名为mvnrepository，用户名为ly641921791，maven仓库地址为https://raw.githubusercontent.com/ly641921791/mvnrepository/master

### 发布项目到maven

1. 首先拉取仓库到本地

例如在用户目录使用命令：git clone xxx，执行后在用户目录下出现mvnrepository文件夹

2. 配置项目的pom文件

在<project>下添加如下节点，表示发布到本地仓库

```
    <distributionManagement>
        <repository>
            <id>local-mvn-repo</id>
            <url>file:${user.dir}/mvnrepository</url>
        </repository>
        <snapshotRepository>
            <id>local-mvn-repo</id>
            <url>file:${user.dir}/mvnrepository</url>
        </snapshotRepository>
    </distributionManagement>
```

3. 发布项目到本地仓库

执行命令：git deploy，执行后，mvnrepository文件夹内出现发布的jar包

4. 提交本地仓库到远程

将本地仓库的文件提交到远程仓库，过程略

### 使用仓库

配置pom文件，在<project>下新增如下节点

```
	<repositories>
        <repository>
            <id>mvnrepo</id>
            <name>mvn repository</name>
            <url>https://raw.githubusercontent.com/ly641921791/mvnrepository/master</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>
```