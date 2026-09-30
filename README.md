# 🚗 Smart Parking System

A modern web-based **Smart Parking and EV Charging Management System** that helps users find and manage EV charging stations while allowing administrators to add and manage station information.

The project uses **Java as the backend web server**, **MySQL as the database**, and **HTML, CSS, and JavaScript** for the frontend.

> **Note:** This project does not use Spring Boot.  
> The backend is built using Java's built-in `HttpServer` and JDBC.

---

## 📌 Project Overview

The Smart Parking System is designed to provide a simple platform for managing EV charging stations and parking-related information.

The system allows users to:

- View charging stations
- Search stations by name or location
- Filter stations by availability
- View detailed station information
- View charging power and pricing
- Add new charging stations
- Store and retrieve station information from MySQL

The frontend communicates with the Java backend through HTTP requests, and the Java backend communicates with MySQL using JDBC.

---

# 🏗️ System Architecture

```text
┌─────────────────────────────────────┐
│             Web Browser             │
│                                     │
│        HTML + CSS + JavaScript      │
└──────────────────┬──────────────────┘
                   │
                   │ HTTP / API
                   ▼
┌─────────────────────────────────────┐
│          Java Web Server            │
│                                     │
│           WebServer.java            │
│                                     │
│        Java HttpServer API          │
└──────────────────┬──────────────────┘
                   │
                   │ JDBC
                   ▼
┌─────────────────────────────────────┐
│               MySQL                │
│                                     │
│   electrical_smart_car_parking      │
│                                     │
│       charging_stations             │
└─────────────────────────────────────┘
```

---

# 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| HTML5 | Website structure |
| CSS3 | Website styling and responsive design |
| JavaScript | Frontend interaction and API communication |
| Java | Backend web server |
| Java HttpServer | Lightweight HTTP server |
| JDBC | Database communication |
| MySQL | Data storage |
| Git | Version control |
| GitHub | Source code hosting |

---

# ✨ Features

## User Features

- 🔎 Search charging stations
- 📍 Search stations by location
- ⚡ View charging power
- 🔌 View connector type
- ✅ View station availability
- 💰 View charging price
- 📋 View station details
- 🔄 Refresh live station data

## Admin Features

- ➕ Add charging station
- 🏷️ Enter station information
- 📍 Set station location
- 🔌 Select connector type
- ⚡ Enter power rating
- 💰 Enter price per unit
- 🟢 Set station status

## Database Features

- Store charging station records
- Retrieve station records
- Search station records
- Filter available stations
- Update station information using SQL

---

# 📂 Project Structure

```text
smartparking-system/
│
├── README.md
├── .gitignore
│
├── DBConnection.java
├── WebServer.java
│
└── frontend/
    ├── index.html
    ├── style.css
    └── script.js
```

### File Description

| File | Description |
|---|---|
| `WebServer.java` | Starts the Java HTTP server and provides API endpoints |
| `DBConnection.java` | Handles JDBC connection to MySQL |
| `index.html` | Main website structure |
| `style.css` | Website design and responsive layout |
| `script.js` | API calls, search, filtering and UI logic |
| `README.md` | Project documentation |
| `.gitignore` | Prevents unnecessary/sensitive files from being uploaded |

---

# 🗄️ Database Setup

The application uses MySQL.

## Database Name

```sql
electrical_smart_car_parking
```

---

# 1. Create the Database

Open MySQL Command Line Client, MySQL Workbench, or another MySQL client.

Run:

```sql
CREATE DATABASE electrical_smart_car_parking;
```

Then select the database:

```sql
USE electrical_smart_car_parking;
```

---

# 2. Create the Charging Stations Table

Run:

```sql
CREATE TABLE charging_stations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    station_name VARCHAR(100) NOT NULL,
    location VARCHAR(150) NOT NULL,
    charging_slot VARCHAR(50) NOT NULL,
    connector_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    power_kw DOUBLE NOT NULL,
    price_per_unit DOUBLE NOT NULL
);
```

---

# 3. Verify the Database

Check all databases:

```sql
SHOW DATABASES;
```

Select the project database:

```sql
USE electrical_smart_car_parking;
```

Check tables:

```sql
SHOW TABLES;
```

You should see:

```text
charging_stations
```

Check the table structure:

```sql
DESC charging_stations;
```

---

# 4. Add Sample Data

You can add test data using:

```sql
INSERT INTO charging_stations
(
    station_name,
    location,
    charging_slot,
    connector_type,
    status,
    power_kw,
    price_per_unit
)
VALUES
(
    'Station A',
    'Aurangabad',
    'A01',
    'Type 2',
    'Available',
    7.5,
    12
);
```

Add another station:

