package hello;

import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import static hello.ConfigishController.BadConfigurationException.badConfiguration;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * {@code ConfigishController} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@RestController
public class ConfigishController {
    @RequestMapping(value = "/config/{name:.*}", method = GET,
            produces = "application/json;charset=UTF-8")
    public InputStreamResource config(
            @ApiParam("Configuration path without extension")
            @PathVariable("name") final String name)
            throws IOException {
        return new InputStreamResource(streamFor(name), "JSON for " + name);
    }

    @ResponseStatus(BAD_REQUEST)
    public static class BadConfigurationException
            extends IllegalArgumentException {
        public static Supplier<BadConfigurationException> badConfiguration(
                final String name) {
            return () -> new BadConfigurationException(name);
        }

        public BadConfigurationException(final String name) {
            super(format("No such configuration: %s", name));
        }
    }

    private InputStream streamFor(final String name)
            throws IOException {
        return ofNullable(getClass().getResource(pathFor(name))).
                orElseThrow(badConfiguration(name)).
                openStream();
    }

    private static String pathFor(final String name) {
        return format("/%s.json", name);
    }
}
