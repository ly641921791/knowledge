package sca.example.nacos.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ly
 * @since 2019-04-10 11:57
 **/
@EnableDiscoveryClient
@SpringBootApplication
public class ProducerApplication {

    public static final String SPRING_APPLICATION_NAME = "nacos-producer";

    public static void main(String[] args) {
        System.setProperty("server.port", "8081");
        System.setProperty("spring.application.name", SPRING_APPLICATION_NAME);
        SpringApplication.run(ProducerApplication.class, args);
    }

    @Slf4j
    @RestController
    static class ProducerController {

        @GetMapping("/hello")
        public String hello(String name) {
            log.info("Invoke hello = {}", name);
            return "hello " + name;
        }

    }

}
