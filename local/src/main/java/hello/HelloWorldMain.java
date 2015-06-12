package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableFeignClients
@EnableHystrix
@EnableSwagger2
@SpringBootApplication
public class HelloWorldMain {
    public static void main(final String... args) {
        SpringApplication.run(HelloWorldMain.class, args);
    }
}
