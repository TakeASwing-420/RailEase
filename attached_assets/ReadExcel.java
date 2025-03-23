import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.*;

public class ReadExcel {
private static final String TRAIN_DATA_FILE = "/home/sourasish/Desktop/data.xlsx";

    private static final String PASSENGER_TICKETS_FILE = "/home/sourasish/Desktop/passenger_tickets.xlsx";
    private static final String PLATFORM_TICKETS_FILE = "/home/sourasish/Desktop/platform_tickets.xlsx";
    private static final int TICKET_THRESHOLD = 500;
    private static int totalTicketsSold = 0;
    private static int serialNumberPassenger = 1;
    private static int serialNumberPlatform = 1;
    private static final Map<String, Integer> platformTickets = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n1. Search by Train Number\n2. Search by Destination Station\n3. Book Platform Ticket\n4. Hourly Report\n5. Query Platform Tickets\n6. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    System.out.print("Enter Train Number: ");
                    searchByTrainNumber(scanner.nextLine().trim(), scanner);
                    break;
                case "2":
                    System.out.print("Enter Destination Station: ");
                    searchByDestination(scanner.nextLine().trim(), scanner);
                    break;
                case "3":
                    System.out.print("Enter Train Number for Platform Ticket: ");
                    bookPlatformTicket(scanner.nextLine().trim(), scanner);
                    break;
                case "4":
                    generateHourlyReport();
                    break;
                case "5":
                    System.out.print("Enter Train Number to check platform tickets: ");
                    queryPlatformTickets(scanner.nextLine().trim());
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
    
    private static void searchByTrainNumber(String trainNumber, Scanner scanner) {
        try (FileInputStream fis = new FileInputStream(TRAIN_DATA_FILE);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean found = false;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                String trainNum = getCellValue(row.getCell(1));
                if (trainNum.equals(trainNumber)) {
                    found = true;
                    System.out.println("\nTrain Details:");
                    System.out.println("Train Number: " + trainNum);
                    System.out.println("From: " + getCellValue(row.getCell(2)));
                    System.out.println("To: " + getCellValue(row.getCell(3)));
                    System.out.println("Departure Time: " + getCellValue(row.getCell(6)));
                    
                    System.out.print("Do you want to buy a ticket? (yes/no): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                        if (totalTicketsSold < TICKET_THRESHOLD) {
                            processTicketPurchase(trainNumber, scanner);
                        } else {
                            System.out.println("Ticket booking disabled! Threshold reached.");
                        }
                    }
                    break;
                }
            }
            if (!found) System.out.println("Train not found!");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
     private static void searchByDestination(String destination, Scanner scanner) {
        try (FileInputStream fis = new FileInputStream(TRAIN_DATA_FILE);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean found = false;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                String dest = getCellValue(row.getCell(3));
                if (dest.equalsIgnoreCase(destination)) {
                    found = true;
                    System.out.println("\nTrain Found:");
                    System.out.println("Train Number: " + getCellValue(row.getCell(1)));
                    System.out.println("From: " + getCellValue(row.getCell(2)));
                    System.out.println("To: " + dest);
                    System.out.println("Departure Time: " + getCellValue(row.getCell(6)));
                }
            }
            if (!found) System.out.println("No trains found to this destination.");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    
    private static void processTicketPurchase(String trainNumber, Scanner scanner) {
        System.out.print("Enter number of tickets: ");
        int numTickets = scanner.nextInt();
        scanner.nextLine();
        
        for (int i = 0; i < numTickets; i++) {
            System.out.println("Enter details for passenger " + (i + 1));
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Age: ");
            int age = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Gender: ");
            String gender = scanner.nextLine().trim();
            String seatStatus = (totalTicketsSold < TICKET_THRESHOLD) ? "Confirmed" : "RSC";
            
            savePassengerData(trainNumber, name, age, gender, seatStatus);
            totalTicketsSold++;
        }
        System.out.println("Tickets booked successfully!");
    }
    
private static void generateHourlyReport() {
    System.out.println("Hourly Report:");
    System.out.println("Total Passenger Tickets Sold: " + totalTicketsSold);
    System.out.println("Total Platform Tickets Sold: " + platformTickets.values().stream().mapToInt(Integer::intValue).sum());
}


private static void queryPlatformTickets(String trainNumber) {
    int count = platformTickets.getOrDefault(trainNumber, 0);
    System.out.println("Platform Tickets Sold for Train " + trainNumber + ": " + count);
}
    
    private static void savePassengerData(String trainNumber, String name, int age, String gender, String seatStatus) {
        try {
            File file = new File(PASSENGER_TICKETS_FILE);
            Workbook workbook;
            Sheet sheet;

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
            }

            sheet = workbook.getSheet("Passengers");
            if (sheet == null) {
                sheet = workbook.createSheet("Passengers");
            }

            int rowNum = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(serialNumberPassenger++);
            row.createCell(1).setCellValue(trainNumber);
            row.createCell(2).setCellValue(name);
            row.createCell(3).setCellValue(age);
            row.createCell(4).setCellValue(gender);
            row.createCell(5).setCellValue(seatStatus);

            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            workbook.close();
        } catch (Exception e) {
            System.out.println("Error writing to passenger file: " + e.getMessage());
        }
    }
    
    private static void bookPlatformTicket(String trainNumber, Scanner scanner) {
        System.out.print("Enter number of platform tickets: ");
        int numTickets = scanner.nextInt();
        scanner.nextLine();
        
        platformTickets.put(trainNumber, platformTickets.getOrDefault(trainNumber, 0) + numTickets);
        savePlatformTicketData(trainNumber, platformTickets.get(trainNumber));
        System.out.println("Platform tickets booked successfully!");
    }
    
    private static void savePlatformTicketData(String trainNumber, int totalTickets) {
        try {
            File file = new File(PLATFORM_TICKETS_FILE);
            Workbook workbook;
            Sheet sheet;

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
            }

            sheet = workbook.getSheet("Platform Tickets");
            if (sheet == null) {
                sheet = workbook.createSheet("Platform Tickets");
            }

            int rowNum = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(serialNumberPlatform++);
            row.createCell(1).setCellValue(trainNumber);
            row.createCell(2).setCellValue(totalTickets);

            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            workbook.close();
        } catch (Exception e) {
            System.out.println("Error writing to platform ticket file: " + e.getMessage());
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf((int) cell.getNumericCellValue());
            default: return "";
        }
    }
}

