package hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.UnknownHostException;

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
}
