package hello;

import com.netflix.client.RetryHandler;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerStats;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClientSpecification;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * {@code RibbonLogging} <b>needs documentation</b>.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @todo Needs documentation.
 */
@Component
@ConditionalOnClass(RibbonClientConfiguration.class)
public class RibbonLogging
        extends RibbonClientSpecification {
    public RibbonLogging() {
        super("default.ribbon-logging",
                new Class[]{RibbonLoggingConfiguration.class});
    }

    @Configuration
    @RibbonClients
    @AutoConfigureAfter(RibbonClientConfiguration.class)
    public static class RibbonLoggingConfiguration<T extends Server> {
        public RibbonLoadBalancerContext loggingRibbonContext(final ILoadBalancer balancer, final
                IClientConfig config) {
            return new RibbonLoadBalancerContext(balancer, config) {
                @Override
                public void noteRequestCompletion(final ServerStats stats,
                        final Object response, final Throwable e,
                        final long responseTime) {
                    System.out.println(
                            "RibbonLoggingConfiguration.noteRequestCompletion");
                    super.noteRequestCompletion(stats, response, e,
                            responseTime);
                }

                @Override
                public void noteRequestCompletion(final ServerStats stats,
                        final Object response, final Throwable e,
                        final long responseTime,
                        final RetryHandler errorHandler) {
                    System.out.println(
                            "RibbonLoggingConfiguration.noteRequestCompletion");
                    super.noteRequestCompletion(stats, response, e,
                            responseTime, errorHandler);
                }
            };
        }

        @Bean
        @ConditionalOnMissingBean
        public Class<? extends ILoadBalancer> loggingLoadBalancer() {
            return LoggingLoadBalancer.class;
        }
    }
}
