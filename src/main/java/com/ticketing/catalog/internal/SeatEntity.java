package com.ticketing.catalog.internal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seats", schema = "catalog")
public class SeatEntity {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(nullable = false, length = 50)
    private String sector;

    @Column(name = "row_no", nullable = false)
    private int rowNo;

    @Column(name = "seat_no", nullable = false)
    private int seatNo;
}
