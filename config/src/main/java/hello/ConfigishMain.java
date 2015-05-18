package hello;

import com.mangofactory.swagger.plugin.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * {@code RemoteHelloMain} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@EnableSwagger
@SpringBootApplication
public class ConfigishMain {
    public static void main(final String... args) {
        SpringApplication.run(ConfigishMain.class, args);
    }
}
