package hello;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.lang.String.format;
import static java.util.Collections.list;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

/**
 * {@code XCorrelationIdFilter} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@Component
public class XCorrelationIdFilter
        extends OncePerRequestFilter {
    private static final String HEADER = "X-Correlation-ID";

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        // Use set to ignore duplicate identical headers; use sorted set to
        // have first()
        final SortedSet<String> headers = new TreeSet<>();
        headers.addAll(list(request.getHeaders(HEADER)));

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
            final String correlationID)
            throws IOException, ServletException {
        response.setHeader(HEADER, correlationID);
        filterChain.doFilter(request, response);
    }

    private static void reject(final HttpServletRequest request,
            final HttpServletResponse response, final String format)
            throws IOException {
        final String message = format(format, HEADER);
        response.setHeader("Warning",
                format("299 %s \"%s\"", request.getLocalName(), message));
        response.sendError(SC_BAD_REQUEST, message);
    }
}
