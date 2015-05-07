package hello;

import lombok.Builder;
import lombok.Data;

/**
 * {@code Greeting} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@Data
@Builder
public class Greeting {
    private String message;

    public Greeting() {
    }

    public Greeting(final String message) {
        this.message = message;
    }
}
