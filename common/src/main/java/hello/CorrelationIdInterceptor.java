package hello;

import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.io.IOException;
import java.util.List;

import static java.util.UUID.randomUUID;

/**
 * {@code XCorrelationIdInterceptor} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@Component
@ConditionalOnProperty("headers.correlation-id.client.paths")
@ConfigurationProperties(prefix = "headers.correlation-id.client",
        ignoreUnknownFields = false)
public class CorrelationIdInterceptor
        implements ClientHttpRequestInterceptor {
    private static final PathMatcher matcher = new AntPathMatcher();

    @NotEmpty
    @Setter
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<String> paths;

    @Override
    public ClientHttpResponse intercept(final HttpRequest request,
            final byte[] body, final ClientHttpRequestExecution execution)
            throws IOException {
        if (needsHeader(request))
            request.getHeaders().
                    add("X-Correlation-ID", randomUUID().toString());

        return execution.execute(request, body);
    }

    private boolean needsHeader(final HttpRequest request) {
        if (request.getHeaders().containsKey("X-Correlation-ID"))
            return false;

        final String uri = request.getURI().getPath();
        return paths.stream().
                anyMatch(path -> matcher.match(path, uri));
    }
}
