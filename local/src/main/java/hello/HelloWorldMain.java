package hello;

import com.mangofactory.swagger.plugin.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients
@EnableSwagger
@SpringBootApplication
public class HelloWorldMain {
    public static void main(final String... args) {
        SpringApplication.run(HelloWorldMain.class, args);
    }
}
