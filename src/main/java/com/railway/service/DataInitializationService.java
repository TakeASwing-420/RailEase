package com.railway.service;

import com.railway.model.Passenger;
import com.railway.model.Train;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataInitializationService {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);
    
    @Value("${app.data.dir}")
    private String dataDirectory;
    
    private final TrainService trainService;
    private final PassengerService passengerService;
    
    @Autowired
    public DataInitializationService(TrainService trainService, PassengerService passengerService) {
        this.trainService = trainService;
        this.passengerService = passengerService;
    }
    
    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void initializeData() {
        logger.info("Initializing sample data asynchronously...");
        
        File dataDir = new File(dataDirectory);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
            logger.info("Created data directory: {}", dataDirectory);
        }
        
        createSampleTrainData();
        createSamplePassengerData();
        
        logger.info("Data initialization completed.");
    }
    
    private void createSamplePassengerData() {
        // Skip creating sample passenger data if we already have passengers
        if (!passengerService.getAllPassengers().isEmpty()) {
            logger.info("Passenger data already exists. Skipping sample data creation.");
            return;
        }
        
        List<Passenger> samplePassengers = new ArrayList<>();
        
        // Only add if train exists
        if (trainService.trainExistsByNumber("12345")) {
            samplePassengers.add(new Passenger(1, "12345", "Rahul Sharma", 28, "Male", "Confirmed"));
            samplePassengers.add(new Passenger(2, "12345", "Priya Singh", 25, "Female", "Confirmed"));
        }
        
        if (trainService.trainExistsByNumber("23456")) {
            samplePassengers.add(new Passenger(1, "23456", "Amit Kumar", 35, "Male", "Confirmed"));
            samplePassengers.add(new Passenger(2, "23456", "Sneha Patel", 30, "Female", "Waitlist"));
        }
        
        for (Passenger passenger : samplePassengers) {
            passengerService.savePassenger(passenger);
        }
        
        logger.info("Created {} sample passengers", samplePassengers.size());
    }
    
    private void createSampleTrainData() {
        // Skip creating sample train data if we already have trains
        if (!trainService.getAllTrains().isEmpty()) {
            logger.info("Train data already exists. Skipping sample data creation.");
            return;
        }
        
        List<Train> sampleTrains = new ArrayList<>();
        
        sampleTrains.add(new Train("12345", "Delhi", "Mumbai", "08:00", "16:00", "Rajdhani Express"));
        sampleTrains.add(new Train("23456", "Chennai", "Bangalore", "09:30", "12:30", "Shatabdi Express"));
        sampleTrains.add(new Train("34567", "Kolkata", "Delhi", "19:00", "08:00", "Duronto Express"));
        sampleTrains.add(new Train("45678", "Mumbai", "Goa", "07:15", "15:45", "Jan Shatabdi Express"));
        sampleTrains.add(new Train("56789", "Hyderabad", "Chennai", "13:00", "20:30", "Charminar Express"));
        
        trainService.saveAllTrains(sampleTrains);
        logger.info("Created {} sample trains", sampleTrains.size());
    }
}