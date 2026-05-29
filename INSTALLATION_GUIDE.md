# IMEI & Mobile Repair Super Tool Pro - Deployment and Installation Guide

This guide is designed for developers, mobile shop owners, and technicians looking to install, build, and deploy the application.

---

## 🛠️ Requirements & Tech Stack

- **Operating System**: Android 8.0 (API Level 26) or higher.
- **Development Tool**: Android Studio Koala (or newer)
- **Minimum SDK**: `26`
- **Target SDK**: `34`
- **Primary Language**: Kotlin (100% Jetpack Compose UI)
- **Database Architecture**: SQLite via modern Jetpack Room
- **State Engine**: Jetpack ViewModel + Coroutines StateFlow

---

## 📦 How to Build the Application (Step-by-Step)

### 1. Extract and Import the Project
1. Download or export the project ZIP from **Google AI Studio**.
2. Extract the file contents to your local development directory.
3. Open **Android Studio** and click on **File > New > Import Project...**, select the project folder containing `settings.gradle.kts`.

### 2. Synchronization of Gradle Dependencies
Android Studio will automatically detect the **Version Catalog (`gradle/libs.versions.toml`)** and start synchronizing dependencies. 
- Room compiler uses **KSP (Kotlin Symbol Processing)** for faster builds.
- Material 3 extended icons and Navigation compose are automatically cached and synchronized.

### 3. Native APK Compilation
To generate an installable `.apk` file for testing on physical devices:
1. In Android Studio, go to the upper toolbar and click **Build**.
2. Select **Build Bundle(s) / APK(s) > Build APK(s)**.
3. Once completed, a pop-up in the bottom right corner will show **"APK(s) generated successfully"**. Click **Locate** to retrieve the `app-debug.apk` file.

### 4. Direct Device Installation (Sideloading)
1. Transfer the generated `app-debug.apk` to your phone via USB or local share services (such as WhatsApp Web or Google Drive).
2. On your target Android phone, open the File Manager, locate the APK, and tap to install.
3. If prompted, toggle **"Allow installation from unknown sources"** representing standard developer verification processes.

---

## 🔒 Administrative Console Passcodes

The **Security System panel** inside the Settings screen locks administrative database and factory reset controls:
- **Default Master Admin Passcode**: `admin123`
- To bypass the password screen for testing purposes, you can tap on the **"Quick Login Bypass (admin123)"** link built inside the panel.

---

## 📂 Multi-Lingual and Local Directory Options

The application features full English and Urdu translations powered by an offline, zero-latency key-value dictionary. 
- To switch language directories instantly, navigate to the **Settings** screen and tap either **ENG** or **اردو**. 
- The entire dashboard, repair invoice inputs, PTA tax estimators, and IMEI checklists will adapt dynamically without requiring an application restart.
