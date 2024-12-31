# Car Search Application

A modern Spring Boot application for searching and managing car listings.

## Overview

This application provides a robust REST API for managing and searching car listings. Built with Spring Boot 3.x and Java 23, it offers powerful search capabilities and efficient data management.

## Prerequisites

- Java 23 JDK
- Maven 3.8+
- PostgreSQL

## Local Development Setup

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd car-search
   ```

2. Setup PostgreSQL and initialize the database:
   ```bash
   make install
   ```
   This command will:
   - Install PostgreSQL if not already installed
   - Create the database and user
   - Setup necessary permissions

3. Populate the database with sample data (optional):
   ```bash
   make populate-db
   ```

4. Build the project:
   ```bash
   make build
   ```

5. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## Key Features

- RESTful API endpoints for car management
- PostgreSQL database for data persistence
- Validation using Spring Boot Validation
- Monitoring with Spring Boot Actuator
- Modern Java features with preview enabled

## API Documentation

Once the application is running, you can access:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/v3/api-docs`

## Building for Production

1. Build the production JAR:
   ```bash
   mvn clean package
   ```

2. The JAR file will be created in the `target` directory

## Database Configuration

The application uses PostgreSQL with the following default configuration:
- Database Name: car_search
- Username: postgres
- Password: postgres
- Host: localhost
- Port: 5432

You can modify these settings in the Makefile if needed.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
