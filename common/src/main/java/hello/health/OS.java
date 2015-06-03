package hello.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.stereotype.Component;

import java.lang.management.OperatingSystemMXBean;

import static java.lang.management.ManagementFactory.getOperatingSystemMXBean;
import static org.springframework.boot.actuate.health.Health.Builder;

/**
 * {@code OS} describes the underlying OS for health checks.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@Component
public class OS
        extends AbstractHealthIndicator {
    @Override
    protected void doHealthCheck(final Builder builder)
            throws Exception {
        final OperatingSystemMXBean os = getOperatingSystemMXBean();
        builder.
                withDetail("arch", os.getArch()).
                withDetail("processors", os.getAvailableProcessors()).
                withDetail("uname", os.getName()).
                withDetail("loadavg", os.getSystemLoadAverage()).
                withDetail("version", os.getVersion()).
                up();
    }
}
