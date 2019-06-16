过滤器
-

Spring Boot添加拦截器

方法1 声明为组件，Spring会自动将其注册到Web容器中，可以通过@WebFilter配置拦截的路径

```java
@Component
public class MyFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    chain.doFilter(request, response);
	}
	
	@Override
	public void destroy() {
	
	}
}
```

方法2 @WebFilter声明为拦截器，通过@ServletComponentScan扫描相关组件即可


```java
@Configuration
@ServletComponentScan
public class WebConfig {
    
}
```


```java
@WebFilter
public class MyFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    chain.doFilter(request, response);
	}
	
	@Override
	public void destroy() {
	
	}
}
```

方法3 通过FilterRegistrationBean

```java
@Bean
public FilterRegistrationBean myFilter() {
	FilterRegistrationBean registration = new FilterRegistrationBean();
	registration.setFilter(new MyFilter());
	...
	registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
	return registration;
}

@Bean
public FilterRegistrationBean registration(MyFilter filter) {
	FilterRegistrationBean registration = new FilterRegistrationBean(filter);
	registration.setEnabled(false);
	return registration;
}
```

过滤器的顺序：@Order或Ordered接口