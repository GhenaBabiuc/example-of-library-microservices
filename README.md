# Library Management System

A modern microservices-based library management system built with Spring Boot, featuring separate services for user management, book catalog, and borrowing operations.

## Architecture

The system consists of four main components:

- **API Gateway** (Port 8080) - Routes requests and provides a unified entry point
- **Books Service** (Port 8081) - Manages books and authors
- **Borrowing Service** (Port 8082) - Handles book borrowing operations
- **Users Service** (Port 8083) - Manages library users

## Technology Stack

- **Backend**: Java 21, Spring Boot 3.5.5
- **Database**: PostgreSQL 15
- **ORM**: Spring Data JPA with Hibernate
- **Database Migration**: Liquibase
- **Mapping**: MapStruct
- **Validation**: Jakarta Bean Validation
- **API Gateway**: Spring Cloud Gateway
- **Containerization**: Docker & Docker Compose

## Prerequisites

- Java 21+
- Maven 3.8+
- Docker & Docker Compose
- Git

## Quick Start

### 1. Clone the Repository

```bash
  git clone <repository-url>
  cd example-of-library-microservices
```

### 2. Start Database Services

```bash
  docker-compose up -d
```

This will start three PostgreSQL instances:
- **users-db**: localhost:5431
- **books-db**: localhost:5432
- **borrowing-db**: localhost:5433

### 3. Build and Run Services

Build all services:
```bash
    # Build Users Service
    cd UsersService
    mvn clean install
    mvn spring-boot:run
    
    # Build Books Service  
    cd ../BooksService
    mvn clean install
    mvn spring-boot:run
    
    # Build Borrowing Service
    cd ../BorrowingService
    mvn clean install
    mvn spring-boot:run
    
    # Build and Run API Gateway
    cd ../ApiGateway
    mvn clean install
    mvn spring-boot:run
```

### 4. Verify Installation

Check if all services are running:
- API Gateway: http://localhost:8080
- Books Service: http://localhost:8081
- Borrowing Service: http://localhost:8082
- Users Service: http://localhost:8083
