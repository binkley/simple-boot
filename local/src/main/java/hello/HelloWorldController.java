package hello;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
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
@Api(value = "hello-world-controller", description = "Hello, World!")
@RestController
public class HelloWorldController {
    private final HystrixRemoteHello remote;

    @Inject
    public HelloWorldController(final HystrixRemoteHello remote) {
        this.remote = remote;
    }

    @ApiOperation("Say hello")
    @ApiResponses({@ApiResponse(code = 203,
            message = "Remote services are unavailable"),
            @ApiResponse(code = 400,
                    message = "Recheck your X-Correlation-ID")})
    @RequestMapping(value = "/hello/{name}", method = GET)
    public Greeting hello(
            @ApiParam("What is your name?") @PathVariable final String name,
            @SuppressWarnings("UnusedParameters")
            @ApiParam("I want to track you")
            @RequestHeader(value = "X-Correlation-ID", required = true)
            final String correlationID, final HttpServletResponse response) {
        return remote.greet(In.builder().name(name).build(), response);
    }
}