```sql
INSERT INTO charging_stations
(
    station_name,
    location,
    charging_slot,
    connector_type,
    status,
    power_kw,
    price_per_unit
)
VALUES
(
    'Station B',
    'Pune',
    'B01',
    'CCS',
    'Occupied',
    22,
    15
);
```

Add another station:

```sql
INSERT INTO charging_stations
(
    station_name,
    location,
    charging_slot,
    connector_type,
    status,
    power_kw,
    price_per_unit
)
VALUES
(
    'Station C',
    'Nashik',
    'C01',
    'Type 2',
    'Available',
    11,
    14
);
```

---

# 5. View Data

View all stations:

```sql
SELECT * FROM charging_stations;
```

View only available stations:

```sql
SELECT *
FROM charging_stations
WHERE status = 'Available';
```

Count total stations:

```sql
SELECT COUNT(*) AS total_stations
FROM charging_stations;
```

Search by location:

```sql
SELECT *
FROM charging_stations
WHERE location = 'Aurangabad';
```

Search station by name:

```sql
SELECT *
FROM charging_stations
WHERE station_name = 'Station A';
```

---

# 6. Update Station Data

Example:

```sql
UPDATE charging_stations
SET status = 'Occupied'
WHERE id = 1;
```

Update price:

```sql
UPDATE charging_stations
SET price_per_unit = 15
WHERE id = 1;
```

Update station location:

```sql
UPDATE charging_stations
SET location = 'Pune'
WHERE id = 1;
```

---

# 7. Delete Station Data

Delete a specific station:

```sql
DELETE FROM charging_stations
WHERE id = 1;
```

Delete all station records:

```sql
DELETE FROM charging_stations;
```

> Be careful when using `DELETE` because deleted records cannot be recovered unless you have a database backup.

---

# 🔐 Database Configuration

The application connects to MySQL using JDBC.

The database configuration is:

```text
Host: localhost
Port: 3306
Database: electrical_smart_car_parking
User: root
```

For security, the MySQL password should **not be stored directly in the GitHub source code**.

Use environment variables instead.

---

# 🔒 Secure DBConnection.java

Use:

```java
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    static final String URL =
        "jdbc:mysql://localhost:3306/electrical_smart_car_parking";

    static final String USER =
        System.getenv("DB_USER");

    static final String PASSWORD =
        System.getenv("DB_PASSWORD");

    public static Connection getConnection() {

        try {

            Connection con =
                DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
                );

            System.out.println(
                "MySQL Connected Successfully!"
            );

            return con;

        } catch (Exception e) {

            System.out.println(
                "Database Connection Failed!"
            );

            e.printStackTrace();

            return null;
        }
    }
}
```

---

# 🖥️ Environment Variables

## Windows PowerShell

Set MySQL username:

```powershell
$env:DB_USER="root"
```

Set MySQL password:

```powershell
$env:DB_PASSWORD="YOUR_MYSQL_PASSWORD"
```

Replace:

```text
YOUR_MYSQL_PASSWORD
```

with the password of your MySQL account.

These environment variables are available to Java while the current PowerShell session is running.

---

# 📦 MySQL Connector/J

The project requires the MySQL JDBC driver.

Example:

```text
mysql-connector-j-26.7.0.jar
```

Download MySQL Connector/J from the official MySQL website and place the JAR in your local project directory.

Example local structure:

```text
smartparking-system/
│
├── WebServer.java
├── DBConnection.java
├── mysql-connector-j-26.7.0.jar
│
└── frontend/
```

The connector JAR is intentionally excluded from GitHub using `.gitignore`.

---

# ⚙️ `.gitignore`

The project should contain a `.gitignore` similar to:

```gitignore
*.class
*.jar
.vscode/
*.log
```

This prevents compiled Java files, dependency JARs, editor configuration and log files from being committed.

---

# 🚀 Running the Project

## Step 1 — Open the Project

Open the project folder in VS Code.

Example:

```powershell
cd "C:\Users\Asus\Desktop\SmartCarParkingWeb"
```

---

# Step 2 — Start MySQL

Make sure your MySQL server is running.

Then verify:

```sql
USE electrical_smart_car_parking;
```

and:

```sql
SELECT * FROM charging_stations;
```

---

# Step 3 — Set Environment Variables

PowerShell:

```powershell
$env:DB_USER="root"
$env:DB_PASSWORD="YOUR_MYSQL_PASSWORD"
```

---

# Step 4 — Make Sure MySQL Connector Exists

Place the connector JAR in the project directory:

```text
mysql-connector-j-26.7.0.jar
```

---

# Step 5 — Compile Java

Run:

```powershell
javac --add-modules jdk.httpserver -cp ".;mysql-connector-j-26.7.0.jar" *.java
```

If there are no errors, compilation was successful.

---

