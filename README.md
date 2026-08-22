
# TechFix Android Application

TechFix is a mobile application designed to support computer and mobile phone repair services, customer repair requests, branch operations, technicians, spare parts, payments, and repair tracking.

The application allows customers to:

- Create and manage an account
- Browse available repair services
- Manage their devices
- Submit repair appointments
- Upload images of device problems
- Automatically match suitable nearby TechFix branch
- Track repair progress
- View repair history
- Make payments through a payment gateway

Management users can:

- Manage repair requests
- Manage branches
- Manage technicians
- Manage spare parts
- Manage repair services and prices
- Monitor repair statuses
- Manage payments

---

# Technology Stack

## Android Application

- Java
- Android Studio
- XML layouts
- RecyclerView
- Android Location Services
- Camera integration
- SQLite
- Content Provider
- Firebase SDK

## Remote Data / Backend

The application uses **Firebase** for remote data and cloud services.

Firebase is used instead of maintaining a separate PHP server.

Main Firebase components:

- Firebase Authentication
- Cloud Firestore
- Firebase Storage

### Firebase Architecture

```text
Android Application
        |
        | Firebase SDK
        |
        +----------------------+
        |                      |
        v                      v
Firebase Authentication   Cloud Firestore
                               |
                               |
                         Firebase Storage
Firebase provides the remote/cloud data layer for the application.
Local / Offline Data
The application also uses SQLite for local Android data.
SQLite is used for:
Cached branch information
Cached repair/service information
Local device information
Pending repair/request data
Offline application functionality
The local architecture is:
Android Application
        |
        v
Content Provider
        |
        v
SQLite
When remote connectivity is available, Firebase is used for cloud data.
When connectivity is unavailable, locally stored information can still be accessed where supported by the application.
Main System Architecture
                    TECHFIX ANDROID APP
                           |
          +----------------+----------------+
          |                                 |
     CUSTOMER SIDE                    MANAGEMENT SIDE
          |                                 |
     Browse Services                  Manage Repairs
     Manage Devices                   Manage Branches
     Book Repair                      Manage Technicians
     Track Repair                     Manage Spare Parts
     Repair History                   Manage Payments
     Payment
          |
          +----------------+----------------+
                           |
                      Firebase
                           |
          +----------------+----------------+
          |                |               |
       Auth            Firestore        Storage
          |                |               |
          |                |               |
       Users          Remote Data       Images
                       & Repairs
                           
                           +
                           
                      Local SQLite
                           |
                     Offline Data
Main Functional Flow
Login / Register
       |
       v
     Home
       |
       v
 Browse Services
       |
       v
 Service Details
       |
       v
  Select Device
       |
       v
   Book Repair
       |
       v
 Describe Problem
       |
       v
  Capture Image
       |
       v
 Appointment Date/Time
       |
       v
   Get GPS Location
       |
       v
 Find Suitable Branch
       |
       v
 Branch Assigned
       |
       v
 Repair Confirmation
       |
       v
 Repair Tracking
       |
       v
 Payment
       |
       v
 Repair History
Branch Assignment
One of the main features of TechFix is automatic branch assignment.
The customer location is obtained using Android Location Services.
Customer GPS
     |
     v
Latitude + Longitude
     |
     v
Retrieve Active Branches
from Firebase
     |
     v
Calculate Distance
to Each Branch
     |
     v
Check Technician Availability
     |
     v
Check Required Spare Parts
     |
     v
Remove Ineligible Branches
     |
     v
Select Nearest Eligible Branch
     |
     v
Assign Branch
to Repair Request
The system does not hardcode individual branches.
For example, it does not contain logic such as:
IF Colombo
ELSE IF Kandy
ELSE IF Galle
Instead, branches are retrieved dynamically from the remote database.
This allows TechFix to add new branches without changing the branch-selection algorithm.
Maps / Navigation
The application uses the customer's GPS coordinates for branch assignment.
A full map/navigation system is not required.
After a suitable branch has been selected, the application can pass the branch latitude and longitude to an external map application.
Selected Branch
       |
       v
Branch Latitude + Longitude
       |
       v
External Maps Application
       |
       v
Directions to TechFix Branch
This keeps the map functionality lightweight while still demonstrating GPS/location integration.
Camera & Images
Customers can capture an image of their device problem during the repair request process.
Book Repair
     |
     v
Camera
     |
     v
Capture Image
     |
     v
Preview
     |
     v
Upload / Store Image
     |
     v
Repair Request
Firebase Storage can be used for remote image storage.
Sample repair/service images can also be displayed to customers.
Repair Tracking
Repair requests progress through different statuses:
SUBMITTED
     |
     v
BRANCH_ASSIGNED
     |
     v
TECHNICIAN_ASSIGNED
     |
     v
IN_PROGRESS
     |
     v
READY_FOR_COLLECTION
     |
     v
COMPLETED
The current repair status is stored with the repair request.
Status changes can also be recorded as repair status history.
Payment
The application includes payment processing through a payment gateway.
The payment process is:
Repair Completed
       |
       v
Final Cost
       |
       v
Payment Screen
       |
       v
Payment Gateway
       |
       v
Test / Sandbox Card
       |
       v
Gateway Response
       |
    +--+--+
    |     |
 Success  Failed
    |     |
    v     v
Transaction ID
    |
    v
Payment Record
The application does not simply mark a payment as successful when the user presses a button.
A test/sandbox payment environment can be used for demonstration.
Database / Data Model
The original logical database model contains 14 entities:
Users
Device Categories
Devices
Branches
Technicians
Repair Services
Service Sample Images
Spare Parts
Repair Requests
Repair Images
Repair Status History
Repair Request Parts
Repair Part Usage
Payments
The logical relationships represented by these entities are implemented using the application's Firebase/Firestore data structure.
The original SQL schema is retained in the repository as a reference for the system's relational data model and documentation.
Project Structure
TechFix/
|
+-- app/
|   |
|   +-- src/main/
|       |
|       +-- java/com/example/techfix/
|       |   |
|       |   +-- customer/
|       |   +-- booking/
|       |   +-- branch/
|       |   +-- management/
|       |   +-- model/
|       |   +-- database/
|       |   +-- network/
|       |   +-- adapter/
|       |   +-- utils/
|       |
|       +-- res/
|           |
|           +-- layout/
|           +-- drawable/
|           +-- mipmap/
|           +-- values/
|
+-- database/
|   +-- SQL reference files
|
+-- README.md
+-- build.gradle.kts
+-- settings.gradle.kts
+-- gradle.properties

# Team Responsibilities
Person 1 — Customer Accounts & Services
Responsible for:
Splash
Login
Registration
Home
Services
Service Details
Customer account functionality
Service RecyclerView adapters
Main data:
Users
Device Categories
Repair Services
Service Sample Images
Person 2 — Devices, Booking & Camera
Responsible for:
My Devices
Add Device
Edit Device
Book Repair
Problem Description
Appointment
Camera integration
Repair image upload
Main data:
Devices
Repair Requests
Repair Images
Person 3 — Branch, GPS & Repair Tracking
Responsible for:
Branches
Branch Details
GPS location
Distance calculation
Nearest branch selection
Technician availability checking
Spare-part availability checking
Repair Tracking
Repair History
SQLite
Content Provider
Offline functionality
External map integration
Main data:
Branches
Technicians
Spare Parts
Repair Requests
Repair Status History
Repair Request Parts
Repair Part Usage
Person 4 — Management & Payment
Responsible for:
Management Dashboard
Repair Management
Technician Management
Spare Parts Management
Branch Management
Payment
Payment Result / Receipt
Main data:
Technicians
Spare Parts
Payments
Repair Requests
Branches
Required Coursework Features
Requirement
Implementation
Locations / Map GPS
Android Location Services + external map
Web Services / Remote Data
Firebase
Complex Data Model
14-entity logical model
Data Adapters
RecyclerView adapters
Camera Integration
Android Camera
Image Integration
Firebase Storage
SQLite
Local Android database
Content Provider
Local SQLite access
Offline Application
SQLite/local data + Firebase offline capabilities
Payment Gateway
Sandbox/test payment gateway
Branch Scalability
Dynamic branch data + distance algorithm
Repair Tracking
Repair status + status history
Device Management
Device CRUD
Spare Parts
Branch-level inventory
Technician Management
Availability and assignment

## Academic Context

**Module:** Mobile Application Development
**Course:** Higher National Diploma in Software Engineering
**Institution:** National Institute of Business Management – School of Computing and Engineering
**Batch:** 25.2 Full Time

## 📄 License

This project is developed for academic purposes in completion of the mbile application development coursework.
