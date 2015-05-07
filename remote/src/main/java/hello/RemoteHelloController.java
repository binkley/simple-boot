package hello;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * {@code RemoteHelloController} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@RestController
public class RemoteHelloController {
    @RequestMapping(value = "/greet", method = POST)
    public Greeting greet(@RequestBody final In in) {
        return Greeting.builder().
                message(format("Hats off to you, %s!", in.getName())).
                build();
    }
}
