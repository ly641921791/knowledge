package com.example.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author ly
 */
@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {

        //SpringApplication.run(SecurityApplication.class,args);

        System.out.println(System.nanoTime());
        System.out.println(System.nanoTime());

        long id = 27144295230255L;
        long ii = 2714429523025511111L;

        long id1 = 1127144295230255111L;

    }

}
