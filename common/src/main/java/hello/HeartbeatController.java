package hello;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
public class HeartbeatController {
    @RequestMapping(value = "/heartbeat", method = GET)
    public Heartbeat beat()
            throws UnknownHostException {
        return new Heartbeat();
    }

    @Data
    public static class Heartbeat {
        private final String hostname;
        private final String timestamp = now().toString();
        private final String startTime = ofInstant(
                ofEpochMilli(getRuntimeMXBean().getStartTime()),
                systemDefault()).toString();

        public Heartbeat()
                throws UnknownHostException {
            hostname = getLocalHost().getHostName();
        }
    }
}
