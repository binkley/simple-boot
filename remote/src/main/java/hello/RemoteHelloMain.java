package hello;

import com.mangofactory.swagger.plugin.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSwagger
@SpringBootApplication
public class RemoteHelloMain {
    public static void main(final String... args) {
        SpringApplication.run(RemoteHelloMain.class, args);
    }
}
