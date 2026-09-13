package com.ticketing;

import com.ticketing.inventory.api.ReservationResult;
import com.ticketing.inventory.api.SeatReservationService;
import com.ticketing.inventory.internal.SeatStatusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import static org.assertj.core.api.Assertions.assertThat;

class OversellTest extends AbstractIntegrationTest {

    @Autowired
    SeatReservationService reservationService;

    @Autowired
    SeatStatusRepository seatStatusRepository;

    @Test
    void naiveImplementationOversellsSingleSeat() throws Exception {
        UUID seatId = seatStatusRepository.findAll().get(0).getSeatId();
        int threads = 50;

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch finishGate = new CountDownLatch(threads);
        AtomicInteger confirmed = new AtomicInteger();

        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                try {
                    startGate.await();
                    ReservationResult result =
                            reservationService.reserve(seatId, UUID.randomUUID());
                    if (result.isConfirmed()) {
                        confirmed.incrementAndGet();
                    }
                } catch (Exception ignored) {
                    // отказы и исключения считаем неуспехом
                } finally {
                    finishGate.countDown();
                }
            });
        }

        startGate.countDown();
        finishGate.await(30, TimeUnit.SECONDS);
        pool.shutdown();

        System.out.println("Подтверждённых резервов: " + confirmed.get());
        assertThat(confirmed.get()).isEqualTo(1);
    }
}
