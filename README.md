## Description:
This project is a simple **Spring Boot MVC application** developed for **Lab 10 Spring boot http server– HTTP Fundamentals**.

The goal of this lab is to understand how an HTTP server works by implementing
basic endpoints and observing the full **request ==> response** flow, including
headers, request bodies, validation, and status codes.

The application is intentionally simple and serves as a foundation for future labs.

## Technologies Used:
- Java 17
- Spring Boot
- Spring MVC
- Thymeleaf
- Spring Validation
- Spring Security (basic configuration)
- Flyway
- SQLite
- Maven

## Requirements:
- Java 17+
- Maven

## Setup:
1. Create a `.env` file in the project root:
   DB_URL=jdbc:sqlite:database.db

2. Run the application:
   ./mvnw spring-boot:run  

or

Run the application using the **Run button in the IDE**
by starting the `Lab10Application` main class.


## Application Features:

### Basic HTTP GET
- **GET** `/hello`  
  Returns a simple text response (`OK`) to demonstrate a basic HTTP GET request
  and response flow.

---

### MVC Notes Application
- **GET** `/notes`  
  Displays a list of notes.
- **GET** `/notes/create`  
  Displays a form to create a new note.
- **POST** `/notes/create`  
  Handles form submission using `application/x-www-form-urlencoded`.

The form follows the **Post-Redirect-Get (PRG)** pattern to prevent duplicate
submissions.

---

### REST Endpoint (JSON)
- **POST** `/notes/api`  
  Accepts JSON data (`application/json`) to create a note using `@RequestBody`.

This endpoint is used to demonstrate HTTP request bodies and status codes.

---

### File Upload (Multipart)
- **POST** `/notes/upload`  
  Accepts file uploads using `multipart/form-data`.

Uploaded files are stored locally in:
C:\Users<username>\lab10_uploads\

yaml
Copy code

---

## HTTP Headers
The application adds a basic security header to all responses:

- `Content-Security-Policy: default-src 'self'`

This is implemented using a Spring MVC interceptor.

---

## Validation
The project demonstrates:
- Standard validation annotations (`@NotBlank`, `@Size`)
- A custom validator (`@ValidTitle`) that enforces a minimum title length

Validation errors in MVC forms are displayed directly on the page using Thymeleaf.

---

## Error Handling
A global exception handler is implemented using `@ControllerAdvice` to handle:
- Validation errors (400)
- Unsupported media types (415)
- Generic server errors (500)

---

## HTTP Status Codes Demonstration

| Scenario | HTTP Status |
|--------|-------------|
| Successful form submission (MVC) | 302 (Redirect – PRG pattern) |
| Invalid form input (MVC) | 200 (Form re-rendered with errors) |
| Invalid JSON request | 400 |
| Unsupported Content-Type | 415 |
| Unexpected server error | 500 |

---


## How to Test:
1. Start the application from the IDE.
2. Open a browser and visit:
    - `http://localhost:8080/hello`
    - `http://localhost:8080/notes`
3. Use the browser DevTools (Network tab) to inspect:
    - HTTP requests and responses
    - Status codes
    - Headers (including CSP)
4. Use the notes form and file upload to test validation and multipart requests.

---

## Notes
- The project focuses on **HTTP fundamentals**, not production-ready security.
- The structure is designed to be extended in future labs.

---

## Author
Asael Banuelos Ortiz – Lab 10 (Spring Boot HTTP Server + HTTP Fundamentals)

