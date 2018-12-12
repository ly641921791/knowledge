doGetBean


1  解析bean真实名，解析别名

2  获取单例bean

	根据不同条件依次从单例bean缓存、早期单例bean缓存、单例bean工厂中获取bean返回

3  如果 若获得的bean不是空 & 参数是空

	3.1  若不是 factoryBean 则直接返回
	3.2  获取通过 factoryBean 获取对象，过程略

4  否则

	4.1  若 父beanFactory不是空 & 当前beanFactory没有bean定义信息，通过父beanFactory获取bean并返回

	4.2  标记bean正在创建

	4.3  通过bean定义信息创建bean，过程略

5  类型转换


