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
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.net.URI;

import static com.jayway.jsonassert.JsonAssert.with;
import static hello.CorrelationIdFilter.WC_CORRELATION_ID;
import static java.lang.String.format;
import static java.net.InetAddress.getLoopbackAddress;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NON_AUTHORITATIVE_INFORMATION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HelloWorldMain.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class HelloWorldIT {
    @Value("${local.server.port}")
    private int port;

    @Inject
    private FeignRemoteHello feign;
    @Value("${security.user.name:user}")
    private String user;
    @Value("${security.user.password}")
    private String password;

    private TestRestTemplate rest;

    @Before
    public void setUp()
            throws Exception {
        reset(feign);
        rest = new TestRestTemplate(user, password);
    }

    @Test
    public void shouldRejectMissingCorrelationIDHeader() {
        final HttpHeaders headers = new HttpHeaders();
        final ResponseEntity<String> response = callWith(headers);

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        assertThat(response.getHeaders().get("Warning"), anyOf(
                is(singletonList(
                        format("%d %s:%d \"Missing X-Correlation-ID header\"",
                                WC_CORRELATION_ID,
                                getLoopbackAddress().getHostName(),
                                port))), is(singletonList(
                        format("%d %s:%d \"Missing X-Correlation-ID header\"",
                                WC_CORRELATION_ID,
                                getLoopbackAddress().getHostAddress(),
                                port)))));
    }

    @Test
    public void shouldSayHelloToBob()
            throws Exception {
        final ArgumentCaptor<In> in = ArgumentCaptor.forClass(In.class);
        when(feign.greet(in.capture())).
                thenReturn(Greeting.builder().message("Hello, Bob!").build());

        final HttpHeaders headers = new HttpHeaders();
        headers.set("X-Correlation-ID", "Mary");
        final ResponseEntity<String> response = callWith(headers);

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getHeaders().containsKey("Warning"), is(false));
        assertThat(response.getHeaders().get("X-Correlation-ID"),
                is(equalTo(singletonList("Mary"))));
        assertThat(response.getHeaders().getContentType().
                isCompatibleWith(APPLICATION_JSON), is(true));
        with(response.getBody()).
                assertEquals("$.message", "Hello, Bob!");

        assertThat(in.getValue().getName(), is(equalTo("Bob")));
    }

    @Test
    public void shouldDieGracefully() {
        when(feign.greet(any())).
                thenThrow(new RuntimeException("No dice, Bob."));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("X-Correlation-ID", "Mary");
        final ResponseEntity<String> response = callWith(headers);

        assertThat(response.getStatusCode(),
                is(NON_AUTHORITATIVE_INFORMATION));
        assertThat(response.getHeaders().get("Warning"), is(equalTo(
                singletonList("199 remote-hello \"Service unavailable\""))));
        assertThat(response.getHeaders().get("X-Correlation-ID"),
                is(equalTo(singletonList("Mary"))));
        assertThat(response.getHeaders().getContentType().
                isCompatibleWith(APPLICATION_JSON), is(true));
        with(response.getBody()).
                assertEquals("$.message", "No dice, Bob.");
    }

    private ResponseEntity<String> callWith(final HttpHeaders headers) {
        return rest.exchange(new RequestEntity<In>(headers, GET, URI.create(
                        format("http://localhost:%d/hello/Bob", port))),
                String.class);
    }
}
