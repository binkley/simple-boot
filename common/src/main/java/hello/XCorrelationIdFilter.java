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
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        // Use set to ignore duplicate identical headers; use sorted set to
        // have first()
        final SortedSet<String> headers = new TreeSet<>();
        headers.addAll(list(request.getHeaders("X-Correlation-ID")));

        switch (headers.size()) {
        case 1:
            response.setHeader("X-Correlation-ID", headers.first());
            filterChain.doFilter(request, response);
            return;
        case 0:
            response.setHeader("Warning",
                    format("299 %s \"Missing X-Correlation-ID header\"",
                            request.getLocalName()));
            response.sendError(SC_BAD_REQUEST,
                    "Missing X-Correlation-ID header");
            return;
        default:
            response.setHeader("Warning",
                    format("299 %s \"Multiple X-Correlation-ID headers\"",
                            request.getLocalName()));
            response.sendError(SC_BAD_REQUEST,
                    "Multiple X-Correlation-ID headers");
        }
    }
}
