package com.ticketing.inventory.api;

import java.util.UUID;

public record ReservationRequest(UUID seatId, UUID userId) {
}
