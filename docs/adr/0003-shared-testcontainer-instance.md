Status: accepted Date: 2026-09-13

Context

AbstractIntegrationTest originally used @Testcontainers with @Container on a static PostgreSQLContainer field, following the standard Testcontainers/JUnit 5 pattern. InventoryRepositoryTest and OversellTest both extend this base class.

Running the full test suite (./mvnw clean test) produced a hard-to-read failure:

org.springframework.transaction.CannotCreateTransactionException:
Could not open JPA EntityManager for transaction
Caused by: ... Connection refused: getsockopt

Investigation of the full Maven log (not just the [ERROR] summary) showed the actual sequence of events:

InventoryRepositoryTest runs first. @Testcontainers starts a Postgres container on a random host port (e.g. 49845). Spring Boot boots a full application context, @ServiceConnection wires Hikari to that port, tests pass.
At the end of the class, @Testcontainers stops the container, because its lifecycle is scoped to the test class it annotates — even though the container field is inherited from a shared parent.
OversellTest runs next. @Testcontainers starts a new container on a different random port (e.g. 50170).
Spring Boot's test context caching kicks in: because the effective configuration looks identical to the previous test class, Spring reuses the already-built application context from step 1 instead of creating a new one. That cached context still points Hikari at the old, now-dead port 49845.
Every query in OversellTest times out after 30 seconds trying to reach a port that no longer has a container behind it.

The bug was invisible from the [ERROR] block alone; it only became legible by reading the full log and noticing two different Creating container for image: postgres:16 lines with two different ports, and a missing "Started OversellTest" boot banner that would have indicated a fresh context.

Decision

Replace the @Testcontainers / @Container lifecycle annotations with a single container instance, started once via a static initializer, shared for the entire test JVM process:

java
@SpringBootTest
public abstract class AbstractIntegrationTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    static {
        postgres.start();
    }
}

The container is never stopped explicitly. Testcontainers' Ryuk sidecar process (visible in the logs as testcontainers/ryuk) reaps it automatically when the test JVM exits.

Reason

@Testcontainers manages container lifecycle per test class by design — this is correct and desirable when each class needs full isolation. It becomes a problem specifically in combination with Spring Boot's test context caching: the cache key is derived from the declared configuration, not from the live container state, so Spring cannot detect that the container behind a cached context has already been torn down and replaced.

A single static container, started once and never restarted, removes the mismatch entirely: there is only ever one port, one container, and the cached Spring context remains valid for the whole test run.

This is also faster: starting a Postgres container takes over a second: paying that cost once for the whole suite instead of once per test class shortens mvn test noticeably as the number of test classes grows.

Rejected
Keep @Testcontainers per class, disable Spring context caching (@DirtiesContext on every class). Works, but forces a full Spring Boot context rebuild before every test class, which is slower than the problem it solves and does not scale as more test classes are added.
Keep @Testcontainers, add @DynamicPropertySource per class to re-inject the container's JDBC URL fresh for every class. Correct in principle, but reintroduces a new container (and a new schema migration run) per class, which is unnecessary overhead for tests that are fine sharing one database.
Consequences
All integration tests in the suite now share one Postgres instance and one schema. Tests must not assume a pristine, empty database — data created by one test class can be visible to another if they run in the same JVM. This did not require changes yet, but should be kept in mind: prefer querying for specific, uniquely generated ids over findAll().get(0)-style assumptions wherever possible, or clean up state at the end of a test.
Debugging this required reading the entire Maven log, not just the [ERROR] block — the root cause (two containers, one stale port) was only visible several hundred lines above the exception. Worth remembering as a general debugging habit for the rest of the project.
Revisit when

If a future test class genuinely needs full database isolation from the rest of the suite (e.g. destructive schema-level tests), give that specific class its own container explicitly rather than reintroducing @Testcontainers globally.