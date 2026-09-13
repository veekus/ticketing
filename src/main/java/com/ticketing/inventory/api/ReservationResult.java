package com.ticketing.inventory.api;

import java.util.UUID;

public class ReservationResult {

    private final boolean confirmed;
    private final UUID reservationId;
    private final String rejectionReason;

    private ReservationResult(boolean confirmed, UUID reservationId, String rejectionReason) {
        this.confirmed = confirmed;
        this.reservationId = reservationId;
        this.rejectionReason = rejectionReason;
    }

    public static ReservationResult confirmed(UUID reservationId) {
        return new ReservationResult(true, reservationId, null);
    }

    public static ReservationResult rejected(String reason) {
        return new ReservationResult(false, null, reason);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }
}
