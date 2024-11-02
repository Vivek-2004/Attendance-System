# NSHM College Attendance System

A Kotlin-based Android app developed using Jetpack Compose to streamline attendance management for CodeNest. The app allows a faculty to scan Student ID barcodes to mark attendance, register students, and view attendance records efficiently.

## Features

The app comprises three primary screens, each designed to fulfill a specific task:

1. **Scan & Mark Attendance**  
   - Allows faculty to scan a student's ID card barcode and automatically mark the student as present in the database.

2. **Register Student**  
   - Enables the registration of new students by adding their details to the database.

3. **Attendance Records**  
   - Displays a list of all students who were marked present at a specified time.

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Database**: Remote MongoDB
- **Barcode Scanning**: CameraX
- **Backend**: ExpressJS (https://github.com/aashish-65/RegistrationSystem)

## Setup and Installation

To run this project locally, follow these steps:

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Vivek-2004/Attendance-System
   cd Attendance-System
