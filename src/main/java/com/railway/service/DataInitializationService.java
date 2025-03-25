package com.railway.service;

import com.railway.model.Passenger;
import com.railway.model.Train;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
    public void initializeData() {
        logger.info("Initializing data from Excel files...");
        
        File dataDir = new File(dataDirectory);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
            logger.info("Created data directory: {}", dataDirectory);
        }
        
        loadTrainsFromExcel();
        loadPassengersFromExcel();
        
        logger.info("Data initialization completed.");
    }
    
    private void loadTrainsFromExcel() {
        File trainFile = new File(dataDirectory + "/trains.xlsx");
        if (!trainFile.exists()) {
            logger.info("Train data file not found. Creating sample data.");
            createSampleTrainData();
            return;
        }
        
        try (FileInputStream fis = new FileInputStream(trainFile);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            
            // Skip the header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            
            List<Train> trains = new ArrayList<>();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                
                String trainNumber = getStringCellValue(row.getCell(0));
                String trainName = getStringCellValue(row.getCell(1));
                String fromStation = getStringCellValue(row.getCell(2));
                String toStation = getStringCellValue(row.getCell(3));
                String departureTime = getStringCellValue(row.getCell(4));
                String arrivalTime = getStringCellValue(row.getCell(5));
                
                if (trainNumber != null && !trainNumber.isEmpty()) {
                    Train train = new Train(trainNumber, fromStation, toStation, departureTime, arrivalTime, trainName);
                    trains.add(train);
                }
            }
            
            trainService.saveAllTrains(trains);
            logger.info("Loaded {} trains from Excel", trains.size());
            
        } catch (IOException e) {
            logger.error("Error loading train data from Excel", e);
        }
    }
    
    private void loadPassengersFromExcel() {
        File passengerFile = new File(dataDirectory + "/passengers.xlsx");
        if (!passengerFile.exists()) {
            logger.info("Passenger data file not found. Skipping passenger data loading.");
            return;
        }
        
        try (FileInputStream fis = new FileInputStream(passengerFile);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            
            // Skip the header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            
            List<Passenger> passengers = new ArrayList<>();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                
                int serialNumber = (int) row.getCell(0).getNumericCellValue();
                String trainNumber = getStringCellValue(row.getCell(1));
                String name = getStringCellValue(row.getCell(2));
                int age = (int) row.getCell(3).getNumericCellValue();
                String gender = getStringCellValue(row.getCell(4));
                String seatStatus = getStringCellValue(row.getCell(5));
                
                if (trainNumber != null && !trainNumber.isEmpty()) {
                    Passenger passenger = new Passenger(serialNumber, trainNumber, name, age, gender, seatStatus);
                    passengers.add(passenger);
                }
            }
            
            for (Passenger passenger : passengers) {
                passengerService.savePassenger(passenger);
            }
            logger.info("Loaded {} passengers from Excel", passengers.size());
            
        } catch (IOException e) {
            logger.error("Error loading passenger data from Excel", e);
        }
    }
    
    private void createSampleTrainData() {
        List<Train> sampleTrains = new ArrayList<>();
        List<Train> trainsToSave = new ArrayList<>();
        
        sampleTrains.add(new Train("12345", "Delhi", "Mumbai", "08:00", "16:00", "Rajdhani Express"));
        sampleTrains.add(new Train("23456", "Chennai", "Bangalore", "09:30", "12:30", "Shatabdi Express"));
        sampleTrains.add(new Train("34567", "Kolkata", "Delhi", "19:00", "08:00", "Duronto Express"));
        sampleTrains.add(new Train("45678", "Mumbai", "Goa", "07:15", "15:45", "Jan Shatabdi Express"));
        sampleTrains.add(new Train("56789", "Hyderabad", "Chennai", "13:00", "20:30", "Charminar Express"));
        
        // Only add trains that don't already exist
        for (Train train : sampleTrains) {
            if (!trainService.trainExistsByNumber(train.getTrainNumber())) {
                trainsToSave.add(train);
            } else {
                logger.info("Train with number {} already exists. Skipping.", train.getTrainNumber());
            }
        }
        
        if (!trainsToSave.isEmpty()) {
            trainService.saveAllTrains(trainsToSave);
            logger.info("Created {} sample trains", trainsToSave.size());
        } else {
            logger.info("No new sample trains needed to be created");
        }
    }
    
    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }
}