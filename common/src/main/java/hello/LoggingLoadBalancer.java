package hello;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.netflix.loadbalancer.ServerListFilter;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * {@code LoggingLoadBalancer} <b>needs documentation</b>.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @todo Needs documentation.
 */
public class LoggingLoadBalancer<T extends Server>
        extends ZoneAwareLoadBalancer<T> {
    private static final Logger log = getLogger(LoggingLoadBalancer.class);

    private final String serviceName;

    @Inject
    public LoggingLoadBalancer(final IClientConfig config,
            final ServerList<T> serverList,
            final ServerListFilter<T> serverListFilter, final IRule rule,
            final IPing ping) {
        super(config, rule, ping, serverList, serverListFilter);

        serviceName = config.getClientName();

        log.info("Initial servers for {}: {}", serviceName,
                serverList.getInitialListOfServers());
    }

    @PostConstruct
    public void init() {
        addServerListChangeListener((oldList, newList) -> log.
                warn("Updating servers for {}: {} <- {}", serviceName,
                        newList, oldList));
    }

    @Override
    public void markServerDown(final Server server) {
        super.markServerDown(server);

        log.error("Server down for {}: {}", serviceName, server);
    }
}
