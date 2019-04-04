AbstractApplicationContext
-

AbstractApplicationContext

```java
public class AbstractApplicationContext{
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			/*
				准备刷新
				1. 设置启动时间
				2. 重置启停状态
				3. 初始化属性资源
				4. 校验必须的环境属性
				5. 重置早期应用事件
			 */
			prepareRefresh();

			/*
				刷新并获得BeanFactory
				1. 刷新BeanFactory
				2. 获得BeanFactory
				注：两个方法都由子类实现，可以理解为通知子类刷新BeanFactory并获得
			 */
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			/*
				准备BeanFactory
				1. 设置Bean的类加载器
				2. 设置Bean的SpEL表达式解析器。通过BeanFactory后置器修改SpEL表达式#{和}，达到自定义匹配的效果
				3. 设置属性编辑器的注册器
				
				4. 添加Bean后置处理器：ApplicationContextAwareProcessor
				
				5. registerResolvableDependency不懂
				
				6. 添加Bean后置处理器：ApplicationListenerDetector，用于发现ApplicationListener
				
				7. 添加Bean后置处理器：LoadTimeWeaverAwareProcessor，LoadTimeWeaver（加载期织入）
				
				8. 将一些环境属性注册到BeanFactory
			 */
			prepareBeanFactory(beanFactory);

			try {
				/*
					后置处理BeanFactory。由子类实现
				 */
				postProcessBeanFactory(beanFactory);

				/*
					调用后置处理器处理BeanFactory
					1. BeanFactory是否属于BeanDefinitionRegistry区分操作
						1.1 若属于
							1.1.1 将来自上下文的后置处理器根据是否属于BeanDefinitionRegistryPostProcessor分类
								  后置处理Bean定义信息
							1.1.2 处理BeanFactory中的BeanDefinitionRegistryPostProcessor
								  实例化、排序、使用PriorityOrdered类型的处理器处理Bean定义信息，保存在1.1.1的分类中
								  实例化、排序、使用Ordered类型的处理器处理Bean定义信息，保存在1.1.1的分类中
								  实例化、排序、使用剩余的处理器处理Bean定义信息，保存在1.1.1的分类中
							1.1.3 使用全部的BeanDefinitionRegistryPostProcessor类型的后置处理BeanFactory
								  使用其他的处理器处理BeanFactory
						1.2 不属于
							使用来自上下文的处理器处理BeanFactory
					2. 处理BeanFactory中的BeanFactoryPostProcessor，类似1.1.2，实例化、排序、使用
					注：每个处理器使用后都会记录，避免出现同一处理器处理多次的情况
				 */
				invokeBeanFactoryPostProcessors(beanFactory);

				/*
					注册Bean后置处理器
					1. 添加处理器BeanPostProcessorChecker
					2. 将后置处理器按照PriorityOrdered、Ordered、未实现排序，MergedBeanDefinitionPostProcessor类型的也按照该顺序在最后
					3. 添加处理器ApplicationListenerDetector
				 */
				registerBeanPostProcessors(beanFactory);

				/*
					初始化消息资源，BeanFactory有则使用，没有则使用默认的
				 */
				initMessageSource();

				/*
					初始化应用事件发布器
				 */
				initApplicationEventMulticaster();

				/*
					刷新容器，由子类重写
				 */
				onRefresh();

				// Check for listener beans and register them.
				/*
					注册监听器
					1. 将容器监听器注册到发布器
					2. 将BeanFactory中的监听器注册
					3. 发布早期的应用事件
				 */
				registerListeners();

				/*
					实例化所有非lazy单例Bean
					1. BeanFactory添加ConversionService
					2. BeanFactory添加StringValueResolver
					3. 实例化LoadTimeWeaverAware类型的Bean
					4. 缓存全部的Bean定义元数据
					5. 实例化剩余的Bean
				 */
				finishBeanFactoryInitialization(beanFactory);

				// 完成实例化
				finishRefresh();
			}

			catch (BeansException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Exception encountered during context initialization - " +
							"cancelling refresh attempt: " + ex);
				}

				// Destroy already created singletons to avoid dangling resources.
				destroyBeans();

				// Reset 'active' flag.
				cancelRefresh(ex);

				// Propagate exception to caller.
				throw ex;
			}

			finally {
				// Reset common introspection caches in Spring's core, since we
				// might not ever need metadata for singleton beans anymore...
				resetCommonCaches();
			}
		}
	}
}
```
