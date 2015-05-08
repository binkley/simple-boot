package hello;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import static java.lang.String.format;
import static javax.servlet.http.HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION;

/**
 * {@code HystrixRemoteHello} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@Component
public class HystrixRemoteHello {
    private final FeignRemoteHello remote;

    @Inject
    public HystrixRemoteHello(
            final FeignRemoteHello remote) {
        this.remote = remote;
    }

    @HystrixCommand(groupKey = "remote-hello", fallbackMethod = "die")
    public Greeting greet(final In in, @SuppressWarnings("UnusedParameters")
    final HttpServletResponse response) {
        return remote.greet(in);
    }

    @SuppressWarnings("unused")
    private Greeting die(final In in, final HttpServletResponse response) {
        response.setStatus(SC_NON_AUTHORITATIVE_INFORMATION);
        response.addHeader("Warning", "remote-hello unavailable");
        return Greeting.builder().
                message(format("No dice, %s.", in.getName())).
                build();
    }
}
