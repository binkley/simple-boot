/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hello.health;

import org.springframework.boot.actuate.health.DataSourceHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.jdbc.UncategorizedSQLException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.springframework.jdbc.datasource.DataSourceUtils.getConnection;
import static org.springframework.jdbc.datasource.DataSourceUtils.releaseConnection;

public class DataSourceEnhancedHealthIndicator
        extends DataSourceHealthIndicator {
    private DataSource dataSource;
    private String jdbcURL;

    public DataSourceEnhancedHealthIndicator() {
    }

    public DataSourceEnhancedHealthIndicator(final DataSource dataSource) {
        this(dataSource, null);
    }

    public DataSourceEnhancedHealthIndicator(final DataSource dataSource,
            final String query) {
        super(dataSource, query);
        this.dataSource = dataSource;
        jdbcURL = jdbcURL(dataSource);
    }

    @Override
    protected void doHealthCheck(final Builder builder)
            throws Exception {
        super.doHealthCheck(builder);
        builder.withDetail("jdbc-url", jdbcURL);
    }

    public void setDataSource(final DataSource dataSource) {
        super.setDataSource(dataSource);
        this.dataSource = dataSource;
        jdbcURL = jdbcURL(dataSource);
    }

    private static String jdbcURL(final DataSource dataSource) {
        final Connection conn = getConnection(dataSource);
        try {
            return conn.getMetaData().getURL();
        } catch (final SQLException e) {
            throw new UncategorizedSQLException("Cannot get metadata", null,
                    e);
        } finally {
            releaseConnection(conn, dataSource);
        }
    }
}
