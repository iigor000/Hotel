# Hotel Management System

A comprehensive hotel management application built with **Java** and **Swing UI** framework. This system provides complete functionality for managing hotel operations including reservations, room management, employee administration, pricing, and guest services.

---

## 📋 Overview

The Hotel Management System is a desktop application designed to streamline hotel operations and provide role-based access for different types of users. It manages reservations, guest information, room availability, staff assignments, pricing, and service requests with persistent data storage.

---

## 🔧 Technologies & Architecture

- **Language:** Java
- **UI Framework:** Swing (Java GUI)
- **Persistence:** CSV-based data storage
- **Design Patterns:** 
  - Singleton Pattern (ManagerFactory)
  - MVC (Model-View-Controller)
  - Factory Pattern

---

## 👥 User Roles & Features

### 1. **Administrator**
- Manage employees (Receptionists and Cleaners)
- Manage guests
- Create and edit price lists for room types and services
- Create and manage reservations
- View and manage all reservation requests
- Generate comprehensive hotel reports
- Manage available rooms

### 2. **Receptionist**
- Handle guest check-in and check-out
- Create new reservations
- View and process guest service requests
- Edit guest profile information
- Change password

### 3. **Cleaner**
- View assigned rooms
- Mark rooms as cleaned
- Track room cleaning status
- Change password

### 4. **Guest**
- Make new reservations
- View my reservations and their status
- Submit service requests
- Edit profile information
- Change password

---

## 📦 Project Structure

```
Hotel/
├── src/
│   ├── Main.java                         # Application entry point
│   ├── manage/                           # Business logic & data management
│   │   ├── ManagerFactory.java           # Singleton factory for all managers
│   │   ├── CleanersManager.java         # Cleaner operations
│   │   ├── PriceListManager.java        # Pricing management
│   │   ├── ReservationManager.java      # Reservation operations
│   │   ├── ReservationRequestManager.java # Service request management
│   │   ├── RoomManager.java             # Room management
│   │   └── UserManager.java             # User authentication & management
│   ├── model/                            # Data models & entities
│   │   ├── User.java, Guest.java        # User types
│   │   ├── Room.java                    # Room entity
│   │   ├── Reservation.java             # Reservation entity
│   │   ├── ReservationRequest.java      # Service requests
│   │   ├── PriceList.java               # Pricing information
│   │   ├── RoomType.java, RoomStatus.java, RoomQuality.java  # Room enums
│   │   └── EmployeeType.java            # Employee role enumeration
│   ├── view/                             # UI components
│   │   ├── LoginView.java               # Authentication screen
│   │   ├── admin/                       # Administrator interfaces
│   │   ├── receptionist/                # Receptionist interfaces
│   │   ├── cleaner/                     # Cleaner interfaces
│   │   ├── guest/                       # Guest interfaces
│   │   └── reports/                     # Reporting interfaces
│   ├── tableModels/                      # JTable models for data display
│   ├── util/                             # Utility classes
│   │   └── AppSettings.java             # Configuration & file paths
│   └── test/                             # Unit tests for managers
├── database/                             # CSV data files
│   ├── Rooms.csv
│   ├── Reservations.csv
│   ├── Prices.csv
│   ├── Users.csv
│   ├── Cleaners.csv
│   └── Requests.csv
└── libs/                                 # External libraries
```

---

## 🎯 Key Features

### Room Management
- Create and maintain room inventory with multiple room types and qualities
- Track room availability and occupancy
- Assign cleaners to specific rooms
- Monitor room status (available, occupied, cleaning)

### Reservation System
- Create new guest reservations with automatic price calculation
- Track reservation status through lifecycle (confirmed, checked in, checked out, cancelled)
- Support multiple room types with different pricing
- Add optional services to reservations
- Display check-in and check-out dates

### Guest Services
- Process guest service requests (cleaning, maintenance, etc.)
- Track request status through fulfillment
- Manage service completion and documentation

### Pricing Management
- Create and manage price lists for different room types
- Add additional service pricing
- Automatic calculation of reservation costs based on duration and services

### Employee Management
- Create administrator, receptionist, and cleaner accounts
- Assign cleaners to specific rooms
- Track personnel assignments and responsibilities

### Reporting
- Generate comprehensive hotel reports
- View room occupancy statistics
- Access reservation analytics
- Track cleaner assignments and performance

---

## 🚀 Getting Started

### Prerequisites
- Java 8 or higher
- No additional dependencies required (Swing is built-in)

### Running the Application

1. **Navigate to project directory:**
   ```bash
   cd Hotel
   ```

2. **Compile the Java files:**
   ```bash
   javac -d bin src/**/*.java
   ```

3. **Run the application:**
   ```bash
   java -cp bin:libs/* Main
   ```

4. **Login with credentials:**
   - The application loads user data from `database/Users.csv`
   - Default credentials are available in the CSV file

---

## 💾 Data Persistence

The application uses CSV files for persistent data storage:

| File | Purpose |
|------|---------|
| `Rooms.csv` | Room inventory and status |
| `Reservations.csv` | All reservations and bookings |
| `Prices.csv` | Room types and service pricing |
| `Users.csv` | User accounts and credentials |
| `Cleaners.csv` | Cleaner assignments |
| `Requests.csv` | Service and maintenance requests |

Data is automatically loaded on startup and saved on application shutdown via a shutdown hook.

---

## 🧪 Testing

Unit tests are provided for all manager classes:

```bash
# Compile and run tests
javac -d bin src/**/*.java
java -cp bin org.junit.runner.JUnitCore test.ManagerTest
```

Tests cover:
- CleanersManager
- PriceListManager
- ReservationManager
- ReservationRequestManager
- RoomManager
- UserManager

---

## 🏗️ Architecture Highlights

### Singleton Pattern
`ManagerFactory` uses the singleton pattern to ensure a single instance of all managers throughout the application lifecycle, providing centralized data management.

### CSV Persistence
All data is stored in CSV format, making it easy to view, edit, and backup data without requiring a database server.

### Role-Based UI
Different user interfaces are displayed based on user role (Administrator, Receptionist, Cleaner, Guest), ensuring each user only sees relevant information.

### Price Calculation
Reservation prices are automatically calculated based on room type, number of nights, and selected services.

---

## 📝 Notes

- The application supports multiple languages (UI has Serbian labels)
- All user passwords are stored (consider adding encryption for production use)
- CSV files must maintain proper formatting for correct data loading
- The application prevents double-booking of rooms through reservation status validation

---

## 📄 License

This project is created for educational purposes.
