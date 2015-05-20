package hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;

/**
 * {@code XCorrelationIdInterceptor} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@Component
@ConditionalOnProperty("headers.correlation-id.client.paths")
public class CorrelationIdInterceptor
        implements ClientHttpRequestInterceptor {
    private static final Pattern comma = Pattern.compile("\\s*,\\s*");
    private static final PathMatcher matcher = new AntPathMatcher();

    private final List<String> paths;

    @Inject
    public CorrelationIdInterceptor(
            @Value("${headers.correlation-id.client.paths}")
            final String paths) {
        this.paths = asList(comma.split(paths));
    }

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
