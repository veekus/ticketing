package com.ticketing.inventory.internal;

import com.ticketing.inventory.api.SeatState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seat_status", schema = "inventory")
public class SeatStatusEntity {

    @Id
    @Column(name = "seat_id")
    private UUID seatId;

    @Column(name = "event_id", nullable = false)
    private UUID event_id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatState status;

    @Column(nullable = false)
    private long version;
}
