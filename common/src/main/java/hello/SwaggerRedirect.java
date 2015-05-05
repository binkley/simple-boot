package hello;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * {@code SwaggerRedirect} makes Swagger the default root web page.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@Configuration
public class SwaggerRedirect
        extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/sdoc.jsp");
        registry.setOrder(HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }
}
