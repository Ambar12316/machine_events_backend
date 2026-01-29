# Benchmark & Performance Notes

## Test Environment
- Java 17
- Spring Boot (STS 4.3.0)
- H2 In-Memory Database
- Machine: Acer Nitro 5 AN515-45-R23Z(Ryzen 5 5600H, 16GB DDR4 3200MT/S)

## Ingestion Throughput
- Tested batch ingestion with 10 concurrent threads
- Each thread attempted to ingest the same event
- Result:
  - No duplicate records
  - Deduplication enforced via primary key
  - Thread safety validated using pessimistic locking

## Concurrency Handling
- Database-level locking using JPA
- Transactions ensure atomic ingestion
- Verified using CountDownLatch-based concurrency test

## Stats Query Performance
- Aggregation queries use GROUP BY and SUM
- Queries run in O(n) over filtered time window
- Suitable for moderate traffic volumes

## Known Limitations
- H2 is in-memory (data resets on restart)
- Not optimized for large-scale production workloads

## Future Improvements
- Switch to PostgreSQL for persistence
- Add DB indexes on (eventTime, machineId)
- Introduce async ingestion with Kafka
- Cache stats responses
