package com.ticketing;

import com.ticketing.inventory.api.ReservationState;
import com.ticketing.inventory.api.SeatState;
import com.ticketing.inventory.internal.ReservationEntity;
import com.ticketing.inventory.internal.ReservationRepository;
import com.ticketing.inventory.internal.SeatStatusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

class InventoryRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    SeatStatusRepository seatStatusRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Test
    void seatStatusIsSeeded() {
        long available = seatStatusRepository.countByStatus(SeatState.AVAILABLE);
        assertThat(available).isEqualTo(500);
    }

    @Test
    void canPersistAndReadReservation() {
        UUID seatId = seatStatusRepository.findAll().get(0).getSeatId();

        ReservationEntity reservation = new ReservationEntity(
                UUID.randomUUID(),
                seatId,
                UUID.randomUUID(),
                ReservationState.ACTIVE,
                Instant.now(),
                Instant.now().plus(Duration.ofMinutes(10))
        );

        reservationRepository.save(reservation);

        ReservationEntity found = reservationRepository.findById(reservation.getId())
                .orElseThrow();

        assertThat(found.getStatus()).isEqualTo(ReservationState.ACTIVE);
        assertThat(found.getSeatId()).isEqualTo(seatId);
    }
}