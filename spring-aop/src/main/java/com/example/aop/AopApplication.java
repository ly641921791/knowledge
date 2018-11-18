package com.example.aop;

import com.example.aop.bean.Target;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * @author ly
 */
@Import(Target.class)
@SpringBootApplication
public class AopApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AopApplication.class, args);
        Target target = context.getBean(Target.class);
        target.target(null);
    }

}
