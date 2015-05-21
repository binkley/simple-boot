package hello;

import lombok.Data;

import java.net.UnknownHostException;

import static java.lang.management.ManagementFactory.getRuntimeMXBean;
import static java.net.InetAddress.getLocalHost;
import static java.time.Instant.ofEpochMilli;
import static java.time.OffsetDateTime.now;
import static java.time.OffsetDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;

/**
 * {@code Heartbeat} is a heartbeat data type for the {@code /heartbeat}
 * endpoint in {@link HeartbeatController}.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@Data
public class Heartbeat {
    // Field order determines JSON order
    private final String service;
    private final String startTime = ofInstant(
            ofEpochMilli(getRuntimeMXBean().getStartTime()), systemDefault()).
            toString();
    private final String timestamp = now().toString();
    private final String hostname;

    private final int port;

    public Heartbeat(final String service, final int port)
            throws UnknownHostException {
        this.service = service;
        hostname = getLocalHost().getHostName();
        this.port = port;
    }
}
