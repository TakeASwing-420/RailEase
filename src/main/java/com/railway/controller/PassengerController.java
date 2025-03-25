package com.railway.controller;

import com.railway.model.Passenger;
import com.railway.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    @Autowired
    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }
    
    @GetMapping
    public ResponseEntity<List<Passenger>> getAllPassengers() {
        List<Passenger> passengers = passengerService.getAllPassengers();
        return ResponseEntity.ok(passengers);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Passenger> getPassengerById(@PathVariable Long id) {
        return passengerService.getPassengerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/train/{trainNumber}")
    public ResponseEntity<List<Passenger>> getPassengersByTrainNumber(@PathVariable String trainNumber) {
        List<Passenger> passengers = passengerService.getPassengersByTrainNumber(trainNumber);
        return ResponseEntity.ok(passengers);
    }
    
    @PostMapping
    public ResponseEntity<Passenger> createPassenger(@Valid @RequestBody Passenger passenger) {
        if (passenger.getSerialNumber() == 0) {
            // Auto-assign a serial number if not provided
            int nextSerialNumber = passengerService.getNextSerialNumber(passenger.getTrainNumber());
            passenger.setSerialNumber(nextSerialNumber);
        }
        
        Passenger savedPassenger = passengerService.savePassenger(passenger);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPassenger);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Passenger> updatePassenger(@PathVariable Long id, @Valid @RequestBody Passenger passenger) {
        if (!passengerService.getPassengerById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        passenger.setId(id);
        Passenger updatedPassenger = passengerService.savePassenger(passenger);
        return ResponseEntity.ok(updatedPassenger);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        if (!passengerService.getPassengerById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }
}