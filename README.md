# Railway Management System

A Java-based railway management system built with Spring Boot that allows for train searches, ticket bookings, and platform ticket management using H2 database for data storage.

## Features

- Search trains by train number
- Search trains by destination station
- Book passenger tickets
- Book platform tickets
- Generate reports
- Query platform tickets by time range or train number
- RESTful API endpoints for all operations

## Requirements

- Java 11 or higher
- Spring Boot 2.7.8
- H2 Database
- Maven for dependency management

## Building the Application

```bash
mvn clean package
```

## Running the Application

### Console Mode (Legacy)
```bash
mvn compile exec:java -Dexec.mainClass="com.railway.RailwayManagementSystem" -Dexec.cleanupDaemonThreads=false
```

### Spring Boot API Server
```bash
mvn spring-boot:run
```

## API Documentation

The Spring Boot app exposes the following RESTful endpoints:

### Train APIs

- `GET /api/trains` - Get all trains
- `GET /api/trains/{trainNumber}` - Get train by train number
- `GET /api/trains/search?trainNumber={number}` - Search train by number
- `GET /api/trains/destination/{destination}` - Get trains by destination
- `GET /api/trains/search/destination?destination={destination}` - Search trains by destination
- `POST /api/trains` - Create a new train
- `PUT /api/trains/{trainNumber}` - Update train by number
- `DELETE /api/trains/{trainNumber}` - Delete train by number

### Passenger APIs

- `GET /api/passengers` - Get all passengers
- `GET /api/passengers/{id}` - Get passenger by ID
- `GET /api/passengers/train/{trainNumber}` - Get passengers by train number
- `POST /api/passengers` - Create a new passenger
- `PUT /api/passengers/{id}` - Update passenger
- `DELETE /api/passengers/{id}` - Delete passenger

### Platform Ticket APIs

- `GET /api/platform-tickets` - Get all platform tickets
- `GET /api/platform-tickets/{id}` - Get platform ticket by ID
- `GET /api/platform-tickets/train/{trainNumber}` - Get platform tickets by train number
- `GET /api/platform-tickets/timerange?startTime={startTime}&endTime={endTime}` - Get platform tickets in time range
- `POST /api/platform-tickets` - Create a new platform ticket
- `PUT /api/platform-tickets/{id}` - Update platform ticket
- `DELETE /api/platform-tickets/{id}` - Delete platform ticket

## Database

The application uses an H2 database stored at `./data/railease`. The H2 console is available at `/h2-console`.

## Postman Collection

A Postman collection file (`RailwayManagementSystem.postman_collection.json`) is included in the repository to help test the API endpoints. You can import this file into Postman to quickly test all available endpoints.
