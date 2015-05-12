package hello;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.UnknownHostException;

import static java.lang.management.ManagementFactory.getRuntimeMXBean;
import static java.net.InetAddress.getLocalHost;
import static java.time.Instant.ofEpochMilli;
import static java.time.OffsetDateTime.now;
import static java.time.OffsetDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * {@code Heartbeat} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@ConditionalOnProperty(value = "endpoints.heartbeat.enabled",
        matchIfMissing = true)
@RestController
public class HeartbeatController {
    private final String service;
    private final int port;

    @Inject
    public HeartbeatController(
            @Value("${spring.application.name}") final String service,
            @Value("${server.port}") final int port) {
        this.service = service;
        this.port = port;
    }

    @RequestMapping(value = "/heartbeat", method = GET)
    public Heartbeat beat()
            throws UnknownHostException {
        return new Heartbeat(service, port);
    }

    @Data
    public static class Heartbeat {
        private final String hostname;
        private final String timestamp = now().toString();
        private final String startTime = ofInstant(
                ofEpochMilli(getRuntimeMXBean().getStartTime()),
                systemDefault()).toString();

        private final String service;
        private final int port;

        public Heartbeat(final String service, final int port)
                throws UnknownHostException {
            this.service = service;
            this.port = port;
            hostname = getLocalHost().getHostName();
        }
    }
}