# Step 6 — Start the Java Web Server

Run:

```powershell
java --add-modules jdk.httpserver -cp ".;mysql-connector-j-26.7.0.jar" WebServer
```

You should see:

```text
--------------------------------
Smart Car Parking Web Server
--------------------------------
Server running at:
http://localhost:8080
--------------------------------
```

---

# Step 7 — Open the Website

Open your browser and visit:

```text
http://localhost:8080
```

The Smart Parking website should now open.

---

# 🌐 API Endpoints

The Java web server provides API endpoints for communication between the frontend and database.

---

## Get All Stations

### Request

```http
GET /api/stations
```

Example:

```text
http://localhost:8080/api/stations
```

The API returns JSON data similar to:

```json
[
    {
        "id": 1,
        "station_name": "Station A",
        "location": "Aurangabad",
        "charging_slot": "A01",
        "connector_type": "Type 2",
        "status": "Available",
        "power_kw": 7.5,
        "price_per_unit": 12
    }
]
```

---

# Add a Charging Station

### Request

```http
POST /api/stations
```

The frontend sends:

```text
station_name
location
charging_slot
connector_type
status
power_kw
price_per_unit
```

Example:

```text
Station Name: Station D
Location: Mumbai
Charging Slot: D01
Connector Type: CCS
Status: Available
Power: 30
Price: 18
```

After submission, the Java backend inserts the data into MySQL.

---

# 🔄 Data Flow

## Loading Stations

```text
Browser
   |
   | GET /api/stations
   v
WebServer.java
   |
   | SELECT * FROM charging_stations
   v
MySQL
   |
   | ResultSet
   v
WebServer.java
   |
   | JSON
   v
JavaScript
   |
   v
Station Cards
```

---

# ➕ Adding a Station

```text
Admin Form
     |
     v
JavaScript
     |
     | POST /api/stations
     v
WebServer.java
     |
     | INSERT INTO charging_stations
     v
MySQL
```

---

# 🎨 Frontend

The frontend is built using standard web technologies.

## HTML

`index.html`

Provides:

- Navigation
- Hero section
- Station section
- Search interface
- Station cards
- Feature section
- Admin form
- Footer
- Modal window

## CSS

`style.css`

Provides:

- Responsive design
- Navigation styling
- Hero section
- Station cards
- Dashboard statistics
- Buttons
- Forms
- Modal
- Mobile layout

## JavaScript

`script.js`

Provides:

- API communication
- Loading station data
- Search functionality
- Status filtering
- Statistics calculation
- Station details modal
- Adding new stations
- Error handling

---

# 📊 Current Database Schema

## `charging_stations`

| Column | Type | Description |
|---|---|---|
| `id` | INT | Unique station ID |
| `station_name` | VARCHAR(100) | Charging station name |
| `location` | VARCHAR(150) | Station location |
| `charging_slot` | VARCHAR(50) | Charging slot |
| `connector_type` | VARCHAR(50) | Connector type |
| `status` | VARCHAR(50) | Station status |
| `power_kw` | DOUBLE | Charging power |
| `price_per_unit` | DOUBLE | Charging price |

---

# 🧪 Testing the System

After starting the server:

## Test 1

Open:

```text
http://localhost:8080
```

Verify that the website loads.

## Test 2

Check stations:

```text
http://localhost:8080/api/stations
```

Verify that JSON data is displayed.

## Test 3

Add a station using the website.

Then run:

```sql
SELECT * FROM charging_stations;
```

Verify that the new record exists.

## Test 4

Change a station's status:

```sql
UPDATE charging_stations
SET status = 'Occupied'
WHERE id = 1;
```

Refresh the website.

The station should display the updated status.

---

# 🛑 Stop the Server

To stop the Java web server:

```text
Ctrl + C
```

Press this inside the terminal where `WebServer` is running.

---

# 🐛 Troubleshooting

## MySQL connection failed

Check that:

- MySQL is running
- Database exists
- Username is correct
- Password is correct
- Port `3306` is available
- Environment variables are configured

Test:

```sql
SHOW DATABASES;
```

and:

```sql
USE electrical_smart_car_parking;
```

---

## Table doesn't exist

Run:

```sql
USE electrical_smart_car_parking;
```

Then:

```sql
SHOW TABLES;
```

If `charging_stations` is missing, create it again:

```sql
CREATE TABLE charging_stations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    station_name VARCHAR(100) NOT NULL,
    location VARCHAR(150) NOT NULL,
    charging_slot VARCHAR(50) NOT NULL,
    connector_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    power_kw DOUBLE NOT NULL,
    price_per_unit DOUBLE NOT NULL
);
```

---

## `javac` is not recognized

Java JDK is not installed or is not configured in the system PATH.

Check:

