package hello;

import lombok.Builder;
import lombok.Data;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

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
    private final RemoteHello remote;

    @Inject
    public HelloWorldController(final RemoteHello remote) {
        this.remote = remote;
    }

    @RequestMapping(value = "/hello/{name}", method = GET)
    public Greeting hello(@PathVariable final String name) {
        return remote.greet(In.builder().name(name).build());
    }

    @FeignClient("remote-hello")
    public interface RemoteHello {
        @RequestMapping(value = "/greet", method = POST,
                consumes = APPLICATION_JSON_VALUE)
        Greeting greet(@RequestBody final In in);
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
