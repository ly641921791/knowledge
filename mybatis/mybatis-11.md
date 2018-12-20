[TOC]

# 源码-初始化

本文分析一波MyBatis的初始化过程，源码版本V3.4.6

首先回顾一波MyBatis的使用流程

1 创建SqlSessionFactoryBuilder
2 通过SqlSessionFactoryBuilder的build方法构建SqlSessionFactory
3 通过SqlSessionFactory创建SqlSession用来执行SQL

## SqlSessionFactory的创建过程

**SqlSessionFactoryBuilder**

```java
public class SqlSessionFactoryBuilder {
    
  public SqlSessionFactory build(Reader reader){}
  public SqlSessionFactory build(Reader reader, String environment){}
  public SqlSessionFactory build(Reader reader, Properties properties) {}
  public SqlSessionFactory build(Reader reader, String environment, Properties properties){}

  public SqlSessionFactory build(InputStream inputStream){}
  public SqlSessionFactory build(InputStream inputStream, String environment){}
  public SqlSessionFactory build(InputStream inputStream, Properties properties){}
  public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties){}
    
  public SqlSessionFactory build(Configuration config) {
    return new DefaultSqlSessionFactory(config);
  }
}
```

源码很简洁，主要做了下面几件事

1 创建XMLConfigBuilder
	1.1 创建Configuration
	1.2 保存Configuration
	1.3 保存其他入参
2 解析配置
	2.1 调用parse解析配置文件，对Configuration各属性进行赋值
	2.2 返回Configuration
2 创建DefaultSqlSessionFactory
	2.1 保存入参Configuration
	2.2 返回DefaultSqlSessionFactory

**parse**

解析配置的核心过程就在parse，下面是parse的源码，对代码做了简化

```java
public class XMLConfigBuilder extends BaseBuilder {
	public Configuration parse() {
		parseConfiguration(parser.evalNode("/configuration"));
		return configuration;
	}

	private void parseConfiguration(XNode root) {
		propertiesElement(root.evalNode("properties"));
		Properties settings = settingsAsProperties(root.evalNode("settings"));
		loadCustomVfs(settings);
		typeAliasesElement(root.evalNode("typeAliases"));
		pluginElement(root.evalNode("plugins"));
		objectFactoryElement(root.evalNode("objectFactory"));
		objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"));
		reflectorFactoryElement(root.evalNode("reflectorFactory"));
		settingsElement(settings);
		environmentsElement(root.evalNode("environments"));
		databaseIdProviderElement(root.evalNode("databaseIdProvider"));
		typeHandlerElement(root.evalNode("typeHandlers"));
		mapperElement(root.evalNode("mappers"));
	}
}
```

源码很简洁，主要做了下面几件事

1 解析获得配置文件中的configuration节点
	1.1 依次解析configuration的子节点
	
**mapperElement**

解析过程基本大致相同，这里重点看mapper节点的处理过程，源码如下

```java
public class XMLConfigBuilder extends BaseBuilder {
	private void mapperElement(XNode parent) throws Exception {
		if (parent != null) {
			for (XNode child : parent.getChildren()) {
			    // 1 解析 <package name="xxx"> 的情况
				if ("package".equals(child.getName())) {
					String mapperPackage = child.getStringAttribute("name");
					configuration.addMappers(mapperPackage);
				} else {
					String resource = child.getStringAttribute("resource");
					String url = child.getStringAttribute("url");
					String mapperClass = child.getStringAttribute("class");
					// 2 解析 <mapper resource="xxx"> 的情况
					if (resource != null && url == null && mapperClass == null) {
						ErrorContext.instance().resource(resource);
						InputStream inputStream = Resources.getResourceAsStream(resource);
						XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
						mapperParser.parse();
					}
					// 3 解析 <mapper url="xxx"> 的情况
					else if (resource == null && url != null && mapperClass == null) {
						ErrorContext.instance().resource(url);
						InputStream inputStream = Resources.getUrlAsStream(url);
						XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, url, configuration.getSqlFragments());
						mapperParser.parse();
					}
					// 4 解析 <mapper class="xxx"> 的情况
					else if (resource == null && url == null && mapperClass != null) {
					    // 4.1 通过反射获得Mapper类
						Class<?> mapperInterface = Resources.classForName(mapperClass);
						// 4.2 添加并解析Mapper类
						configuration.addMapper(mapperInterface);
					} else {
						throw new BuilderException("A mapper element may only specify a url, resource or class, but not more than one.");
					}
				}
			}
		}
	}
}
```

mapper的4种配置方式，这里都有解析，后面分析一下Mapper类的解析，毕竟现在的配置很多都注解化了。

**addMapper**

源码如下，解析过程已通过注释标注

```java
public class MapperRegistry{
	public <T> boolean hasMapper(Class<T> type) {
		return knownMappers.containsKey(type);
	}
	public <T> void addMapper(Class<T> type) {
	    // 1 检查是否接口
		if (type.isInterface()) {
		    // 2 检查是否解析过
			if (hasMapper(type)) {
				throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
			}
			boolean loadCompleted = false;
			try {
			    // 3 创建Mapper代理工厂，保存到已解析列表
				knownMappers.put(type, new MapperProxyFactory<T>(type));
				// 4 解析Mapper类中通过注解配置的SQL
				MapperAnnotationBuilder parser = new MapperAnnotationBuilder(config, type);
				parser.parse();
				loadCompleted = true;
			} finally {
			    // 5 若解析出现异常，移除已解析列表
				if (!loadCompleted) {
					knownMappers.remove(type);
				}
			}
		}
	}
}
```

过程3创建了Mapper代理工厂，方便使用时创建Mapper代理。过程4为解析SQL的过程

**parse**

源码如下，解析过程已通过注释标注

```java
public class MapperAnnotationBuilder {
	public void parse() {
		String resource = type.toString();
		// 1 检查是否加载过资源
		if (!configuration.isResourceLoaded(resource)) {
		    // 2 解析Mapper类同名mapper.xml
			loadXmlResource();
			// 3 添加到已加载资源列表
			configuration.addLoadedResource(resource);
			assistant.setCurrentNamespace(type.getName());
			// 4 自带缓存功能没有使用过，分析略过
			parseCache();
			parseCacheRef();
			// 5 遍历方法，解析出SQL
			Method[] methods = type.getMethods();
			for (Method method : methods) {
			try {
				if (!method.isBridge()) {
				    // 6 解析方法
					parseStatement(method);
				}
				} catch (IncompleteElementException e) {
					configuration.addIncompleteMethod(new MethodResolver(this, method));
				}
			}
		}
		parsePendingMethods();
	}
}
```

重点看过程6

**parseStatement**

解析结果

```java
public class MapperAnnotationBuilder {
	void parseStatement(Method method) {
	    // 1 准备工作，略
	    
	    // 2 解析SQL类型和SQL语句
	    SqlSource sqlSource = getSqlSourceFromAnnotations(method, parameterTypeClass, languageDriver);
	    
	    // 3 其他注解解析，略
	    
	    // 4 将解析结果封装成MappedStatement保存
		assistant.addMappedStatement(
			mappedStatementId,
			sqlSource,
			statementType,
			sqlCommandType,
			fetchSize,
			timeout,
			null,
			parameterTypeClass,
			resultMapId,
			getReturnType(method),
			resultSetType,
			flushCache,
			useCache,
			false,
			keyGenerator,
			keyProperty,
			keyColumn,
			null,
			languageDriver,
			options != null ? nullOrEmpty(options.resultSets()) : null);
	}
}
```