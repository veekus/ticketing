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
@Table(name = "events", schema = "catalog")
public class EventEntity {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "venue", nullable = false, length = 200)
    private String venue;

    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;
}
