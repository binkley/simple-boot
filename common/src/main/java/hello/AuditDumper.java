package hello;

import org.slf4j.Logger;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * {@code AuditDumper} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@Component
public class AuditDumper
        implements ApplicationListener<AuditApplicationEvent> {
    private final Logger logger = getLogger(getClass());

    @Override
    public void onApplicationEvent(final AuditApplicationEvent event) {
        logger.error("*** Example AUDITING: {}", event);
    }
}
