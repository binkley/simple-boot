package hello;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ServerListChangeListener;
import org.slf4j.Logger;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * {@code RibbonLogging} <b>needs documentation</b>.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @todo Needs documentation.
 */
//@Configuration
//@ConditionalOnClass({IClient.class, RestTemplate.class})
//@RibbonClients
//@AutoConfigureAfter(SpringSwaggerConfig.class)
public class RibbonLogging {
    private static final Logger logger = getLogger(RibbonLogging.class);

    @Bean
    public ServerListChangeListener ribbonLoggingListener(
            final SpringClientFactory factory) {
        final ServerListChangeListener listener = (oldList, newList) -> logger
                .info("Load balancing for '{}' to {} <- {}", "remote-hello",
                        newList, oldList);
        ((BaseLoadBalancer) factory.getLoadBalancer("remote-hello")).
                addServerListChangeListener(listener);
        return listener;
    }
}
