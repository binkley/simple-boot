package hello;

/**
 * {@code ConfigishIT} tests {@link ConfigishMain}.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */

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
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConfigishMain.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class ConfigishIT {
    @Value("${local.server.port}")
    private int port;

    private TestRestTemplate rest;

    @Before
    public void setUp()
            throws Exception {
        rest = new TestRestTemplate("user", "secret");
    }

    @Test
    public void shouldRequireXCorrelationIDHeader() {
        final ResponseEntity<String> response = rest.exchange(
                new RequestEntity<String>(POST, URI.create(
                        format("http://localhost:%d/config/test", port))),
                String.class);

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        assertThat(response.getHeaders().get("Warning"), is(singletonList(
                "299 localhost \"Missing X-Correlation-ID header\"")));
    }

    @Test
    public void shouldGetConfiguration()
            throws Exception {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("X-Correlation-ID", "Mary");
        final RequestEntity<?> request = new RequestEntity<>(headers, GET,
                URI.create(format("http://localhost:%d/config/test", port)));
        final ResponseEntity<String> response = rest.
                exchange(request, String.class);

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getHeaders().get("X-Correlation-ID"),
                is(equalTo(singletonList("Mary"))));
        assertThat(response.getHeaders().getContentType().
                isCompatibleWith(APPLICATION_JSON), is(true));
        with(response.getBody()).
                assertEquals("$.one", "two");
        with(response.getBody()).
                assertEquals("$['three.four']", 5);
    }
}
