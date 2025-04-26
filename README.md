# Todo Application

This is a Java Spring Boot application for managing tasks. The project uses Maven as the build tool and Docker Compose
for container orchestration.

## Technologies Used

- **Java 21** and Spring Boot
- **Maven** as the build tool
- **Docker Compose** for managing services:
    - MySQL database
    - phpMyAdmin for database management
    - OpenJDK 21 container for running the application
    - Caddy as a reverse proxy

## Environment Configuration

Customize the `.env` file according to your needs. Refer to the `.env.example` file for guidance.

## Running the Application

1. Build the project using: `mvn clean package`
2. Run Docker Compose using: `docker-compose up`

For debugging the application, use IntelliJ IDEA 2025.1.

## Designed By

- **Name**: [Radek Hloušek]
- **Email**: [radek@gradepoint.cz]
- **GitHub**: [github.com/RadekSassy]
- **project URL**: [https://todo.sassy.cz]