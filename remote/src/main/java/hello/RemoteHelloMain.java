package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * {@code RemoteHelloMain} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@EnableSwagger2
@SpringBootApplication
public class RemoteHelloMain {
    public static void main(final String... args) {
        SpringApplication.run(RemoteHelloMain.class, args);
    }
}
