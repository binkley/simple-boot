# Simple Boot

## Modules

* [common](common/) - Code shared by other modules
* [config](config) - Spring Cloud Config server
* [local](local/) - "Local" service demonstrating GET calling to POST
* [remote](remote/) - "Remote" service demonstrating POST

## Features

### Spring Boot

### Audit trail

- Example auditing, prints to log.
- Audit controller for querying.

### Management

All management endpoints moved to `/admin`.

### Spring Cloud config

Spring Cloud Config server against a local git repo.

*TODO*: Move config repo to GitHub.

### Error handling

See `X-Correlation-ID` handling in
[CorrelationID*](common/src/main/java/hellp).

### Health

Simple health indicator for a remote service.

### Heartbeat

A simple timestamp endpoint on `/heartbeat`.  Text vs JSON.

### Logging

Configure logging in `application.yml` and `bootstrap.yml`.

### Netflix Feign

GET on local calls to POST on remote via a Feign client.  Passes all HTTP
headers from caller through to Feign target.  Example required header,
`X-Correlation-ID` on non-management endpoints.

### Netflix Hystrix

Local calls remote with hystrix and fallback.

### RAML

Example of RAML in [remote](remote/src/main/resources/hello/remote-hello.raml).

### Spring Actuator

Example health indicator with `hello.health.RemoteHello`.

### Spring Security

Simply configured Spring security on management endpoints.

### Swagger

Root service path shows Swagger UI.  Requires Tomcat.  See [Swagger SpringMVC
issue #15](https://github.com/adrianbk/swagger-springmvc-demo/issues/15).

### Testing

Unit and integration tests.
