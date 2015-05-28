package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@code TestController} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@RestController
public class TestController {
    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @RequestMapping("/automated")
    public String automated() {
        return "automated";
    }
}
