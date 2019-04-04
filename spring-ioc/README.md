Spring IOC
-

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