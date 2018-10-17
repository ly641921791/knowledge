package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ly
 */
@RestController
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping("/test1")
	public String test1(String msg, @RequestParam String msg1) {
		return msg;
	}

	@RequestMapping("/test2")
	public Map test2(@RequestParam Map map) {
		return map;
	}
}

@Configuration
class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/*").allowedOrigins("*").allowedHeaders("token");
	}
}

/**
 * 特殊字符过滤器
 */
@Slf4j
@WebFilter
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class SpecialCharacterFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("SpecialCharacterFilter-------------------------初始化");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 使用包装类替换request
		chain.doFilter(new SpecialCharacterRequestWrapper((HttpServletRequest) request), response);
	}

	@Override
	public void destroy() {
		log.info("SpecialCharacterFilter-------------------------销毁");
	}
}

class SpecialCharacterRequestWrapper extends HttpServletRequestWrapper {

	/**
	 * Constructs a request object wrapping the given request.
	 *
	 * @param request The request to wrap
	 * @throws IllegalArgumentException if the request is null
	 */
	public SpecialCharacterRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {
		return replaceSpecialCharacter(super.getParameter(name));
	}

	@Override
	public String[] getParameterValues(String name) {
		return replaceSpecialCharacter(super.getParameterValues(name));
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> oldParams = super.getParameterMap();
		Map<String, String[]> newParams = new HashMap<>(oldParams.size());
		oldParams.forEach((k, v) -> newParams.put(k, replaceSpecialCharacter(v)));
		return oldParams;
	}

	/**
	 * 替换指定特殊字符
	 *
	 * @param oldStr 旧字符串
	 * @return 新字符串
	 */
	private String replaceSpecialCharacter(String oldStr) {
		return oldStr == null ? null : oldStr.replace("cnm", "***");
	}

	/**
	 * 替换指定特殊字符
	 *
	 * @param oldStr 旧字符串
	 * @return 新字符串
	 */
	private String[] replaceSpecialCharacter(String[] oldStr) {
		if (oldStr == null) {
			return null;
		}
		for (int i = 0, length = oldStr.length; i < length; i++) {
			oldStr[i] = replaceSpecialCharacter(oldStr[i]);
		}
		return oldStr;
	}
}