Spring IOC
-

Spring中beanFactory和ApplicationContext的联系和区别。

接口
	1.ApplicationContext接口间接继承了BeanFactory接口
实现类
	1.BeanFactory当需要调用时读取配置文件，生成类的实例，只要读入的Bean配置正确，其它配置错误也不会报错；ApplicationContext在初始化时把XML配置读入内存，并校验，占用内存且应多较多时，启动慢。
	2.BeanFactory只有基本的IOC功能，ApplicationContext提供了AOP、web等功能

http://blog.csdn.net/hi_kevin/article/details/7325554

```
BeanFactory     Bean工厂顶级接口
	|-  HierarchicalBeanFactory     层级化的Bean工厂接口，增加了父工厂支持


AliasRegistry   Alias的CURD接口
	|-  SimpleAliasRegistry         AliasRegistry的简单实现
	|-  BeanDefinitionRegistry      BeanDefinition的CURD接口
	
	
SingletonBeanRegistry   单例Bean的CURD接口
```

### 源码

- [AbstractApplicationContext](AbstractApplicationContext.md)