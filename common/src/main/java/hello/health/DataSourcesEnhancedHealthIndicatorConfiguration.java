package hello.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProviders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.Collections.emptyList;

/**
 * {@code DataSourcesHealthIndicatorConfiguration} <strong>needs
 * documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Fix Spring Configuration ordering so our bean replaces theirs rather
 * than turning theirs off, then we can use the same config setting
 */
@Configuration
@ConditionalOnBean(DataSource.class)
//@ConditionalOnProperty(prefix = "management.health.db", name = "enabled",
//        matchIfMissing = true)
public class DataSourcesEnhancedHealthIndicatorConfiguration {
    @Autowired
    private HealthAggregator healthAggregator;

    @Autowired(required = false)
    private Map<String, DataSource> dataSources;

    @Autowired(required = false)
    private final Collection<DataSourcePoolMetadataProvider> metadataProviders
            = emptyList();

    @Bean
    @Primary
    public HealthIndicator dbHealthIndicator() {
        final DataSourcePoolMetadataProvider metadataProvider
                = new DataSourcePoolMetadataProviders(metadataProviders);
        if (1 == dataSources.size()) {
            final DataSource dataSource = dataSources.values().iterator()
                    .next();
            return createDataSourceHealthIndicator(metadataProvider,
                    dataSource);
        }
        final CompositeHealthIndicator composite
                = new CompositeHealthIndicator(healthAggregator);
        for (final Entry<String, DataSource> entry : dataSources.entrySet()) {
            final String name = entry.getKey();
            final DataSource dataSource = entry.getValue();
            composite.addHealthIndicator(name,
                    createDataSourceHealthIndicator(metadataProvider,
                            dataSource));
        }
        return composite;
    }

    private DataSourceEnhancedHealthIndicator createDataSourceHealthIndicator(
            final DataSourcePoolMetadataProvider provider,
            final DataSource dataSource) {
        final DataSourcePoolMetadata poolMetadata = provider
                .getDataSourcePoolMetadata(dataSource);
        final String validationQuery = validationQueryFor(poolMetadata);
        return new DataSourceEnhancedHealthIndicator(dataSource,
                validationQuery);
    }

    private static String validationQueryFor(
            final DataSourcePoolMetadata poolMetadata) {
        if (null == poolMetadata)
            return null;
        else
            return poolMetadata.getValidationQuery();
    }
}
