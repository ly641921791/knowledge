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


[官方配置文档](http://maven.apache.org/ref/3.6.0/maven-settings/settings.html)


### 向Maven中央仓库发布jar

https://www.jianshu.com/p/2c29f50e4ec0

https://blog.csdn.net/z960339491/article/details/80334384

### 通过Git搭建Maven仓库

###### 变量属性

外部文件支持变量属性，配置filtering为true，可以使指定文件支持变量属性

``` xml
<project>
    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
                <includes>
                    <include>xxx.txt</include>
                </includes>
            </resource>
        </resources>
    </build>
</project>

```

[教程](build_by_git.md)

[maven的三大生命周期](https://www.cnblogs.com/huxinga/p/6740897.html)

[maven构建生命周期和各种plugin插件](https://blog.csdn.net/zhaojianting/article/details/80321488)

[依赖冲突问题](https://mp.weixin.qq.com/s/zGlPbimTuYy7UbmgCmnkqA)


[Apache Maven 最全教程，7000 字总结！](https://mp.weixin.qq.com/s/BDBhXBHbIxAf6lJNNb8xnA)

[重量级！Maven史上最全教程，看了必懂](https://www.cnblogs.com/hzg110/p/6936101.html)

[私服搭建 - Nexus](https://mp.weixin.qq.com/s/dnFz_p2LFV9OQaapKPiZ9A)
