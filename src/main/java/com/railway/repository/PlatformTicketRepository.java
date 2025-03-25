package com.railway.repository;

import com.railway.model.PlatformTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlatformTicketRepository extends JpaRepository<PlatformTicket, Long> {
    
    List<PlatformTicket> findByTrainNumber(String trainNumber);
    
    @Query("SELECT pt FROM PlatformTicket pt WHERE pt.issueTime BETWEEN :startTime AND :endTime")
    List<PlatformTicket> findTicketsInTimeRange(LocalDateTime startTime, LocalDateTime endTime);
}