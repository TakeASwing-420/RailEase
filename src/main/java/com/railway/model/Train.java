package com.railway.model;

public class Train {
    private String trainNumber;
    private String fromStation;
    private String toStation;
    private String departureTime;
    private String arrivalTime;
    private String trainName;
    
    public Train() {
    }
    
    public Train(String trainNumber, String fromStation, String toStation, String departureTime, String arrivalTime, String trainName) {
        this.trainNumber = trainNumber;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.trainName = trainName;
    }
    
    // Getters and setters
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