```powershell
java -version
```

and:

```powershell
javac -version
```

---

## `git` is not recognized

Install Git and restart VS Code.

Check:

```powershell
git --version
```

---

## Website does not open

Make sure the Java server is running:

```powershell
java --add-modules jdk.httpserver -cp ".;mysql-connector-j-26.7.0.jar" WebServer
```

Then visit:

```text
http://localhost:8080
```

---

# 🔐 Security

Never commit sensitive information to GitHub.

Do not upload:

- MySQL passwords
- API keys
- Private credentials
- Authentication tokens
- `.env` files containing secrets
- Compiled `.class` files
- Unnecessary binary files

Use environment variables for database credentials.

---

# 🌱 Git and GitHub

Clone the repository:

```powershell
git clone https://github.com/omrayate90-cmd/smartparking-system.git
```

Enter the repository:

```powershell
cd smartparking-system
```

Check the repository:

```powershell
git status
```

---

# 📤 Upload Changes to GitHub

After making changes:

```powershell
git add .
```

Commit:

```powershell
git commit -m "Update Smart Parking System"
```

Push:

```powershell
git push
```

---

# 📥 Pull Latest Changes

To download the latest changes from GitHub:

```powershell
git pull
```

---

# 🔮 Future Improvements

The following features can be added in future versions:

- 👤 User registration and login
- 🔐 Authentication and authorization
- 🚗 Vehicle management
- 🅿️ Parking slot management
- 📅 Parking slot booking
- ⚡ EV charging booking
- 💳 Online payment
- 🧾 Booking history
- 📱 Mobile-friendly dashboard
- 🗺️ Interactive map
- 📍 GPS-based station search
- 🔔 Notifications
- 📊 Admin analytics dashboard
- 📈 Parking usage reports
- 🧑‍💼 Admin and user roles
- 🔑 Password reset
- 📷 QR code based parking/charging
- 🧾 Digital receipts
- ☁️ Cloud deployment

---

# 🚀 Future Database Expansion

The current database contains charging station information.

Future versions may include tables such as:

```text
users
vehicles
parking_slots
parking_bookings
charging_bookings
payments
notifications
reviews
```

A possible future architecture:

```text
                 Smart Parking System
                         |
        ┌────────────────┼────────────────┐
        │                │                │
      Users           Parking          EV Charging
        │                │                │
        ▼                ▼                ▼
    Login/Auth       Slot Booking     Charging Booking
        │                │                │
        └────────────────┼────────────────┘
                         │
                         ▼
                       MySQL
```

---

# 📌 Project Status

Current version supports:

- ✅ Java HTTP server
- ✅ MySQL database
- ✅ JDBC connectivity
- ✅ Charging station management
- ✅ Station search
- ✅ Station filtering
- ✅ Station details
- ✅ Add station
- ✅ Responsive frontend
- ✅ GitHub repository

---

# 👨‍💻 Author

**Om Rayate**

Smart Parking and EV Charging Management System

GitHub:

```text
https://github.com/omrayate90-cmd/smartparking-system
```

---

# 📄 License

This project is currently provided for educational and development purposes.

A formal open-source license can be added later, such as the MIT License.

---

# ⭐ Acknowledgement

This project demonstrates how a traditional Java application can be extended into a web-based system using:

```text
HTML
CSS
JavaScript
   +
Java
   +
JDBC
   +
MySQL
```

without requiring a large backend framework.

---

# 📚 Quick Start

For a quick setup, follow these steps:

### 1. Create database

```sql
CREATE DATABASE electrical_smart_car_parking;
```

### 2. Select database

```sql
USE electrical_smart_car_parking;
```

### 3. Create table

```sql
CREATE TABLE charging_stations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    station_name VARCHAR(100) NOT NULL,
    location VARCHAR(150) NOT NULL,
    charging_slot VARCHAR(50) NOT NULL,
    connector_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    power_kw DOUBLE NOT NULL,
    price_per_unit DOUBLE NOT NULL
);
```

### 4. Set database credentials

```powershell
$env:DB_USER="root"
$env:DB_PASSWORD="YOUR_MYSQL_PASSWORD"
```

### 5. Compile

```powershell
javac --add-modules jdk.httpserver -cp ".;mysql-connector-j-26.7.0.jar" *.java
```

### 6. Start server

```powershell
java --add-modules jdk.httpserver -cp ".;mysql-connector-j-26.7.0.jar" WebServer
```

### 7. Open website

```text
http://localhost:8080
```

### 8. Check database

```sql
USE electrical_smart_car_parking;

SELECT * FROM charging_stations;
```

---

# ✅ End

Your Smart Parking System is ready to run using:

**HTML + CSS + JavaScript + Java + JDBC + MySQL**

No Spring Boot required.
