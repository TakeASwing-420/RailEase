package com.railway.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_tickets")
public class PlatformTicket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "serial_number")
    private int serialNumber;
    
    @Column(name = "train_number", nullable = false)
    private String trainNumber;
    
    @Column(name = "tickets_count", nullable = false)
    private int ticketsCount;
    
    @Column(name = "issue_time", nullable = false)
    private LocalDateTime issueTime;
    
    // Default constructor required by JPA
    public PlatformTicket() {
    }
    
    // Full constructor
    public PlatformTicket(Long id, int serialNumber, String trainNumber, int ticketsCount, LocalDateTime issueTime) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.trainNumber = trainNumber;
        this.ticketsCount = ticketsCount;
        this.issueTime = issueTime;
    }
    
    // Constructor without id for easier creation
    public PlatformTicket(int serialNumber, String trainNumber, int ticketsCount) {
        this.serialNumber = serialNumber;
        this.trainNumber = trainNumber;
        this.ticketsCount = ticketsCount;
        this.issueTime = LocalDateTime.now();
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public int getSerialNumber() {
        return serialNumber;
    }
    
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public String getTrainNumber() {
        return trainNumber;
    }
    
    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }
    
    public int getTicketsCount() {
        return ticketsCount;
    }
    
    public void setTicketsCount(int ticketsCount) {
        this.ticketsCount = ticketsCount;
    }
    
    public LocalDateTime getIssueTime() {
        return issueTime;
    }
    
    public void setIssueTime(LocalDateTime issueTime) {
        this.issueTime = issueTime;
    }
    
    @Override
    public String toString() {
        return "Platform Ticket #" + serialNumber +
               " - Train Number: " + trainNumber +
               ", Tickets Count: " + ticketsCount +
               ", Issue Time: " + issueTime;
    }
}