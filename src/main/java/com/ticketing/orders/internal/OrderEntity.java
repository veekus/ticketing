package com.ticketing.orders.internal;

import com.ticketing.inventory.api.SeatState;
import com.ticketing.orders.api.OrderStatus;
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
@Table(name = "orders", schema = "orders")
public class OrderEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID user_id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "amount_cents", nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
