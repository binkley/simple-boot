package hello;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * {@code HeartbeatPing} <b>needs documentation</b>.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @todo Needs documentation.
 * @todo What about Server#isReadyToServe()?
 */
@Component
public class HeartbeatPing
        implements IPing {
    private static final Logger log = getLogger(HeartbeatPing.class);

    @Override
    public boolean isAlive(final Server server) {
        try {
            new RestTemplate().getForEntity(
                    URI.create(format("http://%s/heartbeat", server)),
                    Heartbeat.class);
            return true;
        } catch (final RestClientException ignored) {
            log.warn("Heartbeat failed for {}", server);
            return false;
        }
    }
}
