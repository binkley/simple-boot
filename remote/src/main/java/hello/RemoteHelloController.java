package hello;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Function;

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
    private static final Function<In, Greeting> greet = in -> Greeting.
            builder().
            message(format("Hats off to you, %s!", in.getName())).
            build();

    @RequestMapping(value = "/greet", method = POST)
    public Greeting greet(@RequestBody final In in) {
        return greet.apply(in);
    }

    @Data
    @Builder
    public static class In {
        private String name;

        public In() {
        }

        public In(final String name) {
            this.name = name;
        }
    }
}
