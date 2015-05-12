package hello;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * {@code SwaggerConfiguration} sets up top-level Swagger configuration such
 * as API display properties.  Provide conifguration with the "swagger.api"
 * block in properties or YAML.  To wit: <table><tr><th>Property</th>
 * <th>Purpose</th></tr> <tr><td>{@code swagger.api.title}</td> <td>API
 * Title</td></tr> <tr><td>{@code swagger.api.description}</td> <td>API
 * Description</td></tr> <tr><td>{@code swagger.api.terms-of-service.url}</td>
 * <td>API Terms of Service</td></tr> <tr><td>{@code swagger.api.contact.email-address}</td>
 * <td>API Contact</td></tr> <tr><td>{@code swagger.api.license.name}</i></td>
 * <td>API License Name, <i>e.g.</i>, "Public Domain"</td></tr> <tr><td>{@code
 * swagger.api.license.url}</td> <td>API License</td></tr> <tr><td>{@code
 * swagger.api.version}</td> <td>API Version, defaults to
 * 1.0</td></tr></table>
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 */
@Configuration
@ConditionalOnClass(SpringSwaggerConfig.class)
public class SwaggerConfiguration {
    @Bean
    public SwaggerSpringMvcPlugin setupSwaggerApi(
            final SpringSwaggerConfig config, final Environment env) {
        return new SwaggerSpringMvcPlugin(config).
                apiInfo(new ApiInfo(env.getProperty("swagger.api.title"),
                        env.getProperty("swagger.api.description"),
                        env.getProperty("swagger.api.terms-of-service.url"),
                        env.getProperty("swagger.api.contact.email-address"),
                        env.getProperty("swagger.api.license.name"),
                        env.getProperty("swagger.api.license.url"))).
                apiVersion(env.getProperty("swagger.api.version", "1.0")).
                build();
    }
}
