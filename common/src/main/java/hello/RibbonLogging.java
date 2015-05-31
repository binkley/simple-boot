package hello;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClientSpecification;
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
    @AutoConfigureAfter(RibbonClientConfiguration.class)
    public static class RibbonLoggingConfiguration<T extends Server> {
        @Bean
        @ConditionalOnMissingBean
        public Class<? extends ILoadBalancer> loggingLoadBalancer() {
            return LoggingLoadBalancer.class;
        }
    }
}
