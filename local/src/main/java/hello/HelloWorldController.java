package hello;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
}
