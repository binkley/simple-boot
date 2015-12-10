package hello.health;

import com.sun.management.UnixOperatingSystemMXBean;
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
public class FileHealthIndicator
        extends AbstractHealthIndicator {
    @Override
    protected void doHealthCheck(final Builder builder)
            throws Exception {
        final OperatingSystemMXBean os = getOperatingSystemMXBean();
        if (os instanceof UnixOperatingSystemMXBean) {
            final UnixOperatingSystemMXBean unix
                    = (UnixOperatingSystemMXBean) os;
            builder.withDetail("max-file-descriptors",
                    unix.getMaxFileDescriptorCount()).
                    withDetail("open-file-descriptors",
                            unix.getOpenFileDescriptorCount());
        }

        final java.io.File here = new java.io.File(".");
        builder.withDetail("usable-disk", here.getUsableSpace()).
                withDetail("total-disk", here.getTotalSpace());

        builder.up();
    }
}
