package com.example;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author ly
 */
@SpringBootApplication
public class Application {

    @Data
    static class User {

        private int id;
        private String name;
        private int age;

        public void setId(int id){
            this.id=id;
        }

    }

    @Mapper
    interface UserMapper{
        @Select("SELECT * FROM user")
        User select();
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class,args);
        UserMapper mapper = context.getBean(UserMapper.class);
        mapper.select();
    }

}