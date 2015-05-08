package hello.health;

import hello.FeignRemoteHello;
import hello.In;
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
    private final FeignRemoteHello feign;

    @Inject
    public RemoteHello(final FeignRemoteHello feign) {
        this.feign = feign;
    }

    @Override
    protected void doHealthCheck(final Builder builder)
            throws Exception {
        try {
            feign.greet(In.builder().name("self").build());
            builder.up().
                    withDetail("remote-hello",
                            "http://remote-hello/greet/self");
        } catch (final Exception e) {
            builder.outOfService().
                    withDetail("remote-hello",
                            "http://remote-hello/greet/self").
                    withDetail("exception", e.getMessage());
        }
    }
}
