package hello;

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
import java.net.UnknownHostException;

import static hello.CorrelationIdFilter.WC_CORRELATION_ID;
import static java.lang.String.format;
import static java.net.InetAddress.getLoopbackAddress;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

/**
 * {@code CorrelationIdHeaderIT} provides integration testing for the {@code
 * X-Correlation-ID} header.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestMain.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class CorrelationIdHeaderIT {
    @Value("${local.server.port}")
    private int port;

    private TestRestTemplate rest;

    @Before
    public void setUp()
            throws Exception {
        rest = new TestRestTemplate();
    }

    @Test
    public void shouldCorrelationIdHeaderRequiredOnlyIfConfigured() {
        final ResponseEntity<String> response = rest.exchange(
                new RequestEntity<String>(GET, URI.create(
                        format("http://localhost:%d/heartbeat", port))),
                String.class);

        assertThat(response.getStatusCode(), is(OK));
    }

    @Test
    public void shouldRejectMissingCorrelationIdHeader() {
        final HttpHeaders headers = new HttpHeaders();
        final ResponseEntity<String> response = callWith(headers, "test");

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        assertThat(response.getHeaders().get("Warning"), anyOf(is(
                singletonList(
                        format("%d %s:%d \"Missing X-Correlation-ID header\"",
                                WC_CORRELATION_ID,
                                getLoopbackAddress().getHostName(), port))),
                is(singletonList(
                        format("%d %s:%d \"Missing X-Correlation-ID header\"",
                                WC_CORRELATION_ID,
                                getLoopbackAddress().getHostAddress(),
                                port)))));
    }

    @Test
    public void shouldRejectMultipleCorrelationIdHeaders()
            throws UnknownHostException {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("X-Correlation-ID", "One");
        headers.add("X-Correlation-ID", "Two");
        final ResponseEntity<String> response = callWith(headers, "test");

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        assertThat(response.getHeaders().get("Warning"), anyOf(is(
                singletonList(
                        format("%d %s:%d \"Multiple X-Correlation-ID headers\"",
                                WC_CORRELATION_ID,
                                getLoopbackAddress().getHostName(), port))),
                is(singletonList(
                        format("%d %s:%d \"Multiple X-Correlation-ID headers\"",
                                WC_CORRELATION_ID,
                                getLoopbackAddress().getHostAddress(),
                                port)))));
    }

    @Test
    public void shouldAcceptCorrelationIdHeader() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("X-Correlation-ID", "One");
        final ResponseEntity<String> response = callWith(headers, "test");

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getHeaders().containsKey("Warning"), is(false));
    }

    @Test
    public void shouldAcceptDuplicateCorrelationIdHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("X-Correlation-ID", "One");
        headers.add("X-Correlation-ID", "One");
        final ResponseEntity<String> response = callWith(headers, "test");

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getHeaders().containsKey("Warning"), is(false));
    }

    public void shouldIgnoreCorrelationIdHeaderForWrongPath() {
        final ResponseEntity<String> response = callWith(new HttpHeaders(),
                "test");

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getHeaders().containsKey("Warning"), is(false));
    }

    public void shouldAutomateCorrelationIdHeader() {
        final HttpHeaders headers = new HttpHeaders();
        final ResponseEntity<String> response = callWith(headers,
                "automated");

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getHeaders().containsKey("Warning"), is(false));
    }

    private ResponseEntity<String> callWith(final HttpHeaders headers,
            final String uri) {
        return rest.exchange(new RequestEntity<In>(headers, GET, URI.create(
                        format("http://localhost:%d/%s", port, uri))),
                String.class);
    }
}
