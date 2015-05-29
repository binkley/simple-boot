package hello;

import com.mangofactory.swagger.plugin.EnableSwagger;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ServerListChangeListener;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;

import static org.slf4j.LoggerFactory.getLogger;

@EnableFeignClients
@EnableHystrix
@EnableSwagger
@SpringBootApplication
public class HelloWorldMain {
    private static final Logger logger = getLogger("RIBBON-LOGGING");

    public static void main(final String... args) {
        SpringApplication.run(HelloWorldMain.class, args);
    }

    @Bean
    public ServerListChangeListener ribbonLoggingListener(
            final SpringClientFactory factory) {
        final ServerListChangeListener listener = (oldList, newList) -> {
            // Initial case of configurating from properties
            if (oldList.isEmpty() && !newList.isEmpty())
                logger.info("Load balancing for '{}' to {} <- {}",
                        "remote-hello", newList, oldList);
            else
                logger.warn("Load balancing for '{}' to {} <- {}",
                        "remote-hello", newList, oldList);
        };
        ((BaseLoadBalancer) factory.getLoadBalancer("remote-hello")).
                addServerListChangeListener(listener);
        return listener;
    }
}
