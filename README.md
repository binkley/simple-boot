# Simple Boot

## Modules

* [common](common/) - Code shared by other modules
* [configish](configish) - Example config service
* [local](local/) - "Local" service demonstrating GET calling to POST
* [remote](remote/) - "Remote" service demonstrating POST

## Features

### Spring Boot

### Audit trail

- Example auditing, prints to log.
- Audit controller for querying.

### Mock configuration service

Configish is for a feign client to a foreign configuration service from Spring
cloud config server.

### Health

Simple health indicator for a remote service.

### Heartbeat

A simple timestamp endpoint on `/heartbeat`.

### Logging

Configure logging in `application.yml` and `bootstrap.yml`.

### Netflix Feign

GET on local calls to POST on remote via a Feign client.  Passes all HTTP
headers from caller through to Feign target.  Example required header,
`X-Correlation-ID` on non-management endpoints.

### Netflix Hystrix

Local calls remote with hystrix and fallback.

### Spring Actuator

Example health indicator with `hello.health.RemoteHello`.

### Spring Security

Simply configured Spring security on management endpoints.

### Swagger

Root service path shows Swagger UI.  Requires Tomcat.  See [Swagger SpringMVC
issue #15](https://github.com/adrianbk/swagger-springmvc-demo/issues/15).

### Testing

Unit and integration tests.
