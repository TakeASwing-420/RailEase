package com.railway.model;

public class Passenger {
    private int serialNumber;
    private String trainNumber;
    private String name;
    private int age;
    private String gender;
    private String seatStatus;
    
    public Passenger() {
    }
    
    public Passenger(int serialNumber, String trainNumber, String name, int age, String gender, String seatStatus) {
        this.serialNumber = serialNumber;
        this.trainNumber = trainNumber;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.seatStatus = seatStatus;
    }
    
    // Getters and setters
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getSeatStatus() {
        return seatStatus;
    }
    
    public void setSeatStatus(String seatStatus) {
        this.seatStatus = seatStatus;
    }
    
    @Override
    public String toString() {
        return "Passenger #" + serialNumber +
               " - Name: " + name +
               ", Age: " + age +
               ", Gender: " + gender +
               ", Seat Status: " + seatStatus;
    }
}
