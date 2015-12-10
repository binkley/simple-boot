package hello.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.stereotype.Component;

import java.lang.management.OperatingSystemMXBean;

import static java.lang.management.ManagementFactory.getOperatingSystemMXBean;
import static java.time.Duration.ofNanos;
import static org.springframework.boot.actuate.health.Health.Builder;

/**
 * {@code OS} describes the underlying OS for health checks.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@Component
public class CpuHealthIndicator
        extends AbstractHealthIndicator {
    @Override
    protected void doHealthCheck(final Builder builder)
            throws Exception {
        final OperatingSystemMXBean os = getOperatingSystemMXBean();
        builder.withDetail("processors", os.getAvailableProcessors()).
                withDetail("system-loadavg", os.getSystemLoadAverage());

        if (os instanceof com.sun.management.OperatingSystemMXBean) {
            final com.sun.management.OperatingSystemMXBean oracle
                    = (com.sun.management.OperatingSystemMXBean) os;
            builder.withDetail("process-cpu-load", oracle.getProcessCpuLoad()).
                    withDetail("process-cpu-time",
                            ofNanos(oracle.getProcessCpuTime()).toString()).
                    withDetail("system-cpu-load", oracle.getSystemCpuLoad());
        }

        builder.up();
    }
}
