package com.railway.model;

import javax.persistence.*;

@Entity
@Table(name = "trains")
public class Train {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "train_number", unique = true, nullable = false)
    private String trainNumber;
    
    @Column(name = "from_station", nullable = false)
    private String fromStation;
    
    @Column(name = "to_station", nullable = false)
    private String toStation;
    
    @Column(name = "departure_time", nullable = false)
    private String departureTime;
    
    @Column(name = "arrival_time", nullable = false)
    private String arrivalTime;
    
    @Column(name = "train_name", nullable = false)
    private String trainName;
    
    // Default constructor required by JPA
    public Train() {
    }
    
    // Full constructor
    public Train(String trainNumber, String fromStation, String toStation, String departureTime, String arrivalTime, String trainName) {
        this.trainNumber = trainNumber;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.trainName = trainName;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTrainNumber() {
        return trainNumber;
    }
    
    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }
    
    public String getFromStation() {
        return fromStation;
    }
    
    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }
    
    public String getToStation() {
        return toStation;
    }
    
    public void setToStation(String toStation) {
        this.toStation = toStation;
    }
    
    public String getDepartureTime() {
        return departureTime;
    }
    
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
    
    public String getArrivalTime() {
        return arrivalTime;
    }
    
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    public String getTrainName() {
        return trainName;
    }
    
    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }
    
    @Override
    public String toString() {
        return "Train Number: " + trainNumber + 
               "\nTrain Name: " + trainName +
               "\nFrom: " + fromStation + 
               "\nTo: " + toStation + 
               "\nDeparture Time: " + departureTime + 
               "\nArrival Time: " + arrivalTime;
    }
}
