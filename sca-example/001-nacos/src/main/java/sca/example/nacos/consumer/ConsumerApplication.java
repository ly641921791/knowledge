package sca.example.nacos.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sca.example.nacos.producer.ProducerApplication;

/**
 * @author ly
 * @since 2019-04-10 13:35
 **/
@EnableDiscoveryClient
@SpringBootApplication
public class ConsumerApplication {

    public static final String SPRING_APPLICATION_NAME = "nacos-consumer";

    public static void main(String[] args) {
        System.setProperty("server.port", "8082");
        System.setProperty("spring.application.name", SPRING_APPLICATION_NAME);
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Slf4j
    @RestController
    static class ConsumerController {

        @Autowired
        private LoadBalancerClient loadBalancerClient;

        @GetMapping("/consumer")
        public String consumer() {
            ServiceInstance serviceInstance = loadBalancerClient.choose(ProducerApplication.SPRING_APPLICATION_NAME);
            String url = serviceInstance.getUri() + "/hello?name=consumer";
            String result = new RestTemplate().getForObject(url, String.class);
            log.info("Invoke {} ,return {}", url, result);
            return result;
        }

    }

}
