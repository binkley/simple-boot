package hello;

import com.mangofactory.swagger.plugin.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * {@code RemoteHelloMain} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@EnableConfigServer
@EnableSwagger
@SpringBootApplication
public class ConfigServiceMain {
    public static void main(final String... args) {
        SpringApplication.run(ConfigServiceMain.class, args);
    }
}
