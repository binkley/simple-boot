package hello.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.stereotype.Component;

import java.lang.management.ThreadMXBean;

import static java.lang.management.ManagementFactory.getThreadMXBean;
import static org.springframework.boot.actuate.health.Health.Builder;

/**
 * {@code Threads} describes JVM threads for health checks.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@Component
public class Threads
        extends AbstractHealthIndicator {
    @Override
    protected void doHealthCheck(final Builder builder)
            throws Exception {
        final ThreadMXBean threads = getThreadMXBean();
        builder.withDetail("count", threads.getThreadCount()).
                withDetail("daemon-count", threads.getDaemonThreadCount()).
                withDetail("peak-count", threads.getPeakThreadCount()).
                withDetail("started-count",
                        threads.getTotalStartedThreadCount()).
                up();
    }
}
