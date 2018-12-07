







```java
public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			// 准备上下文刷新
			prepareRefresh();

			// 子类刷新beanFactory，并从子类处获得beanFactory
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			// 准备beanFactory
			prepareBeanFactory(beanFactory);

			try {
				// 由子类后置处理beanFactory
				postProcessBeanFactory(beanFactory);

				// 实例化、排序、调用beanFactoryPostProcessor
				invokeBeanFactoryPostProcessors(beanFactory);

				// 实例化、排序、注册beanPostProcessor
				registerBeanPostProcessors(beanFactory);

				// Initialize message source for this context.
				initMessageSource();

				// Initialize event multicaster for this context.
				initApplicationEventMulticaster();

				// 由子类实例化特殊的bean
				onRefresh();

				// Check for listener beans and register them.
				registerListeners();

				// 实例化其他所有非懒加载bean
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
```

