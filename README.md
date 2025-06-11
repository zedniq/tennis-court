# Tennis Club – Reservation System

This project implements a simple reservation system for a tennis club as a Spring Boot server application. Users can manage courts and reservations via a REST API.

## Features

- **CRUD operations on courts**
- **RUD operations on reservations**
- Retrieve reservations by:
  - **Court ID** (sorted by creation date)
  - **Phone ID** (optionally only future reservations)
- Create a reservation for a given court, game type, customer name and phone number, with calculated pricing and time-overlap validation.

## Technologies

- Java 17
- Spring Boot (REST, JPA)
- H2 in-memory database
- JUnit 5 for unit testing

## Testing

The application is covered with unit tests and uses an in-memory database for test execution.
