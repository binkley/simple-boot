package hello;

import lombok.Builder;
import lombok.Data;

import java.net.UnknownHostException;

import static java.lang.String.format;
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
@Builder
@Data
public class Heartbeat {
    private static final String systemStartTime = ofInstant(
            ofEpochMilli(getRuntimeMXBean().getStartTime()), systemDefault()).
            toString();

    // Field order determines JSON order
    private String service;
    private String startTime;
    private String timestamp;
    private String hostname;
    private int port;

    /** Constructs a new {@code Heartbeat} for JSON binding. */
    public Heartbeat() {}

    /** Constructs a new {@code Heartbeat} for {@link HeartbeatBuilder#build()}. */
    public Heartbeat(final String service, final String startTime,
            final String timestamp, final String hostname, final int port) {
        this.service = service;
        this.startTime = startTime;
        this.timestamp = timestamp;
        this.hostname = hostname;
        this.port = port;
    }

    /** Constructs a new {@code Heartbeat} for {@link HeartbeatController#beat()}. */
    public Heartbeat(final String service, final int port)
            throws UnknownHostException {
        this.service = service;
        startTime = systemStartTime;
        timestamp = now().toString();
        hostname = getLocalHost().getCanonicalHostName();
        this.port = port;
    }

    @Override
    public String toString() {
        return format("%s://%s:%d/%s@%d", service, hostname, port, startTime,
                beats());
    }

    /**
     * @see <a href="http://en.wikipedia.org/wiki/Swatch_Internet_Time">Swatch
     * Internet Time</a>
     */
    private int beats() {
        return (int) (getRuntimeMXBean().getUptime() / 86400);
    }
}
