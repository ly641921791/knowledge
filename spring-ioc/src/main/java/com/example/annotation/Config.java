package com.example.annotation;

import com.example.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

	/**
	 * 类型返回值类型，id默认方法名
	 *
	 * @return
	 */
	@Bean
	public Person person() {
		Person person = new Person();
		person.setName("ly");
		person.setAge(25);
		return person;
	}

}
