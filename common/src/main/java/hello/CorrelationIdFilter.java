package hello;

import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.lang.String.format;
import static java.net.InetAddress.getByName;
import static java.util.Collections.list;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

/**
 * {@code XCorrelationIdFilter} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@Component
@ConditionalOnProperty("headers.correlation-id.server.paths")
@ConfigurationProperties(prefix = "headers.correlation-id.server")
public class CorrelationIdFilter
        extends OncePerRequestFilter {
    public static final int WC_CORRELATION_ID = 250;

    private static final PathMatcher matcher = new AntPathMatcher();

    @NotEmpty
    @Setter
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<String> paths;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        final String uri = request.getRequestURI();
        if (!paths.stream().
                anyMatch(path -> matcher.match(path, uri))) {
            filterChain.doFilter(request, response);
            return;
        }

        // Use set to ignore duplicate identical headers
        final SortedSet<String> headers = new TreeSet<>(
                list(request.getHeaders("X-Correlation-ID")));
        switch (headers.size()) {
        case 1:
            accept(request, response, filterChain, headers.first());
            return;
        case 0:
            reject(request, response, "Missing %s header");
            return;
        default:
            reject(request, response, "Multiple %s headers");
        }
    }

    private static void accept(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain filterChain,
            final String correlationId)
            throws IOException, ServletException {
        response.setHeader("X-Correlation-ID", correlationId);
        filterChain.doFilter(request, response);
    }

    private static void reject(final HttpServletRequest request,
            final HttpServletResponse response, final String format)
            throws IOException {
        final String message = format(format, "X-Correlation-ID");
        response.setHeader("Warning",
                format("%d %s:%d \"%s\"", WC_CORRELATION_ID,
                        getByName(request.getLocalName()).
                                getCanonicalHostName(),
                        request.getLocalPort(), message));
        response.sendError(SC_BAD_REQUEST, message);
    }
}
