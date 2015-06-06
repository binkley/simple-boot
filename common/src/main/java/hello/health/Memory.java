package hello.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.stereotype.Component;

import java.lang.management.OperatingSystemMXBean;

import static java.lang.management.ManagementFactory.getOperatingSystemMXBean;
import static org.springframework.boot.actuate.health.Health.Builder;

/**
 * {@code Memory} describes the underlying OS memory for health checks.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Check on Windows
 */
@Component
public class Memory
        extends AbstractHealthIndicator {
    @Override
    protected void doHealthCheck(final Builder builder)
            throws Exception {
        final OperatingSystemMXBean os = getOperatingSystemMXBean();
        if (os instanceof com.sun.management.OperatingSystemMXBean) {
            final com.sun.management.OperatingSystemMXBean oracle
                    = (com.sun.management.OperatingSystemMXBean) os;
            builder.withDetail("committed-virtual-memory",
                    oracle.getCommittedVirtualMemorySize()).
                    withDetail("free-physical-memory",
                            oracle.getFreePhysicalMemorySize()).
                    withDetail("free-swap-space",
                            oracle.getFreeSwapSpaceSize()).
                    withDetail("total-physical-memory",
                            oracle.getTotalPhysicalMemorySize()).
                    withDetail("total-swap-space",
                            oracle.getTotalSwapSpaceSize());
            builder.up();
        }
    }
}
