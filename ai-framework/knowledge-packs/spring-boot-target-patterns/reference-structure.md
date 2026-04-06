# Spring Boot Reference Structure

```text
src/main/java/com/example/payments/<domain>/
  api/
    <Domain>Controller.java
    dto/
  application/
    <UseCase>Service.java
    mapper/
  domain/
    model/
    policy/
    exception/
  adapters/
    inbound/
    outbound/
    mainframe/
    messaging/
    persistence/
  config/
  observability/
```

## Coding rules
- constructor injection only
- no domain logic inside controller
- no direct legacy protocol details in API DTOs
- external calls isolated in adapters
- explicit exception mapping
- structured logging with correlation ids
