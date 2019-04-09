Maven
- 

### 安装

CentOS 安装

    yum install maven

检查

    mvn -version

### 命令

    mvn clean package


### 版本范围

|Range|Meaning|
|---|---|
|1.0|version = 1.0|
|(,1,0)|version < 1.0|
|(,1,0]|version <= 1.0|
|(1.0,)|version > 1.0|
|[1.0,)|version >= 1.0|
|(1.0,2.0)|1.0 < version < 2.0|
|[1.0,2.0]|1.0 <= version <= 2.0|
|(,1.0],[1.2,)|	version <= 1.0 or version >= 1.2|
|(,1.1),(1.1,)|	version != 1.1|


### 向Maven中央仓库发布jar

https://www.jianshu.com/p/2c29f50e4ec0

https://blog.csdn.net/z960339491/article/details/80334384

### 通过Git搭建Maven仓库

[教程](build_by_git.md)

