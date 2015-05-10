package hello;

/**
 * {@code RemoteHelloIT} <strong>needs documentation</strong>.
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

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestMain.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class TestIT {
    @Value("${local.server.port}")
    private int port;

    private TestRestTemplate rest;

    @Before
    public void setUp()
            throws Exception {
        rest = new TestRestTemplate();
    }

    @Test
    public void shouldXCorrelationIDHeaderRequiredOnlyIfConfigured() {
        final ResponseEntity<String> response = rest.exchange(
                new RequestEntity<String>(GET, URI.create(
                        format("http://localhost:%d/heartbeat", port))),
                String.class);

        assertThat(response.getStatusCode(), is(OK));
    }

    @Test
    public void shouldRejectMissingXCorrelationIDHeader() {
        final HttpHeaders headers = new HttpHeaders();
        final ResponseEntity<String> response = callWith(headers);

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        assertThat(response.getHeaders().get("Warning"), is(singletonList(
                "299 localhost \"Missing X-Correlation-ID header\"")));
    }

    @Test
    public void shouldRejectMultipleXCorrelationIDHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("X-Correlation-ID", "One");
        headers.add("X-Correlation-ID", "Two");
        final ResponseEntity<String> response = callWith(headers);

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        assertThat(response.getHeaders().get("Warning"), is(singletonList(
                "299 localhost \"Multiple X-Correlation-ID headers\"")));
    }

    @Test
    public void shouldAcceptXCorrelationIDHeader() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("X-Correlation-ID", "One");
        final ResponseEntity<String> response = callWith(headers);

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getHeaders().containsKey("Warning"), is(false));
    }

    @Test
    public void shouldAcceptDuplicateXCorrelationIDHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("X-Correlation-ID", "One");
        headers.add("X-Correlation-ID", "One");
        final ResponseEntity<String> response = callWith(headers);

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getHeaders().containsKey("Warning"), is(false));
    }

    public void shouldIgnoreXCorrelationIDHeaderForWrongPath() {
        final ResponseEntity<String> response = callWith(new HttpHeaders());

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getHeaders().containsKey("Warning"), is(false));
    }

    private ResponseEntity<String> callWith(final HttpHeaders headers) {
        return rest.exchange(new RequestEntity<In>(headers, GET,
                        URI.create(format("http://localhost:%d/test", port))),
                String.class);
    }
}
