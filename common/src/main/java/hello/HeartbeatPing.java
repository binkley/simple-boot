package hello;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * {@code HeartbeatPing} is a logging Ribbon ping.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @todo What about {@link Server#isReadyToServe()}?
 * @todo Get into the per-service context, i.e., {@link RibbonLogging}
 */
@Component
public class HeartbeatPing
        implements IPing {
    private static final Logger log = getLogger(HeartbeatPing.class);

    private final ConcurrentHashMap<Server, AtomicBoolean> ups
            = new ConcurrentHashMap<>();

    @Override
    public boolean isAlive(final Server server) {
        final AtomicBoolean up = ups.
                computeIfAbsent(server, $ -> new AtomicBoolean(true));
        try {
            final Heartbeat beat = new RestTemplate().
                    getForEntity(
                            URI.create(format("http://%s/heartbeat", server)),
                            Heartbeat.class).
                    getBody();
            if (up.compareAndSet(false, true))
                log.warn("Heartbeat back for {}: {}", server, beat);
            return true;
        } catch (final RestClientException ignored) {
            if (up.compareAndSet(true, false))
                log.warn("Heartbeat gone for {}", server);
            return false;
        }
    }
}
