package hello.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.stereotype.Component;

import java.lang.management.RuntimeMXBean;

import static java.lang.management.ManagementFactory.getRuntimeMXBean;
import static java.time.Instant.ofEpochMilli;
import static java.time.OffsetDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;
import static org.springframework.boot.actuate.health.Health.Builder;

/**
 * {@code Java} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@Component
public class Java
        extends AbstractHealthIndicator {
    @Override
    protected void doHealthCheck(final Builder builder)
            throws Exception {
        final RuntimeMXBean runtime = getRuntimeMXBean();
        builder.
                withDetail("start-time",
                        ofInstant(ofEpochMilli(runtime.getStartTime()),
                                systemDefault()).toString()).
                withDetail("uptime-beats", runtime.getUptime() / 86400).
                withDetail("vm-name", runtime.getVmName()).
                withDetail("vm-vendor", runtime.getVmVendor()).
                withDetail("vm-version", runtime.getVmVersion()).
                up();
    }
}
