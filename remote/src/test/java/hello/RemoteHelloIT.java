package hello;

/**
 * {@code RemoteHelloIT} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */

import hello.RemoteHelloController.In;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URI;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RemoteHelloMain.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class RemoteHelloIT {
    @Value("${local.server.port}")
    private int port;

    private TestRestTemplate template;

    @Before
    public void setUp()
            throws Exception {
        template = new TestRestTemplate("user", "secret");
    }

    @Test
    public void shouldGreetBob()
            throws Exception {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        final RequestEntity<In> request = new RequestEntity<>(
                In.builder().name("Bob").build(), headers, POST,
                URI.create(format("http://localhost:%d/greet", port)));
        final ResponseEntity<String> response = template.
                exchange(request, String.class);

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getHeaders().getContentType().
                isCompatibleWith(APPLICATION_JSON), is(true));
        with(response.getBody()).
                assertEquals("$.message", "Hats off to you, Bob!");
    }
}
