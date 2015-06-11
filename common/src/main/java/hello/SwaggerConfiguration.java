package hello;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.web.Swagger2Controller;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

/**
 * {@code SwaggerConfiguration} sets up top-level Swagger configuration such
 * as API display properties.  Provide configuration with the "swagger.api"
 * block in properties or YAML.  To wit: <table><tr><th>Property</th>
 * <th>Purpose</th></tr> <tr><td>{@code swagger.api.title}</td> <td>API
 * Title</td></tr> <tr><td>{@code swagger.api.description}</td> <td>API
 * Description</td></tr> <tr><td>{@code swagger.api.terms-of-service.url}</td>
 * <td>API Terms of Service</td></tr> <tr><td>{@code swagger.api.contact.email-address}</td>
 * <td>API Contact</td></tr> <tr><td>{@code swagger.api.license.name}</i></td>
 * <td>API License Name, <i>e.g.</i>, "Public Domain"</td></tr> <tr><td>{@code
 * swagger.api.license.url}</td> <td>API License</td></tr> <tr><td>{@code
 * swagger.api.version}</td> <td>API Version</td></tr></table>
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@Configuration
@ConditionalOnClass(Swagger2Controller.class)
public class SwaggerConfiguration {
    @Bean
    public Docket setupSwaggerApi(final Environment env) {
        return new Docket(SWAGGER_2).apiInfo(
                new ApiInfo(env.getProperty("swagger.api.title"),
                        env.getProperty("swagger.api.description"),
                        env.getProperty("swagger.api.version"),
                        env.getProperty("swagger.api.terms-of-service.url"),
                        env.getProperty("swagger.api.contact.email-address"),
                        env.getProperty("swagger.api.license.name"),
                        env.getProperty("swagger.api.license.url")));
    }
}
