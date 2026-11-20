# Json-dataset-restapi
REST API for JSON dataset management using Java Spring Boot and MySQL
# JSON Dataset Management API

A RESTful backend application developed using **Java, Spring Boot, and MySQL** to manage JSON datasets. The application provides APIs to create, retrieve, and manage datasets through HTTP requests.

## 🚀 Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Swagger / OpenAPI
- JUnit

## ✨ Features

- RESTful API development
- JSON dataset management
- Store and retrieve dataset information
- MySQL database integration
- Exception handling and validation
- API documentation using Swagger UI
- Test APIs using Postman or Swagger

## 📁 Project Structure

```text
freightfox-json-dataset-api/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ...
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
└── README.md
```

## ⚙️ Prerequisites

Make sure you have installed:

- Java JDK 21 or compatible project version
- Maven
- MySQL Server
- Git

## 🛠️ Setup and Installation

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/freightfox-json-dataset-api.git
```

### 2. Navigate to the project directory

```bash
cd freightfox-json-dataset-api
```

### 3. Configure MySQL

Create a MySQL database:

```sql
CREATE DATABASE freightfox_db;
```

Configure your database credentials using environment variables:

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/freightfox_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}
```

### 4. Run the application

Using Maven:

```bash
mvn spring-boot:run
```

Or on Windows:

```bash
mvnw.cmd spring-boot:run
```

The application runs on:

```text
http://localhost:8082
```

## 📖 API Documentation

Swagger UI:

```text
http://localhost:8082/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8082/v3/api-docs
```

## 🧪 API Testing

The REST APIs can be tested using:

- Swagger UI
- Postman
- cURL

## 👨‍💻 Developer

**Adityavardhan Kakade**

GitHub: https://github.com/Avk07-coder

---

Developed as part of the FreightFox Backend Developer Assignment.
