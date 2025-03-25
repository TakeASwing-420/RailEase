package com.railway.service;

import com.railway.model.Train;
import com.railway.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainService {

    private final TrainRepository trainRepository;

    @Autowired
    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }
    
    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }
    
    public Optional<Train> getTrainById(Long id) {
        return trainRepository.findById(id);
    }
    
    public Optional<Train> getTrainByNumber(String trainNumber) {
        return trainRepository.findByTrainNumber(trainNumber);
    }
    
    public List<Train> getTrainsByDestination(String destination) {
        return trainRepository.findByToStationContainingIgnoreCase(destination);
    }
    
    public Train saveTrain(Train train) {
        return trainRepository.save(train);
    }
    
    public List<Train> saveAllTrains(List<Train> trains) {
        return trainRepository.saveAll(trains);
    }
    
    public void deleteTrain(Long id) {
        trainRepository.deleteById(id);
    }
    
    public boolean trainExistsByNumber(String trainNumber) {
        return trainRepository.findByTrainNumber(trainNumber).isPresent();
    }
}