package com.railway.service;

import com.railway.model.Passenger;
import com.railway.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;

    @Autowired
    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }
    
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }
    
    public Optional<Passenger> getPassengerById(Long id) {
        return passengerRepository.findById(id);
    }
    
    public List<Passenger> getPassengersByTrainNumber(String trainNumber) {
        return passengerRepository.findByTrainNumber(trainNumber);
    }
    
    public Passenger savePassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }
    
    public void deletePassenger(Long id) {
        passengerRepository.deleteById(id);
    }
    
    public int getNextSerialNumber(String trainNumber) {
        List<Passenger> passengers = passengerRepository.findByTrainNumber(trainNumber);
        if (passengers.isEmpty()) {
            return 1;
        }
        
        int maxSerialNumber = 0;
        for (Passenger passenger : passengers) {
            if (passenger.getSerialNumber() > maxSerialNumber) {
                maxSerialNumber = passenger.getSerialNumber();
            }
        }
        
        return maxSerialNumber + 1;
    }
}