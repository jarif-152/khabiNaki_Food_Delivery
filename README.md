# Student Info
- Name: Mohammad Faiaj Jarif
- Student ID: 230042152
- Course: SWE-4302
- Semester: 3rd

---
 
# Food Delivery App

Lab Final Project : SWE-4302
Project Type: Single‑person project  
Primary Language: Java (Frontend: HTML)

---

## Project Overview

Dorja Khul Food Delivery is a Java-based food delivery system that supports both **Customer** and **Restaurant Admin** workflows. It uses SOAP (WSDL) web services for all core functionality and provides simple HTML UIs for interaction.

---

## Implemented Features

### Customer Features
- View customers list  
- Search restaurants by area  
- View menu by restaurant  
- Place orders (with optional coupon)  
- Pay for order (cash or card)  
- Assign rider  
- Update delivery progress  
- Track order status  

### Restaurant Admin Features
- Register restaurant  
- Add menu items  
- Update menu item quantity / addons / availability  
- Set opening/closing schedule  
- Assign rider to order  
- View restaurant payments  

---

## Architecture & Design

- One class per functionality  
- Modular package structure  
- Java OOP focused  
- SOAP services exposed via WSDL  
- HTML frontend for UI interaction  

---

## SOAP Services (WSDL)

Endpoints used by the frontend:
- `http://localhost:8080/RestaurantService`
- `http://localhost:8080/OrderService`
- `http://localhost:8080/AdminService`

---

## Frontend Pages

- `frontend/index.html` → Customer UI  
- `frontend/admin.html` → Restaurant Admin UI  

---

## Database Setup (Required Before Running)

Before running the project, you must:
1. Create the database  
2. Create required tables  
3. Configure Java DB connection  

> SQL scripts should be placed in `/database`

---

## How to Run

1. **Create the database and tables** (see `/database`)
2. Configure DB credentials in Java source
3. Build the project:
   ```
   mvn clean install
   ```
4. Run the Java services
5. Open:
   - `frontend/index.html` (customer)
   - `frontend/admin.html` (admin)

---

## Tech Stack

- Java
- HTML
- SOAP (WSDL Web Services)
- Maven

---

## Notes

This project was built under the SWE‑4302 Lab Final Project constraints with emphasis on clean OOP design and modular structure.
