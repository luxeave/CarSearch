# Car Search Application

A modern Spring Boot application for searching and managing car listings.

## Overview

This application provides a robust REST API for managing and searching car listings. Built with Spring Boot 3.x and Java 23, it offers powerful search capabilities and efficient data management.

## Prerequisites

Before starting, you'll need:
- A Linux-based system with `sudo` privileges
- Internet connection for downloading packages
- GNU Make (`sudo apt-get install make` on Debian/Ubuntu)
- PostgreSQL (see Database Setup section below)

The following dependencies will be automatically installed:
- Java 23 JDK
- Maven

## Database Setup

You'll need to set up PostgreSQL manually:

1. Install PostgreSQL:
   ```bash
   sudo apt-get update
   sudo apt-get install postgresql postgresql-contrib
   ```

2. Start PostgreSQL service:
   ```bash
   sudo service postgresql start
   ```

3. Create database and user:
   ```bash
   sudo -u postgres psql
   ```
   Then in the PostgreSQL prompt:
   ```sql
   CREATE DATABASE carsearch;
   CREATE USER carsearch_user WITH PASSWORD 'your_password';
   GRANT ALL PRIVILEGES ON DATABASE carsearch TO carsearch_user;
   ```

4. Run Flyway migrations:
   ```bash
   mvn flyway:migrate
   ```

5. Restore table using `backup.sql`:
   ```bash
   psql -U <username> -h <host> -p <port> -d <database_name> -f backup.sql
   ```   

## Local Development Setup

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd car-search
   ```

2. Install development dependencies (Java 23 and Maven):
   ```bash
   make install
   ```
   This command will:
   - Download and install Java 23
   - Install Maven

   Alternatively, you can install components separately:
   ```bash
   make install-java    # Install only Java 23
   make install-maven   # Install only Maven
   ```

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

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
