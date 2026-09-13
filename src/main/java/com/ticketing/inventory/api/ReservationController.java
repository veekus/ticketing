package com.ticketing.inventory.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final SeatReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResult> reserve(@RequestBody ReservationRequest request) {
        ReservationResult result = reservationService.reserve(request.seatId(), request.userId());
        return ResponseEntity.ok(result);
    }
}
