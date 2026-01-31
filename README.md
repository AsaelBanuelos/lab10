# Spring Boot / MVC / HTTP / Security & Testing Labs

## Description
This project is a **session-based Spring Boot MVC application**.

The main goal of the project is to understand how an **HTTP server** works by
implementing endpoints and observing the full **request → response** flow,
including headers, request bodies, validation, security, and HTTP status codes.

The project was developed incrementally following **Labs 10 to 14**.

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
- JUnit 5 & Mockito (testing)

---

## Requirements
- Java 17+
- Maven

---

## Implemented Features

### 🔹 Lab 10 :  HTTP Fundamentals
- Correct use of HTTP methods (`GET`, `POST`)
- MVC architecture (Controller → Service → Repository)
- DTO-based validation using `@Valid`
- Validation error handling in Thymeleaf views
- File upload functionality
- Proper use of HTTP status codes
- JSON endpoint consuming `application/json`

---

### 🔹 Lab 11–12 :  Authentication & Authorization

#### Authentication
- User registration and login
- Secure logout using `POST`
- Passwords hashed using **BCrypt (strength 12)**

#### Authorization
- Role-based access control (`ROLE_USER`, `ROLE_ADMIN`)
- Ownership-based authorization for notes
- Access to other users’ data results in **404 Not Found**

---

### 🔹 Security Hardening
- CSRF protection enabled
- CSRF tokens required for all POST requests
- Unsafe operations are never exposed via GET
- Session-based authentication
- Session timeout configured

---

### 🔹 Database & Persistence
- Schema managed with **Flyway migrations**
- SQLite database
- Native SQL queries with parameter binding

---

### 🔹 Lab 13 : Advanced Session Security & Application Hardening

This project follows the **session-based security track** (MVC application).

Implemented security hardening measures include:

- Session-based authentication using Spring Security
- Sessions are invalidated on logout
- Automatic session expiration configured via `server.servlet.session.timeout`
- CSRF protection enabled for all state-changing requests
- CSRF tokens required for all POST operations
- Secure session cookies (`HttpOnly`, `SameSite`)
- Security headers enabled by Spring Security:
    - `X-Content-Type-Options`
    - `X-Frame-Options`
- Sensitive data is never logged (no passwords, hashes, or tokens in logs)
- Basic rate limiting filter applied to authentication endpoints

JWT and refresh token mechanisms were **not implemented**, as they apply to the REST-based security track and are not required for this MVC session-based application.

---

### 🔹 Lab 14 : Testing
- Unit test for service logic (password hashing and user registration)
- Integration tests for:
    - CSRF protection
    - Authentication-required routes
    - Role-based access control
- All tests pass using `mvn test`

---

## Default Admin User
Created automatically via Flyway migration:

- **Email:** admin@local.test
- **Password:** Admin123!
- **Role:** ROLE_ADMIN

---

## Main Endpoints

### Public
- `/login`
- `/register`

### Authenticated
- `/notes`
- `/notes/create`
- `/notes/{id}/edit`
- `/notes/{id}/delete` (POST)

### Role Restricted
- `/user`
- `/admin`

---

## Setup & Run

1. Create a `.env` file:
   DB_URL=jdbc:sqlite:database.db


2. Run the application:
   ./mvnw spring-boot:run


3. Open in browser:
   http://localhost:8080/login


---

## Testing

Run all tests with:
./mvnw test


Expected result:
BUILD SUCCESS


---

## Author
**Asael Banuelos Ortiz**  
Spring Boot — HTTP, Security & Testing Labs