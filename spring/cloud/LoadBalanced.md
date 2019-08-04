LoadBalanced Source Code
-

本文以 RestTemplate 为例，介绍 @LoadBalanced 如何实现其负载均衡

##### @LoadBalanced

首先查看 LoadBalanced 源码

``` java
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Qualifier
public @interface LoadBalanced {
}
```

@LoadBalanced 继承了 @Qualifier 注解，拥有了Bean依赖注入时的筛选功能

##### spring.factories

打开 @LoadBalanced 所在jar包的 spring.factories 文件，我们可以发现如下三行

```
org.springframework.cloud.client.loadbalancer.AsyncLoadBalancerAutoConfiguration,\
org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration,\
org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancerAutoConfiguration,\
```

第一行负责 AsyncRestTemplate 客户端负载均衡的自动配置

第二行负责 RestTemplate 客户端负载均衡的自动配置

第三行负责 WebClient 客户端负载均衡的自动配置

##### LoadBalancerAutoConfiguration

通过 LoadBalancerAutoConfiguration 源码分析 RestTemplate 如何实现负载均衡

``` java
@Configuration
@ConditionalOnClass(RestTemplate.class)
@ConditionalOnBean(LoadBalancerClient.class)
@EnableConfigurationProperties(LoadBalancerRetryProperties.class)
public class LoadBalancerAutoConfiguration {

	@LoadBalanced
	@Autowired(required = false)
	private List<RestTemplate> restTemplates = Collections.emptyList();

	@Autowired(required = false)
	private List<LoadBalancerRequestTransformer> transformers = Collections.emptyList();

	@Bean
	public SmartInitializingSingleton loadBalancedRestTemplateInitializerDeprecated(
			final ObjectProvider<List<RestTemplateCustomizer>> restTemplateCustomizers) {
		return () -> restTemplateCustomizers.ifAvailable(customizers -> {
			for (RestTemplate restTemplate : LoadBalancerAutoConfiguration.this.restTemplates) {
				for (RestTemplateCustomizer customizer : customizers) {
					customizer.customize(restTemplate);
				}
			}
		});
	}

	@Bean
	@ConditionalOnMissingBean
	public LoadBalancerRequestFactory loadBalancerRequestFactory(
			LoadBalancerClient loadBalancerClient) {
		return new LoadBalancerRequestFactory(loadBalancerClient, this.transformers);
	}

	@Configuration
	@ConditionalOnMissingClass("org.springframework.retry.support.RetryTemplate")
	static class LoadBalancerInterceptorConfig {

		@Bean
		public LoadBalancerInterceptor ribbonInterceptor(
				LoadBalancerClient loadBalancerClient,
				LoadBalancerRequestFactory requestFactory) {
			return new LoadBalancerInterceptor(loadBalancerClient, requestFactory);
		}

		@Bean
		@ConditionalOnMissingBean
		public RestTemplateCustomizer restTemplateCustomizer(
				final LoadBalancerInterceptor loadBalancerInterceptor) {
			return restTemplate -> {
				List<ClientHttpRequestInterceptor> list = new ArrayList<>(
						restTemplate.getInterceptors());
				list.add(loadBalancerInterceptor);
				restTemplate.setInterceptors(list);
			};
		}

	}

	/**
	 * Auto configuration for retry mechanism.
	 */
	@Configuration
	@ConditionalOnClass(RetryTemplate.class)
	public static class RetryAutoConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public LoadBalancedRetryFactory loadBalancedRetryFactory() {
			return new LoadBalancedRetryFactory() {
			};
		}

	}

	/**
	 * Auto configuration for retry intercepting mechanism.
	 */
	@Configuration
	@ConditionalOnClass(RetryTemplate.class)
	public static class RetryInterceptorAutoConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public RetryLoadBalancerInterceptor ribbonInterceptor(
				LoadBalancerClient loadBalancerClient,
				LoadBalancerRetryProperties properties,
				LoadBalancerRequestFactory requestFactory,
				LoadBalancedRetryFactory loadBalancedRetryFactory) {
			return new RetryLoadBalancerInterceptor(loadBalancerClient, properties,
					requestFactory, loadBalancedRetryFactory);
		}

		@Bean
		@ConditionalOnMissingBean
		public RestTemplateCustomizer restTemplateCustomizer(
				final RetryLoadBalancerInterceptor loadBalancerInterceptor) {
			return restTemplate -> {
				List<ClientHttpRequestInterceptor> list = new ArrayList<>(
						restTemplate.getInterceptors());
				list.add(loadBalancerInterceptor);
				restTemplate.setInterceptors(list);
			};
		}

	}

}
```

第一步 从容器中获得 @LoadBalanced 标记的 RestTemplate

第二步 从容器中获得 LoadBalancerClient 构造出 LoadBalancerRequestFactory

第三步 从容器中获得 LoadBalancerClient 和 LoadBalancerRequestFactory 构造出 LoadBalancerInterceptor

第四步 从容器中获得 LoadBalancerInterceptor 构造出 RestTemplateCustomizer

第五步 从容器中获得 RestTemplateCustomizer 构造出 SmartInitializingSingleton，通过该 Bean 将第四步构造的负载均衡拦截器添加到第一步得到的 RestTemplate 中

##### LoadBalancerInterceptor

RestTemplate 在 createRequest 中通过 InterceptingClientHttpRequestFactory 创建出 InterceptingClientHttpRequest，
InterceptingClientHttpRequest 在 execute 时，会触发 LoadBalancerInterceptor 的拦截功能，下面看一下拦截器的拦截逻辑

``` java
public class LoadBalancerInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
			final ClientHttpRequestExecution execution) throws IOException {
		final URI originalUri = request.getURI();
		String serviceName = originalUri.getHost();
		Assert.state(serviceName != null,
				"Request URI does not contain a valid hostname: " + originalUri);
		return this.loadBalancer.execute(serviceName,
				this.requestFactory.createRequest(request, body, execution));
	}

}
```

LoadBalancerInterceptor 使用 LoadBalancerRequestFactory 将原有 request 包装后通过 LoadBalancerClient 执行，最终实现负载均衡功能
