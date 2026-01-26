## Description
This project is a **Spring Boot MVC application Session based Spring boot app** 

The main goal is to understand how an HTTP server works by implementing
basic endpoints and observing the full **request → response** flow,
including headers, request bodies, validation, and HTTP status codes.

---

## Technologies Used
- Java 17
- Spring Boot
- Spring MVC + Thymeleaf
- Spring Security
- Spring Data JPA (Hibernate)
- Flyway (database migrations)
- SQLite
- Jakarta Bean Validation

---

## Requirements
- Java 17+
- Maven

---

## Implemented Features

### 🔹 Lab 10 — HTTP Fundamentals
- Correct use of HTTP methods (`GET`, `POST`)
- MVC architecture (Controller → Service → Repository)
- DTO-based validation using `@Valid`
- Validation error handling in Thymeleaf views
- File upload functionality
- Proper request/response handling with status codes

---

### 🔹 Lab 11–12 — Authentication & Authorization

#### Authentication
- User registration and login
- Secure logout via `POST`
- Passwords hashed using **BCrypt (strength 12)**
- Custom password policy with complexity rules

#### Authorization
- Role-based access control (`ROLE_USER`, `ROLE_ADMIN`)
- Ownership-based authorization for notes
- Access to other users’ data results in **404 Not Found**

---

### 🔹 Security Hardening
- CSRF protection enabled
- CSRF tokens required for all POST requests
- Unsafe operations are never exposed via GET

---

### 🔹 Database & Persistence
- Schema managed with **Flyway migrations**
- Consistent schema across environments
- Native SQL query with parameter binding

---

## Default Admin User
Created automatically via Flyway migration:

- **Email:** admin@local.test
- **Password:** Admin123!
- **Role:** ROLE_ADMIN

---

## Main Endpoints

Public:
- `/login`
- `/register`

Authenticated:
- `/notes`
- `/notes/create`
- `/notes/{id}/edit`
- `/notes/{id}/delete` (POST)

Role restricted:
- `/user`
- `/admin`

---

## Setup

1. Create a `.env` file:
DB_URL=jdbc:sqlite:database.db

2. Run the application:
   ./mvnw spring-boot:run

3. Then open:
   http://localhost:8080/login


---

## Author
Asael Banuelos Ortiz  
Spring Boot – HTTP & Security Labs