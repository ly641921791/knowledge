package com.sz.sfw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ly
 */
@MapperScan("com.sz.sfw")
@SpringBootApplication
public class SfwApplication {

    public static void main(String[] args) {
        SpringApplication.run(SfwApplication.class, args);
    }

}
