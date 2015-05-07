package hello;

import lombok.Builder;
import lombok.Data;

/**
 * {@code In} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@Data
@Builder
public class In {
    private String name;

    public In() {
    }

    public In(final String name) {
        this.name = name;
    }
}
