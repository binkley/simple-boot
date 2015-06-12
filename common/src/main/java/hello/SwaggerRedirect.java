package hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * {@code SwaggerRedirect} makes Swagger the default root web page.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@Controller
public class SwaggerRedirect {
    @RequestMapping("/")
    public String home() {
        return "swagger-ui.html";
    }
}
