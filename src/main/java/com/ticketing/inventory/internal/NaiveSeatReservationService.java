package com.ticketing.inventory.internal;

import com.ticketing.inventory.api.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class NaiveSeatReservationService implements SeatReservationService {

    private final SeatStatusRepository statusRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public ReservationResult reserve(UUID seatId, UUID userId) {

        SeatStatusEntity seat = statusRepository.findById(seatId)
                .orElseThrow(() -> new SeatNotFoundException(seatId));

        if (seat.getStatus() != SeatState.AVAILABLE) {
            return ReservationResult.rejected("SEAT_NOT_AVAILABLE");
        }

        seat.setStatus(SeatState.RESERVED);
        statusRepository.save(seat);

        ReservationEntity reservation = new ReservationEntity(
                UUID.randomUUID(),
                seatId,
                userId,
                ReservationState.ACTIVE,
                Instant.now(),
                Instant.now().plus(Duration.ofMinutes(10))
        );
        reservationRepository.save(reservation);

        return ReservationResult.confirmed(reservation.getId());
    }
}
