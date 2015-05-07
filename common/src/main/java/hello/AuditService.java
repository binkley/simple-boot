package hello;

import org.slf4j.Logger;
import org.springframework.boot.actuate.audit.AuditEvent;
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
public class AuditService
        implements ApplicationListener<AuditApplicationEvent> {
    private final Logger logger = getLogger("AUDIT");

    @Override
    public void onApplicationEvent(final AuditApplicationEvent event) {
        final AuditEvent auditable = event.getAuditEvent();
        if (auditable.getType().endsWith("_SUCCESS"))
            logger.info("{}", auditable);
        else
            logger.error("{}", auditable);
    }
}
