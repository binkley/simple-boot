package hello;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * {@code RemoteHelloController} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@RestController
public class HelloWorldController {
    private final HystrixRemoteHello remote;

    @Inject
    public HelloWorldController(final HystrixRemoteHello remote) {
        this.remote = remote;
    }

    @RequestMapping(value = "/hello/{name}", method = GET)
    public Greeting hello(@PathVariable final String name,
            final HttpServletResponse response) {
        return remote.greet(In.builder().name(name).build(), response);
    }

    @FeignClient("remote-hello")
    public interface FeignRemoteHello {
        @RequestMapping(value = "/greet", method = POST,
                consumes = APPLICATION_JSON_VALUE)
        Greeting greet(@RequestBody final In in);
    }

    @Component
    public static class HystrixRemoteHello {
        private final FeignRemoteHello remote;

        @Inject
        public HystrixRemoteHello(final FeignRemoteHello remote) {
            this.remote = remote;
        }

        @HystrixCommand(groupKey = "remote-hello", fallbackMethod = "die")
        public Greeting greet(final In in,
                @SuppressWarnings("UnusedParameters")
                final HttpServletResponse response) {
            return remote.greet(in);
        }

        private Greeting die(final In in,
                final HttpServletResponse response) {
            response.setStatus(SERVICE_UNAVAILABLE.value());
            response.addHeader("Warning", "remote-hello unavailable");
            return Greeting.builder().
                    message(format("No dice, %s.", in.getName())).
                    build();
        }
    }
}
