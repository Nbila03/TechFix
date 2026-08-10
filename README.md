# TechFix – Mobile Repair Service Application

TechFix is an Android-based mobile application developed for **TechFix**, a computer and mobile phone repair service provider operating in Sri Lanka. The application is designed to improve the way customers request and track repairs while helping TechFix manage repair services, branches, technicians, spare parts, and payments.

## Project Overview

The application allows customers to:

* Create an account and log in
* Browse available repair services
* Select device categories and repair services
* Submit repair requests
* Provide repair details and images of damaged devices
* View available TechFix branches
* Track repair progress
* View previous repair history
* Make payments through a test/sandbox payment environment

The system also supports management operations such as managing repair requests, technicians, spare parts, services, branches, and repair statuses.

## 🏗️ System Architecture

The application follows a client-server architecture consisting of an Android application, API Gateway, Spring Boot backend, central MySQL database, and a local Room/SQLite database.

```text
                 ┌─────────────────────┐
                 │     Android App     │
                 │   Java / Android    │
                 └──────────┬──────────┘
                            │
                ┌───────────┴───────────┐
                │                       │
                ▼                       ▼
       ┌────────────────┐      ┌─────────────────┐
       │     SQLite     │      │   API Gateway   │
       │ Local Database │      └────────┬────────┘
       └────────────────┘               │
                                        ▼
                              ┌─────────────────┐
                              │  Spring Boot    │
                              │    Backend      │
                              └────────┬────────┘
                                       │
                                       ▼
                              ┌─────────────────┐
                              │  MySQL Central  │
                              │    Database     │
                              └─────────────────┘
```

### Central Database

MySQL is used as the central database for permanent TechFix business data.

The main entities include:

* Users
* Branches
* Device Categories
* Repair Services
* Technicians
* Spare Parts
* Repair Requests
* Repair Status History
* Payments

### Local Database

SQLite is used within the Android application for local data storage, caching, and supporting offline functionality.

## 🛠️ Technologies

| Component            | Technology                              |
| -------------------- | --------------------------------------- |
| Mobile Application   | Android Studio                          |
| Programming Language | Java                                    |
| Local Database       | SQLite                                  |
| Backend              | Spring Boot                             |
| Backend Language     | Java                                    |
| Central Database     | MySQL                                   |
| API                  | REST API                                |
| API Communication    | Retrofit                                |
| API Gateway          | API Gateway                             |
| Maps & Location      | Google Maps                             |
| Image Integration    | Android Camera                          |
| Payment              | Test Payment Gateway                    |
| Version Control      | Git & GitHub                            |

## 🔑 Main Features

### Customer Features

* Registration and login
* Service browsing
* Device selection
* Repair appointment/request submission
* Repair image upload
* Branch and location information
* Repair status tracking
* Repair history
* Payment

### Management Features

* Repair request management
* Branch management
* Technician management
* Spare-part management
* Repair service management
* Repair status updates
* Payment management

## 📍 Branch Assignment

The system is designed to assign a repair request to a suitable TechFix branch based on:

1. Customer location
2. Distance to the branch
3. Technician availability
4. Required spare-part availability

The nearest branch that satisfies the required conditions can be selected for the repair request.

## 💾 Database Structure

The central MySQL database contains the main business entities, while the Android application's Room/SQLite database is used for local/offline data.

```text
Users
   │
   └── Repair Requests
          │
          ├── Repair Service
          ├── Device Category
          ├── Branch
          ├── Technician
          ├── Repair Status History
          └── Payment

Branch
   ├── Technicians
   └── Spare Parts

Device Category
   └── Repair Services
```

## 📂 Repository Structure

```text
TechFix/
│
├── android/
│   └── Android application source code
│
├── backend/
│   └── Spring Boot backend
│
├── database/
│   ├── schema.sql
│   └── sample_data.sql
│
├── docs/
│   ├── diagrams/
│   └── documentation
│
└── README.md
```

## 🚀 Setup

### 1. Clone the repository

```bash
git clone <repository-url>
```

### 2. Set up the database

Create the MySQL database and execute:

```text
database/schema.sql
database/sample_data.sql
```

### 3. Configure the backend

Update the Spring Boot database configuration with the local MySQL credentials.

### 4. Run the backend

Open the backend project in an IDE and run the Spring Boot application.

### 5. Run the Android application

Open the Android project in Android Studio, configure the required API endpoint and run the application using an emulator or Android device.

## 👥 Development

This project is developed as a group coursework project for the **Mobile Application Development** module of the Higher National Diploma in Software Engineering.

Each group member is responsible for specific application interfaces and corresponding backend, database, and functionality components.

## 📚 Academic Context

**Module:** Mobile Application Development
**Course:** Higher National Diploma in Software Engineering
**Institution:** National Institute of Business Management – School of Computing and Engineering
**Batch:** 25.2 Full Time

## 📄 License

This project is developed for academic purposes in completion of the mbile application development coursework.
