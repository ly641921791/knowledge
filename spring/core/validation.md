Spring Validation
-

Spring Validation支持两种验证方式

- 编程式验证 ：使用Spring提供的验证工具在代码中手动验证，本文不做介绍（没用过）
- 声明式验证 ：使用注解声明验证规则及错误提示

##### Spring Validation

Spring Core Validation ： https://docs.spring.io/spring/docs/5.1.8.RELEASE/spring-framework-reference/core.html#validation

##### Spring Web Validation

无论是Web MVC还是WebFlux，LocalValidatorFactoryBean都会被作为默认的验证工具，用于验证Controller方法的参数

Spring Web Validation ： https://docs.spring.io/spring/docs/5.1.8.RELEASE/spring-framework-reference/web.html#mvc-config-validation

##### Spring Boot Validation

Spring Boot Configuration Validation : https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-external-config.html#boot-features-external-config-validation
Spring Boot ： https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-validation.html