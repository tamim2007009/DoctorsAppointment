# Appointment - Healthcare Doctor Booking Application

A comprehensive Android application that connects patients with doctors for scheduling medical consultations. The app streamlines the appointment booking process with role-based access for patients, doctors, and administrators, powered by real-time Firebase backend services.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [User Roles](#user-roles)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Installation](#installation)
- [Firebase Setup](#firebase-setup)
- [Building & Running](#building--running)
- [Architecture](#architecture)
- [Data Models](#data-models)
- [Key Features in Detail](#key-features-in-detail)
- [API & Libraries](#api--libraries)
- [Screenshots & Workflows](#screenshots--workflows)
- [Contributing](#contributing)
- [License](#license)

---

## 🎯 Overview

**Appointment** is a modern healthcare platform developed for Android that revolutionizes how patients book medical appointments. The application provides an intuitive interface for patients to search for doctors by specialty or medical condition, communicate directly with healthcare professionals, and manage their appointment schedules. Simultaneously, doctors can manage incoming appointment requests and maintain their professional profiles, while administrators can manage the medical database.

### Key Objectives:
- **Simplify Appointment Booking** - Eliminate traditional phone-based appointment scheduling
- **Connect Healthcare Providers & Patients** - Create a digital marketplace for medical consultations
- **Real-time Information** - Ensure instant updates across all platform participants
- **Centralized Medical Database** - Maintain comprehensive disease and symptom information

---

## ✨ Features

### 🏥 Patient Features
- **User Authentication**
  - Secure email/password registration and login
  - Password reset via email verification
  - Session persistence with automatic re-authentication

- **Doctor Discovery**
  - Search doctors by specialization (Cardiology, Neurology, General Practice, etc.)
  - View detailed doctor profiles including address, city, and specialization
  - Browse available medical professionals with ratings and information

- **Disease-Based Search**
  - Search medical conditions with associated symptoms
  - Find doctors specialized in specific diseases
  - Educational content on diseases and symptoms

- **Appointment Management**
  - Book appointments by selecting doctor and date/time
  - Track appointment status (pending, confirmed, rejected)
  - View all past and upcoming appointments
  - Cancel or modify appointment requests

- **Personal Dashboard**
  - View appointment history with timestamps
  - Real-time notification of appointment status changes
  - Quick access to doctor contact information

### 👨‍⚕️ Doctor Features
- **Professional Registration**
  - Register with specialization, address, and city information
  - Comprehensive professional profile setup
  - Contact information management

- **Appointment Management**
  - View incoming appointment requests from patients
  - Accept or reject appointment requests
  - Track confirmed appointments with patient details
  - Manage appointment schedule efficiently

- **Patient Information Access**
  - View patient contact details for confirmed appointments
  - Professional communication channel with patients
  - Appointment history tracking

- **Dashboard**
  - Quick summary of pending and confirmed appointments
  - Real-time notification system
  - Easy navigation to manage all appointments

### 🛡️ Admin Features
- **Medical Database Management**
  - Add new diseases and associated symptoms to the system
  - Maintain comprehensive medical reference database
  - Update disease classifications

- **Doctor Administration**
  - Register and validate doctors on the platform
  - Manage doctor profiles and specializations
  - Ensure quality control of medical professionals

- **Admin Dashboard**
  - Centralized panel for managing all system content
  - Access to medical database and doctor registry
  - System administration tools

---

## 👥 User Roles & Workflows

### Authentication Flow
```
Splash Screen
    ↓
(Check User Authentication Status)
    ├─→ Logged-in Patient → Patient Dashboard
    ├─→ Logged-in Doctor → Doctor Dashboard
    ├─→ Logged-in Admin → Admin Dashboard
    └─→ Not Logged In → Choice Screen
        ├─→ Patient Login/Register
        ├─→ Doctor Login/Register
        └─→ Admin Login
```

### Patient Workflow
```
Patient App
├─→ Search Doctors (by specialization)
├─→ Search Diseases (view symptoms & relevant doctors)
├─→ Book Appointment (select doctor & date/time)
├─→ View Appointment Requests (pending/confirmed)
├─→ Manage Appointments (track status)
└─→ Profile & Settings
```

### Doctor Workflow
```
Doctor App
├─→ Dashboard (view pending appointments)
├─→ Appointment Requests (accept/reject)
├─→ Confirmed Appointments (view booked slots)
├─→ Patient Details (contact information)
└─→ Profile & Settings
```

### Admin Workflow
```
Admin Panel
├─→ Add Diseases & Symptoms
├─→ Register Doctors
├─→ Manage Medical Database
└─→ System Administration
```

---

## 🛠️ Technology Stack

### Backend Services
| Service | Purpose | Version |
|---------|---------|---------|
| **Firebase Authentication** | User login, registration, password reset | Latest |
| **Firebase Realtime Database** | Store user data, appointments, disease info | Latest |
| **Firebase Firestore** | Structured data storage | Latest |
| **Firebase Storage** | File and document storage | Latest |
| **Firebase Analytics** | App usage tracking and insights | Latest |

### Frontend & UI Framework
| Component | Details |
|-----------|---------|
| **Language** | Java |
| **Minimum SDK** | 24 (Android 7.0 Nougat) |
| **Target SDK** | 33 |
| **Compile SDK** | 35 |
| **UI Framework** | Material Design 3 |
| **Navigation** | Navigation Drawer with Fragment-based UI |

### Key Libraries & Dependencies
| Library | Version | Purpose |
|---------|---------|---------|
| Firebase BOM | Latest | Firebase services management |
| Firebase Authentication | Latest | User authentication |
| Firebase Database | Latest | Real-time database operations |
| Firebase Firestore | Latest | Cloud managed database |
| Firebase Storage | Latest | Cloud file storage |
| Firebase Analytics | Latest | User analytics |
| AndroidX AppCompat | Latest | Backward compatibility & Material Design |
| Material Components | Latest | Modern Material Design UI components |
| CountryCodePicker | 2.5.4 | International phone number selection |
| Google Play Services Auth | Latest | Google authentication support |
| ConstraintLayout | Latest | Advanced layout management |
| RecyclerView | Latest | Efficient list rendering |

---

## 📁 Project Structure

```
DoctorsAppointment/
├── app/
│   ├── build.gradle                 # App-level build configuration
│   ├── google-services.json         # Firebase configuration
│   ├── proguard-rules.pro          # ProGuard obfuscation rules
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/asdf/appointment/
│       │   │   ├── Activities/
│       │   │   │   ├── MainActivity.java
│       │   │   │   ├── LoginActivity.java
│       │   │   │   ├── DoctorRegister.java
│       │   │   │   ├── PatientLoginRegisterChoice.java
│       │   │   │   ├── PatientRegister.java
│       │   │   │   ├── AdminLogin.java
│       │   │   │   ├── AdminDashboard.java
│       │   │   │   ├── AddDisease.java
│       │   │   │   ├── DoctorMainActivity.java
│       │   │   │   ├── AskDoctorPatient.java
│       │   │   │   ├── FixAppointment.java
│       │   │   │   ├── SplashScreen.java
│       │   │   │   └── PatientMainActivity.java
│       │   │   ├── Fragments/
│       │   │   │   ├── PatientSearchDiseaseFragment.java
│       │   │   │   ├── PatientSearchDoctorsFragment.java
│       │   │   │   ├── MyAppointmentFragment.java
│       │   │   │   ├── PendingAppointmentFragment.java
│       │   │   │   ├── CommonFragment.java
│       │   │   │   ├── AppointmentRequestFragment.java
│       │   │   │   └── AppointmentFragment.java
│       │   │   ├── Adapters/
│       │   │   │   ├── DoctorAdapter.java
│       │   │   │   ├── DiseaseAdapter.java
│       │   │   │   ├── AppointmentAdapter.java
│       │   │   │   └── CustomAdapters.java
│       │   │   ├── Models/
│       │   │   │   ├── UserDetails.java
│       │   │   │   ├── Doctor.java
│       │   │   │   ├── AppointmentRequest.java
│       │   │   │   ├── PatientAppointmentRequest.java
│       │   │   │   └── DiseaseAndSymptoms.java
│       │   │   ├── DataRetrieval/
│       │   │   │   ├── GetDoctorsList.java
│       │   │   │   ├── GetAppointmentsList.java
│       │   │   │   └── GetDiseaseList.java
│       │   │   ├── Utilities/
│       │   │   │   └── ReusableFunctionsAndObjects.java
│       │   │   └── Other components...
│       │   └── res/
│       │       ├── drawable/         # Vector drawables & images
│       │       ├── layout/           # XML layout files
│       │       ├── menu/             # Navigation menu resources
│       │       ├── values/           # Strings, colors, themes
│       │       ├── values-night/     # Dark mode resources
│       │       ├── font/             # Custom fonts
│       │       ├── mipmap-*/         # App icons (various DPIs)
│       │       └── xml/              # Backup & data extraction rules
│       ├── androidTest/              # Instrumented tests
│       └── test/                     # Unit tests
├── gradle/
│   └── wrapper/                      # Gradle wrapper configuration
├── build.gradle                      # Project-level build configuration
├── gradle.properties                 # Gradle properties
├── settings.gradle                   # Gradle settings
├── gradlew                          # Gradle wrapper script (Unix)
├── gradlew.bat                      # Gradle wrapper script (Windows)
└── README.md                        # This file
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** (Arctic Fox or newer recommended)
- **Java Development Kit (JDK)** 11 or higher
- **Android SDK** with minimum API level 24
- **Firebase Project** with enabled authentication and database services
- **Firebase CLI** (optional, for command-line setup)

### System Requirements
- Windows 10/11, macOS 10.15+, or Linux (Ubuntu 18.04+)
- 8GB RAM minimum (16GB recommended)
- 5GB disk space for Android SDK and project files

### Clone the Repository
```bash
git clone https://github.com/yourusername/DoctorsAppointment.git
cd DoctorsAppointment
```

---

## 📥 Installation

### Step 1: Prerequisites Installation

#### Android Studio
1. Download from [developer.android.com](https://developer.android.com/studio)
2. Follow the installation wizard
3. Install Android SDK (API level 24-35)

#### Java Development Kit
```bash
# Windows (using choco if installed)
choco install openjdk11

# macOS
brew install openjdk@11

# Linux (Ubuntu)
sudo apt-get install openjdk-11-jdk
```

### Step 2: Project Setup

1. **Open the project in Android Studio**
   - Launch Android Studio
   - Click "Open" and select the project folder
   - Wait for Gradle sync to complete

2. **Resolve Dependencies**
   - Android Studio will automatically download required dependencies
   - If needed, manually sync via: `File → Sync Now`

3. **Update SDK Components**
   - Go to `Tools → SDK Manager`
   - Ensure API level 24+ and Build Tools 35 are installed

---

## 🔥 Firebase Setup

### Step 1: Create Firebase Project

1. Visit [Firebase Console](https://console.firebase.google.com/)
2. Click "Create a new project"
3. Enter project name (e.g., "Appointment")
4. Accept Firebase terms and create project

### Step 2: Register Android App

1. In Firebase Console, click "Add app" → select Android
2. Enter package name: `com.asdf.appointment`
3. Enter SHA-1 certificate fingerprint:
   ```bash
   # Get SHA-1 from Android Studio
   # Build → Sign Bundle/APK → Create New → 
   # Or use keytool:
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   ```
4. Download `google-services.json`
5. Place in `app/` directory

### Step 3: Enable Firebase Services

In Firebase Console:

1. **Authentication**
   - Go to Build → Authentication
   - Enable "Email/Password" sign-in method
   - (Optional) Enable "Google" for Google Sign-In

2. **Realtime Database**
   - Go to Build → Realtime Database
   - Create database in test mode (for development)
   - Set appropriate security rules (production recommendations below):
   ```json
   {
     "rules": {
       "UserDetails": {
         "$uid": {
           ".read": "$uid === auth.uid",
           ".write": "$uid === auth.uid"
         }
       },
       "AppointmentRequests": {
         ".read": "auth != null",
         ".write": "auth != null"
       }
     }
   }
   ```

3. **Firestore Database**
   - Go to Build → Firestore Database
   - Create database in test mode (for development)

4. **Storage**
   - Go to Build → Storage
   - Create storage bucket with default settings

5. **Analytics** (Optional)
   - Enabled automatically

### Step 4: Verify Configuration

Check `app/google-services.json` is present with:
- `project_id`
- `mobilesdk_app_id`
- `api_key`
- Database URLs

---

## 🏗️ Building & Running

### Using Android Studio

1. **Select Device/Emulator**
   - Click device dropdown → select virtual or physical device
   - Create emulator if needed: `Tools → Device Manager → Create device`

2. **Build the Project**
   ```
   Build → Build Project (Ctrl+F9 / Cmd+B)
   ```

3. **Run the Application**
   ```
   Run → Run 'app' (Shift+F10 / Ctrl+R)
   ```

### Using Gradle (Command Line)

```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug

# Build and run
./gradlew installDebug
adb shell am start -n com.asdf.appointment/com.asdf.appointment.MainActivity
```

### Troubleshooting Build Issues

**Issue: Gradle sync fails**
```bash
./gradlew clean
./gradlew sync
```

**Issue: Firebase configuration not found**
- Ensure `google-services.json` is in `app/` directory
- Rebuild project: `Build → Rebuild Project`

**Issue: SDK version conflicts**
```bash
# Update build.gradle
android {
    compileSdkVersion 35
    targetSdkVersion 33
    minSdkVersion 24
}
```

---

## 🏛️ Architecture

### Architecture Pattern
The application follows **Model-View-Controller (MVC)** with **Firebase Real-time Synchronization**:

```
┌─────────────────────────────────────────────┐
│         User Interface (Activities/Fragments)│
│         (Views & User Interactions)          │
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│        Firebase SDK Integration              │
│   (Authentication, Database, Storage)        │
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│ Firebase Backend Services (Cloud)            │
│ - Authentication Service                     │
│ - Realtime Database                         │
│ - Firestore                                 │
│ - Cloud Storage                             │
└─────────────────────────────────────────────┘
```

### Component Breakdown

**Activities** - Main application screens
- Serve as entry points for different user roles
- Handle user interactions and navigation
- Manage fragments and lifecycle

**Fragments** - Modular UI components
- Display lists (doctors, appointments, diseases)
- Enable tab-like navigation within activities
- Provide better code organization and reusability

**Adapters** - Data binding layer
- Convert data models to UI views (RecyclerView)
- Handle item click listeners
- Manage dynamic list updates

**Models** - Data representation
- POJO (Plain Old Java Objects) for Firebase mapping
- Automatic serialization/deserialization with Gson

**Data Retrieval** - Backend communication
- Query Firebase Realtime Database
- Implement real-time listeners
- Fetch and transform data

**Utilities** - Reusable functions
- Common operations (validation, formatting)
- Shared helper methods
- Error handling implementations

### Real-time Data Synchronization
The app uses Firebase Listeners for instant updates:
```java
DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Appointments");
ref.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        // Auto-update UI when data changes
    }
});
```

---

## 📊 Data Models

### 1. UserDetails
Stores information for both patients and doctors
```java
public class UserDetails {
    public String name;
    public String email;
    public String mobileNo;
    public String userType;        // "Patient" or "Doctor"
    public String userId;
    public String password;        // (Not recommended - use Firebase Auth)
}
```

### 2. Doctor
Professional information for doctors
```java
public class Doctor {
    public String name;
    public String email;
    public String specialization;  // Cardiology, Neurology, etc.
    public String address;
    public String city;
    public String mobileNo;
    public String doctorId;
}
```

### 3. AppointmentRequest
Appointment tracking from doctor's perspective
```java
public class AppointmentRequest {
    public String appointmentId;
    public String doctorId;
    public String patientId;
    public String date;
    public String time;
    public String status;          // Pending, Confirmed, Rejected
    public long timestamp;
}
```

### 4. PatientAppointmentRequest
Extended appointment details for patient view
```java
public class PatientAppointmentRequest {
    public String appointmentId;
    public String doctorName;
    public String doctorSpecialization;
    public String date;
    public String time;
    public String status;
    public String doctorMobileNo;
    public String doctorAddress;
}
```

### 5. DiseaseAndSymptoms
Medical reference information
```java
public class DiseaseAndSymptoms {
    public String diseaseName;
    public String symptoms;        // Comma-separated symptoms
    public String diseaseId;
}
```

---

## 🎨 Key Features in Detail

### 1. Authentication System
- **Email/Password**: Secure Firebase Authentication
- **Password Reset**: Email-based password recovery with verification
- **Session Management**: Automatic re-login on app restart
- **Input Validation**: Email regex and phone number validation

### 2. Search Functionality
- **Doctor Search**: Filter by specialization with RecyclerView
- **Disease Search**: Find conditions and associated symptoms
- **Real-time Search**: Instant filtering as user types
- **Custom Adapter**: Dynamic list updates with search results

### 3. Appointment Management
- **Book Appointment**: Select doctor, date, and time
- **Request Status Tracking**: Pending → Confirmed/Rejected workflow
- **Real-time Updates**: Firebase listeners for instant status changes
- **Appointment History**: View past and upcoming appointments

### 4. Role-Based UI
- **Patient Dashboard**: Appointment bookings and doctor search
- **Doctor Dashboard**: Incoming requests and confirmed appointments
- **Admin Dashboard**: Disease and doctor management
- **Navigation Drawer**: Role-specific menu items

### 5. Theme Support
- **Dark Mode**: System preference and manual toggle
- **Persistent Theme**: SharedPreferences storage
- **Material Design Colors**: Dynamic color adaptation

### 6. Error Handling
- **Custom Dialogs**: User-friendly error messages
- **Firebase Exception**: Specific error handling for auth and database
- **Network Error**: Offline capability with local caching

---

## 📦 API & Libraries

### Firebase SDK
```gradle
// Firebase BOM management
implementation platform('com.google.firebase:firebase-bom:latest.version')

// Individual Firebase services
implementation 'com.google.firebase:firebase-auth'
implementation 'com.google.firebase:firebase-database'
implementation 'com.google.firebase:firebase-firestore'
implementation 'com.google.firebase:firebase-storage'
implementation 'com.google.firebase:firebase-analytics'
```

### AndroidX & Material
```gradle
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.9.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'androidx.recyclerview:recyclerview:1.3.0'
implementation 'androidx.navigation:navigation-fragment:2.5.3'
```

### Third-Party Libraries
```gradle
// CountryCodePicker for phone selection
implementation 'com.hbb20:ccp:2.5.4'

// Google Play Services
implementation 'com.google.android.gms:play-services-auth:20.5.0'
```

### API Level Compatibility
- **Minimum**: API 24 (Android 7.0) - 95% device coverage
- **Target**: 33 - Latest Play Store requirement
- **Compile**: 35 - FutureProofing

---

## 📸 Screenshots & Workflows

### Patient User Flow
1. **Splash/Login Screen** - App launch with authentication
2. **Patient Dashboard** - Main interface with navigation drawer
3. **Doctor Search** - Browse available doctors by specialization
4. **Doctor Details** - View doctor profile and specialization
5. **Book Appointment** - Select date/time for consultation
6. **My Appointments** - Track ongoing and completed appointments
7. **Disease Search** - Find medical conditions and symptoms

### Doctor User Flow
1. **Login Screen** - Doctor authentication
2. **Doctor Dashboard** - View appointment metrics
3. **Appointment Requests** - List of patient appointment requests
4. **Request Details** - Patient information and requested time
5. **Accept/Reject** - Manage appointment requests
6. **Confirmed Appointments** - View scheduled consultations

### Admin Panel
1. **Admin Login** - Secure authentication
2. **Dashboard** - Admin control center
3. **Add Diseases** - Input disease names and symptoms
4. **Register Doctors** - Add doctors to system
5. **Database Management** - Central admin controls

---

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

### How to Contribute

1. **Fork the Repository**
   ```bash
   git clone https://github.com/yourusername/DoctorsAppointment.git
   ```

2. **Create Feature Branch**
   ```bash
   git checkout -b feature/YourFeatureName
   ```

3. **Make Changes**
   - Follow Android development best practices
   - Use meaningful variable and function names
   - Add comments for complex logic

4. **Commit Changes**
   ```bash
   git commit -m "Add: Description of your changes"
   ```

5. **Push to Branch**
   ```bash
   git push origin feature/YourFeatureName
   ```

6. **Submit Pull Request**
   - Provide clear description of changes
   - Reference any related issues

### Code Style Guidelines
- Use camelCase for variables and methods
- Use PascalCase for class names
- Follow Android naming conventions
- Add JavaDoc comments for public methods

### Reporting Issues
When reporting bugs, please include:
- Android version and device model
- Steps to reproduce the issue
- Expected vs actual behavior
- Screenshots or logs if applicable

---

## 📄 License

This project is distributed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

### MIT License Summary
You are free to:
- ✅ Use the software for any purpose
- ✅ Copy, modify, and distribute
- ✅ Include in commercial projects

With the condition that:
- ℹ️ License and attribution are included

---

## 📞 Support & Contact

For questions, issues, or suggestions:

- **Issues**: GitHub Issues page for bug reports
- **Discussions**: Start a discussion for feature requests
- **Email**: [Your Email]
- **Documentation**: Check the wiki for detailed guides

---

## 🙏 Acknowledgments

- Firebase for backend infrastructure
- Android documentation and community
- Material Design guidelines
- Contributors and testers

---

## 📅 Release History

| Version | Date | Notes |
|---------|------|-------|
| 1.0 | 2024 | Initial release with core features |

---

## 🔐 Security Considerations

### Production Deployment
Before releasing to production:

1. **Database Rules** - Implement proper security rules
   ```json
   {
     "rules": {
       ".read": "auth != null",
       ".write": "auth != null && root.child('users').child(auth.uid).exists()"
     }
   }
   ```

2. **Authentication** - Use Firebase Email Verification
3. **Data Validation** - Validate all user inputs server-side
4. **Encryption** - Enable database encryption
5. **API Keys** - Rotate keys periodically
6. **Testing** - Conduct security audit before launch

---

## 📚 Additional Resources

- [Android Developer Documentation](https://developer.android.com/)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Material Design Guidelines](https://material.io/)
- [Java Documentation](https://docs.oracle.com/en/java/)

---

**Last Updated**: April 2026
**Maintained by**: Development Team
**Repository**: [GitHub Link]
**Status**: Active Development ✓

---

