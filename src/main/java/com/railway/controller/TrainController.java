package com.railway.controller;

import com.railway.model.Train;
import com.railway.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trains")
public class TrainController {

    private final TrainService trainService;

    @Autowired
    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }
    
    @GetMapping
    public ResponseEntity<List<Train>> getAllTrains() {
        List<Train> trains = trainService.getAllTrains();
        return ResponseEntity.ok(trains);
    }
    
    @GetMapping("/{trainNumber}")
    public ResponseEntity<Train> getTrainByNumber(@PathVariable String trainNumber) {
        Optional<Train> train = trainService.getTrainByNumber(trainNumber);
        return train.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<Train> searchTrainByNumber(@RequestParam String trainNumber) {
        Optional<Train> train = trainService.getTrainByNumber(trainNumber);
        return train.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/destination/{destination}")
    public ResponseEntity<List<Train>> getTrainsByDestination(@PathVariable String destination) {
        List<Train> trains = trainService.getTrainsByDestination(destination);
        return ResponseEntity.ok(trains);
    }
    
    @GetMapping("/search/destination")
    public ResponseEntity<List<Train>> searchTrainsByDestination(@RequestParam String destination) {
        List<Train> trains = trainService.getTrainsByDestination(destination);
        return ResponseEntity.ok(trains);
    }
    
    @PostMapping
    public ResponseEntity<Train> createTrain(@Valid @RequestBody Train train) {
        if (trainService.trainExistsByNumber(train.getTrainNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Train savedTrain = trainService.saveTrain(train);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrain);
    }
    
    @PutMapping("/{trainNumber}")
    public ResponseEntity<Train> updateTrain(@PathVariable String trainNumber, @Valid @RequestBody Train train) {
        if (!trainService.trainExistsByNumber(trainNumber)) {
            return ResponseEntity.notFound().build();
        }
        train.setTrainNumber(trainNumber);
        Train updatedTrain = trainService.saveTrain(train);
        return ResponseEntity.ok(updatedTrain);
    }
    
    @DeleteMapping("/{trainNumber}")
    public ResponseEntity<Void> deleteTrain(@PathVariable String trainNumber) {
        if (!trainService.trainExistsByNumber(trainNumber)) {
            return ResponseEntity.notFound().build();
        }
        // Get the train first to know its ID
        Optional<Train> trainOptional = trainService.getTrainByNumber(trainNumber);
        if (trainOptional.isPresent()) {
            trainService.deleteTrain(trainOptional.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}