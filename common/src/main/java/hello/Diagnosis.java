package hello;

import lombok.Data;

import java.util.Map;

/**
 * {@code Diagnosis} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
@Data
public final class Diagnosis {
    private final String status;
    private final Map<String, Object> details;
}
