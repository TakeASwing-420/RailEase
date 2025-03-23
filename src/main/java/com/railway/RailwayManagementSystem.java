package com.railway;

import com.railway.model.Train;
import com.railway.service.ExcelService;
import com.railway.service.TicketService;

import java.io.File;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class RailwayManagementSystem {
    private static final String DATA_DIR = "data";
    private static final String TRAIN_DATA_FILE = "data/data.xlsx";
    private static final String PASSENGER_TICKETS_FILE = "data/passenger_tickets.xlsx";
    private static final String PLATFORM_TICKETS_FILE = "data/platform_tickets.xlsx";

    private static final ExcelService excelService = new ExcelService();
    private static final TicketService ticketService = new TicketService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Create the data directory if it doesn't exist
        initializeDataDirectory();
        
        while (true) {
            displayMenu();
            String choice = getUserChoice();
            
            switch (choice) {
                case "1":
                    searchByTrainNumber();
                    break;
                case "2":
                    searchByDestination();
                    break;
                case "3":
                    bookPlatformTicket();
                    break;
                case "4":
                    generateHourlyReport();
                    break;
                case "5":
                    queryPlatformTickets();
                    break;
                case "6":
                    System.out.println("Exiting program...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
    
    private static void initializeDataDirectory() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            if (dataDir.mkdir()) {
                System.out.println("Created data directory");
            } else {
                System.out.println("Failed to create data directory. Using current directory.");
            }
        }
        
        // Initialize Excel files if they don't exist
        excelService.initializeExcelFiles(TRAIN_DATA_FILE, PASSENGER_TICKETS_FILE, PLATFORM_TICKETS_FILE);
    }
    
    private static void displayMenu() {
        System.out.println("\n===== Railway Management System =====");
        System.out.println("1. Search by Train Number");
        System.out.println("2. Search by Destination Station");
        System.out.println("3. Book Platform Ticket");
        System.out.println("4. Generate Hourly Report");
        System.out.println("5. Query Platform Tickets");
        System.out.println("6. Exit");
        System.out.println("=====================================");
    }
    
    private static String getUserChoice() {
        System.out.print("Enter your choice: ");
        return scanner.nextLine().trim();
    }
    
    private static void searchByTrainNumber() {
        System.out.print("Enter Train Number: ");
        String trainNumber = scanner.nextLine().trim();
        
        Train train = excelService.findTrainByNumber(TRAIN_DATA_FILE, trainNumber);
        
        if (train != null) {
            System.out.println("\nTrain Details:");
            System.out.println(train);
            
            System.out.print("Do you want to buy a ticket? (yes/no): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            
            if (answer.equals("yes")) {
                bookPassengerTicket(trainNumber);
            }
        } else {
            System.out.println("Train not found!");
        }
    }
    
    private static void searchByDestination() {
        System.out.print("Enter Destination Station: ");
        String destination = scanner.nextLine().trim();
        
        List<Train> trains = excelService.findTrainsByDestination(TRAIN_DATA_FILE, destination);
        
        if (!trains.isEmpty()) {
            System.out.println("\nTrains Found:");
            for (Train train : trains) {
                System.out.println(train);
                System.out.println("------------");
            }
        } else {
            System.out.println("No trains found to this destination.");
        }
    }
    
    private static void bookPassengerTicket(String trainNumber) {
        if (!ticketService.canBookTickets()) {
            System.out.println("Ticket booking disabled! Threshold reached.");
            return;
        }
        
        try {
            System.out.print("Enter number of tickets: ");
            int numTickets = Integer.parseInt(scanner.nextLine().trim());
            
            for (int i = 0; i < numTickets; i++) {
                System.out.println("Enter details for passenger " + (i + 1));
                System.out.print("Name: ");
                String name = scanner.nextLine().trim();
                
                System.out.print("Age: ");
                int age;
                try {
                    age = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid age! Please enter a number.");
                    i--; // retry this passenger
                    continue;
                }
                
                System.out.print("Gender (M/F/O): ");
                String gender = scanner.nextLine().trim();
                
                String seatStatus = ticketService.canBookTickets() ? "Confirmed" : "RSC";
                
                ticketService.savePassengerData(PASSENGER_TICKETS_FILE, trainNumber, name, age, gender, seatStatus);
            }
            System.out.println("Tickets booked successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        }
    }
    
    private static void bookPlatformTicket() {
        System.out.print("Enter Train Number for Platform Ticket: ");
        String trainNumber = scanner.nextLine().trim();
        
        // Verify if the train exists
        Train train = excelService.findTrainByNumber(TRAIN_DATA_FILE, trainNumber);
        if (train == null) {
            System.out.println("Train not found! Cannot book platform ticket.");
            return;
        }
        
        try {
            System.out.print("Enter number of platform tickets: ");
            int numTickets = Integer.parseInt(scanner.nextLine().trim());
            
            if (numTickets <= 0) {
                System.out.println("Please enter a positive number.");
                return;
            }
            
            ticketService.savePlatformTicketData(PLATFORM_TICKETS_FILE, trainNumber, numTickets);
            System.out.println("Platform tickets booked successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        }
    }
    
    private static void generateHourlyReport() {
        System.out.println("\n===== Hourly Report =====");
        int passengerTickets = ticketService.getTotalPassengerTickets();
        int platformTickets = ticketService.getTotalPlatformTickets();
        
        System.out.println("Total Passenger Tickets Sold: " + passengerTickets);
        System.out.println("Total Platform Tickets Sold: " + platformTickets);
        System.out.println("Total Revenue: ₹" + (passengerTickets * 100 + platformTickets * 50));
        System.out.println("=========================");
    }
    
    private static void queryPlatformTickets() {
        System.out.print("Enter Train Number to check platform tickets: ");
        String trainNumber = scanner.nextLine().trim();
        
        int count = ticketService.getPlatformTicketsForTrain(trainNumber);
        System.out.println("Platform Tickets Sold for Train " + trainNumber + ": " + count);
    }
}
