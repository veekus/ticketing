package com.ticketing.inventory.internal;


import com.ticketing.inventory.api.SeatState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SeatStatusRepository extends JpaRepository<SeatStatusEntity, UUID> {

    long countByStatus(SeatState status);
}
