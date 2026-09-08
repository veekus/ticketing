# Ticketing
A ticket sales system with high competition for a limited resource: thousands of simultanious attempts to secure the last seats for a single event.

The project focuses on correctness under competitive load, rather than on number of features.

## Task
A seat cannot be sold twice. The reservation expires if the payment not recieved on time. A payment system failure should not leave the seat permanently blocked.

## Architecture
Modular monolith: the 'catalog', 'inventory' and 'orders' modules run in a single process, with a strict boundaries ('api'/'internal') and a separate database schema for each module.
The split into services is carried out when a measurable reason arises, not in advance. Details are in docs/adr.

## Technologies
Java 21, Spring Boot 4.1, PostgreSQL 16, Flyway, Testcontainers, k6

## Launch
    docker compose up -d
    ./mvnw spring-boot:run
Check: http://localhost:8080/actuator/health

## Tests
    ./mvnw verify
Integration tests start a real PostgreSQL via Testcontainers

## Measurement results
See benchmarks/ - throughput, p95 of response time, and the number of retries for each concurrency control strategy.

## Decisions made
See docs/adr - context, secision, reason, rejected options.