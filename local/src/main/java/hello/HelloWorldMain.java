package hello;

import hello.health.DataSourceEnhancedHealthIndicator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProviders;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;

@EnableConfigurationProperties
@EnableFeignClients
@EnableHystrix
@EnableSwagger2
@SpringBootApplication
public class HelloWorldMain {
    public static void main(final String... args) {
        SpringApplication.run(HelloWorldMain.class, args);
    }

    @Bean(name = "bob-dataSource")
    @Primary
    @ConfigurationProperties(prefix="datasource.bob")
    public DataSource bobDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "nancy-dataSource")
    @ConfigurationProperties(prefix="datasource.nancy")
    public DataSource nancyDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "wick-fowler-dataSource")
    @ConfigurationProperties(prefix="datasource.wick-fowler")
    public DataSource wickFowlerDataSource() {
        return DataSourceBuilder.create().build();
    }

    /** @todo Why does auto-config misses config class? */
    @Bean
    @Primary
    public HealthIndicator dbHealthIndicator(
            final HealthAggregator healthAggregator,
            final Map<String, DataSource> dataSources,
            final Collection<DataSourcePoolMetadataProvider> metadataProviders) {
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
        for (final Map.Entry<String, DataSource> entry : dataSources
                .entrySet()) {
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
