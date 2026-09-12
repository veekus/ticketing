package com.ticketing.inventory.internal;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID> {
}