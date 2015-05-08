# Simple Boot

## Modules

* [local](local/) - "Local" service demonstrating GET calling to POST
* [remote](remote/) - "Remote" service demonstrating POST

## Features

### Spring Boot

### Audit Service

Example auditing, prints to log.

### Health

Simple health indicator for a remote service.

### Heartbeat

A simple timestamp endpoint on `/heartbeat`.

### Logging

Configure logging in `application.yml` and `bootstrap.yml`.

### Netflix Feign

GET on local calls to POST on remote via a Feign client.

### Netflix Hystrix

Local calls remote with hystrix and fallback.

### Spring Actuator

Spring actuator endpoints under `/manage`.

### Spring Security

Simply configured Spring security on management endpoints.

### Swagger

Root service path shows Swagger UI.  Requires Tomcat.  See [Swagger SpringMVC
issue #15](https://github.com/adrianbk/swagger-springmvc-demo/issues/15).

### Testing

Unit and integration tests.
