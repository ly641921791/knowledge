package com.example.xml.controller;

import com.example.xml.bean.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author ly
 */
@RestController
public class XmlController {

	@RequestMapping(value = "/user",
			consumes = MediaType.APPLICATION_XML_VALUE,
			produces = MediaType.APPLICATION_XML_VALUE
	)
	public User user(@RequestBody User user) {
		return user;
	}

	@RequestMapping(value = "user")//,produces = MediaType.APPLICATION_JSON_VALUE)
	public User user() {
		return new User(1, "ly");
	}

}

@Configuration
class MessageConverterConfig1 extends WebMvcConfigurerAdapter {
	@Override
	public void
	configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.xml();
		builder.indentOutput(true);
		converters.add(new MappingJackson2XmlHttpMessageConverter(builder.build()));
	}
}