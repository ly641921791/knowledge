分页插件
-

[PageHelper](https://pagehelper.github.io)是基于MyBatis拦截器实现的分页插件，通过拦截目标SQL，根据目标SQL生成统计页数SQL并执行得到分页结果

##### 相关依赖

###### 分页插件

``` xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>5.1.10</version>
</dependency>
```

###### Spring Boot Starter

``` xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.2.12</version>
</dependency>
```

##### 配置插件

helperDialect 表示数据库类型，用于分析目标SQL并生成统计页数SQL

虽然PageHelper 支持自动识别数据库类型，但某些情况存在[BUG](https://github.com/pagehelper/Mybatis-PageHelper/issues/414)，
建议最好配置一下

###### MyBatis方式

在 MyBatis 配置文件中配置分页插件

``` xml
<plugins>
    <plugin interceptor="com.github.pagehelper.PageInterceptor">
        <property name="helperDialect" value="mysql"/>
	</plugin>
</plugins>
```

###### Spring方式

``` xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
  <property name="plugins">
    <array>
      <bean class="com.github.pagehelper.PageInterceptor">
        <property name="properties">
          <value>helperDialect=mysql</value>
        </property>
      </bean>
    </array>
  </property>
</bean>
```

###### Spring Boot

Spring Boot 引入 Starter 后默认已经配置PageInterceptor，可以通过配置文件修改其属性，如：

``` properties
pagehelper.helper-dialect = mysql
```

##### 如何使用

###### 静态方法实现分页

通过`PageHelper.startPage()`方法设置页码和每页记录数，后面紧跟的第一次查询被分页

``` java
PageHelper.startPage(1, 10);
List<User> list = userMapper.list();
PageInfo page = new PageInfo(list);
page.getTotal();
```

###### 参数名实现分页

``` properties
pagehelper.helper-dialect = mysql
pagehelper.support-methods-arguments = true
pagehelper.params = pageNum=pageNum;pageSize=pageSize;
```

上面的配置表示，当参数中同时存在参数名为`pageNum`和`pageSize`的参数时，方法分页，如：

``` java
// 两个方法都可以分页
public interface user {
	List<User> list(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
	List<User> list(PageParam param);
}

@Data
public class PageParam {
	private int pageNum;
	private int pageSize
}
```

###### 自定义统计页数SQL

5.0.4手写COUNT语句支持 https://segmentfault.com/q/1010000013444285

> 官网文档 https://pagehelper.github.io/docs/howtouse/