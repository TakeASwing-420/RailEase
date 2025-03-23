package com.railway.service;

import com.railway.util.ExcelUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TicketService {
    private static final int TICKET_THRESHOLD = 500;
    private static int totalPassengerTickets = 0;
    private static final Map<String, Integer> platformTickets = new HashMap<>();
    private static int serialNumberPassenger = 1;
    private static int serialNumberPlatform = 1;
    
    /**
     * Check if tickets can be booked
     */
    public boolean canBookTickets() {
        return totalPassengerTickets < TICKET_THRESHOLD;
    }
    
    /**
     * Save passenger data to Excel file
     */
    public void savePassengerData(String passengerTicketsFile, String trainNumber, String name, int age, String gender, String seatStatus) {
        try {
            File file = new File(passengerTicketsFile);
            Workbook workbook;
            Sheet sheet;

            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    workbook = new XSSFWorkbook(fis);
                }
                sheet = workbook.getSheet("Passengers");
                if (sheet == null) {
                    sheet = workbook.createSheet("Passengers");
                    // Create header row
                    Row headerRow = sheet.createRow(0);
                    String[] headers = {"S.No.", "Train Number", "Name", "Age", "Gender", "Seat Status"};
                    for (int i = 0; i < headers.length; i++) {
                        Cell cell = headerRow.createCell(i);
                        cell.setCellValue(headers[i]);
                    }
                }
                
                // Find the last row number with data
                int lastRowNum = getLastRowNum(sheet);
                serialNumberPassenger = lastRowNum + 1;
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Passengers");
                
                // Create header row
                Row headerRow = sheet.createRow(0);
                String[] headers = {"S.No.", "Train Number", "Name", "Age", "Gender", "Seat Status"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                }
            }

            Row row = sheet.createRow(serialNumberPassenger);
            row.createCell(0).setCellValue(serialNumberPassenger);
            row.createCell(1).setCellValue(trainNumber);
            row.createCell(2).setCellValue(name);
            row.createCell(3).setCellValue(age);
            row.createCell(4).setCellValue(gender);
            row.createCell(5).setCellValue(seatStatus);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
            workbook.close();
            
            totalPassengerTickets++;
            serialNumberPassenger++;
        } catch (IOException e) {
            System.out.println("Error writing to passenger file: " + e.getMessage());
        }
    }
    
    /**
     * Save platform ticket data to Excel file
     */
    public void savePlatformTicketData(String platformTicketsFile, String trainNumber, int numTickets) {
        try {
            File file = new File(platformTicketsFile);
            Workbook workbook;
            Sheet sheet;

            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    workbook = new XSSFWorkbook(fis);
                }
                sheet = workbook.getSheet("Platform Tickets");
                if (sheet == null) {
                    sheet = workbook.createSheet("Platform Tickets");
                    // Create header row
                    Row headerRow = sheet.createRow(0);
                    String[] headers = {"S.No.", "Train Number", "Tickets Count"};
                    for (int i = 0; i < headers.length; i++) {
                        Cell cell = headerRow.createCell(i);
                        cell.setCellValue(headers[i]);
                    }
                }
                
                // Find the last row number with data
                int lastRowNum = getLastRowNum(sheet);
                serialNumberPlatform = lastRowNum + 1;
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Platform Tickets");
                
                // Create header row
                Row headerRow = sheet.createRow(0);
                String[] headers = {"S.No.", "Train Number", "Tickets Count"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                }
            }

            // Update the map
            platformTickets.put(trainNumber, platformTickets.getOrDefault(trainNumber, 0) + numTickets);
            
            Row row = sheet.createRow(serialNumberPlatform);
            row.createCell(0).setCellValue(serialNumberPlatform);
            row.createCell(1).setCellValue(trainNumber);
            row.createCell(2).setCellValue(numTickets);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
            workbook.close();
            
            serialNumberPlatform++;
        } catch (IOException e) {
            System.out.println("Error writing to platform ticket file: " + e.getMessage());
        }
    }
    
    /**
     * Get total passenger tickets sold
     */
    public int getTotalPassengerTickets() {
        return totalPassengerTickets;
    }
    
    /**
     * Get total platform tickets sold
     */
    public int getTotalPlatformTickets() {
        return platformTickets.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Get platform tickets for a specific train
     */
    public int getPlatformTicketsForTrain(String trainNumber) {
        return platformTickets.getOrDefault(trainNumber, 0);
    }
    
    /**
     * Helper method to find the last row number with data
     */
    private int getLastRowNum(Sheet sheet) {
        int lastRowNum = sheet.getLastRowNum();
        if (lastRowNum <= 0) {
            return 0;
        }
        return lastRowNum;
    }
    
    /**
     * Load existing tickets data from Excel files
     */
    public void loadExistingTicketsData(String passengerTicketsFile, String platformTicketsFile) {
        loadPassengerTickets(passengerTicketsFile);
        loadPlatformTickets(platformTicketsFile);
    }
    
    private void loadPassengerTickets(String passengerTicketsFile) {
        File file = new File(passengerTicketsFile);
        if (!file.exists()) {
            return;
        }
        
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet("Passengers");
            if (sheet == null) {
                return;
            }
            
            int lastRowNum = sheet.getLastRowNum();
            totalPassengerTickets = lastRowNum; // Header is row 0
            serialNumberPassenger = lastRowNum + 1;
        } catch (IOException e) {
            System.out.println("Error loading passenger tickets: " + e.getMessage());
        }
    }
    
    private void loadPlatformTickets(String platformTicketsFile) {
        File file = new File(platformTicketsFile);
        if (!file.exists()) {
            return;
        }
        
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet("Platform Tickets");
            if (sheet == null) {
                return;
            }
            
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header
                
                String trainNumber = ExcelUtil.getCellValue(row.getCell(1));
                int count = 0;
                try {
                    count = Integer.parseInt(ExcelUtil.getCellValue(row.getCell(2)));
                } catch (NumberFormatException e) {
                    // Skip invalid rows
                    continue;
                }
                
                platformTickets.put(trainNumber, platformTickets.getOrDefault(trainNumber, 0) + count);
            }
            
            int lastRowNum = sheet.getLastRowNum();
            serialNumberPlatform = lastRowNum + 1;
        } catch (IOException e) {
            System.out.println("Error loading platform tickets: " + e.getMessage());
        }
    }
}
