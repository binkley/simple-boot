package hello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static io.github.robwin.swagger.test.SwaggerAssertions.assertThat;
import static java.lang.String.format;

/**
 * {@code RemoteHelloSwaggerApiIT} validates the implementation against the API.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RemoteHelloMain.class)
@IntegrationTest("server.port=0")
@WebAppConfiguration
public class RemoteHelloSwaggerApiIT {
    @Value("${local.server.port}")
    private int port;

    @Test
    public void shouldMatchApi() {
        assertThat(api()).isEqualTo(
                getClass().getResource("/remote-hello-swagger2.yml")
                        .getPath());
    }

    private String api() {
        return format("http://localhost:%d/remote-hello/v2/api-docs", port);
    }
}
