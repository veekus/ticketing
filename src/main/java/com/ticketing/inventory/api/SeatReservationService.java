package com.ticketing.inventory.api;

import java.util.UUID;

public interface SeatReservationService {
    ReservationResult reserve(UUID seatId, UUID userId);
}
