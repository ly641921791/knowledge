package com.example.xml;

import com.example.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
		Person person = context.getBean(Person.class);
		System.out.println(person);
	}

}
