package hello.health;

import hello.FeignRemoteHello;
import hello.In;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * {@code RemoteHello} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@Component
public class RemoteHello
        extends AbstractHealthIndicator {
    private final String service;
    private final FeignRemoteHello feign;

    @Inject
    public RemoteHello(
            @Value("${spring.application.name}") final String service,
            final FeignRemoteHello feign) {
        this.service = service;
        this.feign = feign;
    }

    @Override
    protected void doHealthCheck(final Builder builder)
            throws Exception {
        try {
            feign.greet(In.builder().name(service).build());
            builder.up().
                    withDetail("endpoint", "http://remote-hello/greet");
        } catch (final Exception e) {
            builder.outOfService().
                    withDetail("endpoint", "http://remote-hello/greet").
                    withDetail("exception", e.getMessage());
        }
    }
}
