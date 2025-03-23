package com.railway.service;

import com.railway.model.Train;
import com.railway.util.ExcelUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelService {
    
    /**
     * Initialize Excel files if they don't exist
     */
    public void initializeExcelFiles(String trainDataFile, String passengerTicketsFile, String platformTicketsFile) {
        createTrainDataFileIfNeeded(trainDataFile);
        createPassengerTicketsFileIfNeeded(passengerTicketsFile);
        createPlatformTicketsFileIfNeeded(platformTicketsFile);
    }
    
    private void createTrainDataFileIfNeeded(String trainDataFile) {
        File file = new File(trainDataFile);
        if (!file.exists()) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Trains");
                
                // Create header row
                Row headerRow = sheet.createRow(0);
                String[] headers = {"S.No.", "Train Number", "From", "To", "Train Name", "Arrival Time", "Departure Time"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                }
                
                // Add some sample train data
                addSampleTrainData(sheet);
                
                // Write to file
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    workbook.write(outputStream);
                }
                System.out.println("Created train data file with sample data.");
            } catch (IOException e) {
                System.out.println("Error creating train data file: " + e.getMessage());
            }
        }
    }
    
    private void addSampleTrainData(Sheet sheet) {
        String[][] sampleData = {
            {"1", "12345", "Delhi", "Mumbai", "Rajdhani Express", "23:00", "08:00"},
            {"2", "22222", "Chennai", "Bangalore", "Shatabdi Express", "10:00", "13:00"},
            {"3", "33333", "Kolkata", "Delhi", "Duronto Express", "18:00", "06:00"},
            {"4", "44444", "Mumbai", "Chennai", "Chennai Mail", "22:00", "14:00"},
            {"5", "55555", "Bangalore", "Delhi", "Karnataka Express", "16:00", "20:00"}
        };
        
        for (int i = 0; i < sampleData.length; i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < sampleData[i].length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(sampleData[i][j]);
            }
        }
    }
    
    private void createPassengerTicketsFileIfNeeded(String passengerTicketsFile) {
        File file = new File(passengerTicketsFile);
        if (!file.exists()) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Passengers");
                
                // Create header row
                Row headerRow = sheet.createRow(0);
                String[] headers = {"S.No.", "Train Number", "Name", "Age", "Gender", "Seat Status"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                }
                
                // Write to file
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    workbook.write(outputStream);
                }
                System.out.println("Created passenger tickets file.");
            } catch (IOException e) {
                System.out.println("Error creating passenger tickets file: " + e.getMessage());
            }
        }
    }
    
    private void createPlatformTicketsFileIfNeeded(String platformTicketsFile) {
        File file = new File(platformTicketsFile);
        if (!file.exists()) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Platform Tickets");
                
                // Create header row
                Row headerRow = sheet.createRow(0);
                String[] headers = {"S.No.", "Train Number", "Tickets Count"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                }
                
                // Write to file
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    workbook.write(outputStream);
                }
                System.out.println("Created platform tickets file.");
            } catch (IOException e) {
                System.out.println("Error creating platform tickets file: " + e.getMessage());
            }
        }
    }
    
    /**
     * Find a train by its number
     */
    public Train findTrainByNumber(String trainDataFile, String trainNumber) {
        try (FileInputStream fis = new FileInputStream(trainDataFile);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header
                
                String currentTrainNumber = ExcelUtil.getCellValue(row.getCell(1));
                if (currentTrainNumber.equals(trainNumber)) {
                    return createTrainFromRow(row);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading train data file: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Find trains by destination
     */
    public List<Train> findTrainsByDestination(String trainDataFile, String destination) {
        List<Train> trains = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(trainDataFile);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header
                
                String currentDestination = ExcelUtil.getCellValue(row.getCell(3));
                if (currentDestination.equalsIgnoreCase(destination)) {
                    trains.add(createTrainFromRow(row));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading train data file: " + e.getMessage());
        }
        
        return trains;
    }
    
    private Train createTrainFromRow(Row row) {
        String trainNumber = ExcelUtil.getCellValue(row.getCell(1));
        String fromStation = ExcelUtil.getCellValue(row.getCell(2));
        String toStation = ExcelUtil.getCellValue(row.getCell(3));
        String trainName = ExcelUtil.getCellValue(row.getCell(4));
        String arrivalTime = ExcelUtil.getCellValue(row.getCell(5));
        String departureTime = ExcelUtil.getCellValue(row.getCell(6));
        
        return new Train(trainNumber, fromStation, toStation, departureTime, arrivalTime, trainName);
    }
}
