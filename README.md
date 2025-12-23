## Description:
This project is a simple **Spring Boot MVC application** developed for **Lab 10 Spring boot http server– HTTP Fundamentals**.

The goal of this lab is to understand how an HTTP server works by implementing
basic endpoints and observing the full **request ==> response** flow, including
headers, request bodies, validation, and status codes.

The application is intentionally simple and serves as a foundation for future labs.


## Technologies Used
- Java 17
- Spring Boot
- Spring MVC + Thymeleaf
- Spring Security
- Spring Data JPA (Hibernate)
- Flyway (database migrations)
- SQLite (local database)
- Bean Validation (Jakarta Validation)

## Requirements:
- Java 17+
- Maven

## Implemented Requirements

### 🔹 Lab 10 — HTTP Fundamentals
- Correct use of HTTP methods:
    - `GET` for pages and data retrieval
    - `POST` for actions that modify state (create, update, delete, login, logout)
- MVC architecture with Controllers, Services, Repositories
- DTO-based validation using `@Valid`
- Validation error handling displayed in Thymeleaf views
- File upload functionality
- Proper request/response handling

---

### 🔹 Lab 11–12 — Authentication & Authorization

#### Authentication
- User registration and login
- Secure logout via `POST`
- Passwords hashed using **BCrypt (strength 12)**
- Custom password policy:
    - Minimum length
    - Uppercase, lowercase, digit, and special character required
    - Common password blacklist

#### Authorization by Role
- Roles implemented:
    - `ROLE_USER`
    - `ROLE_ADMIN`
- Access rules:
    - `/admin` → only `ROLE_ADMIN`
    - `/user` → `ROLE_USER` and `ROLE_ADMIN`
- Unauthorized access results in **access denied behavior**

#### Authorization by Ownership (Core Requirement)
- Notes are linked to users via `user_id`
- Users can only:
    - View their own notes
    - Edit their own notes
    - Delete their own notes
- Accessing another user’s note returns **404 Not Found**
- Ownership checks are enforced in the **service layer**

---

### 🔹 Security Hardening
- CSRF protection enabled (default Spring Security behavior)
- All POST forms include CSRF token
- Requests without CSRF token return **403 Forbidden**
- Logout and delete actions only allowed via POST
- No sensitive operations exposed via GET

---

### 🔹 Database & Persistence
- Database schema managed with **Flyway migrations**
- Migrations ensure consistent schema across environments
- Flyway history tracked via `flyway_schema_history`
- At least one repository method uses **native SQL with parameter binding**

---

## Default Admin User
An admin account is automatically created via Flyway migration.

- **Email:** `admin@local.test`
- **Password:** `Admin123!`
- **Role:** `ROLE_ADMIN`


## Main Endpoints

Public:
- `/login`
- `/register`

Authenticated:
- `/notes` — list own notes
- `/notes/create`
- `/notes/{id}/edit`
- `/notes/{id}/delete` (POST)

Role restricted:
- `/user` — user area
- `/admin` — admin-only area


## Setup:
1. Create a `.env` file in the project root:
   DB_URL=jdbc:sqlite:database.db

2. Run the application:
   ./mvnw spring-boot:run  

or

Run the application using the **Run button in the IDE**
by starting the `Lab10Application` main class.


Then open:

http://localhost:8080/login

### Conclusion

- This project is implementing:
- HTTP fundamentals
- Secure authentication
- Role-based authorization
- Ownership-based access control
- CSRF protection
- Database migrations with Flyway
- Password security best practices

## Author
Asael Banuelos Ortiz – Lab 10 (Spring Boot HTTP Server + HTTP Fundamentals)

