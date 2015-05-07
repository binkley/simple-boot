package hello;

import hello.HelloWorldController.FeignRemoteHello;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

/**
 * {@code RemoteMocks} provides test-only alternatives for remote resources.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@Configuration
public class RemoteMocks {
    @Bean
    @Primary
    public FeignRemoteHello feignRemoteHello() {
        return mock(FeignRemoteHello.class);
    }
}
