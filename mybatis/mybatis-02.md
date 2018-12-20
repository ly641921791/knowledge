[TOC]

# Config配置详解

本文介绍MyBatis配置文件

## 配置文件结构

- configuration
    - properties    属性
    - settings      设置
    - typeAliases   类型别名
    - typeHandlers  类型处理器
    - objectFactory 对象工厂
    - plugins       插件
    - environments  环境
        - environment   环境变量
            - transactionManager    事务管理器
            - dataSource            数据源
    - databaseIdProvider    数据库厂商标识
    - mappers   映射器

## properties

用于配置动态属性。可通过`resource`属性引入外部配置文件，也可以通过`property`节点配置动态属性。动态属性通过${}读取，支持OGNL表达式

**使用示例**

配置如下，environments的default属性通过${env}读取properties中定义的env属性

```xml
<configuration>
    <properties resource="xxxx.properties">
        <property name="env" value="development"/>
    </properties>
    <environments default="${env}"/>
</configuration>
```

**生效优先级**

后读到的属性会覆盖先读到的属性，生效优先级为：方法参数 > resource属性引入的外部配置文件 > property节点定义的属性

**属性默认值**

V3.4.2开始支持默认值功能，默认关闭状态，使用如下

```xml
<configuration>
	<properties>
		<!-- 开启默认值功能 -->
		<property name="org.apache.ibatis.parsing.PropertyParser.enable-default-value" value="true"/>
	</properties>
	<environments default="${env:development}"/>
</configuration>
```

自定义默认值分隔符

```xml
<configuration>
	<properties>
		<!-- 开启默认值功能 -->
		<property name="org.apache.ibatis.parsing.PropertyParser.enable-default-value" value="true"/>
		<!-- 自定义默认值分隔符 -->
		<property name="org.apache.ibatis.parsing.PropertyParser.default-value-separator" value="?:"/>
	</properties>
	<environments default="${env?:development}"/>
</configuration>
```

利用OGNL表达式实现默认值功能

```xml
<configuration>
	<environments default="${env != null ? env : 'development'}"/>
</configuration>
```

## environments

### environment

#### transactionManager

事务管理器，MyBatis有两种事务管理器，JDBC/MANAGED

JDBC ：直接使用JDBC的提交和回滚，依赖从数据源得到的连接管理事物作用域

MANAGED ：不做事情，不提交或回滚，通过容器管理事物的生命周期。默认情况会关闭连接，部分容器不希望这样，需要设置关闭该功能

```xml
<transactionManager type="MANAGED">
  <property name="closeConnection" value="false"/>
</transactionManager>
```

可以通过实现TransactionFactory和Transaction接口自定义事物处理

#### dataSource

数据源

	UNPOOLED 为每一个数据库操作创建新连接，并关闭
	POOLED 创建连接池
	JNDI 向配置好的dataSource获取连接，生产环境建议
	其他第三方数据源：C3P0

## mappers

mappers告诉MyBatis配置的SQL语句在哪里，支持配置文件、配置类、扫描包

```xml
<configuration>
	<mappers>
		<mapper resource="UserMapper.xml"/>
		<mapper url="file:///var/mapper/UserMapper.xml"/>
		<mapper class="mapper.UserMapper"/>
		<package name="mapper"/>
	</mappers>
</configuration>
```