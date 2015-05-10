package hello;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static java.util.Collections.list;
import static org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes;

/**
 * {@code HeadersFeignInterceptor} is a workaround to add caller HTTP request
 * headers into the Feign REST call.  More here: <ul><li><a
 * href="http://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html#_propagating_the_security_context_or_using_spring_scopes><cite></cite>Propagating
 * the Security Context or using Spring Scopes</cite></a></li> <li><a
 * href="http://stackoverflow.com/questions/29566777/does-spring-make-the-securitycontext-available-to-the-thread-executing-a-hystrix"><cite>Does
 * Spring make the SecurityContext available to the thread executing a Hystrix
 * Command</cite></a></li> <li><a href="https://github.com/Netflix/feign/issues/214"><cite>Better
 * support for request-specific headers #214</cite></a> (Solution in
 * progress)</li></ul>
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@Component
@ConditionalOnClass(Feign.class)
public class HeadersFeignInterceptor
        implements RequestInterceptor {
    @Override
    public void apply(final RequestTemplate template) {
        final HttpServletRequest request
                = ((ServletRequestAttributes) currentRequestAttributes())
                .getRequest();
        for (final String header : list(request.getHeaderNames()))
            template.header(header, list(request.getHeaders(header)));
    }
}
