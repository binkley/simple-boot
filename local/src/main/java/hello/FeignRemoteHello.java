package hello;

import feign.Logger.Level;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * {@code FeignRemoteHello} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@FeignClient(value = "remote-hello", logLevel = Level.BASIC)
public interface FeignRemoteHello {
    @RequestMapping(value = "/greet", method = POST,
            consumes = APPLICATION_JSON_VALUE)
    Greeting greet(@RequestBody final In in);
}
