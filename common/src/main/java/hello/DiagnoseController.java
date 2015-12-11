package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static org.springframework.boot.actuate.health.Status.UP;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * {@code Heartbeat} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@ConditionalOnProperty(value = "endpoints.diagnose.enabled",
        matchIfMissing = true)
@RestController
public class DiagnoseController {
    @Autowired(required = false)
    private Map<String, HealthIndicator> healthIndicators = new HashMap<>();

    @RequestMapping(value = "/diagnose", method = GET)
    public Map<String, Diagnosis> diagnoseJson()
            throws UnknownHostException {
        final Map<String, Diagnosis> diagnoses = new HashMap<>();
        for (final Entry<String, HealthIndicator> h : healthIndicators
                .entrySet()) {
            final String name = h.getKey();
            final Health health = h.getValue().health();
            final Status status = health.getStatus();
            if (UP == status)
                continue;
            diagnoses.put(name,
                    new Diagnosis(status.getCode(), health.getDetails()));
        }
        return diagnoses;
    }
}
