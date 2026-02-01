# Machine Events Backend

## Tech Stack
- Java 17
- Spring Boot(STS 4.3.0)
- H2 (in-memory)
- JPA

## How to Run
```bash
mvn spring-boot:run
```
## Structure
src/main/java/com/example/events
│
├── controller
│   ├── EventController.java
│   └── StatsController.java
│
├── service
│   ├── EventIngestionService.java
│   └── StatsService.java
|
├── util
│   ├── PayloadHashUtil.java
│   └── RejectionReason.java
│
├── repository
│   └── MachineEventRepository.java
│
├── models
│   └── MachineEvent.java
│
├── dto
│   ├── EventRequest.java
│   ├── BatchIngestResult.java
|   ├── LineDefectStats.java
│   └── StatsResponse.java
│
└── MachineEventsApplication.java
