package hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

/**
 * {@code RemoteMocks} provides test-only alternatives for remote resources.
 * <p>
 * Be cautious: if method names are the same as non-mock bean names, Spring
 * silently ignores the mocks.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@Configuration
public class RemoteMocks {
    @Bean
    @Primary
    public FeignRemoteHello mockFeignRemoteHello() {
        return mock(FeignRemoteHello.class);
    }
}
